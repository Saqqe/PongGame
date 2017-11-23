package com.cybercom_android_kn.ponggame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by Saqib on 2017-11-22.
 */

public class Player implements GameObject {

    private int paddleWidth;
    private int paddleHeight;
    private Paint paint;
    private int score;
    private RectF playerPaddle;
    private int collision;

    Player(int paddleWidth, int paddleHeight, Paint paint) {
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.paint = paint;
        this.score = 0;
        this.playerPaddle = new RectF(0, 0, paddleWidth, paddleHeight);
        this.collision = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(this.playerPaddle, this.paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        //l,t,r,b
        this.playerPaddle.set(point.x - this.playerPaddle.width()/2,
                              point.y - this.playerPaddle.height()/2,
                              point.x + this.playerPaddle.width()/2,
                              point.y + this.playerPaddle.height()/2);
    }

    public Point getCenterPoint() {
        Point centerPoint = new Point((int)this.playerPaddle.centerX(),
                                      (int)this.playerPaddle.centerY());
        return centerPoint;
    }
}
