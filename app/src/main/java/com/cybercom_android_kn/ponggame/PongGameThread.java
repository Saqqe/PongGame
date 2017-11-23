package com.cybercom_android_kn.ponggame;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.SurfaceHolder;

/**
 * Created by Saqib on 2017-11-22.
 */

public class PongGameThread extends Thread{
    public static final int MAX_FPS = 30;
    private double avarageFPS;
    private SurfaceHolder surfaceHolder;
    private PongView pongView;
    private boolean running;
    private static Canvas canvas;

    public PongGameThread(SurfaceHolder surfaceHolder, PongView pongView){
        this.surfaceHolder = surfaceHolder;
        this.pongView = pongView;

    }


    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            this.canvas = null;

            try{
                this.canvas = this.surfaceHolder.lockCanvas();
                synchronized (this.surfaceHolder){
                    this.pongView.update();
                    this.pongView.draw(this.canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (this.canvas != null){
                    try{
                        this.surfaceHolder.unlockCanvasAndPost(this.canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try {
                if (waitTime > 0){
                    this.sleep(waitTime);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == MAX_FPS) {
                avarageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(avarageFPS);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
