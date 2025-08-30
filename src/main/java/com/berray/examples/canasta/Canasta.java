package com.berray.examples.canasta;


import com.berray.AnimationData;
import com.berray.BerrayApplication;
import com.berray.Game;
import com.berray.GameObject;
import com.berray.assets.CoreAssetShortcuts;
import com.berray.assets.SpriteSheet;
import com.berray.components.CoreComponentShortcuts;
import com.berray.components.core.AnchorType;
import com.berray.event.CoreEvents;
import com.berray.event.Event;
import com.berray.event.UpdateEvent;
import com.berray.math.Color;
import com.berray.math.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.berray.examples.canasta.DragComponent.draggable;
import static com.berray.math.MathUtil.*;

public class Canasta extends BerrayApplication implements CoreComponentShortcuts, CoreAssetShortcuts, CoreEvents {

    private final Vec2 cardSize = new Vec2(56, 80);

    @Override
    public void initWindow() {
        width(1500);
        height(768);
        background(new Color(0,255.0f/100,0));
        title("Canasta Card Game");
    }

    @Override
    public void game() {
        layers(Game.DEFAULT_LAYER, "dragLayer");

        loadSpriteSheet("cards", "resources/kerenel_Cards.png", SpriteSheet.spriteSheet().sliceX(14).sliceY(6));

        List<Card> deck = fullDeck();
        Collections.shuffle(deck);



        game.on("update", (UpdateEvent event) -> {
            if (event.getSource() == null) {
                game.getAnimationManager().animationUpdate(event);
            }
        });

        CardStack stack = new CardStack("Ablagestapel");



        for (int x = 0; x < 14; x++) {
            Card card = deck.get(x);
            stack.addCard(card);

            int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;

            GameObject cardObject = add(
                    "card",
                    rect(cardSize).fill(false),
                    color(Color.GOLD),
                    anchor(AnchorType.TOP_LEFT),
                    property("card", card),
                    pos(x * 65+33, 80),
                    layer(Game.DEFAULT_LAYER),
                    area(),
                    mouse(),
                    draggable()
            );

            cardObject.add(
                    "cardSprite",
                    anchor(AnchorType.CENTER),
                    pos(cardSize.scale(0.5f)),
                    sprite("cards").frame(frame),
                    scale(1.0f)
//                    new FlipComponent(frame, 3*14)
            );
        }

//        add(
//            pos(530, 240),
//            cardStack(stack),
//            area(),
//            mouse(),
//            draggable(),
//            dropTarget("card")
//        );

        // Draw the arc of cards at the bottom of the screen
        int screenCenter = width() / 2;
        int arcRadius = 300;
        float arcAngle = 90; // degrees

        drawCardArc(stack.getCards(), screenCenter, height()/2, arcRadius, arcAngle);
    }

    // Function to draw cards in an arc
    private List<GameObject> drawCardArc(List<Card> cards, int x, int y, int radius, float totalAngle) {
        float angleStep = toRadians(cards.isEmpty() ? 1 : totalAngle / (cards.size()));
        float startAngle = toRadians(-totalAngle / 2);

        List<GameObject> cardObjects = new ArrayList<>();

        add(
                pos(x,y),
                circle(radius),
                color(Color.GOLD),
                anchor(AnchorType.CENTER)
        );

        float zeroAngle = (float) (Math.PI / 2 + Math.PI);
        float start = zeroAngle - toRadians(totalAngle/2);


        for (int i = 0; i < cards.size(); i++) {
            float angle = startAngle + (i * angleStep);
            float xPos = x + radius * sin(angle);
            float yPos = y + radius * cos(angle);

            add(
                    pos(x + cos(start + angleStep * i) * radius, y + sin(start + angleStep * i) * radius),
                    circle(10),
                    color(Color.GREEN),
                    anchor(AnchorType.CENTER)
            );


            Card card = cards.get(i);
            int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;

            // Create a card object
            GameObject cardObj = add(
                    pos(xPos, yPos),
                    area(),
                    rotate(angle),
                    anchor(AnchorType.CENTER),
                    sprite("cards").frame(frame),
                    z(i), // Ensure cards overlap correctly
                    property("originalPos", new Vec2(xPos, yPos)),
                    property("isSelected", false)
            );


//            // Make cards interactive
//            cardObj.onClick(() => {
//                    cardObj.isSelected = !cardObj.isSelected;
//            if (cardObj.isSelected) {
//                cardObj.moveTo(cardObj.originalPos.x, cardObj.originalPos.y - 30);
//                cardObj.color = rgb(200, 200, 255);
//            } else {
//                cardObj.moveTo(cardObj.originalPos);
//                cardObj.color = rgb(255, 255, 255);
//            }
//    });

            cardObj.on(HOVER_ENTER,  (event) -> {
                    cardObj.set("scale", new Vec2(1.05f, 1.05f));
            });

            cardObj.on(HOVER_LEAVE, (event) -> {
                cardObj.set("scale", new Vec2(1.0f, 1.0f));
            });

            cardObjects.add(cardObj);
        }

        return cardObjects;
    }

    private void onHoverEnter(Event event) {
        GameObject source = event.getSource();
        GameObject cardSprite = source.getChild("cardSprite");

        if (cardSprite != null) {
            AnimationData<Float> animationData = new AnimationData<>(
                    "sideVisible",
                    cardSprite.<Float>get("sideVisible"),
                    -1.0f,
                    0.2f,
                    (f, scale) -> f * scale,
                    (a, b) -> a + b,
                    (a, b) -> a - b
            );

            game.getAnimationManager().addAnimation(cardSprite, animationData);
        }
    }

    private void onHoverLeave(Event event) {
        GameObject source = event.getSource();
        GameObject cardSprite = source.getChild("cardSprite");

        if (cardSprite != null) {
            AnimationData<Float> animationData = new AnimationData<>(
                    "sideVisible",
                    cardSprite.<Float>get("sideVisible"),
                    1.0f,
                    0.2f,
                    (f, scale) -> f * scale,
                    (a, b) -> a + b,
                    (a, b) -> a - b
            );

            game.getAnimationManager().addAnimation(cardSprite, animationData);
        }
    }

    public List<Card> fullDeck() {
        List<Card> result = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank value : CardRank.values()) {
                result.add(new Card(suit, value));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new Canasta().runGame();
    }
}
