package com.github.nekdenis.currencylist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColumnGraph extends View {

    public static final int LINES_COUNT = 5;
    private List<Float> datapoints = new ArrayList<Float>();
    private Paint paint = new Paint();
    private Paint textPaint = new Paint();
    private float textLenght;
    private int extraPadding;

    public ColumnGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setChartData(List<Float> datapoints) {
        this.datapoints.clear();
        this.datapoints.addAll(datapoints);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (datapoints.size() != 0) {
            drawBackground(canvas);
            drawLineChart(canvas);
        }
    }

    private void drawBackground(Canvas canvas) {
        float maxValue = getMax();
        float minValue = getMin();
        float range = getLineDistance(maxValue, minValue);

        paint.setStyle(Style.STROKE);
        paint.setColor(Color.GRAY);
        textPaint.setStyle(Style.STROKE);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(22);
        textPaint.setAntiAlias(true);
        textLenght = textPaint.measureText(String.valueOf(maxValue));
        for (float y = minValue; y < maxValue; y += range) {
            final float yPos = getYPos(y);
            canvas.drawLine(textLenght + 4, yPos, getWidth(), yPos, paint);
            canvas.drawText(String.valueOf(y), 0, yPos, textPaint);
        }
    }

    private float getLineDistance(float maxValue, float minValue) {
        float difference = maxValue - minValue;
        float distance = difference / LINES_COUNT;
        return distance;

    }

    private void drawLineChart(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getXPos(0), getYPos(datapoints.get(0)));
        for (int i = 1; i < datapoints.size(); i++) {
            path.lineTo(getXPos(i), getYPos(datapoints.get(i)));
        }

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(0xFF33B5E5);
        paint.setAntiAlias(true);
        paint.setShadowLayer(4, 2, 2, 0x80000000);
        canvas.drawPath(path, paint);
        paint.setShadowLayer(0, 0, 0, 0);
    }

    private float getMax() {
        return Collections.max(datapoints);
    }

    private float getMin() {
        return Collections.min(datapoints);
    }


    private int getYPos(float value) {
        value = value - getMin();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        float diff =  getMax() - getMin();
        int result = (int) ((value * height) / diff);
        result = height - result;
        result += getPaddingTop();
        return result;
    }

    private float getXPos(float value) {
        float width = getWidth() - getPaddingLeft() - getPaddingRight() - textLenght;
        float maxValue = datapoints.size() - 1;
        value = (value / maxValue) * width;
        value += getPaddingLeft();
        value += textLenght;
        return value;
    }

}
