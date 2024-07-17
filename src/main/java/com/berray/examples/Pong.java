package com.berray.examples;

import com.berray.BerrayApplication;
import com.berray.EventCallback;
import com.berray.GameObject;


//import static com.berray.AssetManager.loadSprite;
import static com.berray.components.PosComponent.pos;
import static com.berray.components.SpriteComponent.sprite;

public class Pong extends BerrayApplication {

  public void onEvent(String event, EventCallback callback){}

  @Override
  public void initWindow() {
    width(1024);
    height(768);
    title("This is a Window");
  }

  @Override
  public void initGame() {

    //loadSprite("bean","recouses/berry.png");

    GameObject player = add(
        sprite("resources/berry.png")
            .anim("idle")
        ,
        pos(129, 83)
    );

    //  player.on("collide", (do) ->{
    //     do.add()
    //  });

  }



  public static void main(String[] args) {
    new Pong().runGame();
  }

}

