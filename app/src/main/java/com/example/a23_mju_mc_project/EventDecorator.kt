package com.example.a23_mju_mc_project

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

// 이벤트가 발샐한 날짜에 점을 찍어주는 클래스
class EventDecorator(dates: Collection<CalendarDay>) : DayViewDecorator {
   // dates를 순서를 지정하지 않고 저장
    var dates : HashSet<CalendarDay> = HashSet(dates)
    // 주어진 day가 장식되어야 하는 날짜인지 결정. dates에 day가 포함 되는지에 따라 true/false반환
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }
    // 날짜 뷰에 작은 점 추가.
    // 주어진 view에 장식 추가
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5F, Color.parseColor("#14213D")))
    }
}