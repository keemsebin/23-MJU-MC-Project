package com.example.a23_mju_mc_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentHomeBinding
import com.example.a23_mju_mc_project.databinding.FragmentInputBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding
    lateinit var calendar: MaterialCalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        calendar = binding.calendar
        calendar.setSelectedDate(CalendarDay.today())
        calendar.addDecorator(TodayDecorator())
        calendar.setOnDateChangedListener { widget, date, selected ->
            calendar.addDecorator(
                EventDecorator(Collections.singleton(date))
            )
        }

        return binding.root
    }
}