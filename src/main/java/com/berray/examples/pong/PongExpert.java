package com.berray.examples.pong;

import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.components.AnchorType;
import com.berray.examples.pong.data.GameData;
import com.berray.examples.pong.objects.Ball;
import com.berray.objects.Label;
import com.berray.math.Vec2;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import static com.berray.components.AnchorType.CENTER;

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
      gameObject.getOrDefault("pos", Vec2.origin()).setY(Jaylib.GetMouseY());
    });

    // add score label to the center of the screen
    game.addChild(new Label(center(), CENTER, () -> String.valueOf(gameData.getScore())));
    // add fps label to top left
    game.addChild(new Label(Vec2.origin(), AnchorType.TOP_LEFT, () -> "FPS: "+ Raylib.GetFPS()));
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
