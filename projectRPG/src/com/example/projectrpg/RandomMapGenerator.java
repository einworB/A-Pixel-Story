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
	private RandomMapArrayGenerator rMapArray = new RandomMapArrayGenerator();
	
	private XmlSerializer serializer;
	private int level;

	public RandomMapGenerator(Context context){
		this.context = context;
	}
	
	public InputStream createMap(int index){
		this.level = index;
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
        serializer.attribute("", "name", "tilesetDesert");
        serializer.attribute("", "tilewidth", "32");
        serializer.attribute("", "tileheight", "32");
        
        serializer.startTag("", "image");
        serializer.attribute("", "source", "gfx/tilesetDesert.png");
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
        
        serializer.startTag("", "tile");
       	serializer.attribute("", "id", "12");
       	serializer.startTag("", "properties");
       	serializer.startTag("", "property");
       	serializer.attribute("", "name", "TRANSITION");
       	if(level == 1){
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
       	} else serializer.attribute("", "value", "LEVEL1");
       	
       	serializer.endTag("", "property");
        serializer.endTag("", "properties");
        serializer.endTag("", "tile");
	}
	
	private void setTiles() throws IllegalArgumentException, IllegalStateException, IOException {
//		Log.d("projekt", "davor" + level);
		int[][] mapArray = rMapArray.generateMapArray(level);
//		Log.d("projekt", "danach" + level);
		for(int y = 0; y < mapArray.length; y++) {
			for(int x = 0; x < mapArray.length; x++) {
				
				serializer.startTag("", "tile");
	            serializer.attribute("", "gid", "" + mapArray[x][y]);
	            serializer.endTag("", "tile");
			}
		}
	}

}
