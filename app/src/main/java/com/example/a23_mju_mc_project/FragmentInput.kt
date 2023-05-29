package com.example.a23_mju_mc_project

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentInputBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class FragmentInput: Fragment() {
    lateinit var binding: FragmentInputBinding
    private lateinit var database: MyAppDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputBinding.inflate(inflater,container,false)
        database = MyAppDatabase.getDatabase(requireContext())

        printUserTableData() //로그캣에서 User table 데이터 확인 가능

        binding.startBtn.setOnClickListener {
            val nickname = binding.nickname.text.toString()
            val timer = binding.timer.text.toString()
            val todayComment = binding.todayComment.text.toString()

            val user = User(nickname = nickname, alarm_Time = timer, push_Mes = todayComment)

            // 백그라운드 스레드에서 데이터베이스 작업 수행
            GlobalScope.launch(Dispatchers.IO) {
                database.userDao().insertUser(user)
            }

            Handler().postDelayed({
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }, 1000)
        }

        binding.timer.setOnClickListener{
            val c: Calendar = Calendar.getInstance()
            val h: Int = c.get(Calendar.HOUR_OF_DAY)
            val m: Int = c.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(requireContext(),
                { view, hourOfDay, minute ->
                    binding.timer.setText("$hourOfDay"+":"+"$minute")
                }, h, m, false
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
}