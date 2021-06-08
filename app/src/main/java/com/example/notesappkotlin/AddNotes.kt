package com.example.notesappkotlin

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.notesappkotlin.helper.DbHelperNote
import java.lang.Exception

class AddNotes : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_notes)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id != 0) {
                findViewById<EditText>(R.id.addtitle).setText(bundle.getString("Title"))
                findViewById<EditText>(R.id.add_desc).setText(bundle.getString("Description"))
            }
        }
        catch (e: Exception){}
    }

    fun saveNote(view: View) {
        var dbHelper = DbHelperNote(this)
        var values = ContentValues()
        var cUser = intent.getStringExtra("uname")

        values.put("User", cUser)
        values.put("Title", findViewById<EditText>(R.id.addtitle).text.toString())
        values.put("Description", findViewById<EditText>(R.id.add_desc).text.toString())

        if (id == 0 && !findViewById<EditText>(R.id.addtitle).text.toString().equals("")) {
            val ID = dbHelper.Insert(values)
            if (ID > 0) {
                println(values)
                println("Note added")
                finish()
            }
            else {
                println("Error cant add")
            }
        }
        else if (id != 0) {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbHelper.Update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                println("Note updated")
                finish()
            }
            else {
                println("Error cant update")
            }
        }
    }
}