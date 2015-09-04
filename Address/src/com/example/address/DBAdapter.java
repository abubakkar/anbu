package com.example.address;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

	final static String CREATE_CMD = "create table " + "event_report" + "( "

	+ "Latitude" + " TEXT," + "Longitude" + " TEXT," + "Locality" + " TEXT,"
			+ "City" + " TEXT," + "Region_code" + " TEXT," + "Zipcode"
			+ " TEXT," + "State" + " TEXT," + "Area" + " TEXT," + "Sublocal"
			+ " TEXT," + "Thfare" + " TEXT )";

	final static String DB_NAME = "address.db";
	final private static Integer DB_VERSION = 1;
	final private Context mContext;

	public SQLiteDatabase db;

	private DBHelper dbHelper;

	public DBAdapter(Context _context) {
		mContext = _context;
		dbHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);

	}

	public DBAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public void insertEntry(String strLat, String strLng,
			String localityString, String city, String region_code,
			String zipcode, String state, String area, String sublocal,
			String thfare) {
		// TODO Auto-generated method stub
		ContentValues newValues = new ContentValues();
		// newValues.put("Bearing", bearing);
		// newValues.put("Altitude", altitude);

		newValues.put("Latitude", strLat);
		newValues.put("Longitude", strLng);
		newValues.put("Locality", localityString);
		newValues.put("City", city);
		newValues.put("Region_code", region_code);
		newValues.put("Zipcode", zipcode);
		newValues.put("State", state);
		newValues.put("Area", area);
		newValues.put("Sublocal", sublocal);
		newValues.put("Thfare", thfare);
		db.insert("event_report", null, newValues);
	}

	public void deleteAll() {
		// SQLiteDatabase db = dbHelper.getWritableDatabase();
		// db.delete(TABLE_NAME,null,null);
		// db.execSQL("delete * from"+ TABLE_NAME);
		db.execSQL("DELETE FROM " + "event_report");
		// db.close();
	}
}
