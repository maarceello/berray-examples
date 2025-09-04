package com.berray.examples.canasta;


import com.berray.*;
import com.berray.assets.CoreAssetShortcuts;
import com.berray.assets.SpriteSheet;
import com.berray.components.CoreComponentShortcuts;
import com.berray.components.core.AnchorType;
import com.berray.event.CoreEvents;
import com.berray.event.Event;
import com.berray.event.MouseEvent;
import com.berray.event.UpdateEvent;
import com.berray.math.Color;
import com.berray.math.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.berray.examples.canasta.DragComponent.draggable;
import static com.berray.math.MathUtil.*;

public class Canasta extends BerrayApplication implements CoreComponentShortcuts, CoreAssetShortcuts, CoreEvents {

    public  static final Vec2 cardSize = new Vec2(56, 80);

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

        GameObject handStack = add(
            pos(width() / 2, height() / 2),
            rect(600, 300).fill(false),
            anchor(AnchorType.CENTER),
            new HandStack()
        );


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
                    scale(1.0f),
                    new FlipComponent(frame, 3*14)
            );

            cardObject.on(CoreEvents.MOUSE_CLICK, (event) -> {
                GameObject source = event.getSource();
                Card localCard = source.getProperty("card");
                handStack.doAction("addCard", localCard, 0);
            });
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
        float arcAngle = 60; // degrees

//        drawCardArc(stack.getCards(), screenCenter, height()+200, arcRadius, arcAngle);


    }

    private List<GameObject> drawCardArc(List<Card> cards, int x, int y, int radius, float totalAngle) {
        float angleStep = toRadians(cards.size() < 2 ? 1 : totalAngle / (cards.size()-1));
        float startAngle = toRadians(-totalAngle / 2);

        List<GameObject> cardObjects = new ArrayList<>();

        float zeroAngle = (float) (Math.PI / 2 + Math.PI);
        float start = zeroAngle - toRadians(totalAngle/2);

        for (int i = 0; i < cards.size(); i++) {
            float angle = startAngle + (i * angleStep);

            Card card = cards.get(i);
            int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;

            float xPos = x + cos(start + angleStep * i) * radius;
            float yPos = y + sin(start + angleStep * i) * radius;
            GameObject cardObject = add(
                    pos(xPos, yPos),
                    rect(cardSize),
                    color(Color.GREEN),
                    anchor(AnchorType.CENTER),
                    rotate(toDegrees(angle)),
                    scale(1.0f),
                    area(),
                    mouse(),
                    z(i)
            );

            GameObject cardSprite = cardObject.add(
                "cardSprite",
                anchor(AnchorType.CENTER),
                pos(cardSize.scale(0.5f)),
                sprite("cards").frame(frame),
                scale(1.0f)
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

            cardObject.on(HOVER_ENTER,  (event) -> {
                cardSprite.animate("scale2d", new Vec2(1.5f, 1.5f), 0.5f, EasingFunctions.EASE_OUT_ELASTIC);
            });

            cardObject.on(HOVER,  (MouseEvent event) -> {
                event.consume();
            });

            cardObject.on(HOVER_LEAVE, (event) -> {
                cardSprite.animate("scale2d", new Vec2(1.0f, 1.0f), 0.5f, EasingFunctions.EASE_OUT_ELASTIC);
            });

            cardObjects.add(cardObject);
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
                    EasingFunctions.EASE_OUT_ELASTIC,
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
                    EasingFunctions.EASE_OUT_ELASTIC,
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
