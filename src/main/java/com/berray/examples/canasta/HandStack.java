package com.berray.examples.canasta;

import com.berray.EasingFunctions;
import com.berray.GameObject;
import com.berray.components.CoreComponentShortcuts;
import com.berray.components.core.Action;
import com.berray.components.core.AnchorType;
import com.berray.components.core.Component;
import com.berray.math.Color;
import com.berray.math.Vec2;

import java.util.List;

import static com.berray.math.MathUtil.*;
import static com.berray.math.MathUtil.sin;

public class HandStack extends Component implements CoreComponentShortcuts {

    private CardStack stack = new CardStack("hand");

    /** Radius of the circle on which the cards are placed. */
    private int radius = 300;
    /** Max angle of the arc with the cards. The arc is centered on the top most point of the circle. */
    private float totalAngle = 60;
    /**
     * Max angle the cards are placed from one another. If there are very few cards, the are placed this angle
     * from each other.
     */
    private float maxAngle = 5;

    /** Distance of the center of the circle from the bottom of the stack object. */
    private int height = 200; // maybe calculate from radius and total angle

    public HandStack() {
        super("hand-stack");
    }

    @Override
    public void add(GameObject gameObject) {
        super.add(gameObject);
        registerAction("addCard", this::addCard, AddCardAction::new);
//        registerGetter("size", this::getSize);
    }

    private Vec2 getSize() {
        // todo: calculate size from radius, totalAngle and height
        return new Vec2(300, 200);
    }

    private static class AddCardAction extends Action {

        public AddCardAction(List<Object> params) {
            super(params);
        }

        public Card getNewCard() {
            return getParameter(0);
        }

        public int getPosition() {
            return getParameter(1);
        }
    }

    private void addCard(AddCardAction action) {
        Card newCard = action.getNewCard();
        int position = action.getPosition();

        List<Card> cards = stack.getCards();

        // add card to list of cards
        cards.add(position, newCard);
        // create game object
        GameObject cardObject = gameObject.add(
                pos(radius,  radius),
                rect(Canasta.cardSize),
                color(Color.GREEN),
                anchor(AnchorType.CENTER),
                rotate(0),
                scale(1.0f),
                area(),
                mouse(),
                z(0)
        );

        cardObject.on(DragComponent.EVENT_DRAG_ENTER, (event -> {
            event.getSource().getChild("cardSprite").set("color", Color.GRAY);
        }));
        cardObject.on(DragComponent.EVENT_DRAG_LEAVE, (event -> {
            event.getSource().getChild("cardSprite").set("color", Color.WHITE);
        }));

        int frame = newCard.getCardSuit().ordinal() * 14 + newCard.getCardValue().ordinal() + 1;
        GameObject cardSprite = cardObject.add(
                "cardSprite",
                anchor(AnchorType.CENTER),
                pos(Canasta.cardSize.scale(0.5f)),
                sprite("cards").frame(frame),
                color(Color.WHITE),
                scale(1.0f)
        );

        // game object was added to the end of the children list. move the game object to the correct position
        List<GameObject> children = gameObject.getChildren();
        children.remove(cardObject); // note: this does a linear scan, but as the operation is seldom, it shouldn't be a problem
        children.add(position, cardObject);

        // calculate the step the card are from each other. be sure to clamp the angle to maxAngle when the hand only has
        // few cards.
        float angleStep = cards.size() < 2 ? 0 :  Math.min(maxAngle, totalAngle / (cards.size()-1));

        // start angle of the first card (where 0.0 is the top of the circle)
        // the angle is negative, so the cards start left from 0.0
        float startAngle = -((cards.size() / 2.0f) * angleStep);

        // go over all card objects and move them to their correct position
        for (int i = 0; i < children.size(); i++) {
            GameObject card = children.get(i);
            float angle = startAngle + angleStep * i - 90;
            float xPos = cos(toRadians(angle)) * radius + radius;
            float yPos = sin(toRadians(angle)) * radius + radius;

            card.animate("pos", new Vec2(xPos, yPos), 1f, EasingFunctions.EASE_OUT_QUADRATIC);
            card.animate("angle", (angle - 90 + 360) % 360, 2f, EasingFunctions.EASE_OUT_QUADRATIC);
            card.set("z", i);
            card.setTransformDirty();
        }
    }
}
