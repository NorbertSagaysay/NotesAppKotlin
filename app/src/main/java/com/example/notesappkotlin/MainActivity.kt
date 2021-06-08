package com.example.notesappkotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notesappkotlin.data.Note
import com.example.notesappkotlin.helper.DbHelperNote
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()
    private lateinit var linearMain: LinearLayout
    private val RECORD_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearMain = findViewById(R.id.linearMain)
        setupPermission()
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(linearMain, getString(R.string.error_message_denied), Snackbar.LENGTH_LONG).show()
                }
                else {
                    Snackbar.make(linearMain, getString(R.string.message_granted), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    fun LoadQuery(title: String) {
//        var cUser = intent.getStringExtra("uname")
//        println(cUser)
        var dbHelper = DbHelperNote(this)
        val projection = arrayOf("ID", "User", "Title", "Description")
        var selectionArgs = arrayOf(title)
        val cursor =
            dbHelper.Query(projection, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                //val User = cursor.getString(cursor.getColumnIndex("User"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }
        var mNotesAdapter = NotesAdapter(this, listNotes)
        findViewById<ListView>(R.id.lvmain).adapter = mNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var cUser = intent.getStringExtra("uname")
        println(cUser)

        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    var intent = Intent(this, AddNotes::class.java)
                    intent.putExtra("uname", cUser)
                    println(cUser)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class NotesAdapter : BaseAdapter {
        var context: Context? = null
        var listNotes = ArrayList<Note>()

        constructor(context: Context, listNote: ArrayList<Note>) : super() {
            this.listNotes = listNote
            this.context = context
        }

        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var mView = layoutInflater.inflate(R.layout.ticknote, null)
            var mNote = listNotes[position]
            mView.findViewById<TextView>(R.id.ntitle).text = mNote.nodeName
            mView.findViewById<TextView>(R.id.ndesc).text = mNote.nodeDesc

            mView.findViewById<ImageButton>(R.id.delNote).setOnClickListener {
                var dbHelper = DbHelperNote(this.context)
                val selectionArgs = arrayOf(mNote.nodeID.toString())
                dbHelper.Delete("ID=?", selectionArgs)
                LoadQuery("%")
            }

            mView.findViewById<ImageButton>(R.id.editNote).setOnClickListener {
                UpdateNote(mNote)
            }
            return mView
        }
    }
    fun UpdateNote(note: Note) {
        var upIntent =  Intent(this, AddNotes::class.java)
        upIntent.putExtra("ID", note.nodeID)
        //intent.putExtra("User", note.nodeUser)
        upIntent.putExtra("Title", note.nodeName)
        upIntent.putExtra("Description", note.nodeDesc)
        startActivity(upIntent)
    }
}

