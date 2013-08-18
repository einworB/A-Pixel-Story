package com.example.projectrpg;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXProperty;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Point;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;

/**
 * This is the Activity the Player spends most of the playtime in 
 * The player moves through the different levels in this activity 
 * 
 * @author Philip
 *
 */
public class LevelActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener, IPinchZoomDetectorListener, IClickDetectorListener{

//=======================================================================CONSTANTS========================================================================================	

	/** constant for the camera width */
	private static final int CAMERA_WIDTH = 720;
	/** constant for the camera height */
	private static final int CAMERA_HEIGHT = 480;
	/** constant for maximal camera movement speed */
	private static final float MAX_CAMERA_VELOCITY = 1000;
	/** constant for maximal velocity for changing the zoom factor of the camera */
	private static final float MAX_ZOOM_FACTOR_CHANGE = 5;
	/** constant for the maximal zoom factor */
	private static final float MAX_ZOOM_FACTOR = 3.0f;
	/** constant for the minimal zoom factor */
	private static final float MIN_ZOOM_FACTOR = 1.0f;

//=======================================================================FIELDS========================================================================================	
	/** the smooth camera allowing smooth camera movements, bounds and zoom */
	private SmoothCamera camera;
	
	/** used for loading bitmaps*/
	private BitmapTextureAtlas bitmapTextureAtlas;
	/** used for loading bitmaps*/
	private TiledTextureRegion playerTextureRegion;
	/** the players sprite */
	private AnimatedSprite player;
	
	
	/**
	 * the controller responsible for communication between the Activity and the Algorithm 
	 * and for getting and setting of other technical information
	 */
	private Controller controller;
	
	/** a detector for reacting to pinch zooms */
	private PinchZoomDetector pinchZoomDetector;
	/** var for the zoom factor before the pinch zoom began */
	private float initialTouchZoomFactor;
	/** boolean to help the onscenetouch listener to decide if the screen was touched to zoom(and not to move the player) */
	private ClickDetector clickDetector;
	
	/** necessary as var to be able to stop an already started path */
	private LoopEntityModifier pathModifier;
	/** necessary as var to be able to stop an already started path */
	private boolean stop;
	
	/** the font for the dialog text */
	private Font font;

	/** boolean stating if the player is interacting at the moment */
	private boolean isInteracting;
	
	/** rect for the dialog window */
	private Rectangle rect;
	/** tickertext for the dialog window */
	private TickerText text;
	/** only needed for the dialog at the moment */
	private HUD hud;
	


	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	}

	/**
	 * sets up the camera and engine options like screen orientation 
	 * and ratio resolution policy
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		camera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_CAMERA_VELOCITY, MAX_CAMERA_VELOCITY, MAX_ZOOM_FACTOR_CHANGE);
		camera.setZoomFactorDirect(1.5f);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	/**
	 * loads ressources like sprites using bitmaptextureatlas
	 */
	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128);
		this.playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bitmapTextureAtlas, this, "player.png", 0, 0, 3, 4);
		this.bitmapTextureAtlas.load();
		
		this.font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.font.load();
		
		
		rect = new Rectangle(10, CAMERA_HEIGHT-110, CAMERA_WIDTH-20, 100, this.getVertexBufferObjectManager());
		rect.setColor(Color.WHITE);
		
		

		controller = new Controller();
//		moveXStart = 0;
//		moveYStart = 0;
	}

	/**
	 * creates a scene and its children and adds them to the scene
	 */
	@Override
	public Scene onCreateScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		
		for(int i=1; i<3; i++){			
			TMXTiledMap tmxTiledMap = controller.loadTMXMap(getAssets(), this.mEngine, getVertexBufferObjectManager(), i);
			OurScene scene = new OurScene(i, this, tmxTiledMap, controller.getSpawn());
			controller.addSceneToManager(scene);
		}

		camera.setBounds(0, 0, 50*32, 50*32);	// TODO: insert constants
		camera.setBoundsEnabled(true);
		
		/* set the scene's on touch listener to the activity itself */
