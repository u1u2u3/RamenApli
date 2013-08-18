package com.example.ramenapliv4.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RamenOpenHelper extends SQLiteOpenHelper {

	public RamenOpenHelper(Context context) {
		super(context, "RAMENAPLI_DB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table ramen_shop("
				+ "    _id       integer primary key autoincrement not null,"
				+ "    name      text    not null,"
				+ "    latitude  real    not null,"
				+ "    longitude real    not null,"
				+ "    rating    integer not null," + "    comment   text,"
				+ "    pict      blob" + ");");
		db.execSQL("create table ramen_food("
				+ "    _id       integer primary key autoincrement not null,"
				+ "    shopid    integer not null,"
				+ "    name      text    not null,"
				+ "    rating    integer not null," + "    date      text,"
				+ "    comment   text," + "    pict      blob" + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
