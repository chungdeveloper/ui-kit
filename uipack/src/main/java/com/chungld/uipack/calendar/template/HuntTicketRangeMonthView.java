package com.chungld.uipack.calendar.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.chungld.uipack.calendar.TrpCalendar;
import com.chungld.uipack.calendar.NumberUtils;
import com.chungld.uipack.calendar.RangeMonthView;
import com.chungld.uipack.calendar.model.TicketHuntBundle;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Chungld on 2020/10/15.
 */

public class HuntTicketRangeMonthView extends RangeMonthView {

    private final Paint mTextPaint = new Paint();

    private final Paint mSolarTermTextPaint = new Paint();

    private final int mPadding;

    private final float mCircleRadius;

    private final Paint mSchemeBasicPaint = new Paint();

    private final float mSchemeBaseLine;
    private HashMap<String, BigDecimal> priceMap;
    private HashMap<String, BigDecimal> minPriceMap;

    public HuntTicketRangeMonthView(Context context) {
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

        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }

    private void initAdditionData() {
        if (!(getAdditionData() instanceof TicketHuntBundle)) return;
        TicketHuntBundle mAdditionData = (TicketHuntBundle) getAdditionData();
        priceMap = mAdditionData.getAdditionObjectAt(0);
        minPriceMap = mAdditionData.getAdditionObjectAt(1);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme,
                                     boolean isSelectedPre, boolean isSelectedNext) {
        int cy = y + mItemHeight / 2;
        int itemTop = cy - mItemHeight / 2;
        int itemBottom = cy + mItemHeight / 2;
        if (isSelectedPre) {
            if (isSelectedNext) {//Draw selected between
                canvas.drawRect(x, itemTop, x + mItemWidth, itemBottom, mSelectedRangePaint);
            } else {//Draw last selected
                canvas.drawRect(x, itemTop, x + mItemWidth, itemBottom, mSelectedPaint);
            }
        } else {
            if (isSelectedNext) {//Draw first haft fill
                canvas.drawRect(x, itemTop, x + mItemWidth, itemBottom, mSelectedPaint);
            }
            //Draw first circle haft fill
            canvas.drawRect(x, itemTop, x + mItemWidth, itemBottom, mSelectedPaint);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, TrpCalendar calendar, int x, int y, boolean isSelected) {

    }

    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        initAdditionData();
    }

    @Override
    protected void onDrawText(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;

        if (hasScheme) {
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);
            mTextPaint.setColor(calendar.getSchemeColor());
            canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
        }

        mCurMonthTextPaint.setColor(0xff333333);
        mCurMonthLunarTextPaint.setColor(0xff757575);
        mSchemeTextPaint.setColor(0xff333333);
        mSchemeLunarTextPaint.setColor(0xffffffff);

        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthLunarTextPaint.setColor(0xffBDBDBD);

        if (isSelected) {
            boolean isBetween = isBetweenRange(calendar);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, isBetween ? mSchemeTextPaint : mSelectTextPaint);
            drawPriceHunt(canvas, cx, y, mSelectedLunarTextPaint, isSelected, isBetween, true, calendar);
        } else {
            boolean isInRange = isInRange(calendar);
            boolean isEnable = !onCalendarIntercept(calendar);

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange && isEnable ? mSchemeTextPaint : mOtherMonthTextPaint);

            drawPriceHunt(canvas, cx, y, calendar.isCurrentMonth() && isInRange && isEnable ?
                    mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint, isSelected, false, isInRange, calendar);
        }
    }

    @Override
    protected void onDrawCell(Canvas canvas, TrpCalendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int line = dipToPx(getContext(), 1);
        Paint borderPaint = new Paint();
        borderPaint.setColor(0xffF5F5F5);
        borderPaint.setStrokeWidth(line);
        borderPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, borderPaint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawPriceHunt(Canvas canvas, int cx, int y, Paint paint, boolean isSelected, boolean isBetween, boolean isInRange, TrpCalendar calendar) {
        if (priceMap == null || !isInRange) return;
        String dateFormatted = getStringFromLong(calendar.getTimeInMillis(), "dd-MM-yyyy");
        BigDecimal price = priceMap.get(dateFormatted);
        if (price == null) return;
        paint.setColor(isSelected && !isBetween ? 0xffffffff : getColorPrice(dateFormatted, price));
        canvas.drawText(formatPrice(price), cx, mTextBaseLine + y + mItemHeight / 10, paint);
    }

    private int getColorPrice(String dateFormatted, BigDecimal price) {
        BigDecimal priceCondition;
        if (minPriceMap != null &&
                (priceCondition = minPriceMap.get(dateFormatted.substring(3))) != null &&
                price.equals(priceCondition)) {
            return 0xff00AD50;
        }
        return 0xffBDBDBD;
    }

    private String formatPrice(BigDecimal price) {
        return NumberUtils.formatCurrency(price.longValue() / 1000) + "k";
    }

    public static String getStringFromLong(long date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, new Locale("vi"));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(date);
        return formatter.format(cal.getTime());
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

//    @Override
//    protected int getCellHeight(int mItemWidth) {
//        return mItemWidth * 44 / 49;
//    }
}
