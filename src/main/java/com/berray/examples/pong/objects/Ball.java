package com.berray.examples.pong.objects;

import com.berray.GameObject;
import com.berray.components.AreaComponent;
import com.berray.components.CircleComponent;
import com.berray.components.PosComponent;
import com.berray.event.Event;
import com.berray.examples.pong.data.GameData;
import com.berray.math.Rect;
import com.berray.math.Vec2;

public class Ball extends GameObject {

  private final GameData gameData;

  public Ball(GameData gameData) {
    super();
    this.gameData = gameData;
    addComponents(
        PosComponent.pos(new Vec2(gameData.getWidth() / 2.0f, gameData.getHeight() / 2.0f)),
        CircleComponent.circle(16),
//        outline(4),
        AreaComponent.area(new Rect(-16, -16, 32, 32))
    );
    setProperty("vel", Vec2.fromAngle((float) ((Math.random() - 0.5) * 40)));
    on("update", this::onUpdate);
    onCollide("paddle", this::onCollideWithPaddle);
  }

  private void onCollideWithPaddle(Event event) {
    gameData.setSpeed(gameData.getSpeed() + 60);
    GameObject other = event.getParameter(0);
    Vec2 ballPos = get("pos");
    Vec2 otherPos = other.get("pos");
    setProperty("vel", Vec2.fromAngle(ballPos.angle(otherPos)));
    gameData.setScore(gameData.getScore() + 1);
  }

  private void onUpdate(Event event) {
    float deltaTime = event.getParameter(0);
    Vec2 vel = getProperty("vel");
    Vec2 pos = get("pos");
    pos = pos.move(vel.scale(gameData.getSpeed() * deltaTime));

    if (pos.getX() < 0 || pos.getX() > gameData.getWidth()) {
      gameData.setScore(0);
      pos = new Vec2(gameData.getWidth() / 2.0f, gameData.getHeight() / 2.0f);
      vel = Vec2.fromAngle((float) ((Math.random() - 0.5) * 40));
      gameData.setSpeed(320);
    }
    if (pos.getY() < 0 || pos.getY() > gameData.getHeight()) {
      vel.setY(-vel.getY());
    }
    set("pos", pos);
    setProperty("vel", vel);
  }


}
