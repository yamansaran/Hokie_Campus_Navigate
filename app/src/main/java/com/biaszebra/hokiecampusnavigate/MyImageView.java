package com.biaszebra.hokiecampusnavigate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jsibbold.zoomage.ZoomageView;


public class MyImageView extends ZoomageView {

    public MyImageView(Context context) {
        super(context);

        invalidate();
        // TODO Auto-generated constructor stub
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        invalidate();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        invalidate();
    }

    public void onDrawCo(float startX , float startY, float endX, float endY , Canvas canvas){
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        canvas.drawLine(startX,startY,endX,endY,p);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        canvas.drawLine(0, 0, 100, 100, p);

        canvas.drawLine(0, 0, 20, 20, p);
        canvas.drawLine(20, 0, 0, 20, p);
        super.onDraw(canvas);


    }
}