//		scene.setOnSceneTouchListener(this);
		pinchZoomDetector = new PinchZoomDetector(this);
		pinchZoomDetector.setEnabled(true);
		clickDetector = new ClickDetector(this);
		clickDetector.setEnabled(true);
		
		/* Calculate the coordinates for the player sprite, so it's spawned in the center of the camera. */
		/** BEKOMMT FALSCHE SCENE ZURÜCK????!!!!!????!!! WIESO?????!!!!!!!*/
		OurScene scene = controller.getCurrentScene();
		Log.d("RPG", "Scene: "+scene.getID());
		Log.d("RPG", "Hashmap: "+scene.getSpawns());
		float[] coords = scene.getSpawn("SPAWN");
		Log.d("RPG", ""+coords[0]+","+coords[1]);
		TMXLayer layer = controller.getTMXLayer();
		final TMXTile spawnTile = layer.getTMXTileAt(coords[0], coords[1]);
		final float spawnX = spawnTile.getTileX() + 4;
		final float spawnY = spawnTile.getTileY();

		/* Create the sprite and add it to the scene. */
		player = new AnimatedSprite(spawnX, spawnY, 24, 32, this.playerTextureRegion, this.getVertexBufferObjectManager());
		controller.getCurrentScene().attachChild(player);
		
		/* let the camera chase the player */
		camera.setChaseEntity(player);
		
		hud = new HUD();
		camera.setHUD(hud);
		
		return controller.getCurrentScene();
	}

	/**
	 * on scene touch listener, called when player touches the screen 
	 * gets the path to the touched tile from the controller 
	 * and starts the movement(if the player is not moving already)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene scene, final TouchEvent sceneTouchEvent) {
		pinchZoomDetector.onTouchEvent(sceneTouchEvent);
		clickDetector.onTouchEvent(sceneTouchEvent);
		
		return true;
	}

	/**
	 * adds the dialog window(consisting of a rect and a tickertext) to the hud
	 * @param interactionText - the string to show in the dialog
	 */
	private void startInteraction(String interactionText) {
		isInteracting = true;
		
		hud.attachChild(rect);
		
		text = new TickerText(rect.getX()+10, rect.getY()+10, font, interactionText, new TickerTextOptions(HorizontalAlign.CENTER, 10), this.getVertexBufferObjectManager());
		text.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		hud.attachChild(text);
	}
	
	/**
	 * detaches dialog window(consisting of a rect and a tickertext) fromt he hud
	 */
	private void stopInteraction() {
		hud.detachChild(text);
		hud.detachChild(rect);
		isInteracting = false;
	}

	/**
	 * Responsible for moving the player along the given path 
	 * and for showing the correct animation
	 * 
	 * @param path - The path the player shall go to
	 */
	private void startPath(final Path path) {
		Log.d("RPG", "Run to: "+path.getCoordinatesX()[path.getSize()-1]+", "+path.getCoordinatesY()[path.getSize()-1]);
		pathModifier = new LoopEntityModifier(new OurPathModifier(50, path, null, new IPathModifierListener() {

			@Override
			/**
			 * called at the beginning of the movement, 
			 * sets the isMoving boolean in the controller 
			 */
			public void onPathStarted(final PathModifier pathModifier, final IEntity entity) {
				controller.animationStarted();
				stop = false;
			}

			@Override
			/**
			 * called whenever a new waypoint is started 
			 * gets the correct type of animation from the controller and shows it
			 */
			public void onPathWaypointStarted(final PathModifier pathModifier, final IEntity entity, final int waypointIndex) {
				if(!stop){
					switch(controller.getAnimationType(path, waypointIndex)){
						/* move left */
						case 1:
							player.animate(new long[]{200, 200, 200}, 9, 11, true);
							break;
						/* move right */
						case 2:
							player.animate(new long[]{200, 200, 200}, 3, 5, true);
							break;
						/* move up */
						case 3:
							player.animate(new long[]{200, 200, 200}, 0, 2, true);
							break;
						/* move down */
						case 4:
							player.animate(new long[]{200, 200, 200}, 6, 8, true);
							break;
					}	
				}
							
			}
			
			@Override
			/** called whenever a waypoint is finished, stops the animation */
			public void onPathWaypointFinished(final PathModifier modifier, final IEntity entity, final int waypointIndex) {
				if(stop){
					player.unregisterEntityModifier(pathModifier);
					controller.animationFinished();
				}
				
				player.stopAnimation();
			}

			@Override
			/**
			 *  called when the path is finished 
			 *  stops the animation  
			 *  and sets the isMoving boolean in the controller false
			 */
			public void onPathFinished(final PathModifier pathModifier, final IEntity entity) {
				player.stopAnimation();
				controller.animationFinished();
//				camera.setChaseEntity(null);
				
				TMXTile tile = controller.getTMXLayer().getTMXTileAt(player.getX(), player.getY());
				TMXTiledMap map = controller.getCurrentScene().getMap();
				if(tile.getTMXTileProperties(map)!=null){
					TMXProperties<TMXTileProperty> properties = tile.getTMXTileProperties(map);
					for(int i=0; i<properties.size(); i++) {
						TMXTileProperty property = properties.get(i);
						if(property.getName().contentEquals("TRANSITION")) startNewLevel(property.getValue());
					}
				}
			}
		}), 0);
		pathModifier.setAutoUnregisterWhenFinished(false);
		player.registerEntityModifier(pathModifier);
	}
	
	/**
	 * notifies the controller to start a new level and recreate the scene
	 * @param id 
	 */
	private void startNewLevel(String nextIdString) {
		Log.d("RPG", "NEW LEVEL");
		int nextId = Integer.parseInt(nextIdString.substring(nextIdString.length()-1));
		OurScene scene = controller.getCurrentScene();
		int currentId = scene.getID();
		scene.detachChild(player);
		if(nextId>currentId) controller.nextLevel();
		else controller.previousLevel();
		scene = controller.getCurrentScene();
		scene.attachChild(player);
		
		/* Calculate the coordinates for the player sprite, so it's spawned in the center of the camera. */
		final float[] coords = scene.getSpawn("LEVEL"+currentId);
		final TMXTile spawnTile = controller.getTMXLayer().getTMXTileAt(coords[0], coords[1]);
		final float spawnX = spawnTile.getTileX() + 4;
		final float spawnY = spawnTile.getTileY();
		player.setX(spawnX);
		player.setY(spawnY);
		
		this.mEngine.setScene(scene);
	}

	/**
	 * called when the player is already moving and the user touches the display
	 * interrupts the current path
	 */
	private void stopPath() {
		stop = true;
	}

	/**
	 * called when the user begins to pinch
	 */
	@Override
	public void onPinchZoomStarted(PinchZoomDetector pinchZoomDetector,
			TouchEvent sceneTouchEvent) {
		initialTouchZoomFactor = camera.getZoomFactor();
	}

	/**
	 * called during the pinch zoom, zooms the camera accordingly
	 */
	@Override
	public void onPinchZoom(PinchZoomDetector pinchZoomDetector,
			TouchEvent touchEvent, float zoomFactor) {
		final float newZoomFactor = initialTouchZoomFactor * zoomFactor;
		if(newZoomFactor < MAX_ZOOM_FACTOR && newZoomFactor > MIN_ZOOM_FACTOR){
			camera.setZoomFactor(newZoomFactor);
		}
	}

	/**
	 * calles when the user has stopped pinching
	 */
	@Override
	public void onPinchZoomFinished(PinchZoomDetector pinchZoomDetector,
			TouchEvent touchEvent, float zoomFactor) {

	}

	@Override
	public void onClick(ClickDetector clickDetector, int pointerID,
			float sceneX, float sceneY) {
		if(!controller.isMoving()){
			if(isInteracting){
				stopInteraction();
			}
			TMXTile startTile = ((TMXLayer) mEngine.getScene().getChildByIndex(0)).getTMXTileAt(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
			TMXTile destinationTile = ((TMXLayer) mEngine.getScene().getChildByIndex(0)).getTMXTileAt(sceneX, sceneY);
			OurScene scene = controller.getCurrentScene();
			if(controller.doAction(startTile, destinationTile, scene.getMap(), scene)){
				Path path = controller.getPath(startTile, destinationTile, scene.getMap());
				if(path!=null) startPath(path);
			} else{
				String interActionText = controller.getInteractionText();
				startInteraction(interActionText);
			}					
		} else{
			stopPath();
		}
	}

}
