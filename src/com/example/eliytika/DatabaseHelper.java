package com.example.eliytika;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "gamescore.db";
	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_NAME = "gs";
	private static final String ID = "id";
	private static final String SCORE = "score";

	DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db){
		String query =
				"create table " + TABLE_NAME + "("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ SCORE + " INTEGER);";

		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}

}
