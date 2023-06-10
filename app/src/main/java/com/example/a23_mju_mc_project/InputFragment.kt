package com.example.a23_mju_mc_project

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.Database.MyAppDatabase
import com.example.a23_mju_mc_project.databinding.FragmentInputBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class InputFragment: Fragment() {
    lateinit var binding: FragmentInputBinding
    private lateinit var database: MyAppDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputBinding.inflate(inflater,container,false)
        database = MyAppDatabase.getDatabase(requireContext())

        binding.startBtn.setOnClickListener {
            val nickname = binding.nickname.text.toString()
            val timer = binding.timer.text.toString()
            val todayComment = binding.todayComment.text.toString()

            if (nickname.isBlank() || timer.isBlank() || todayComment.isBlank()) {
                // 필수 입력 필드가 비어있을 때 Toast 메시지 표시
                Toast.makeText(requireContext(), "입력 창을 모두 채워주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(nickname = nickname, alarm_Time = timer, push_Mes = todayComment)

                // 백그라운드 스레드에서 데이터베이스 작업 수행
                GlobalScope.launch(Dispatchers.IO) {
                    database.userDao().insertUser(user)
                    printUserTableData()  //로그캣에서 User table 데이터 확인 가능
                    val users = withContext(Dispatchers.IO) {
                        database.userDao().getAllUsers()
                    }
                    setAlarm(users[0].alarm_Time, users[0].push_Mes)
                }

                Handler().postDelayed({
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    Handler(Looper.getMainLooper()).post {
                        requireActivity().finish()
                    }
                }, 1000)
            }
        }

        binding.timer.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val h: Int = c.get(Calendar.HOUR_OF_DAY)
            val m: Int = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                AlertDialog.THEME_HOLO_LIGHT,
                { _, hourOfDay, minute ->
                    val timeString =
                        String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    binding.timer.setText(timeString)
                },
                h, m, false
            )

            timePickerDialog.show()
        }
        return binding.root
    }

    private fun printUserTableData() {
        GlobalScope.launch(Dispatchers.IO) {
            val users = database.userDao().getAllUsers()
            Log.d("UserTable", "User table data:")
            for (user in users) {
                Log.d("UserTable", "User ID: ${user.user_Id}, Nickname: ${user.nickname}, Alarm Time: ${user.alarm_Time}, Push Message: ${user.push_Mes}")
            }
        }
    }

    //AlarmReceiver에 User table의 알람시간, 푸시메세지 전달
    private fun setAlarm(alarmTime: String, pushMessage: String) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Parse alarmTime to hour and minute
        val timeParts = alarmTime.split(":")
        val hourOfDay = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        // Check if the alarm time has already passed for today
        val currentTime = Calendar.getInstance()
        if (calendar.before(currentTime)) {
            // Add a day to the alarm time to set it for tomorrow
            calendar.add(Calendar.DATE, 1)
        }

        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("pushMessage", pushMessage)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // Set the alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}