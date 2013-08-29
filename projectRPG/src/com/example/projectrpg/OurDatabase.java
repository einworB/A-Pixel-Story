package com.example.projectrpg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OurDatabase {

	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.projectrpg/databases/";
 
    private static String DB_NAME = "androidprojectdatabase";
 
    private SQLiteDatabase db; 
 
    private final Context context;

	private String dir;
	
	private OurDatabaseHelper dbHelper;
	
	private OurRandomGenerator rgen = new OurRandomGenerator();
 
	public OurDatabase(Context context){
        this.context = context;
		dbHelper = new OurDatabaseHelper();
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class OurDatabaseHelper extends SQLiteOpenHelper{
	    /**
	     * Constructor
	     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	     * @param context
	     */
	    public OurDatabaseHelper() {	 
	    	super(context, DB_NAME, null, 1);
			dir = context.getFilesDir().getPath();
	    }	
	 
	  /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     * */
	    public void createDataBase() throws IOException{
	 
	    	boolean dbExist = checkDataBase();
	 
	    	if(dbExist){
	    		//do nothing - database already exist
	    	}else{
	 
	    		//By calling this method an empty database will be created into the default system path
	               //of your application so we are gonna be able to overwrite that database with our database.
	        	this.getReadableDatabase();
	        	this.close();
	        	try {
	        		this.close();
	    			copyDataBase();
	 
	    		} catch (IOException e) {
	    			e.printStackTrace();
	        		throw new Error("Error copying database");
	 
	        	}
	    	}
	 
	    }
	 
	    /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	 
	    	try{
	    		String dir = context.getFilesDir().getPath();
	    		String myPath = DB_PATH + DB_NAME;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    	}catch(SQLiteException e){
	 
	    		//database doesn't exist yet.
	    		Log.d("RPG", "GOT IT!");
	    	}
	 
	    	if(checkDB != null){
	 
	    		checkDB.close();
	 
	    	}
	 
	    	return checkDB != null ? true : false;
	    }
	 
	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     * */
	    private void copyDataBase() throws IOException{
	 
	    	//Open your local db as the input stream
	    	InputStream myInput = context.getAssets().open("db/"+DB_NAME);
	 
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH + DB_NAME;
	 
	    	//Open the empty db as the output stream	    	
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	Log.d("RPG", "db created");
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	 
	    }
	 
	    public void openDataBase() throws SQLException{	 
	    	//Open the database
	        String myPath = DB_PATH + DB_NAME;
	    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    }
	 
	    @Override
		public synchronized void close() {
	 
	    	    if(db != null)
	    		    db.close();
	 
	    	    super.close();
	 
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
	 
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
		}
	
	
 
	}
	
	public void open(){
		dbHelper.openDataBase();
	}
	public void close(){
		dbHelper.close();
	}
	
	public Item getItem(String name){
		String sql = new String("SELECT * FROM item WHERE name='"+name+"'");
		Cursor cursor = db.rawQuery(sql, null);
		
		if (cursor.moveToFirst()) {
				// Gets the Task in the current row from the Cursor
				// Note: We get the column index from the Adapter class! Makes
				// debugging / changes easier...
				String itemType = cursor.getString(1);
				int itemID = cursor.getInt(0);
				int levelNeeded = cursor.getInt(3);
				sql = new String("SELECT * FROM "+itemType+" WHERE itemID='"+itemID+"'");
				cursor = db.rawQuery(sql, null);
				if(itemType.contentEquals("weapon")){
					if (cursor.moveToFirst()) {
						int attackValue = cursor.getInt(2);
						int weaponType = cursor.getInt(3);
						Weapon weapon = new Weapon(name, levelNeeded, attackValue, weaponType);
						return weapon;
					}
					else return null;					
				} else if(itemType.contentEquals("armor")){
					if (cursor.moveToFirst()) {
						int defenseValue = cursor.getInt(2);
						int armorType = cursor.getInt(3);
						Armor armor = new Armor(name, levelNeeded, defenseValue, armorType);
						return armor;
					} else return null;
				}
				else return null;
		} else return null;
	}
	public Object[] getLoot(int[] loot) {
		String sql = new String("SELECT * FROM item WHERE levelNeeded='"+loot[0]+"'");
		Cursor itemCursor = db.rawQuery(sql, null);
		int itemCount = itemCursor.getCount();
		Object[] objects = new Object[itemCount];
		int counter = 0;
		
		if (itemCursor.moveToFirst()) {
			do{
				// Gets the Task in the current row from the Cursor
				// Note: We get the column index from the Adapter class! Makes
				// debugging / changes easier...
				int itemID = itemCursor.getInt(0);
				String itemType = itemCursor.getString(1);
				String name = itemCursor.getString(2);
				int levelNeeded = itemCursor.getInt(3);
				sql = new String("SELECT * FROM "+itemType+" WHERE itemID='"+itemID+"'");
				Cursor cursor = db.rawQuery(sql, null);
				if(itemType.contentEquals("weapon")){
					if (cursor.moveToFirst()) {
						int attackValue = cursor.getInt(2);
						int weaponType = cursor.getInt(3);
						Weapon weapon = new Weapon(name, levelNeeded, attackValue, weaponType);
						objects[counter] = weapon;
						Log.d("DB", "weapon");
					}					
				} else if(itemType.contentEquals("armor")){
					if (cursor.moveToFirst()) {
						int defenseValue = cursor.getInt(2);
						int armorType = cursor.getInt(3);
						Armor armor = new Armor(name, levelNeeded, defenseValue, armorType);
						objects[counter] = armor;
						Log.d("DB", "weapon");
					}
				}
				counter++;
			}while(itemCursor.moveToNext());
		} else return null;
		
		int[] randomInts = rgen.getInts(3, itemCount);
		Object[] result = new Object[3];
		for(int i=0; i<3; i++){
			result[i] = objects[randomInts[i]];
		}
		return result;
	}
		
		
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
}
