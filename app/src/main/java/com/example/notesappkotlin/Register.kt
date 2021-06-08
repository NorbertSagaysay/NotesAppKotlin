package com.example.notesappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.notesappkotlin.data.User
import com.example.notesappkotlin.helper.DbHelper
import com.example.notesappkotlin.helper.InputValidation
import com.google.android.material.snackbar.Snackbar


class Register : AppCompatActivity(), View.OnClickListener {

    private val activity = this@Register
    private lateinit var textEditTextUser: EditText
    private lateinit var textEditTextPassword1: EditText
    private lateinit var textEditTextPassword2: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var linearBodyReg: LinearLayout
    private lateinit var inputValidation: InputValidation
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar!!.hide()

        initViews()
        initListeners()
        initObjects()
    }

    //Initialize views
    private fun initViews() {
        textEditTextUser = findViewById(R.id.user_field)
        textEditTextPassword1 = findViewById(R.id.pass_field)
        textEditTextPassword2 = findViewById(R.id.pass_field2)
        btnLogin = findViewById(R.id.log_button)
        btnRegister = findViewById(R.id.reg_button)
        linearBodyReg = findViewById(R.id.linearBodyReg)
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
            R.id.log_button -> {
                val intentLog = Intent(applicationContext, Login::class.java)
                startActivity(intentLog)
            }
            R.id.reg_button -> postData()
        }
    }

    //Save data to db
    private fun postData() {
        if (!inputValidation.isTextFilled(textEditTextUser, getString(R.string.error_message_nofill_name))) {
            return
        }
        if (!inputValidation.isTextFilled(textEditTextPassword1, getString(R.string.error_message_nofill_pass))) {
            return
        }
        if (!inputValidation.isTextFilled(textEditTextPassword2, getString(R.string.error_message_nofill_pass))) {
            return
        }
        if (!inputValidation.isPassMatch(textEditTextPassword1, textEditTextPassword2, getString(R.string.error_message_notmatch))) {
            return
        }

        if (dbHelper.regCheckUser(textEditTextUser.text.toString().trim())) {

            var user = User(name = textEditTextUser.text.toString().trim(),
                password = textEditTextPassword1.text.toString().trim())

            dbHelper.addUser(user)

            //println("Record saved $user")

            val intentMain = Intent(applicationContext, MainActivity::class.java)
            intentMain.putExtra("uname", textEditTextUser.text.toString().trim())
            startActivity(intentMain)
        }
        else {
            Snackbar.make(
                linearBodyReg,
                getString(R.string.error_message_name),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}