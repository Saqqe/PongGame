package com.cybercom_android_kn.ponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static java.lang.Math.abs;

/**
 * Created by Saqib on 2017-11-22.
 */

public class PongView extends SurfaceView implements SurfaceHolder.Callback {
    //Game thread
    private PongGameThread gameThread;

    //Real player
    private Player player;
    private Point playerPoint;

    //AI player
    private Player aiPlayer;
    private Point aiPlayerPoint;
    private int aiYPos = this.getHeight()/2;
    private boolean aiMoveDir = true; //true = up, false = down

    //Common
    private int paddleWidth = 50;
    private int paddleHeight = 200;
    private int paddleCenter = (paddleHeight/2);

    public PongView(Context context) {
        super(context);

        getHolder().addCallback(this);

        this.gameThread = new PongGameThread(getHolder(), this);

        Paint playerPaint = new Paint();
        playerPaint.setAntiAlias(true);
        playerPaint.setColor(Color.WHITE);

        this.player = new Player(paddleWidth, paddleHeight, playerPaint);
        this.playerPoint = player.getCenterPoint();

        Paint aiPaint = new Paint();
        aiPaint.setAntiAlias(true);
        aiPaint.setColor(Color.BLUE);
        this.aiPlayer = new Player(paddleWidth, paddleHeight, aiPaint);
        this.aiPlayerPoint = aiPlayer.getCenterPoint();

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameThread = new PongGameThread(getHolder(), this);

        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder,
                               int format,
                               int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry){
            try{
                gameThread.setRunning(false);
                gameThread.join();
                retry = false;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                this.playerPoint.set(this.getLeft() + paddleWidth,
                                     getPlayerPos((int)event.getY()));

        }
        return true;
        //return super.onTouchEvent(event);
    }


    public void update(){
        this.player.update(this.playerPoint);
        updateAI();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);

        this.player.draw(canvas);
        this.aiPlayer.draw(canvas);
    }

    /* PRIVATE METHOD SECTION */
    private int getPlayerPos(int eventY){
        int yPos;

        if ( (eventY + paddleCenter) >= this.getBottom() )
        {
            yPos = this.getBottom() - paddleCenter;
        }
        else if ( (eventY - paddleCenter) <= this.getTop() ){
            yPos = this.getTop() + paddleCenter ;
        }
        else {
            yPos = eventY;
        }

        return yPos;
    }

    private void updateAI() {
        this.aiPlayerPoint.set(this.getRight() - paddleWidth, aiYPos);
        this.aiPlayer.update(this.aiPlayerPoint);

        if ((aiYPos + paddleCenter) == this.getBottom())
        {
            aiMoveDir = true;
        }
        else if ((aiYPos - paddleCenter) <= this.getTop())
        {
            aiMoveDir = false;
        }

        aiYPos = (aiMoveDir) ? (aiYPos -=10) : (aiYPos +=10);
    }
}