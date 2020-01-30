package com.itschool.draw.surfaceviewthread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MySurfaceThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private MuView muView;
    private boolean flag = false;


    public MySurfaceThread(SurfaceHolder holder, MuView view) {
        surfaceHolder = holder;
        muView = view;
    }


    public void setRunning(boolean b) {
        flag = b;
    }


    @Override
    public void run() {
        while (flag) {
            Canvas canvas = null;

            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    muView.onDraw(canvas);
                }

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                if (canvas !=null) surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
