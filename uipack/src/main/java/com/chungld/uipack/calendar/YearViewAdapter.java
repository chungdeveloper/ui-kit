/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungld.uipack.calendar;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;

final class YearViewAdapter extends BaseRecyclerAdapter<com.chungld.uipack.calendar.Month> {
    private com.chungld.uipack.calendar.CalendarViewDelegate mDelegate;
    private int mItemWidth, mItemHeight;

    YearViewAdapter(Context context) {
        super(context);
    }

    final void setup(com.chungld.uipack.calendar.CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
    }


    final void setYearViewSize(int width, int height) {
        this.mItemWidth = width;
        this.mItemHeight = height;
    }

    @Override
    RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        com.chungld.uipack.calendar.YearView yearView;
        if (TextUtils.isEmpty(mDelegate.getYearViewClassPath())) {
            yearView = new com.chungld.uipack.calendar.DefaultYearView(mContext);
        } else {
            try {
                Constructor constructor = mDelegate.getYearViewClass().getConstructor(Context.class);
                yearView = (com.chungld.uipack.calendar.YearView) constructor.newInstance(mContext);
            } catch (Exception e) {
                e.printStackTrace();
                yearView = new com.chungld.uipack.calendar.DefaultYearView(mContext);
            }
        }
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);
        yearView.setLayoutParams(params);
        return new YearViewHolder(yearView, mDelegate);
    }

    @Override
    void onBindViewHolder(RecyclerView.ViewHolder holder, com.chungld.uipack.calendar.Month item, int position) {
        YearViewHolder h = (YearViewHolder) holder;
        com.chungld.uipack.calendar.YearView view = h.mYearView;
        view.init(item.getYear(), item.getMonth());
        view.measureSize(mItemWidth, mItemHeight);
    }

    private static class YearViewHolder extends RecyclerView.ViewHolder {
        com.chungld.uipack.calendar.YearView mYearView;

        YearViewHolder(View itemView, com.chungld.uipack.calendar.CalendarViewDelegate delegate) {
            super(itemView);
            mYearView = (com.chungld.uipack.calendar.YearView) itemView;
            mYearView.setup(delegate);
        }
    }
}
