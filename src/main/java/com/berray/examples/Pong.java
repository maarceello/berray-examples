package com.berray.examples;

import com.berray.BerrayApplication;
import com.berray.GameObject;


import static com.berray.AssetManager.loadSprite;
import static com.berray.AssetManager.loadMusic;
import static com.berray.components.PosComponent.pos;
import static com.berray.components.SpriteComponent.sprite;
import static com.berray.components.RotateComponent.rotate;

public class Pong extends BerrayApplication {

  @Override
  public void initWindow() {
    width(1024);
    height(768);
    title("This is a Window");
  }

  @Override
  public void initGame() {

    loadSprite("bean","resources/berry.png");
    loadMusic("wind", "resources/wind.mp3");

    GameObject player = add(
        sprite("bean")
            .anim("idle")
        ,
        pos(129, 83),
        rotate(89)
    );

  }



  public static void main(String[] args) {
    new Pong().runGame();
  }

}

