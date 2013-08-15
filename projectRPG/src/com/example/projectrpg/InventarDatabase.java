package com.example.projectrpg;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventarDatabase {

	private static final String DATABASE_NAME = "inventar.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_TABLE = "inventar";

	public static final String KEY_ID = "_id";
	public static final String KEY_ITEM_TYPE = "type";
	public static final String KEY_ITEM_NAME = "name";
	public static final String KEY_NUMBER_OF_ITEMS = "0";

	public static final int COLUMN_NAME_INDEX = 1;

	private HighScoreDBOpenHelper dbHelper;

	private SQLiteDatabase db;

	public InventarDatabase(Context context) {
		dbHelper = new HighScoreDBOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	private class HighScoreDBOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" + KEY_ID
				+ " integer primary key autoincrement, " + KEY_ITEM_NAME
				+ " text not null," + KEY_ITEM_TYPE + " text not null," + KEY_NUMBER_OF_ITEMS
				+ " text not null);";

		public HighScoreDBOpenHelper(Context c, String dbname,
				SQLiteDatabase.CursorFactory factory, int version) {
			super(c, dbname, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		// Only needed if we want to upgrade the database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Do Nothing now...
		}
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}

	public long insertSlotItem(Slot slot) {
		ContentValues values = new ContentValues();
		values.put(KEY_ITEM_NAME, slot.getItemName());
		values.put(KEY_ITEM_TYPE, slot.getItemType());
		values.put(KEY_NUMBER_OF_ITEMS, slot.getNumberOfItems());
		return db.insert(DATABASE_TABLE, null, values);
	}

	public void removeAll() {
		db.execSQL("DELETE FROM " + DATABASE_TABLE);
	}

	public Slot getSingleSlot(int id) {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ID,
				KEY_ITEM_NAME, KEY_ITEM_TYPE, KEY_NUMBER_OF_ITEMS }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Slot score = new Slot(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));

		return score;
	}

	public ArrayList<Slot> getAllSlots() {
		ArrayList<Slot> slotList = new ArrayList<Slot>();

		String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Slot slot = new Slot();
				slot.setSlotID(Integer.parseInt(cursor.getString(0)));
				slot.setItemName(cursor.getString(1));
				slot.setItemType(cursor.getString(2));
				slot.setNumberOfItems(Integer.parseInt(cursor.getString(3)));

				slotList.add(slot);
			} while (cursor.moveToNext());
		}

		return slotList;
	}
	
	public int getScoresCount() {
		String countQuery = "SELECT  * FROM " + DATABASE_TABLE;
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}
}
