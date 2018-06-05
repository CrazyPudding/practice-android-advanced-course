package com.crazypudding.practice.propertyanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PulseAnimationView extends View {

    private float radius;
    private final Paint mPaint = new Paint();
    private static final int COLOR_ADJUST = 5;
    private static final int ANIMATION_DURATION = 4000;
    private static final long ANIMATION_DELAY = 1000;

    private float mX, mY;
    private AnimatorSet mPulseAnimatorSet = new AnimatorSet();

    public PulseAnimationView(Context context) {
        this(context, null);
    }

    public PulseAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mY, radius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mX = event.getX();
            mY = event.getY();
        }
        if (mPulseAnimatorSet != null && mPulseAnimatorSet.isRunning()) {
            mPulseAnimatorSet.cancel();
        }
        mPulseAnimatorSet.start();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ObjectAnimator growAnimator = ObjectAnimator.ofFloat(this, "radius", 0, getWidth());
        growAnimator.setDuration(ANIMATION_DURATION);
        ObjectAnimator shrinkAnimator = ObjectAnimator.ofFloat(this, "radius", getWidth(), 0);
        shrinkAnimator.setDuration(ANIMATION_DURATION);
        shrinkAnimator.setStartDelay(ANIMATION_DELAY);
        ObjectAnimator repeatAnimator = ObjectAnimator.ofFloat(this, "radius", 0, getWidth());
        repeatAnimator.setDuration(ANIMATION_DURATION);
        repeatAnimator.setStartDelay(ANIMATION_DELAY);
        repeatAnimator.setRepeatCount(1);
        repeatAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mPulseAnimatorSet.play(growAnimator).before(shrinkAnimator);
        mPulseAnimatorSet.play(repeatAnimator).after(shrinkAnimator);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        mPaint.setColor((int) (Color.GREEN + radius / COLOR_ADJUST));
        invalidate();
    }
}
