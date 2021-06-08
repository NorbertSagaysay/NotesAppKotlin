package com.example.notesappkotlin.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class DbHelperNote {

    val dbName = "Note.db"
    val dbTable = "Note"
    val colID = "ID"
    val colUser = "User"
    val colTitle = "Title"
    val colDesc = "Description"
    val dbVer = 1

    val createTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colID + " INTEGER PRIMARY KEY," +
            colUser + " TEXT," + colTitle + " TEXT," + colDesc + " TEXT);"

    var sqlDB: SQLiteDatabase? = null
    constructor(context: Context?) {
        var db = context?.let { DatabaseHelper(it) }
        sqlDB = db?.writableDatabase
    }

    inner class DatabaseHelper: SQLiteOpenHelper {
        var context: Context? = null
        constructor(context: Context): super(context, dbName, null, dbVer) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(createTable)
            println("DB created")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("ALTER TABLE " + dbTable)
        }
    }

    fun Insert(values: ContentValues): Long {
        var ID = sqlDB!!.insert(dbTable, "", values)
        return ID
    }

    fun Query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        val que = SQLiteQueryBuilder()
        que.tables = dbTable
        return que.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)
    }

    fun Delete(selection: String, selectionArgs: Array<String>): Int {
        return sqlDB!!.delete(dbTable, selection, selectionArgs)
    }

    fun Update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        return sqlDB!!.update(dbTable, values, selection, selectionArgs)
    }
}