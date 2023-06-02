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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var calendar: MaterialCalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        calendar = binding.calendar
        calendar.setSelectedDate(CalendarDay.today())
        calendar.addDecorator(TodayDecorator())
        calendar.setOnDateChangedListener { widget, date, selected ->
            calendar.addDecorator(
                EventDecorator(Collections.singleton(date))
            )
        }

        // 닉네임에 따라 프레그먼트 상단 문장 달라짐.
        val userDao = MyAppDatabase.getDatabase(requireContext()).userDao()
        GlobalScope.launch(Dispatchers.IO) {
            val user = userDao.getUserById(1)
            binding.welcomment.text = "${user.nickname}님, 하루를 기록해보세요."
        }

        return binding.root
    }
}