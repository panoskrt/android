package com.panoskrt.dbadapter;

/**
 ########################################################################
 # Copyright (C) 2012 Panagiotis Kritikakos <panoskrt@gmail.com> 		#
 # 																		#
 # This program is free software: you can redistribute it and/or modify #
 # it under the terms of the GNU General Public License as published by #
 # the Free Software Foundation, either version 3 of the License, or 	#
 # (at your option) any later version. 									#
 # 																		#
 # This program is distributed in the hope that it will be useful, 		#
 # but WITHOUT ANY WARRANTY; without even the implied warranty of 		#
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 		#
 # GNU General Public License for more details. 						#
 # 																		#
 # You should have received a copy of the GNU General Public License 	#
 # along with this program. If not, see <http://www.gnu.org/licenses/>. #
 ########################################################################
 **/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;

public class DBAdapter {

	private SQLiteDatabase database = null;
	private String dbPath;
	private String dbName;
	private Context context;

	public DBAdapter(Context context, String dbName) {
		this.context = context;
		this.dbName = dbName;
		dbPath = "/data/data/" + context.getPackageName() + "/databases/";
	}

	public void copyDB() {
		try {
			try {
				database = SQLiteDatabase.openDatabase(dbPath + dbName, null,
						SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
				// Checking if required dirs exist as sometimes they do not and
				// they need to be created.
				File dbDir = new File(dbPath);
				if (!dbDir.exists()) {
					dbDir.mkdirs();
				}
				// Copying db from assets to db directory.
				InputStream in = context.getAssets().open(dbName);
				OutputStream out = new FileOutputStream(dbPath + dbName);
				bufCopy(in, out);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (database != null) {
			database.close();
		}
	}

	public SQLiteDatabase openDB() {
		database = SQLiteDatabase.openDatabase(dbPath + dbName, null,
				SQLiteDatabase.OPEN_READONLY);
		return database;
	}

	public Cursor runQuery(String query) {
		Cursor c = database.rawQuery(query, null);
		return c;
	}

	public String getStringEntry(String column, String table, String match) {
		String text = null;
		Cursor c = database.rawQuery("SELECT " + column + " FROM " + table
				+ " WHERE " + column + "=" + "'" + match + "'", null);
		if (c != null) {
			if (c.moveToFirst()) {
				text = c.getString(c.getColumnIndex(column));
			}
		}
		return text;
	}

	public int getIntEntry(String column, String table, String match) {
		int number = 0;
		Cursor c = database.rawQuery("SELECT " + column + " FROM " + table
				+ " WHERE " + column + "=" + match, null);
		if (c != null) {
			if (c.moveToFirst()) {
				number = c.getInt(c.getColumnIndex(column));
			}
		}
		return number;
	}

	public long getLongEntry(String column, String table, String match) {
		long number = 0;
		Cursor c = database.rawQuery("SELECT " + column + " FROM " + table
				+ " WHERE " + column + "=" + match, null);
		if (c != null) {
			if (c.moveToFirst()) {
				number = c.getInt(c.getColumnIndex(column));
			}
		}
		return number;
	}

	public double getDoubleEntry(String column, String table, String match) {
		double number = 0;
		Cursor c = database.rawQuery("SELECT " + column + " FROM " + table
				+ " WHERE " + column + "=" + match, null);
		if (c != null) {
			if (c.moveToFirst()) {
				number = c.getInt(c.getColumnIndex(column));
			}
		}
		return number;
	}

	public void dropTable(String tableName) {
		database.rawQuery("DROP TABLE " + tableName, null);
	}

	public long tableCount(String tableName) {
		long count = DatabaseUtils.queryNumEntries(database, tableName);
		return count;
	}

	// The .deleteDatabase method is supported on API 16 and higher.
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void deleteDB() {
		File oldDB = new File(dbPath + dbName);
		SQLiteDatabase.releaseMemory();
		SQLiteDatabase.deleteDatabase(oldDB);
	}

	public SQLiteDatabase getDB() {
		return database;
	}
	
	public void closeDB() {
		database.close();
	}

	public void downloadDB(Context context, String dbName, String urlLink) {
		try {
			URL url = new URL(urlLink);
			URLConnection ucon = url.openConnection();

			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);

			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			FileOutputStream fos = context.openFileOutput(dbName,
					Context.MODE_PRIVATE);
			fos.write(baf.toByteArray());
			fos.close();

			File dbFile = new File(context.getFilesDir() + "/" + dbName);
			InputStream in = new FileInputStream(dbFile);
			OutputStream out = new FileOutputStream(dbPath + dbName);
			bufCopy(in, out);
			dbFile.delete();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void bufCopy(InputStream in, OutputStream out) {
		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
