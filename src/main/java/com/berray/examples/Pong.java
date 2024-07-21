package com.berray.examples;

import com.berray.BerrayApplication;
import com.berray.GameObject;

import static com.raylib.Jaylib.RED;


import static com.berray.AssetManager.loadSprite;
import static com.berray.AssetManager.loadMusic;
import static com.berray.components.PosComponent.pos;
import static com.berray.components.RectComponent.rect;
import static com.berray.components.CircleComponent.circle;
import static com.berray.components.SpriteComponent.sprite;
import static com.berray.components.RotateComponent.rotate;
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


    GameObject infoTxt = add(
        text("Hello im a berry")
    );

    GameObject startTxt = add(
        text("Press Space to start...not"),
        pos(1024 / 2, 768 / 2)
    );

    GameObject rect = add(
        rect(20, 80),
        pos(129, 83)
    );

    GameObject rect2 = add(
        rect(20, 80),
        pos(300, 300),
        rotate(45),
        "foo"
    );

    GameObject rect3 = add(
        rect(80, 80),
        pos(40, 768 - 40)
    );

    GameObject circle = add(
        circle(80),
        pos(400, 183)
    );

    GameObject berry = add(
        sprite("berry"),
        pos(110, 100),
        rotate(45)
    );

    GameObject berry2 = add(
        sprite("berry"),
        pos(500, 100)
    );

  }


  public static void main(String[] args) {
    new Pong().runGame();
  }

}

