package com.berray.examples.canasta;

import com.berray.EasingFunctions;
import com.berray.GameObject;
import com.berray.components.CoreComponentShortcuts;
import com.berray.components.core.*;
import com.berray.event.CoreEvents;
import com.berray.event.UpdateEvent;
import com.berray.examples.canasta.events.DragEvent;
import com.berray.math.Color;
import com.berray.math.MathUtil;
import com.berray.math.Vec2;

import java.util.*;

import static com.berray.math.MathUtil.*;

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

    /** List of cards over which another card is currently dragged. */
    private List<GameObject> draggedOverCards = new ArrayList<>();

    /** The card which is dragged over the hand. */
    private GameObject draggedCard = null;

    /** Container for the card arc. */
    private GameObject cardsContainer = null;
    private GameObject debugDragPoint;

    public HandStack() {
        super("hand-stack");
    }

    @Override
    public void add(GameObject gameObject) {
        super.add(gameObject);
        registerAction("addCard", this::addCard, AddCardAction::new);

        on(DragEvent.EVENT_DRAG_ENTER, this::processStackDragEnter);
        on(DragEvent.EVENT_DRAG_LEAVE, this::processStackDragLeave);
        onGame(CoreEvents.UPDATE, this::onUpdate);

        gameObject.add(
                circle(radius),
                anchor(AnchorType.CENTER),
                pos(radius,radius*1.5f),
                color(Color.RED)
        );
        this.cardsContainer = gameObject.add(pos(0,0));
        debugDragPoint = gameObject.add(
                circle(10),
                anchor(AnchorType.CENTER),
                pos(0,0),
                color(Color.BLACK)
        );
    }

    private void onUpdate(UpdateEvent event) {
        if (draggedCard != null) {
            // calculate angle in which the card is

            // calculate midpoint of the circle on which the hand cards are placed
            Vec2 midPoint = new Vec2(radius, radius * 1.5f);

            // get position of hovered object in local coordinate space. Add half of the card size to get the midpoint of the card
            Vec2 localHoverPoint = draggedCard.localPosToOtherLocalPos(gameObject, Canasta.cardSize.scale(0.5f));

            debugDragPoint.set("pos", localHoverPoint);

            float angleRad = (float) Math.atan2(localHoverPoint.getY() - midPoint.getY(), localHoverPoint.getX() - midPoint.getX());
            float angleDeg = MathUtil.toDegrees(angleRad);

            draggedCard.animate("angle", angleDeg-90, 0.3f, EasingFunctions.EASE_OUT_ELASTIC);
        }
    }



    public void processStackDragEnter(DragEvent event) {
        draggedCard = event.getDraggedObject();
        gameObject.set("color", Color.GOLD);
    }

    public void processStackDragLeave(DragEvent event) {
        gameObject.set("color", Color.GREEN);
        if (draggedCard != null) {
            draggedCard.set("angle", 0f);
            draggedCard = null;
        }
    }

    private void addCard(AddCardAction action) {
        Card newCard = action.getNewCard();
        int position = action.getPosition();

        List<Card> cards = stack.getCards();

        // add card to list of cards
        cards.add(position, newCard);
        // create game object
        GameObject cardObject = cardsContainer.add(
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

        cardObject.on(DragEvent.EVENT_DRAG_ENTER, this::processCardDragEnter);
        cardObject.on(DragEvent.EVENT_DRAG_LEAVE, this::processCardDragLeave);

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
        List<GameObject> children = cardsContainer.getChildren();
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
            float yPos = sin(toRadians(angle)) * radius + radius * 1.5f;

            card.animate("pos", new Vec2(xPos, yPos), 1f, EasingFunctions.EASE_OUT_QUADRATIC);
            card.animate("angle", (angle - 90 + 360) % 360, 2f, EasingFunctions.EASE_OUT_QUADRATIC);
            card.set("z", i);
            card.setTransformDirty();
        }
    }

    public void processCardDragEnter(DragEvent event) {
        // remove color from all dragged over cards
        draggedOverCards.forEach(card -> card.getChild("cardSprite").set("color", Color.WHITE));
        // add current card and sort by z  (highest z first)
        draggedOverCards.add(event.getTarget());
        draggedOverCards.sort((a, b) -> b.getZ() - a.getZ());
        // only add the highlight color to the card with the highest z (first in the list)
        draggedOverCards.get(0).getChild("cardSprite").set("color", Color.GRAY);
    }

    public void processCardDragLeave(DragEvent event) {
        // remove color from all dragged over cards and add only the first one
        draggedOverCards.forEach(card -> card.getChild("cardSprite").set("color", Color.WHITE));
        // remove current card and sort by z  (highest z first)
        draggedOverCards.remove(event.getTarget());
        // only if there are cards which are dragged over
        if (!draggedOverCards.isEmpty()) {
            draggedOverCards.sort((a, b) -> b.getZ() - a.getZ());
            // only add the highlight color to the card with the highest z (first in the list)
            draggedOverCards.get(0).getChild("cardSprite").set("color", Color.GRAY);
        }
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

}
