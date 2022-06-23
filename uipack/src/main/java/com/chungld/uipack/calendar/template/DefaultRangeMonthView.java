package com.chungld.uipack.calendar.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.chungld.uipack.R;
import com.chungld.uipack.calendar.RangeMonthView;
import com.chungld.uipack.calendar.TrpCalendar;


/**
 * Created by ChungLD on 2018/2/9.
 */

public class DefaultRangeMonthView extends RangeMonthView {

    private int mRadius;

    private final Paint mTextPaint = new Paint();

    private final Paint mSolarTermTextPaint = new Paint();

    private final float mLineSpace;

    private final int mPadding;

    private final float mCircleRadius;

    private final Paint mSchemeBasicPaint = new Paint();

    private final float mSchemeBaseLine;

    public DefaultRangeMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

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

        mCircleRadius = context.getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_8);
        mLineSpace = context.getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_1);
        mPadding = dipToPx(getContext(), 3);
        mRadius = getContext().getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_10);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);
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
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = getContext().getResources().getDimensionPixelSize(R.dimen.trp_uipack_dip_8);
    }

    @Override
    protected void onDrawText(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;

        if (hasScheme) {
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);
            mTextPaint.setColor(calendar.getSchemeColor());
            canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
        }

        mCurMonthTextPaint.setColor(0xff333333);
        mCurMonthLunarTextPaint.setColor(0xff757575);
        mSchemeTextPaint.setColor(0xff333333);

        mOtherMonthTextPaint.setColor(0xFFe1e1e1);

        if (isSelected) {
            boolean isBetween = isBetweenRange(calendar);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y, isBetween ? mSchemeTextPaint : mSelectTextPaint);
        } else {
            boolean isInRange = isInRange(calendar);
            boolean isEnable = !onCalendarIntercept(calendar);

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange && isEnable ? mSchemeTextPaint : mOtherMonthTextPaint);
        }
    }

    @Override
    protected void onDrawCell(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {

    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected int getCellHeight(int itemWidth) {
        return itemWidth;
    }

}
