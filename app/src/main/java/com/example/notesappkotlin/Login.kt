package com.example.notesappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.notesappkotlin.helper.DbHelper
import com.example.notesappkotlin.helper.InputValidation
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity(), View.OnClickListener {

    private val activity = this@Login
    private lateinit var linearBody: LinearLayout
    private lateinit var textEditTextUser: EditText
    private lateinit var textEditTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var inputValidation: InputValidation
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.hide()

        initViews()
        initListeners()
        initObjects()
    }

    //Initialize views
    private fun initViews() {
        textEditTextUser = findViewById(R.id.username_field)
        textEditTextPassword = findViewById(R.id.password_field)
        btnLogin = findViewById(R.id.login_button)
        btnRegister = findViewById(R.id.register_button)
        linearBody = findViewById(R.id.linearBody)
    }
    //Initialize listeners
    private fun initListeners() {
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }
    //Initialize objects
    private fun initObjects() {
        dbHelper = DbHelper(activity)
        inputValidation = InputValidation(activity)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_button -> verifyFromDB()
            R.id.register_button -> {
                val intentReg = Intent(applicationContext, Register::class.java)
                startActivity(intentReg)
            }
        }
    }

    private fun verifyFromDB() {
        if (!inputValidation.isTextFilled(textEditTextUser, getString(R.string.error_message_nofill_name))) {
            return
        }
        if (!inputValidation.isTextFilled(textEditTextPassword, getString(R.string.error_message_nofill_pass))) {
            return
        }

        if (dbHelper.logCheckUser(textEditTextUser.text.toString().trim() { it <= ' '}, textEditTextPassword.text.toString().trim() { it <= ' '})) {

            val mainIntent = Intent(activity, MainActivity::class.java)
            mainIntent.putExtra("uname", textEditTextUser.text.toString().trim())
            textEditTextUser.text = null
            textEditTextPassword.text = null
            startActivity(mainIntent)
        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }
}