package com.artisanasif.gradecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import android.widget.Toast
import android.text.Spanned
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var lastToastTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val subject1: EditText = findViewById<EditText>(R.id.subject1)
        val subject2: EditText = findViewById<EditText>(R.id.subject2)
        val subject3: EditText = findViewById<EditText>(R.id.subject3)
        val calculateResultButton: Button = findViewById<Button>(R.id.calculateButton)
        val resetButton: Button = findViewById<Button>(R.id.resetButton)
        val totalMarksObtained: TextView = findViewById<TextView>(R.id.totalMarksObtained)
        val averageField: TextView = findViewById<TextView>(R.id.average)
        val gradeField: TextView = findViewById<TextView>(R.id.grade)


        applyMarksLimit(subject1)
        applyMarksLimit(subject2)
        applyMarksLimit(subject3)

        calculateResultButton.setOnClickListener {
            val s1 = subject1.text.toString().trim()
            val s2 = subject2.text.toString().trim()
            val s3 = subject3.text.toString().trim()

            if(s1.isEmpty() || s2.isEmpty() || s3.isEmpty()){
                Toast.makeText(this, "Please enter value to all subject fields",Toast.LENGTH_SHORT).show()
            }else{
                val totalMarks = s1.toInt() + s2.toInt() + s3.toInt()
                val average = (totalMarks/3).toInt()
                var grade = ""
                gradeField.setTextColor(ContextCompat.getColor(this,R.color.pGrade))
                if(average >= 80){
                    grade = "A"
                }else if(average >= 70){
                    grade = "B"
                }else if(average >= 60){
                    grade = "C"
                }else if(average >= 50){
                    grade = "D"
                }else{
                    grade = "F"
                    gradeField.setTextColor(ContextCompat.getColor(this,R.color.fGrade))
                }
                totalMarksObtained.setText("${totalMarks}/300")
                averageField.setText(average.toString())
                gradeField.setText(grade)

            }
        }

        resetButton.setOnClickListener {
            subject1.setText("")
            subject2.setText("")
            subject3.setText("")
            totalMarksObtained.setText("0/300")
            averageField.setText("0")
            gradeField.setText("F")
            gradeField.setTextColor(ContextCompat.getColor(this,R.color.fGrade))
        }
    }

    private fun applyMarksLimit(editText: EditText){
        val filter = object : InputFilter{
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                val newText = dest.replaceRange(dstart, dend, source.subSequence(start, end)).toString()
                if(newText.isEmpty()) return null
                val value = newText.toIntOrNull() ?: return ""
                return if (value in 0..100) {
                    null
                }else{
                        val now = System.currentTimeMillis()
                        if(now - lastToastTime > 800){
                            Toast.makeText(this@MainActivity,"Marks must be between 0 and 100", Toast.LENGTH_SHORT).show()
                            lastToastTime = now
                        }
                        ""
                    }
                }
            }
            editText.filters = arrayOf(filter)
        }
}