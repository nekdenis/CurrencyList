package com.github.nekdenis.currencylist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColumnGraph extends View {

    private static final int MIN_LINES = 4;
    private static final int MAX_LINES = 7;
    private static final int[] DISTANCES = {1, 2, 5};

    private List<Float> datapoints = new ArrayList<Float>();
    private Paint paint = new Paint();
    private Paint textPaint = new Paint();
    private float textLenght;

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
        int range = getLineDistance(maxValue);

        paint.setStyle(Style.STROKE);
        paint.setColor(Color.GRAY);
        textPaint.setStyle(Style.STROKE);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(22);
        textPaint.setAntiAlias(true);
        textLenght = textPaint.measureText(String.valueOf(maxValue));
        for (int y = 0; y < maxValue; y += range) {
            final float yPos = getYPos(y);
            canvas.drawLine(textLenght + 4, yPos, getWidth(), yPos, paint);
            canvas.drawText(String.valueOf(y), 0, yPos, textPaint);
        }
    }

    private int getLineDistance(float maxValue) {
        int distance;
        int distanceIndex = 0;
        int distanceMultiplier = 1;
        int numberOfLines = MIN_LINES;

        do {
            distance = DISTANCES[distanceIndex] * distanceMultiplier;
            numberOfLines = (int) FloatMath.ceil(maxValue / distance);

            distanceIndex++;
            if (distanceIndex == DISTANCES.length) {
                distanceIndex = 0;
                distanceMultiplier *= 10;
            }
        } while (numberOfLines < MIN_LINES || numberOfLines > MAX_LINES);

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

    private float getYPos(float value) {
        float height = getHeight() - getPaddingTop() - getPaddingBottom();
        float maxValue = getMax();
        value = (value / maxValue) * height;
        value = height - value;
        value += getPaddingTop();
        return value;
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
