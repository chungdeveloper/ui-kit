package com.chungld.uipack.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chungld.uipack.R;

import java.lang.reflect.Constructor;
import java.util.List;

public class MonthViewList extends RecyclerView {
    public CalendarLayout mParentLayout;
    private CalendarViewDelegate mDelegate;

    public MonthViewList(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MonthViewList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonthViewList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
    }

    public void setDelegate(CalendarViewDelegate mDelegate) {
        this.mDelegate = mDelegate;
        setAdapter(new MonthViewAdapter().setViewDelegate(mDelegate));
    }

    public void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            com.chungld.uipack.calendar.BaseMonthView view = (com.chungld.uipack.calendar.BaseMonthView) (((ViewGroup) ((ViewGroup) getChildAt(i)).getChildAt(1)).getChildAt(0));
            view.update();
        }
    }

    public void updateStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            com.chungld.uipack.calendar.BaseMonthView view = (com.chungld.uipack.calendar.BaseMonthView) (((ViewGroup) ((ViewGroup) getChildAt(i)).getChildAt(1)).getChildAt(0));
            view.updateStyle();
            view.invalidate();
        }
    }

    public void updateDefaultSelect() {
        com.chungld.uipack.calendar.BaseMonthView view = findViewWithTag(getCurrentItem());
        if (view != null) {
            int index = view.getSelectedIndex(mDelegate.mSelectedCalendar);
            view.mCurrentItem = index;
            if (index >= 0 && mParentLayout != null) {
                mParentLayout.updateSelectPosition(index);
            }
            view.invalidate();
        }
    }

    public void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            com.chungld.uipack.calendar.BaseMonthView view = (com.chungld.uipack.calendar.BaseMonthView) (((ViewGroup) ((ViewGroup) getChildAt(i)).getChildAt(1)).getChildAt(0));
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    public void updateWeekStart() {
        if (getAdapter() == null) return;
        getAdapter().notifyDataSetChanged();
    }

    public void updateShowMode() {
//        for (int i = 0; i < getChildCount(); i++) {
//            BaseMonthView view = (BaseMonthView) getChildAt(i);
//            view.updateShowMode();
//            view.requestLayout();
//        }
//        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
//            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
//            mNextViewHeight = mCurrentViewHeight;
//            mPreViewHeight = mCurrentViewHeight;
//        } else {
//            updateMonthViewHeight(mDelegate.mSelectedCalendar.getYear(), mDelegate.mSelectedCalendar.getMonth());
//        }
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.height = mCurrentViewHeight;
//        setLayoutParams(params);
//        if (mParentLayout != null) {
//            mParentLayout.updateContentViewTranslateY();
//        }
    }

    public void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            com.chungld.uipack.calendar.BaseMonthView view = (com.chungld.uipack.calendar.BaseMonthView) (((ViewGroup) ((ViewGroup) getChildAt(i)).getChildAt(1)).getChildAt(0));
            view.updateCurrentDate();
        }
    }

    public List<TrpCalendar> getCurrentMonthCalendars() {
        return null;
    }

    public void updateRange() {
        if (getAdapter() == null) return;
        if (getAdapter() instanceof MonthViewAdapter) {
            ((MonthViewAdapter) getAdapter()).updateMonthCount();
        }
        getAdapter().notifyDataSetChanged();
        if (getVisibility() != VISIBLE) {
            return;
        }
        TrpCalendar calendar = mDelegate.mSelectedCalendar;
        if (calendar == null) return;
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        com.chungld.uipack.calendar.BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }


        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        updateSelected();
    }

    public int getCurrentItem() {
        return 0;
    }

    public void setCurrentItem(int position, boolean b) {

    }

    public void setCurrentItem(int mCurrentMonthViewItem) {

    }

    final void clearSelectRange() {
        if (getAdapter() == null) return;
        getAdapter().notifyDataSetChanged();
//        for (int i = 0; i < getChildCount(); i++) {
//            if (!(getChildAt(i) instanceof BaseMonthView)) continue;
//            BaseMonthView view = (BaseMonthView) getChildAt(i);
//            view.invalidate();
//        }
    }

    final void clearSingleSelect() {
        if (getAdapter() == null) return;
        getAdapter().notifyDataSetChanged();
//        for (int i = 0; i < getChildCount(); i++) {
//            if (!(getChildAt(i) instanceof BaseMonthView)) continue;
//            BaseMonthView view = (BaseMonthView) getChildAt(i);
//            view.mCurrentItem = -1;
//            view.invalidate();
//        }
    }

    final void clearMultiSelect() {
        if (getAdapter() == null) return;
        getAdapter().notifyDataSetChanged();
//        for (int i = 0; i < getChildCount(); i++) {
//            if (!(getChildAt(i) instanceof BaseMonthView)) continue;
//            BaseMonthView view = (BaseMonthView) getChildAt(i);
//            view.mCurrentItem = -1;
//            view.invalidate();
//        }
    }

    public void updateMonthViewClass() {
        if (getAdapter() == null) return;
        getAdapter().notifyDataSetChanged();
    }

    public void scrollToCurrent(boolean smoothScroll) {
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth();
        int curItem = getCurrentItem();
//        if (curItem == position) {
//        }
//
//        setCurrentItem(position, smoothScroll);
//
//        BaseMonthView view = findViewWithTag(position);
//        if (view != null) {
//            view.setSelectedCalendar(mDelegate.getCurrentDay());
//            view.invalidate();
//            if (mParentLayout != null) {
//                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.getCurrentDay()));
//            }
//        }
//
//        if (mDelegate.mCalendarSelectListener != null && getVisibility() == VISIBLE) {
//            mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendar, false);
//        }
    }

    private static class MonthViewAdapter extends Adapter<MonthViewAdapter.ViewHolder> {

        private int mountCount;
        private CalendarViewDelegate viewDelegate;

        public MonthViewAdapter updateMonthCount() {
            this.mountCount = 12 * (viewDelegate.getMaxYear() - viewDelegate.getMinYear())
                    - viewDelegate.getMinYearMonth() + 1 +
                    viewDelegate.getMaxYearMonth();
            return this;
        }

        public MonthViewAdapter setViewDelegate(CalendarViewDelegate viewDelegate) {
            this.viewDelegate = viewDelegate;
            return updateMonthCount();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindData(position, viewDelegate);
        }

        @Override
        public int getItemCount() {
            return mountCount;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private FrameLayout content;
            private TextView title;

            public ViewHolder(@NonNull View view) {
                super(view);
                content = view.findViewById(R.id.frameContent);
                title = view.findViewById(R.id.tvTitle);
            }

            @SuppressWarnings("rawtypes")
            @SuppressLint("DefaultLocale")
            public void bindData(int position, CalendarViewDelegate viewDelegate) {
                content.removeAllViews();
                int year = (position + viewDelegate.getMinYearMonth() - 1) / 12 + viewDelegate.getMinYear();
                int month = (position + viewDelegate.getMinYearMonth() - 1) % 12 + 1;
                title.setText(String.format("Tháng %d", month));
                com.chungld.uipack.calendar.BaseMonthView view;
                try {
                    Constructor constructor = viewDelegate.getMonthViewClass().getConstructor(Context.class);
                    view = (com.chungld.uipack.calendar.BaseMonthView) constructor.newInstance(itemView.getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
//                view.mMonthViewPager = MonthViewPager.this;
//                view.mParentLayout = view.getPa;
                view.setup(viewDelegate);
                view.setTag(position);
                view.initMonthWithDate(year, month);
                view.setSelectedCalendar(viewDelegate.mSelectedCalendar);
                content.addView(view);
            }
        }
    }
}
