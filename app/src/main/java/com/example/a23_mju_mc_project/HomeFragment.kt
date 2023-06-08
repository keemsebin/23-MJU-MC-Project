package com.example.a23_mju_mc_project

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentHomeBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var calendar: MaterialCalendarView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        calendar = binding.calendar
        calendar.setSelectedDate(CalendarDay.today())
        calendar.addDecorator(TodayDecorator())

        // 닉네임에 따라 프레그먼트 상단 문장 달라짐.
        val userDao = MyAppDatabase.getDatabase(requireContext()).userDao()
        GlobalScope.launch(Dispatchers.IO) {
            val user = userDao.getUserById(1)
            binding.welcomment.text = "${user.nickname}님, 하루를 기록해보세요."
        }

        val feedDao = MyAppDatabase.getDatabase(requireContext()).feedDao()
        GlobalScope.launch(Dispatchers.IO) {
            val feeds = feedDao.getAllFeeds()
            val eventDates = mutableSetOf<CalendarDay>()

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            for (feed in feeds) {
                val date = format.parse(feed.upload_Date)
                val calendar = Calendar.getInstance()
                calendar.time = date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줌
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val calendarDay = CalendarDay.from(year, month, day)
                eventDates.add(calendarDay)
            }

            calendar.addDecorator(EventDecorator(eventDates))
        }


        return binding.root
    }
}