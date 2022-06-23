package com.chungld.uikit

import android.os.Bundle
import android.util.Log
import com.chungld.uikit.databinding.ActivityCalendarBinding
import com.chungld.uipack.calendar.CalendarView
import com.chungld.uipack.calendar.TrpCalendar
import com.chungld.uipack.calendar.model.TicketHuntBundle
import kotlinx.android.synthetic.main.activity_calendar.*
import java.math.BigDecimal
import java.util.*

class CalendarActivity : BaseActivity<ActivityCalendarBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_calendar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        calendarView.setRange(2020, 10, 16, 2021, 9, 30)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 1)
        val minDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -2)
        }
        calendarView.setRange(minDate, maxDate)

        val currentDate = TrpCalendar()
        currentDate.day = 23
        currentDate.month = 2
        currentDate.year = 2021

        calendarView.setCurrentSelected(currentDate)

//        calendarView.setSelectRangeMode()
//        calendarView.setMonthView(javaClass<HuntTicketRangeMonthView>);

        val endRangeDate = Calendar.getInstance()
        endRangeDate.add(Calendar.MONTH, 3);

        calendarView.setSelectStartCalendar(TrpCalendar().setTime(Calendar.getInstance().time))
        calendarView.setSelectEndCalendar(TrpCalendar().setTime(endRangeDate.time))

        val minPrice = HashMap<String, BigDecimal>()
        minPrice["10-2020"] = BigDecimal.valueOf(60000)
        minPrice["11-2020"] = BigDecimal.valueOf(70000)

        val bundle = TicketHuntBundle()
        bundle.setPriceMap(mockPrice())
        bundle.setPriceMinMap(minPrice)
        calendarView.setAdditionData(bundle)
        calendarView.setOnCalendarRangeSelectListener(object :
            CalendarView.OnCalendarRangeSelectListener {
            override fun onCalendarSelectOutOfRange(calendar: TrpCalendar?) {
                Log.d("TAG", "onSelectOutOfRange: " + calendar)
            }

            override fun onSelectOutOfRange(calendar: TrpCalendar?, isOutOfMinRange: Boolean) {
                Log.d("TAG", "onSelectOutOfRange: " + calendar)
            }

            override fun onCalendarRangeSelect(calendar: TrpCalendar?, isEnd: Boolean) {
                Log.d("TAG", "onSelectOutOfRange: " + calendar)
            }
        })

        btnReset.setOnClickListener {
//            calendarView.clearSelectRange()
            calendarView.scrollToCurrent()
//            calendarView.clearSingleSelect()
        }
    }

    private fun mockPrice(): HashMap<String, BigDecimal>? {
        val price = HashMap<String, BigDecimal>()
        price["15-10-2020"] = BigDecimal.valueOf(2000000)
        price["16-10-2020"] = BigDecimal.valueOf(3000000)
        price["17-10-2020"] = BigDecimal.valueOf(400000)
        price["18-10-2020"] = BigDecimal.valueOf(500000)
        price["19-10-2020"] = BigDecimal.valueOf(603000)
        price["21-10-2020"] = BigDecimal.valueOf(60000)
        price["22-10-2020"] = BigDecimal.valueOf(620000)
        price["23-10-2020"] = BigDecimal.valueOf(602000)
        price["24-10-2020"] = BigDecimal.valueOf(160000)
        price["25-10-2020"] = BigDecimal.valueOf(260000)
        price["26-10-2020"] = BigDecimal.valueOf(460000)
        price["27-10-2020"] = BigDecimal.valueOf(560000)
        price["28-10-2020"] = BigDecimal.valueOf(70000)
        price["29-10-2020"] = BigDecimal.valueOf(60000)
        price["01-10-2020"] = BigDecimal.valueOf(2000000)
        price["02-10-2020"] = BigDecimal.valueOf(3000000)
        price["03-10-2020"] = BigDecimal.valueOf(400000)
        price["04-10-2020"] = BigDecimal.valueOf(500000)
        price["05-10-2020"] = BigDecimal.valueOf(603000)
        price["06-10-2020"] = BigDecimal.valueOf(60000)
        price["07-10-2020"] = BigDecimal.valueOf(620000)
        price["08-10-2020"] = BigDecimal.valueOf(602000)
        price["09-10-2020"] = BigDecimal.valueOf(160000)
        price["10-10-2020"] = BigDecimal.valueOf(260000)
        price["11-10-2020"] = BigDecimal.valueOf(460000)
        price["12-10-2020"] = BigDecimal.valueOf(560000)
        price["13-10-2020"] = BigDecimal.valueOf(70000)
        price["14-10-2020"] = BigDecimal.valueOf(60000)
        price["15-11-2020"] = BigDecimal.valueOf(2000000)
        price["16-11-2020"] = BigDecimal.valueOf(3000000)
        price["17-11-2020"] = BigDecimal.valueOf(400000)
        price["18-11-2020"] = BigDecimal.valueOf(500000)
        price["19-11-2020"] = BigDecimal.valueOf(603000)
        price["21-11-2020"] = BigDecimal.valueOf(60000)
        price["22-11-2020"] = BigDecimal.valueOf(620000)
        price["23-11-2020"] = BigDecimal.valueOf(602000)
        price["24-11-2020"] = BigDecimal.valueOf(160000)
        price["25-11-2020"] = BigDecimal.valueOf(260000)
        price["26-11-2020"] = BigDecimal.valueOf(460000)
        price["27-11-2020"] = BigDecimal.valueOf(560000)
        price["28-11-2020"] = BigDecimal.valueOf(70000)
        price["29-11-2020"] = BigDecimal.valueOf(60000)
        price["01-11-2020"] = BigDecimal.valueOf(2000000)
        price["02-11-2020"] = BigDecimal.valueOf(3000000)
        price["03-11-2020"] = BigDecimal.valueOf(400000)
        price["04-11-2020"] = BigDecimal.valueOf(500000)
        price["05-11-2020"] = BigDecimal.valueOf(603000)
        price["06-11-2020"] = BigDecimal.valueOf(60000)
        price["07-11-2020"] = BigDecimal.valueOf(620000)
        price["08-11-2020"] = BigDecimal.valueOf(602000)
        price["09-11-2020"] = BigDecimal.valueOf(160000)
        price["10-11-2020"] = BigDecimal.valueOf(260000)
        price["11-11-2020"] = BigDecimal.valueOf(460000)
        price["12-11-2020"] = BigDecimal.valueOf(560000)
        price["13-11-2020"] = BigDecimal.valueOf(70000)
        price["14-11-2020"] = BigDecimal.valueOf(60000)
        price["15-12-2020"] = BigDecimal.valueOf(2000000)
        price["16-12-2020"] = BigDecimal.valueOf(3000000)
        price["17-12-2020"] = BigDecimal.valueOf(400000)
        price["18-12-2020"] = BigDecimal.valueOf(500000)
        price["19-12-2020"] = BigDecimal.valueOf(603000)
        price["21-12-2020"] = BigDecimal.valueOf(60000)
        price["22-12-2020"] = BigDecimal.valueOf(620000)
        price["23-12-2020"] = BigDecimal.valueOf(602000)
        price["24-12-2020"] = BigDecimal.valueOf(160000)
        price["25-12-2020"] = BigDecimal.valueOf(260000)
        price["26-12-2020"] = BigDecimal.valueOf(460000)
        price["27-12-2020"] = BigDecimal.valueOf(560000)
        price["28-12-2020"] = BigDecimal.valueOf(70000)
        price["29-12-2020"] = BigDecimal.valueOf(60000)
        price["01-12-2020"] = BigDecimal.valueOf(2000000)
        price["02-12-2020"] = BigDecimal.valueOf(3000000)
        price["03-12-2020"] = BigDecimal.valueOf(400000)
        price["04-12-2020"] = BigDecimal.valueOf(500000)
        price["05-12-2020"] = BigDecimal.valueOf(603000)
        price["06-12-2020"] = BigDecimal.valueOf(60000)
        price["07-12-2020"] = BigDecimal.valueOf(620000)
        price["08-12-2020"] = BigDecimal.valueOf(602000)
        price["09-12-2020"] = BigDecimal.valueOf(160000)
        price["10-12-2020"] = BigDecimal.valueOf(260000)
        price["11-12-2020"] = BigDecimal.valueOf(460000)
        price["12-12-2020"] = BigDecimal.valueOf(560000)
        price["13-12-2020"] = BigDecimal.valueOf(70000)
        price["14-12-2020"] = BigDecimal.valueOf(60000)
        price["15-01-2021"] = BigDecimal.valueOf(2000000)
        price["16-01-2021"] = BigDecimal.valueOf(3000000)
        price["17-01-2021"] = BigDecimal.valueOf(400000)
        price["18-01-2021"] = BigDecimal.valueOf(500000)
        price["19-01-2021"] = BigDecimal.valueOf(603000)
        price["21-01-2021"] = BigDecimal.valueOf(60000)
        price["22-01-2021"] = BigDecimal.valueOf(620000)
        price["23-01-2021"] = BigDecimal.valueOf(602000)
        price["24-01-2021"] = BigDecimal.valueOf(160000)
        price["25-01-2021"] = BigDecimal.valueOf(260000)
        price["26-01-2021"] = BigDecimal.valueOf(460000)
        price["27-01-2021"] = BigDecimal.valueOf(560000)
        price["28-01-2021"] = BigDecimal.valueOf(70000)
        price["29-01-2021"] = BigDecimal.valueOf(60000)
        price["01-01-2021"] = BigDecimal.valueOf(2000000)
        price["02-01-2021"] = BigDecimal.valueOf(3000000)
        price["03-01-2021"] = BigDecimal.valueOf(400000)
        price["04-01-2021"] = BigDecimal.valueOf(500000)
        price["05-01-2021"] = BigDecimal.valueOf(603000)
        price["06-01-2021"] = BigDecimal.valueOf(60000)
        price["07-01-2021"] = BigDecimal.valueOf(620000)
        price["08-01-2021"] = BigDecimal.valueOf(602000)
        price["09-01-2021"] = BigDecimal.valueOf(160000)
        price["10-01-2021"] = BigDecimal.valueOf(260000)
        price["11-01-2021"] = BigDecimal.valueOf(460000)
        price["12-01-2021"] = BigDecimal.valueOf(560000)
        price["13-01-2021"] = BigDecimal.valueOf(70000)
        price["14-01-2021"] = BigDecimal.valueOf(60000)
        return price
    }
}
