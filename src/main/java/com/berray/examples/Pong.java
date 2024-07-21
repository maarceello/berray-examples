package com.berray.examples;

import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.math.Vec2;

import static com.raylib.Jaylib.RED;


import static com.berray.AssetManager.loadSprite;
import static com.berray.AssetManager.loadMusic;
import static com.berray.components.PosComponent.pos;
import static com.berray.components.RectComponent.rect;
import static com.berray.components.CircleComponent.circle;
import static com.berray.components.SpriteComponent.sprite;
import static com.berray.components.RotateComponent.rotate;
import static com.berray.components.AreaComponent.*;
import static com.berray.components.TextComponent.text;

public class Pong extends BerrayApplication {

  @Override
  public void initWindow() {
    width(1024);
    height(768);
    background(RED);
    title("Pong Game");
  }

  @Override
  public void initGame() {

    loadSprite("berry", "resources/berry.png");
    loadMusic("wind", "resources/wind.mp3");

    debug = true;

    GameObject infoTxt = add(
        text("Hello im a berry"),
        area()
    );

    GameObject startTxt = add(
        text("Press Space to start...not"),
        pos(1024 / 2, 768 / 2),
        area()
    );

    GameObject rect = add(
        rect(20, 80),
        pos(129, 83),
        area()
    );

    GameObject rect2 = add(
        rect(20, 80),
        pos(300, 300),
        rotate(45),
        "foo",
        area()
    );

    GameObject rect3 = add(
        rect(80, 80),
        pos(40, 768 - 40)
    );

    GameObject circle = add(
        circle(80),
        pos(400, 183),
        area()
    );

    GameObject berry = add(
        sprite("berry"),
        pos(110, 100),
        rotate(45),
        area()
    );

    GameObject berry2 = add(
        sprite("berry"),
        pos(500, 100),
        area()
    );

    on("mousePress", (event) -> {
      Vec2 pos = event.getParameter(0);
      berry2.set("pos", pos);
    });

  }


  public static void main(String[] args) {
    new Pong().runGame();
  }

}

