package com.pyxx.chinesetourism.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义画虚线
 * 
 * @author wll
 */
@SuppressLint("DrawAllocation")
public class DashedLine extends View {

	public DashedLine(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.DKGRAY);
		Path path = new Path();
		path.moveTo(0, 10);
		path.lineTo(1280, 10);
		//DashPathEffect:虚线路径效果
		PathEffect effects = new DashPathEffect(new float[] { 10, 10, 10, 10 }, 2);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}
	
	/* 在DashPathEffect(new float[] { 1, 2, 4, 8}, 1)中:
	 * float数组,必须是偶数长度,且>=2,指定了多少长度的实线之后再画多少长度的空白.
	 * 如本代码中,绘制长度1的实线,再绘制长度2的空白,再绘制长度4的实线,再绘制长度8的空白,依次重复.
	 * 2是偏移量,可以不用理会.
	 */

}
