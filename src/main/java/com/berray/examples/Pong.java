package com.berray.examples;

import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.math.Vec2;
import com.raylib.Jaylib;

import static com.berray.AssetManager.loadSprite;
import static com.berray.components.AnchorType.CENTER;

public class Pong extends BerrayApplication {
  private int score = 0;

  @Override
  public void initWindow() {
    width(1024);
    height(768);
    background(new Jaylib.Color(255, 255, 128, 255));
    title("Pong Game");
  }

  @Override
  public void initGame() {

    loadSprite("berry", "resources/berry.png");

    debug = true;

    // add paddles
    add(
        pos(40, 0),
        rect(20, 80),
//        outline(4),
        anchor(CENTER),
        area(),
        "paddle"
    );

    add(
        pos(width() - 40, 0),
        rect(20, 80),
//        outline(4),
        anchor(CENTER),
        area(),
        "paddle"
    );

    // move paddles with mouse
    game.onUpdate("paddle", event -> {
      GameObject gameObject = event.getParameter(0);
      gameObject.getOrDefault("pos", Vec2.origin()).setY(Jaylib.GetMouseY());
    });

    // score counter
    GameObject scoreCounter = add(
        text(String.valueOf(score)),
        pos(center()),
        anchor(CENTER)
//        z(50),
    );
    scoreCounter.on("update", event -> {
      scoreCounter.set("text", String.valueOf(score));

    });

  }


//
//    on("mousePress", (event) -> {
//      Vec2 pos = event.getParameter(0);
//      berry2.set("pos", pos);
//    });


  public static void main(String[] args) {
    new Pong().runGame();
  }

}

