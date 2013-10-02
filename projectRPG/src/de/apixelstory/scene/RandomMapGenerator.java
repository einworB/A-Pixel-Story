package de.apixelstory.scene;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import de.apixelstory.util.OurRandomGenerator;

import android.content.Context;
import android.util.Xml;

/**
 * This class generates a random tmx map for a level. 
 */
public class RandomMapGenerator {
	
	/** the calling activity*/
	private Context context;
	/** the random map in an array*/
	private RandomMapArrayGenerator rMapArray;
	
	/** the xml serializer*/
	private XmlSerializer serializer;
	/** the actual level*/
	private int level;
	/** the side of the map the transition tile was in the last level*/
	private int lastTransitionExitSide = -1;
	/** the index of the last level*/
	private int lastLevel;
	/** the slot in which the map should be saved*/
	private int slot;
	
	/**
	 * the constructor
	 * @param context the calling activity
	 * @param lastLevel the index of the last level
	 * @param slot the slot in which the map should be saved
	 */
	public RandomMapGenerator(Context context, int lastLevel, int slot){
		this.context = context;
		this.lastLevel = lastLevel;
		rMapArray = new RandomMapArrayGenerator(lastLevel);
		this.slot = slot;
	}
	
	/**
	 * create the map. Therefore open a file output stream and write the xml statements into it.
	 * @param index the index of the actual level
	 * @param lastTransitionExitSide the side of the map the transition tile was in the last level
	 * @return an input stream to load the map
	 */
	public InputStream createMap(int index, int lastTransitionExitSide){
		this.level = index;
		if(index != 1) {
			this.lastTransitionExitSide = lastTransitionExitSide;
		}
		String filename = "slot" + slot + "level"+index+".tmx";
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			OutputStreamWriter writer = new OutputStreamWriter(fos);
			writer.write(writeXml());
			writer.flush();
			writer.close();
			
			
			InputStream fin = context.openFileInput(filename);
			return fin;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * write the xml statements.
	 * @return a string with the xmlstatements into it
	 */
	private String writeXml(){
	    serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        
	        setupDoc();        
	        
           
	        serializer.endDocument();
        } catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return writer.toString();
	     
	}

