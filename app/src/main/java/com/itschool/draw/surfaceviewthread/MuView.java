package com.itschool.draw.surfaceviewthread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

class MuView extends SurfaceView implements SurfaceHolder.Callback {

    private MySurfaceThread thread;
    boolean drawing;
    Paint paint;
    float initX, initY;
    float targetX, targetY;

    public MuView(Context context) {
        super(context);
        init();

    }

    private void init() {
        thread = new MySurfaceThread(getHolder(), this);
        getHolder().addCallback(this);
        setFocusable(true);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);

        initX = targetX = 0;
        initY = targetY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawing) {
            canvas.drawRGB(0,0,0);
            canvas.drawCircle(initX,initY,30, paint);
            if ((initX == targetX) && (initY == targetY)) {
                drawing = false;
            }
            else {
                initX = (initX + targetX) / 2;
                initY = (initY + targetY) / 2;
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();


        if (action == MotionEvent.ACTION_DOWN) {
            targetX = event.getX();
            targetY = event.getY();
            drawing = true;
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


        boolean retry = true;
        thread.setRunning(false);
        while (retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
