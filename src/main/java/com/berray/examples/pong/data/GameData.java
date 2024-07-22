package com.berray.examples.pong.data;

import com.berray.math.Vec2;

public class GameData {
  private int width = 1024;
  private int height = 768;

  private int score = 0;
  private int speed = 480;

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public Vec2 center() {
    return new Vec2(width/2.0f, height/2.0f);
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

}
