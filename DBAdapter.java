package com.panoskrt.dbadapter;

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

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DBAdapter {

	private String dbPath;
	private String dbName;
	private Context context;

	public DBAdapter(Context context, String dbName) {
		this.context = context;
		this.dbName = dbName;
		dbPath = "/data/data/" + context.getPackageName() + "/databases/";
	}

	public void copyDB() {
		SQLiteDatabase checkDB = null;
		try {
			try {
				checkDB = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
				// Checking if required dirs exist as sometimes they do not and they need to be created.
				File dbDir = new File(dbPath);
				if (!dbDir.exists()){ dbDir.mkdirs(); }
				// Copying db from assets to db directory.
				InputStream in = context.getAssets().open(dbName);
				OutputStream out = new FileOutputStream(dbPath + dbName);
				bufCopy(in, out);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (checkDB != null) {
			checkDB.close();
		}
	}

	public SQLiteDatabase openDB() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READONLY);
		return db;
	}

	public Cursor runQuery(SQLiteDatabase database, String query) {
		Cursor c = database.rawQuery(query, null);
		return c;
	}
	
	public String getStringEntry(SQLiteDatabase database, String column, String table, String match){
		String text = null;
		Cursor c = database.rawQuery("SELECT " + column + " FROM " + table + " WHERE " + column + "=" + "'" + match + "'", null);
		if (c != null) {
			if (c.moveToFirst()) {
				text = c.getString(c.getColumnIndex(column));
			}
		}
		return text;
	}
	
	public int getIntEntry(SQLiteDatabase database, String column, String table, String match){
		int number = 0;
		Cursor c = database.rawQuery("SELECT " +  column + " FROM " + table + " WHERE " + column + "=" + match, null);
		if (c != null) {
			if (c.moveToFirst()) {
				number = c.getInt(c.getColumnIndex(column));
			}
		}
		return number;
	}

	public long tableCount(SQLiteDatabase database, String tableName) {
		long count = DatabaseUtils.queryNumEntries(database, tableName);
		return count;
	}

	public void closeDB(SQLiteDatabase database) {
		database.close();
	}
	
	public void deleteDB(){
		File oldDB = new File(dbPath + dbName);
		SQLiteDatabase.deleteDatabase(oldDB);
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

			FileOutputStream fos = context.openFileOutput(dbName, Context.MODE_PRIVATE);
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
	
	private void bufCopy(InputStream in, OutputStream out){
		try {
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
