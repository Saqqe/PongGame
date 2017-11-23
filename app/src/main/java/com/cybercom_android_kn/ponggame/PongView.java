package com.cybercom_android_kn.ponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Saqib on 2017-11-22.
 */

public class PongView extends SurfaceView implements SurfaceHolder.Callback {
    private PongGameThread gameThread;

    private Player player;
    private Point playerPoint;

    public PongView(Context context) {
        super(context);

        getHolder().addCallback(this);

        this.gameThread = new PongGameThread(getHolder(), this);

        Paint humanPlayerPaint = new Paint();
        humanPlayerPaint.setAntiAlias(true);
        humanPlayerPaint.setColor(Color.BLUE);
        this.player = new Player(50, 200, humanPlayerPaint);
        this.playerPoint = player.getCenterPoint();

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameThread = new PongGameThread(getHolder(), this);

        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry){
            try{
                gameThread.setRunning(false);
                gameThread.join();
            }catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                this.playerPoint.set(50, (int)event.getY());
        }
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        this.player.update(playerPoint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);

        this.player.draw(canvas);
    }
}
