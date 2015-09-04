package com.example.address;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button getloc;
	TextView lati;
	TextView longi;
	TextView address;
	public DBAdapter dbAdapter;
	public static JSONArray resultSet;
	String strLat, strLng, localityString, city, region_code, zipcode, state,
			area, sublocal, thfare;
	String strLat_json, strLng_json, localityString_json, city_json,
			region_code_json, zipcode_json, state_json, area_json,
			sublocal_json, thfare_json;

	LocationManager location_manager;
	String getLatitude, getLongitude;

	double x;
	double y;

	Geocoder geocoder;
	List<Address> addresses;
	Location loc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbAdapter = new DBAdapter(this);
		dbAdapter = dbAdapter.open();

		getloc = (Button) findViewById(R.id.getlocation);
		lati = (TextView) findViewById(R.id.latitude);
		longi = (TextView) findViewById(R.id.longitude);
		address = (TextView) findViewById(R.id.address);
		location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		getloc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				LocationListener listner = new MyLocationListner();
				location_manager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 50,
		                 listner);
				location_manager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 50,
		                 listner);

			}
		});

	}

	public class MyLocationListner implements LocationListener {

		@SuppressWarnings("static-access")
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			getLatitude = "" + location.getLatitude();
			getLongitude = "" + location.getLongitude();
			strLat = lati.getText().toString();
			strLng = longi.getText().toString();

			lati.setText(getLatitude);
			longi.setText(getLongitude);

			x = location.getLatitude();
			y = location.getLongitude();

			try {
				geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
				addresses = geocoder.getFromLocation(x, y, 1);
				StringBuilder str = new StringBuilder();
				if (geocoder.isPresent()) {
					// Toast.makeText(getApplicationContext(),
					// "geocoder present",
					// Toast.LENGTH_SHORT).show();
					Address returnAddress = addresses.get(0);

					String area = returnAddress.getFeatureName();
					String thfare = returnAddress.getThoroughfare();
					String localityString = returnAddress.getLocality();
					// String region_code = returnAddress.getCountryCode();
					String zipcode = returnAddress.getPostalCode();
					String state = returnAddress.getAdminArea();
					String sublocal = returnAddress.getSubLocality();
					String city = returnAddress.getCountryName();

					dbAdapter
							.insertEntry(strLat, strLng, localityString, city,
									region_code, zipcode, state, area,
									sublocal, thfare);
					getResults();
					display();
					str.append(area + "," + sublocal + " , " + thfare + " , "
							+ localityString + "," + zipcode + "," + state
							+ "," + city);
					String myPath = "/data/data/com.example.address/databases/" + "address.db";
		        	String myTable = "event_report";//Set name of your table
					SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
					String searchQuery = "SELECT  * FROM " + myTable;
					Cursor cursor = myDataBase.rawQuery(searchQuery, null );
					Cursor c = myDataBase.rawQuery(searchQuery, null);
		        	ExportToCSV(c, "test.csv");
					/*
					 * str.append(localityString + ""); str.append(city + "" +
					 * region_code + ""); str.append(zipcode + "");
					 * str.append(state + ""); str.append(area + "");
					 * str.append(sublocal + ""); str.append(thfare + "");
					 */

					address.setText(str);
					Toast.makeText(getApplicationContext(), str,
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(),
							"geocoder not present", Toast.LENGTH_SHORT).show();
				}

				// } else {
				// Toast.makeText(getApplicationContext(),
				// "address not available", Toast.LENGTH_SHORT).show();
				// }
			} catch (IOException e) {
				// TODO Auto-generated catch block

				Log.e("tag", e.getMessage());
			}

		}

		private void ExportToCSV(Cursor c, String string) {
			// TODO Auto-generated method stub
			int rowCount = 0;
			int colCount = 0;
			FileWriter fw;
			BufferedWriter bfw;
			File sdCardDir = Environment.getExternalStorageDirectory();
			File saveFile = new File(sdCardDir, string);
			try {

				rowCount = c.getCount();
				colCount = c.getColumnCount();
				fw = new FileWriter(saveFile);
				bfw = new BufferedWriter(fw);
				if (rowCount > 0) {
					c.moveToFirst();
					// 
					for (int i = 0; i < colCount; i++) {
						if (i != colCount - 1)
						   bfw.write(c.getColumnName(i) + ',');
						else
						   bfw.write(c.getColumnName(i));
					}
					// 
					bfw.newLine();
					// 
					for (int i = 0; i < rowCount; i++) {
						c.moveToPosition(i);
						// Toast.makeText(mContext, ""+(i+1)+"",
						// Toast.LENGTH_SHORT).show();
						Log.v("", "" + (i + 1) + "");
						for (int j = 0; j < colCount; j++) {
							if (j != colCount - 1)
							    bfw.write(c.getString(j) + ',');
							else
							   bfw.write(c.getString(j));
						}
						// 
						bfw.newLine();
					}
				}
				// 
				bfw.flush();
				// 
				bfw.close();
				// Toast.makeText(mContext, "?", Toast.LENGTH_SHORT).show();
				Log.v("", "?");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				c.close();
			}
		}

		public JSONArray getResults() {

			String myPath = "/data/data/com.example.address/databases/"
					+ "address.db";// Set path to your database
			String myTable = "event_report";// Set name of your table
			SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath,
					null, SQLiteDatabase.OPEN_READONLY);
			String searchQuery = "SELECT  * FROM " + myTable;
			Cursor cursor = myDataBase.rawQuery(searchQuery, null);
			resultSet = new JSONArray();
			cursor.moveToFirst();
			while (cursor.isAfterLast() == false) {
				int totalColumn = cursor.getColumnCount();
				JSONObject rowObject = new JSONObject();
				for (int i = 0; i < totalColumn; i++) {
					if (cursor.getColumnName(i) != null) {
						try {
							if (cursor.getString(i) != null) {
								// Log.d("TAG_NAME", cursor.getString(i) );
								rowObject.put(cursor.getColumnName(i),
										cursor.getString(i));
							} else {
								rowObject.put(cursor.getColumnName(i), "");
							}
						} catch (Exception e) {
							Log.d("TAG_NAME", e.getMessage());
						}
					}
				}
				resultSet.put(rowObject);
				cursor.moveToNext();
			}
			cursor.close();
			Log.d("TAG_NAME", resultSet.toString());
			return resultSet;
		}

		public void display() {
			// TODO Auto-generated method stub
			try {
				String s = resultSet.toString();
				JSONArray jsonarray = new JSONArray(s);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);

					strLat_json = obj.getString("Latitude");
					strLng_json = obj.getString("Longitude");
					localityString_json = obj.getString("Locality");
					city_json = obj.getString("City");
					region_code_json = obj.getString("Region_code");
					zipcode_json = obj.getString("Zipcode");
					state_json = obj.getString("State");
					area_json = obj.getString("Area");
					sublocal_json = obj.getString("Sublocal");
					thfare_json = obj.getString("Thfare");

					Log.e("TAG_lat", strLat_json.toString());
					Log.e("TAG_log", strLng_json.toString());
					Log.e("TAG_locality", localityString_json.toString());
					Log.e("TAG_city", city_json.toString());
					Log.e("TAG_regioncode", region_code_json.toString());
					Log.e("TAG_zipcode", zipcode_json.toString());
					Log.e("TAG_state", state_json.toString());
					Log.e("TAG_area", area_json.toString());
					Log.e("TAG_sublocal", sublocal_json.toString());
					Log.e("TAG_thfare", thfare_json.toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}