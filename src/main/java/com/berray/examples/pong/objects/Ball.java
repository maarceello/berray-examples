package com.berray.examples.pong.objects;

import com.berray.GameObject;
import com.berray.components.core.AreaComponent;
import com.berray.components.core.CircleComponent;
import com.berray.components.core.PosComponent;
import com.berray.event.Event;
import com.berray.examples.pong.data.GameData;
import com.berray.math.Rect;
import com.berray.math.Vec2;

public class Ball extends GameObject {

  public static final int RADIUS = 16;
  private final GameData gameData;
  private Vec2 velocity;

  public Ball(GameData gameData) {
    super();
    this.gameData = gameData;
    addComponents(
        // initialize pos with zero. Once the object is added to the scene, we can get the center
        // center of the screen.
        PosComponent.pos(0, 0),
        CircleComponent.circle(RADIUS),
        AreaComponent.area(new Rect(-RADIUS, -RADIUS, RADIUS * 2, RADIUS * 2))
    );
    velocity = Vec2.fromAngle((float) ((Math.random() - 0.5) * 40));
    on("update", this::onUpdate);
    // register to add event to set start position to center of screen
    on("add", this::onAdd);
    onCollide("paddle", this::onCollideWithPaddle);
  }

  private void onAdd(Event event) {
    GameObject addedObject = event.getParameter(1);
    if (addedObject == this) {
      // now we have access to the game object and can set the start position to the center of the screen
      set("pos", game.center());
    }
  }

  private void onCollideWithPaddle(Event event) {
    gameData.setSpeed(gameData.getSpeed() + 60);
    GameObject other = event.getParameter(0);
    Vec2 ballPos = get("pos");
    Vec2 otherPos = other.get("pos");
    velocity = Vec2.fromAngle(ballPos.angle(otherPos));
    gameData.setScore(gameData.getScore() + 1);
  }

  private void onUpdate(Event event) {
    float deltaTime = event.getParameter(0);
    Vec2 vel = velocity;
    // calculate new frame position based on pos and current velocity
    Vec2 pos = get("pos");
    pos = pos.move(vel.scale(gameData.getSpeed() * deltaTime));

    // if the ball leaves the screen to the sides, reset score, ball position and velocity
    if (pos.getX() < 0 || pos.getX() > game.width()) {
      gameData.setScore(0);
      pos = game.center();
      vel = Vec2.fromAngle((float) ((Math.random() - 0.5) * 40));
      gameData.setSpeed(320);
    }
    // if the ball collides with the top or bottom screen, invert the y part of the velocity
    if (pos.getY() < RADIUS || pos.getY() > (game.height() - RADIUS)) {
      vel.setY(-vel.getY());
    }
    set("pos", pos);
    velocity = vel;
  }
}
