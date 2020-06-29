package com.example.paintapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class MyCanvas extends View implements View.OnTouchListener{

    Paint m_Paint;
    Path m_Path;
    Canvas m_Canvas;
    public static boolean isEraserActive = false;
    public static boolean undo1 = false;
    private int color = Color.BLACK;
    private int stroke = 6;
    private float mX, mY;
    ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
    ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();
    private static final float TOUCH_TOLERANCE = 4;



    public MyCanvas(final Context context, AttributeSet attrs) {
        super(context, attrs);
       onCanvasInitialization();

    }

    /**
     * Initialize all objects required for drawing here.
     * One time initialization reduces resource consumption.
     */
    public void onCanvasInitialization() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.WHITE);
        this.setOnTouchListener(this);
        m_Paint = new Paint();
        m_Paint.setAntiAlias(true);
        m_Paint.setDither(true);
        m_Paint.setColor(Color.parseColor("#000000"));
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeJoin(Paint.Join.ROUND);
        m_Paint.setStrokeCap(Paint.Cap.ROUND);
        m_Paint.setStrokeWidth(2);

        m_Canvas = new Canvas();

        m_Path = new Path();
        Paint newPaint = new Paint(m_Paint);
        paths.add(new Pair<Path, Paint>(m_Path, newPaint));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        {
            for (Pair<Path, Paint> p : paths) {
                canvas.drawPath(p.first, p.second);
            }
        }
        super.onDraw(canvas);
    }



    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touch_start(xPos, yPos);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(xPos, yPos);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
            default:
               break;
        }
        invalidate();
        return true;
    }

    private void touch_start(float x, float y) {

        if (isEraserActive) {
            m_Paint.setColor(Color.WHITE);
            m_Paint.setStrokeWidth(50);
            Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
            paths.add(new Pair<Path, Paint>(m_Path, newPaint));
        } else {
            m_Paint.setColor(color);
            m_Paint.setStrokeWidth(stroke);
            Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
            paths.add(new Pair<Path, Paint>(m_Path, newPaint));
        }

        m_Path.reset();
        m_Path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_up() {
        m_Path.lineTo(mX, mY);

        // commit the path to our offscreen
        m_Canvas.drawPath(m_Path, m_Paint);

        // kill this so we don't double draw
        m_Path = new Path();
        Paint newPaint = new Paint(m_Paint); // Clones the mPaint object
        paths.add(new Pair<Path, Paint>(m_Path, newPaint));
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            m_Path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void onClickUndo() {
        if (!paths.isEmpty()) {//paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            undo1 = true;
            invalidate();
        }
    }

    public void onClickRedo() {
        if (!undonePaths.isEmpty()) {//undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            undo1 = true;
            invalidate();
        }
    }
    public void clear()
    {
        m_Path.reset();
    }

}
