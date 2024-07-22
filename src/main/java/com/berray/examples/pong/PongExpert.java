package com.berray.examples.pong;

import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.examples.pong.data.GameData;
import com.berray.examples.pong.objects.Ball;
import com.berray.math.Vec2;
import com.raylib.Jaylib;

import static com.berray.components.AnchorType.CENTER;

/** Pong with more object-oriented structure. */
public class PongExpert extends BerrayApplication {
  private GameData gameData = new GameData();

  @Override
  public void initWindow() {
    width(gameData.getWidth());
    height(gameData.getHeight());
    background(Jaylib.GRAY);
    title("Pong Game - Expert");
  }

  @Override
  public void runGame() {
    addPaddle(40);
    addPaddle(width() - 40);

    // move paddles with mouse
    game.onUpdate("paddle", event -> {
      GameObject gameObject = event.getParameter(0);
      gameObject.getOrDefault("pos", Vec2.origin()).setY(Jaylib.GetMouseY());
    });

    GameObject scoreCounter = add(
        text(String.valueOf(gameData.getScore())),
        pos(center()),
        anchor(CENTER)
    );
    scoreCounter.on("update", event -> {
      scoreCounter.set("text", String.valueOf(gameData.getScore()));
    });

    GameObject ball = new Ball(gameData);
    game.addChild(ball);
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
    new Pong().runGame();
  }

}
