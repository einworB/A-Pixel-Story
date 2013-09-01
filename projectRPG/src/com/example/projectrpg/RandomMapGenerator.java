package com.example.projectrpg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

public class RandomMapGenerator {

	private Context context;
	private RandomMapArrayGenerator rMapArray;
	
	private XmlSerializer serializer;
	private int level;
	private int lastTransitionExitSide = -1;
	private int lastLevel;
	public RandomMapGenerator(Context context, int lastLevel){
		this.context = context;
		this.lastLevel = lastLevel;
		rMapArray = new RandomMapArrayGenerator(lastLevel);
	}
	
	public InputStream createMap(int index, int lastTransitionExitSide){
		this.level = index;
		if(index != 1) {
			this.lastTransitionExitSide = lastTransitionExitSide;
		}
		String filename = "LEVEL"+index+".tmx";
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			OutputStreamWriter writer = new OutputStreamWriter(fos);
			writer.write(writeXml());
			writer.flush();
			writer.close();
			
			
			InputStream fin = context.openFileInput(filename);
			return fin;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	private String writeXml(){
	    serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        
	        setupDoc();        
	        
           
	        serializer.endDocument();
        } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return writer.toString();
	     
	}

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
	
	public int getLastSpawnSide() {
		return rMapArray.getLastSpawnSide();
	}
}
