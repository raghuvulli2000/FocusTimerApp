package com.example.locofoco

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import java.util.*

lateinit var PickTime: Button
lateinit var SetTime: TextView
var time = 0
class TimePicker : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var minute = 0
    var hour = 0
    var savedhour = 0
    var savedminute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_picker)

        PickTime = findViewById(R.id.PickTime)
        SetTime = findViewById(R.id.SetTime)
        pickTime()

    }

    private fun getTimeCalender(){
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickTime(){
        PickTime.setOnClickListener{
            getTimeCalender()
            TimePickerDialog(this, this, hour, minute, true).show()

        }
    }

    override fun onTimeSet(p0: TimePicker?, Hour: Int, Minute: Int) {
        savedhour = Hour
        savedminute = Minute
        SetTime.text = makeTimeString(savedhour, savedminute, 0)

        if (!(savedhour == 0 && savedminute == 0)) {
            // testing: change savedminute * 60 to savedminute || dont forget to change back!!!
            time = (savedhour * 3600) + (savedminute)
            goToMainActivity()
        }

    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds )

    private fun goToMainActivity(){
        val intent = Intent(this@TimePicker, MainActivity::class.java)
        intent.putExtra("TIME", time)
        startActivity(intent)
        finish()
    }

}