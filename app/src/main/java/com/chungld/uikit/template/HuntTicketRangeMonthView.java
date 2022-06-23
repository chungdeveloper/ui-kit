package com.chungld.uikit.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.chungld.uikit.R;
import com.chungld.uipack.calendar.RangeMonthView;
import com.chungld.uipack.calendar.TrpCalendar;


/**
 * Created by Chungld on 2020/10/15.
 */

public class HuntTicketRangeMonthView extends RangeMonthView {

    private final Paint mSolarTermTextPaint = new Paint();

    private final float mLineSpace;
    private final int mRadius;

    public HuntTicketRangeMonthView(Context context) {
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

        mLineSpace = context.getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_1);
        mRadius = getContext().getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_10);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme,
                                     boolean isSelectedPre, boolean isSelectedNext) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        int itemTop = cy - mItemHeight / 2;
        int itemBottom = cy + mItemHeight / 2;
        if (isSelectedPre) {
            if (isSelectedNext) {//Draw selected between
                canvas.drawRect(x, itemTop + mLineSpace, x + mItemWidth, itemBottom - mLineSpace, mSelectedRangePaint);
            } else {//Draw last selected
                canvas.drawRect(x, itemTop + mLineSpace, cx - mLineSpace - mLineSpace, itemBottom - mLineSpace, mSelectedPaint);
                canvas.drawRoundRect(new RectF(x, itemTop + mLineSpace, x + mItemWidth, itemBottom - mLineSpace), mRadius, mRadius, mSelectedPaint);
            }
        } else {
            if (isSelectedNext) {//Draw first haft fill
                canvas.drawRect(cx - mLineSpace, itemTop + mLineSpace, x + mItemWidth, itemBottom - mLineSpace, mSelectedPaint);
            }
            //Draw first selected
            canvas.drawRoundRect(new RectF(x, itemTop + mLineSpace, x + mItemWidth, itemBottom - mLineSpace), mRadius, mRadius, mSelectedPaint);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, TrpCalendar calendar, int x, int y, boolean isSelected) {

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
        mCurMonthLunarTextPaint.setColor(0xff718096);
        mSchemeTextPaint.setColor(0xff1A202C);
        mSchemeLunarTextPaint.setColor(0xffffffff);

        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthLunarTextPaint.setColor(0xffBDBDBD);
        boolean isBetween = isBetweenRange(calendar);
        if (hasScheme) {
            Paint paint = isSelected && !isBetween ? mSelectedLunarTextPaint : calendar.isCurrentMonth() && isEnable && isInRange ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint;
            drawPriceHunt(canvas, cx, y, paint, isSelected, isBetween, isInRange, calendar);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, isBetween ? mSchemeTextPaint : mSelectTextPaint);
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

    private void drawPriceHunt(Canvas canvas, int cx, int y, Paint paint, boolean isSelected, boolean isBetween, boolean isInRange, TrpCalendar calendar) {
        if (!isInRange) return;
        paint.setColor(isSelected ? isBetween ? 0xff718096 : 0xffffffff : calendar.getSchemeColor());
        canvas.drawText(calendar.getScheme(), cx, mTextBaseLine + y + mItemHeight / 10.0f, paint);
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
