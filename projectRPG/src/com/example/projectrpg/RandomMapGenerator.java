package com.example.projectrpg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

public class RandomMapGenerator {

	private Context context;
	private OurRandomGenerator rand = new OurRandomGenerator();
	private XmlSerializer serializer;
	private int index;

	public RandomMapGenerator(Context context){
		this.context = context;
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
		boolean spawnSet = false;
    	while(true){
    		// rand[0,28)+1 to exclude edges
    		int randomInt = rand.nextInt(28);
    		randomInt++;
    		int num = 0;
    		switch(rand.nextInt(4)){
        		case 0:
        			num = randomInt;
        			break;
        		case 1:
        			num = randomInt*30;
        			break;
        		case 2:
        			num = (randomInt+1)*30 - 1;
        			break;
        		case 3:
        			num = 870 + randomInt;
        			break;
    		}
    		if(kindOfTile[num]==-1){
    			if(index==1 && !spawnSet){
    				kindOfTile[num] = 23;
    				if(num<30) kindOfTile[num+30] = 1;
    				else if(num%30==0) kindOfTile[num+1] = 1;
    				else if((num+1)%30==0) kindOfTile[num-1] = 1;
    				else if(num>870) kindOfTile[num-30] = 1;
    				spawnSet = true;
    				continue;
    			}
				kindOfTile[num] = 13;
				if(num<30) kindOfTile[num+30] = 1;
				else if(num%30==0) kindOfTile[num+1] = 1;
				else if((num+1)%30==0) kindOfTile[num-1] = 1;
				else if(num>870) kindOfTile[num-30] = 1;
				
    			break;
    		}
    	}
        
        kindOfTile[0] = 2;
        kindOfTile[29] = 2;
        kindOfTile[870] = 2;
        kindOfTile[899] = 2;
//		boolean[] spawnSet = new boolean[2];
	    for (int i=0; i<900; i++){
			serializer.startTag("", "tile");
//	            Log.d("RANDOMDOUBLE", ""+randomDouble);
//            if(i==868 || i==867) kindOfTile[i] = 1;
//            else if(i<30 || i%30==0 || (i+1)%30==0 || i>870){
//            	setEdgeTile(randomDouble, spawnSet, kindOfTile, i);
//            	
//            }
//            ) && i!=0 && i!=29 && i!=870 && i!=899){
        	if(kindOfTile[i]==-1){
        		if(!(i<30 || i%30==0 || (i+1)%30==0 || i>870)){
            		int counter = 0;
	            	if(kindOfTile[i-1]>1 && kindOfTile[i-1]!=13 && kindOfTile[i-1]!=23) counter++;
	            	if(kindOfTile[i+1]>1 && kindOfTile[i+1]!=13 && kindOfTile[i+1]!=23) counter++;
	            	if(kindOfTile[i+30]>1 && kindOfTile[i+30]!=13 && kindOfTile[i+30]!=23) counter++;
	            	if(kindOfTile[i-30]>1 && kindOfTile[i-30]!=13 && kindOfTile[i-30]!=23) counter++;
	            	if(counter>2 || rand.getBoolean(0.9)) kindOfTile[i] = 1;
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
            	} else kindOfTile[i] = 2;
            	
            }
            serializer.attribute("", "gid", ""+kindOfTile[i]);
            
            
            serializer.endTag("", "tile");
        }
	}

//	private void setEdgeTile(double randomDouble, boolean[] spawnSet, int[] kindOfTile, int i) throws IllegalArgumentException, IllegalStateException, IOException {
//		if(i>896 && (!spawnSet[0] || !spawnSet[1])){
//			if(index==1){
//				if(!spawnSet[0]){
//					spawnSet[0] = true;
//					kindOfTile[i] = 13;
//				} else{
//					spawnSet[1] = true;
//					kindOfTile[i] = 23;
//				}
//			}
//		}
//		if(index==1){
//    		if((!spawnSet[0] || !spawnSet[1]) && randomDouble<0.05 && i!=0 && i!=29 && i!=899 && i!=870){
//				if(((i+1)%30==0  && kindOfTile[i-1]==1) || (i>870 && kindOfTile[i-30]==1) || ((i+1)%30!=0 && i<870)){
//    				if(!spawnSet[0]){
//						spawnSet[0] = true;
//						kindOfTile[i] = 13;
//						freeNeighboringTile(i, kindOfTile);
//					} else{
//						spawnSet[1] = true;
//						kindOfTile[i] = 23;
//						freeNeighboringTile(i, kindOfTile);
//					}
//				} else kindOfTile[i] = 2;
//    		} else kindOfTile[i] = 2;
//    	} else{
//    		if(!spawnSet[0] && rand.nextDouble()<0.05 && i!=0 && i!=29 && i!=899 && i!=870){
//    			if(((i+1)%30==0  && kindOfTile[i-1]==1) || (i>870 && kindOfTile[i-30]==1) || (i+1)%30!=0 || i<870){
//					spawnSet[0] = true;
//					kindOfTile[i] = 13;
//					freeNeighboringTile(i, kindOfTile);
//    			} else kindOfTile[i] = 2;
//    		} else kindOfTile[i] = 2;
//    	}		
//	}
//
//	private void freeNeighboringTile(int i, int[] kindOfTile) {
//		if(i<30) kindOfTile[i+30] = 1;
//		else if(i%30==0) kindOfTile[i+1] = 1;
//	}
}
