package com.berray.examples.pong;

import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.components.core.AnchorType;
import com.berray.examples.pong.data.GameData;
import com.berray.examples.pong.objects.Ball;
import com.berray.math.Vec2;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import static com.berray.components.core.AnchorType.CENTER;
import static com.berray.objects.Label.label;

/** Pong with more object-oriented structure. */
public class PongExpert extends BerrayApplication {
  /** Global game data moved to it's own object. */
  private GameData gameData = new GameData();

  @Override
  public void initWindow() {
    width(1024);
    height(600);
    background(Jaylib.GRAY);
    title("Pong Game - Expert");
  }

  @Override
  public void game() {
    // add paddles left and right
    addPaddle(40);
    addPaddle(width() - 40);

    // move paddles with mouse
    game.onUpdate("paddle", event -> {
      GameObject gameObject = event.getParameter(0);
      Vec2 pos = gameObject.getOrDefault("pos", Vec2.origin());
      pos.setY(Jaylib.GetMouseY());
      gameObject.set("pos", pos);
    });

    // add score label to the center of the screen
    game.add(
        label(() -> String.valueOf(gameData.getScore())),
        pos(center()),
        anchor( CENTER)
    );
    // add fps label to top left
    game.add(
        label(() -> "FPS: "+ Raylib.GetFPS()),
        pos(Vec2.origin()),
        anchor(AnchorType.TOP_LEFT)
    );
    // add ball
    game.addChild(new Ball(gameData));
  }

  private void addPaddle(int x) {
    add(
        pos(x, 0),
        rect(20, 80),
        anchor(CENTER),
        area(),
        "paddle"
    );
  }


  public static void main(String[] args) {
    new PongExpert().runGame();
  }
}
