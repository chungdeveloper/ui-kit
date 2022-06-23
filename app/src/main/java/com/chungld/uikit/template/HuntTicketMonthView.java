package com.chungld.uikit.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.chungld.uikit.R;
import com.chungld.uipack.calendar.MonthView;
import com.chungld.uipack.calendar.TrpCalendar;


/**
 * Created by Chungld on 2020/10/15.
 */

public class HuntTicketMonthView extends MonthView {

    private final Paint mSolarTermTextPaint = new Paint();

    private final float mCircleRadius;

    public HuntTicketMonthView(Context context) {
        super(context);

        Paint mTextPaint = new Paint();
        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        Paint mSchemeBasicPaint = new Paint();
        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);


        Paint mCurrentDayPaint = new Paint();
        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);

        Paint mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

        mCircleRadius = getContext().getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_10);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme) {
        int cy = y + mItemHeight / 2;
        int itemTop = cy - mItemHeight / 2;
        int itemBottom = cy + mItemHeight / 2;
        RectF rect = new RectF(x, itemTop, x + mItemWidth, itemBottom);
        canvas.drawRoundRect(rect, mCircleRadius, mCircleRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, TrpCalendar calendar, int x, int y) {

    }

    @Override
    protected int getCellHeight(int itemWidth) {
        return itemWidth;
    }

    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
    }

    @Override
    protected void onDrawText(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;
        boolean isEnable = !onCalendarIntercept(calendar);
        boolean isInRange = isInRange(calendar);

        mCurMonthTextPaint.setColor(0xff333333);
        mCurMonthLunarTextPaint.setColor(0xff757575);
        mSchemeTextPaint.setColor(0xff333333);
        mSchemeLunarTextPaint.setColor(0xffffffff);

        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthLunarTextPaint.setColor(0xffBDBDBD);

        if (hasScheme) {
            Paint paint = isSelected ? mSelectedLunarTextPaint : calendar.isCurrentMonth() && isEnable && isInRange ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint;
            drawPriceHunt(canvas, cx, y, paint, isSelected, isInRange, calendar);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isEnable && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
        }
    }

    @Override
    protected void onDrawCell(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
//        int line = dipToPx(getContext(), 1);
//        Paint borderPaint = new Paint();
//        borderPaint.setColor(0xffF5F5F5);
//        borderPaint.setStrokeWidth(line);
//        borderPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, borderPaint);
    }

    private void drawPriceHunt(Canvas canvas, int cx, int y, Paint paint, boolean isSelected, boolean isInRange, TrpCalendar calendar) {
        if (!isInRange) return;
        paint.setColor(isSelected ? 0xffffffff : calendar.getSchemeColor());
        canvas.drawText(calendar.getScheme(), cx, mTextBaseLine + y + mItemHeight / 10.0f, paint);
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
