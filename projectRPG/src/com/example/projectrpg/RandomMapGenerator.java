package com.example.projectrpg;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Random;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

public class RandomMapGenerator {

	private Context context;
	private AssetManager assetManager;
	private Random rand = new Random();
	private XmlSerializer serializer;
	private int index;

	public RandomMapGenerator(Context context, AssetManager assetManager){
		this.context = context;
		this.assetManager = assetManager;
	}
	
	public InputStream createMap(int index){
		this.index = index;
		String filename = "LEVEL"+index+".tmx";
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			OutputStreamWriter writer = new OutputStreamWriter(fos);
			writer.write(writeXml());
			writer.flush();
			writer.close();
			
			
			InputStream fin = context.openFileInput(filename);//assetManager.open("tmx/mytmx.tmx");
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
        serializer.attribute("", "name", "tileset10x10");
        serializer.attribute("", "tilewidth", "32");
        serializer.attribute("", "tileheight", "32");
        
        serializer.startTag("", "image");
        serializer.attribute("", "source", "gfx/tileset10x10.png");
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
		 for(int i=1; i<22; i++){
	        	if(i==12) continue;
	        	serializer.startTag("", "tile");
	        	serializer.attribute("", "id", ""+i);
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
	       	if(index==1){
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
	       	}
	       	else serializer.attribute("", "value", "LEVEL1");
	       	serializer.endTag("", "property");
	        serializer.endTag("", "properties");
	        serializer.endTag("", "tile");
	}

	private void setTiles() throws IllegalArgumentException, IllegalStateException, IOException {
		int[] kindOfTile = new int[900];
		for (int i=0; i<900; i++){
			kindOfTile[i] = -1;
		}
		boolean[] spawnSet = new boolean[2];
	    for (int i=0; i<900; i++){
			serializer.startTag("", "tile");
            double randomDouble = rand.nextDouble();
//	            Log.d("RANDOMDOUBLE", ""+randomDouble);
            if(i<30 || i%30==0 || (i+1)%30==0 || i>870){
            	setEdgeTile(randomDouble, spawnSet, kindOfTile, i);
            	
            } else{
            	if(kindOfTile[i]==-1){
            		int counter = 0;
	            	if(kindOfTile[i-1]>1 && kindOfTile[i-1]!=13 && kindOfTile[i-1]!=13) counter++;
	            	if(kindOfTile[i+1]>1 && kindOfTile[i+1]!=13 && kindOfTile[i+1]!=13) counter++;
	            	if(kindOfTile[i+30]>1 && kindOfTile[i+30]!=13 && kindOfTile[i+30]!=13) counter++;
	            	if(kindOfTile[i-30]>1 && kindOfTile[i-30]!=13 && kindOfTile[i-30]!=13) counter++;
	            	if(counter>2 || randomDouble<0.9) kindOfTile[i] = 1;
	            	else{
	            		int randomInt = rand.nextInt(30);
	            		if(randomInt<15) kindOfTile[i] = 2;
	            		else if(randomInt<25) kindOfTile[i] = 3;
	            		else if(randomInt<29){
	            			if((29-(i%30))>=2 && i<840 && kindOfTile[i]==-1 && kindOfTile[i+1]==-1 && kindOfTile[i+30]==-1 && kindOfTile[i+31]==-1){
	            				kindOfTile[i] = 11;
		                		kindOfTile[i+1] = 12;
		                		kindOfTile[i+30] = 21;
		                		kindOfTile[i+31] = 22;
	            			}
	            			else kindOfTile[i] = 1;
	            		}
	            		else{
	            			if((29-(i%30))>=3 && i<840 && kindOfTile[i]==-1 && kindOfTile[i+1]==-1 && kindOfTile[i+2]==-1 && kindOfTile[i+30]==-1
	            					&& kindOfTile[i+31]==-1 && kindOfTile[i+32]==-1 && kindOfTile[i+61]==-1){
	            				kindOfTile[i] = 4;
		                		kindOfTile[i+1] = 5;
		                		kindOfTile[i+2] = 6;
		                		kindOfTile[i+30] = 14;
		                		kindOfTile[i+31] = 15;
		                		kindOfTile[i+32] = 16;
		                		kindOfTile[i+61] = 1;
	            			}
	            			else kindOfTile[i] = 1;
	            		}
	            		
	            	}
            	}
            	serializer.attribute("", "gid", ""+kindOfTile[i]);
            }
            
            
            
            serializer.endTag("", "tile");
        }
	}

	private void setEdgeTile(double randomDouble, boolean[] spawnSet, int[] kindOfTile, int i) throws IllegalArgumentException, IllegalStateException, IOException {
		if(index==1){
    		if((!spawnSet[0] || !spawnSet[1]) && randomDouble<0.05 && i!=0 && i!=29 && i!=899 && i!=870){
				if(!spawnSet[0]){
					spawnSet[0] = true;
					kindOfTile[i] = 13;
					freeNeighboringTile(i, kindOfTile);
				} else{
					spawnSet[1] = true;
					kindOfTile[i] = 23;
					freeNeighboringTile(i, kindOfTile);
				}
    		} else kindOfTile[i] = 2;
    	} else{
    		if(!spawnSet[0] && rand.nextDouble()<0.05 && i!=0 && i!=29 && i!=899 && i!=870){
				spawnSet[0] = true;
				kindOfTile[i] = 13;
				freeNeighboringTile(i, kindOfTile);
    		} else kindOfTile[i] = 2;
    	}
		serializer.attribute("", "gid", ""+kindOfTile[i]);		
	}

	private void freeNeighboringTile(int i, int[] kindOfTile) {
		if(i<30) kindOfTile[i+30] = 1;
		else if(i%30==0) kindOfTile[i+1] = 1;
		else if((i+1)%30==0) kindOfTile[i-1] = 1;
		else if(i>870) kindOfTile[i-30] = 1;
	}
}
