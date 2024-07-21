package com.berray.examples;

import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.math.Rect;
import com.berray.math.Vec2;
import com.raylib.Jaylib;

import static com.berray.AssetManager.loadSprite;
import static com.berray.components.AnchorType.CENTER;

public class Pong extends BerrayApplication {
  private int score = 0;
  private int speed = 480;

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


    GameObject ball = add(
        pos(center()),
        circle(16),
//        outline(4),
        area(new Rect(-16, -16, 32, 32))
    );
    ball.setProperty("vel", Vec2.fromAngle((float) ((Math.random() - 0.5) * 40)));
// move ball, bounce it when touche horizontal edges, respawn when touch vertical edges
    ball.on("update", event -> {
      float deltaTime = event.getParameter(0);
      Vec2 vel = ball.getProperty("vel");
      Vec2 pos = ball.get("pos");
      pos = pos.move(vel.scale(speed * deltaTime));

      if (pos.getX() < 0 || pos.getX() > width()) {
        score = 0;
        pos = center();
        vel = Vec2.fromAngle((float) ((Math.random() - 0.5) * 40));
        speed = 320;
      }
      if (pos.getX() < 0 || pos.getY() > height()) {
        vel.setY(vel.getY());
      }
      ball.set("pos", pos);
      ball.setProperty("vel", vel);

    });

    // bounce when touch paddle
    ball.onCollide("paddle", (event) -> {
      speed += 60;
      GameObject other = event.getParameter(0);
      Vec2 ballPos = ball.get("pos");
      Vec2 otherPos = other.get("pos");
      ball.setProperty("vel", Vec2.fromAngle(ballPos.angle(otherPos)));
      score++;
    });
  }


  public static void main(String[] args) {
    new Pong().runGame();
  }

}

