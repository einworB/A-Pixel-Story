package de.apixelstory.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.apixelstory.quest.GetItemQuest;
import de.apixelstory.quest.KillQuest;
import de.apixelstory.quest.Quest;
import de.apixelstory.quest.QuestManager;
import de.apixelstory.quest.TalkToQuest;
import de.apixelstory.sprites.NPC;
import de.apixelstory.util.OurRandomGenerator;

/**
 * Database class
 * sets up and connects to a database(using an OurDatabaseHelper)
 * containing text, quests(kill/talkto/getitem), npc and item(heal/armor/weapon) tables
 */
public class OurDatabase {

	/** the path to the database in the project's file directory */
    private static final String DB_PATH = "/data/data/de.projectrpg/databases/";
 
    /** the name of the database */
    private static final String DB_NAME = "androidprojectdatabase";
 
    /** the database object */
    private SQLiteDatabase db; 
 
    /** the context of the calling activity */
    private final Context context;

    /** the sqlite database helper responsible for opening, closing, etc the database */
    private OurDatabaseHelper dbHelper;
	
    /** a random generator */
	private OurRandomGenerator rgen = new OurRandomGenerator();

	/** a boolean stating whether the merchant screen(InventarActivity) should be shown */
	private boolean startMerchant;
 
	/**
	 * constructor
	 * @param context the context of the calling activity
	 */
	public OurDatabase(Context context){
        this.context = context;
		dbHelper = new OurDatabaseHelper();
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * opens the database
	 */
	public void open(){
		dbHelper.openDataBase();
	}
	/**
	 * closes the database 
	 */
	public void close(){
		dbHelper.close();
	}
	
	/**
	 * gets the item from the database with the given name
	 * @param name the name of the item
	 * @return the new created item with the values from the database(or null if not exists)
	 */
	public Item getItem(String name){
		String sql = String.format("SELECT * FROM item WHERE name='%s'", name);
		Cursor cursor = db.rawQuery(sql, null);
		
		if (cursor.moveToFirst()) {
				// Gets the Task in the current row from the Cursor
				// Note: We get the column index from the Adapter class! Makes
				// debugging / changes easier...
				String itemType = cursor.getString(1);
				int itemID = cursor.getInt(0);
				int levelNeeded = cursor.getInt(3);
				sql = String.format("SELECT * FROM %s WHERE itemID='%s'", itemType, itemID);
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
				} else if(itemType.contentEquals("healItem")){
					if (cursor.moveToFirst()) {
						int healValue = cursor.getInt(2);
						HealItem item = new HealItem(name, levelNeeded, itemType, healValue);
						return item;
					} else return null;
				}
				else return null;
		} else return null;
	}
	
