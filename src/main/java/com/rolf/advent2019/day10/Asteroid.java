package com.rolf.advent2019.day10;

public class Asteroid {

    private final int x;
    private final int y;
    private double angle;
    private double distance;

    public Asteroid(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setCenterPoint(final int x, final int y) {
//        this.angle = Math.toDegrees(Math.atan2(this.y - y, x - this.x));
        this.angle = Math.toDegrees(Math.atan2(this.x - x, y - this.y));
        if (this.angle < 0) {
            angle += 360;
        }
        this.distance = Math.sqrt((y - this.y) * (y - this.y) + (x - this.x) * (x - this.x));
    }

    @Override
    public String toString() {
        return "Asteroid{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", distance=" + distance +
                '}';
    }
}