	/**
	 * setup the document. 
	 * Define which tileset should be used and the common information of the tmx map.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void setupDoc() throws IllegalArgumentException, IllegalStateException, IOException {
		OurRandomGenerator rGen = new OurRandomGenerator();
		boolean desert = rGen.getBoolean(0.5);
		String tileset = "";
		if(desert) {
			tileset = "tilesetDesert";
		} else {
			tileset = "tilesetGras";
		}
		
		serializer.startDocument("UTF-8", true);
        
        serializer.startTag("", "map");
        serializer.attribute("", "version", "1.0");
        serializer.attribute("", "orientation", "orthogonal");
        serializer.attribute("", "width", "30");
        serializer.attribute("", "height", "30");
        serializer.attribute("", "tilewidth", "32");
        serializer.attribute("", "tileheight", "32");
        
        serializer.startTag("", "tileset");
        serializer.attribute("", "firstgid", "1");
        serializer.attribute("", "name", tileset);
        serializer.attribute("", "tilewidth", "32");
        serializer.attribute("", "tileheight", "32");
        
        serializer.startTag("", "image");
        serializer.attribute("", "source", "gfx/" + tileset + ".png");
        serializer.attribute("", "width", "320");
        serializer.attribute("", "height", "320");
        serializer.endTag("", "image");
        
        setupProperties();
        
        serializer.endTag("", "tileset");        
        
        serializer.startTag("", "layer");
        serializer.attribute("", "name", "Ground");
        serializer.attribute("", "width", "30");
        serializer.attribute("", "height", "30");
        serializer.startTag("", "data");        

        setTiles();
        
        serializer.endTag("", "data");
        serializer.endTag("", "layer");
        serializer.endTag("", "map");
	}

	/**
	 * Define all properties of special tiles like the spawn and collision tiles.
	 * A tile with the property collision a sprite cannot walk on. 
	 * A tile with propertiy transition is to get from one level into another.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void setupProperties() throws IllegalArgumentException, IllegalStateException, IOException {
		for(int i = 1; i < 22; i++) {
	        if(i == 12) continue;
        	serializer.startTag("", "tile");
        	serializer.attribute("", "id", "" + i);
        	serializer.startTag("", "properties");
        	serializer.startTag("", "property");
        	serializer.attribute("", "name", "COLLISION");
        	
			serializer.attribute("", "value", "true");
			
        	serializer.endTag("", "property");
        	serializer.endTag("", "properties");
        	serializer.endTag("", "tile");
        }
        
       	if(level == 1){
            serializer.startTag("", "tile");
           	serializer.attribute("", "id", "12");
           	serializer.startTag("", "properties");
           	serializer.startTag("", "property");
           	serializer.attribute("", "name", "TRANSITION");
       		serializer.attribute("", "value", "LEVEL2");
       		serializer.endTag("", "property");
	        serializer.endTag("", "properties");
	        serializer.endTag("", "tile");
	        
	        serializer.startTag("", "tile");
	       	serializer.attribute("", "id", "22");
	       	serializer.startTag("", "properties");
	       	serializer.startTag("", "property");
	       	serializer.attribute("", "name", "TRANSITION");
	       	serializer.attribute("", "value", "SPAWN");
       	} else if(level == lastLevel) {
            serializer.startTag("", "tile");
           	serializer.attribute("", "id", "32");
           	serializer.startTag("", "properties");
           	serializer.startTag("", "property");
           	serializer.attribute("", "name", "TRANSITION");
           	serializer.attribute("", "value", "LEVEL" + (level - 1));
       	} else {
            serializer.startTag("", "tile");
           	serializer.attribute("", "id", "32");
           	serializer.startTag("", "properties");
           	serializer.startTag("", "property");
           	serializer.attribute("", "name", "TRANSITION");
           	serializer.attribute("", "value", "LEVEL" + (level - 1));
           	serializer.endTag("", "property");
            serializer.endTag("", "properties");
            serializer.endTag("", "tile");

           	serializer.startTag("", "tile");
           	serializer.attribute("", "id", "12");
           	serializer.startTag("", "properties");
           	serializer.startTag("", "property");
           	serializer.attribute("", "name", "TRANSITION");
           	serializer.attribute("", "value", "LEVEL" + (level + 1));
       	}
       	
       	serializer.endTag("", "property");
        serializer.endTag("", "properties");
        serializer.endTag("", "tile");
        
    	serializer.startTag("", "tile");
    	serializer.attribute("", "id", "" + 25);
    	serializer.startTag("", "properties");
    	serializer.startTag("", "property");
    	serializer.attribute("", "name", "COLLISION");
    	
		serializer.attribute("", "value", "true");
		
    	serializer.endTag("", "property");
    	serializer.endTag("", "properties");
    	serializer.endTag("", "tile");
	}
	
	/**
	 * set all Tiles into the map. The values of the tiles are determined in the class RandomMapArrayyGenerator.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void setTiles() throws IllegalArgumentException, IllegalStateException, IOException {
		int[][] mapArray = rMapArray.generateMapArray(level, lastTransitionExitSide);
		
		for(int y = 0; y < mapArray.length; y++) {
			for(int x = 0; x < mapArray.length; x++) {
				
				serializer.startTag("", "tile");
	            serializer.attribute("", "gid", "" + mapArray[x][y]);
	            serializer.endTag("", "tile");
			}
		}
	}
	
	/**
	 * get the side of the spawn in the previous level.
	 * @return the side of the spawn
	 */
	public int getLastSpawnSide() {
		return rMapArray.getLastSpawnSide();
	}
}
