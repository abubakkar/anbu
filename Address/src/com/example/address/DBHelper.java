package com.example.address;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase _db) {
		// TODO Auto-generated method stub
		_db.execSQL(DBAdapter.CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// TODO Auto-generated method stub
		// Log the version upgrade.
		Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion + " to "
				+ _newVersion + ", which will destroy all old data");
		_db.execSQL("DROP TABLE IF EXISTS " + "event_report");
		onCreate(_db);

	}

}