	/**
	 * Called when the player defeated an opponent
	 * gets a random Item of the given level for the player to loot from the Database
	 * @param level the item level
	 * @return a new created Item with the values from the database
	 */
	public Item getLoot(int level) {
		String sql = String.format("SELECT * FROM item WHERE levelNeeded='%s'", level);
		Cursor itemCursor = db.rawQuery(sql, null);
		int itemCount = itemCursor.getCount();
		Item[] objects = new Item[itemCount];
		int counter = 0;
		
		if (itemCursor.moveToFirst()) {
			do{
				// Gets the Task in the current row from the Cursor
				int itemID = itemCursor.getInt(0);
				String itemType = itemCursor.getString(1);
				String name = itemCursor.getString(2);
				int levelNeeded = itemCursor.getInt(3);
				sql = String.format("SELECT * FROM %s WHERE itemID='%s'", itemType, itemID);
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
						Log.d("DB", "armor");
					}
				} else if(itemType.contentEquals("healItem")){
					if (cursor.moveToFirst()) {
						int healValue = cursor.getInt(2);
						HealItem item = new HealItem(name, levelNeeded, itemType, healValue);
						objects[counter] = item;
						Log.d("DB", "healItem");
					}
				}
				counter++;
			}while(itemCursor.moveToNext());
		} else return null;
		Item result = null;
		while(result==null){
			int randomInt = rgen.nextInt(itemCount);
			result = objects[randomInt];
		}
		return result;
	}
	
	/**
	 * called when a textScroll shall be displayed
	 * gets the according text from the database and also creates a new quest
	 * @param npc the NPC the player is talking to
	 * @param questManager the questManager containing the active and closed quests
	 * @return an ArrayList containing the Strings to be displayed in the Textscroll one after another
	 */
	public ArrayList<String> getText(NPC npc, QuestManager questManager){
		int npcID;
		int textID = 0;
		String sql;
		if(npc!=null){
			npcID = npc.getID(); 
			sql = String.format("SELECT * FROM npc WHERE _id='%s'", npcID);
			Cursor npcCursor = db.rawQuery(sql, null);
			if (npcCursor.moveToFirst()) {
				textID = npcCursor.getInt(1);
			}
		}
		else npcID = 0;
		ArrayList<Quest> openQuests = questManager.getActiveQuests();
		boolean questActive = false;
		Quest quest = null;
		for(int i=0; i<openQuests.size(); i++){
			quest = openQuests.get(i);
			if((npcID==1 && quest.getNpcID()==0) || (npcID==quest.getNpcID())){
				questActive = true;
				break;
			}
		}
		if(questActive){
			boolean isFulfilled = quest.isFulfilled();
			
			if(isFulfilled){
				questManager.endQuestByIndex(openQuests.indexOf(quest));
				if(quest.getID()!=1){
					return quest.getEndText();
				}else{
					ArrayList<String> text = new ArrayList<String>();
					text.addAll(quest.getEndText());
					text.addAll(getText(npc, questManager));
					return text;
				}
			}
			else{
				return quest.getDuringText();
			}
		}
		else{
			ArrayList<Quest> closedQuests = questManager.getClosedQuests();
			boolean questClosed = false;
			for(int i=0; i<closedQuests.size(); i++){
				quest = closedQuests.get(i);
				if(npcID==quest.getNpcID()){
					questClosed = true;
					break;
				}
			}
			if(questClosed){
				sql = String.format("SELECT text FROM text WHERE _id='%s'", textID);
				Cursor textCursor = db.rawQuery(sql, null);
				if(textCursor.moveToFirst()) {
					startMerchant = true;
					return convertStringToArrayList(textCursor.getString(0));
				} else return null;
			}
			else{
				sql = String.format("SELECT * FROM quest WHERE npcID='%s'", npcID);
				Cursor questCursor = db.rawQuery(sql, null);
				if(questCursor.moveToFirst()) {
					do{					
						int previousID = questCursor.getInt(1);
						boolean previousFulfilled = false;
						for(int i=0; i<closedQuests.size(); i++){
							if(closedQuests.get(i).getID()==previousID){
								previousFulfilled = true;
								break;
							}
						}
						if(previousID==0 || previousFulfilled){
							quest = getQuestFromDatabase(questCursor, npcID);
							
							questManager.startQuest(quest);
							return quest.getStartText();
						}
					} while(questCursor.moveToNext());
				}
				sql = String.format("SELECT text FROM text WHERE _id='%s'", textID);
				Cursor textCursor = db.rawQuery(sql, null);
				if(textCursor.moveToFirst()) {
					startMerchant = true;
					return convertStringToArrayList(textCursor.getString(0));
				} else return null;					
			}
		}		
	}
	
	/** 
	 * @return if the merchant screen(InventarActivity) should be shown
	 */
	public boolean startMerchant(){
		return startMerchant;
	}
	
	/**
	 * splits the String into an ArrayList of Strings(each element is shown one after another when displayed in the textscroll)
	 * @param str the str to be converted
	 * @return the ArrayList containing the splitted String
	 */
	private ArrayList<String> convertStringToArrayList(String str){
		String[] strArray = str.split(";");
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i< strArray.length; i++){
			result.add(strArray[i]);
		}
		return result;
	}
	
	/**
	 * get the name of a NPC from the database
	 * @param id the ID of the npc
	 * @return the name of the npc or null
	 */
	public String getNPCName(int id) {
		String sql = String.format("SELECT name FROM npc WHERE _id='%s'", id);
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			return cursor.getString(0);
		} else return null;
	}
		
	/**
	 * creates a new Quest
	 * @param npcID the ID of the npc the player is interacting with
	 * @return the new Quest
	 */
	public Quest getQuest(int npcID) {
		String sql;
		Quest quest = null;
		
		sql = String.format("SELECT * FROM quest WHERE npcID='%s'", npcID);
		Cursor questCursor = db.rawQuery(sql, null);
		// only when npc has a Quest
		if(questCursor.moveToFirst()) {			
			quest = getQuestFromDatabase(questCursor, npcID);
		}
		return quest;
	}
	
	/**
	 * creates a new Quest with values from the database
	 * @param questCursor Cursor containing results from the previous SQL query on the quest table of the database
	 * @param npcID the ID of the npc the player is interacting with
	 * @return the new Quest
	 */
	private Quest getQuestFromDatabase(Cursor questCursor, int npcID) {
		Quest quest = null;
		String sql;
		int questID = questCursor.getInt(0);
		String name = questCursor.getString(7);
		int startTextID = questCursor.getInt(3);
		int duringTextID = questCursor.getInt(4);
		int endTextID = questCursor.getInt(5);
		ArrayList<String> startText = null;
		ArrayList<String> duringText = null;
		ArrayList<String> endText = null;
		sql = String.format("SELECT text FROM text WHERE _id='%s'", startTextID);
		Cursor textCursor = db.rawQuery(sql, null);
		if(textCursor.moveToFirst()) {
			startMerchant = false;
			String str =textCursor.getString(0);
			if(str!=null) startText = convertStringToArrayList(str);
		}
		sql = String.format("SELECT text FROM text WHERE _id='%s'", duringTextID);
		textCursor = db.rawQuery(sql, null);
		if(textCursor.moveToFirst()) {
			startMerchant = false;
			String str =textCursor.getString(0);
			if(str!=null) duringText = convertStringToArrayList(str);
		}
		sql = String.format("SELECT text FROM text WHERE _id='%s'", endTextID);
		textCursor = db.rawQuery(sql, null);
		if(textCursor.moveToFirst()) {
			startMerchant = false;
			String str =textCursor.getString(0);
			if(str!=null) endText = convertStringToArrayList(str);
		}
		
		int level = questCursor.getInt(6);
		int specialItemID = questCursor.getInt(9);
		Item specialReward = null;
		if(specialItemID!=0){
			// TODO: get item from database
		}
		String type = questCursor.getString(8);
		String shortText = questCursor.getString(10);
		if(type.contentEquals("talkTo")){
			sql = String.format("SELECT talkToQuest.npcID FROM talkToQuest WHERE questID='%s'", questID);
			Cursor specificQuestCursor = db.rawQuery(sql, null);
			if(specificQuestCursor.moveToFirst()){
				int targetNPC =  specificQuestCursor.getInt(0);
				quest = new TalkToQuest(questID, name, npcID, startText, duringText, endText, targetNPC, level, specialReward, shortText);
			}
		}else if(type.contentEquals("killQuest")){
			sql = String.format("SELECT target, killCount FROM killQuest WHERE questID='%s'", questID);
			Cursor specificQuestCursor = db.rawQuery(sql, null);
			if(specificQuestCursor.moveToFirst()){
				String enemyName = specificQuestCursor.getString(0);
				int killCount = specificQuestCursor.getInt(1);
				Log.d("projekt", "killcount: " + killCount);
				quest = new KillQuest(questID, name, npcID, startText, duringText, endText, enemyName, killCount, level, specialReward, shortText);
			}
		}else{
			sql = String.format("SELECT itemName, count FROM getItemQuest WHERE questID='%s'", questID);
			Cursor specificQuestCursor = db.rawQuery(sql, null);
			if(specificQuestCursor.moveToFirst()){
				String itemName = specificQuestCursor.getString(0);
				int count = specificQuestCursor.getInt(1);
				quest = new GetItemQuest(questID, name, npcID, startText, duringText, endText, itemName, count, level, specialReward, shortText);
			}
		}
		return quest;
	}
	
	
	/**
	 * Helper for opening the Database
	 */
	private class OurDatabaseHelper extends SQLiteOpenHelper{
	    /**
	     * Constructor
	     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	     * @param context
	     */
	    public OurDatabaseHelper() {	 
	    	super(context, DB_NAME, null, 1);
	    }	
	 
	    /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     */
	    public void createDataBase() throws IOException{
	 
	    	boolean dbExist = checkDataBase();
	 
	    	if(dbExist){
	    		//do nothing - database already exist
	    	}else{
	 
	    		//Creates an empty database in the default system path
	            //which will be overwritten with our database.
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
	     * Check if the database already exist to avoid re-copying the file on each application start.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	 
	    	try{
	    		String myPath = DB_PATH + DB_NAME;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    	}catch(SQLiteException e){
	 
	    		//database doesn't exist yet.
	    		Log.d("RPG", "GOT IT!");
	    	}
	 
	    	if(checkDB != null){	 
	    		checkDB.close();
	    		
	    		return true;
	    	} 
	    	else return false;
	    }
	 
	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled by transfering bytestream.
	     */
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
	 
	    /**
	     * opens the database
	     * @throws SQLException
	     */
	    public void openDataBase() throws SQLException{
	        String myPath = DB_PATH + DB_NAME;
	    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    }
	 
	    /**
	     * closes the database
	     */
	    @Override
		public synchronized void close() {
	 
	    	    if(db != null)
	    		    db.close();
	 
	    	    super.close();
	 
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
			// nothing to do here
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// nothing to do here
		}
	
	}
}
