package com.example.projectrpg;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

/**
 * This is the Activity the Player spends most of the playtime in 
 * The player moves through the different levels in this activity 
 * 
 * @author Philip
 *
 */
public class LevelActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener, IPinchZoomDetectorListener{

	/** constant for the camera width */
	private static final int CAMERA_WIDTH = 720;
	/** constant for the camera height */
	private static final int CAMERA_HEIGHT = 480;
	/** constant for maximal camera movement speed */
	private static final float MAX_CAMERA_VELOCITY = 50;
	/** constant for maximal velocity for changing the zoom factor of the camera */
	private static final float MAX_ZOOM_FACTOR_CHANGE = 5;
	
	private static final float MAX_ZOOM_FACTOR = 1.5f;
	
	private static final float MIN_ZOOM_FACTOR = 0.5f;
	
	/** the smooth camera allowing smooth camera movements, bounds and zoom */
	private SmoothCamera camera;
	
	/** used for loading bitmaps*/
	private BitmapTextureAtlas bitmapTextureAtlas;
	/** used for loading bitmaps*/
	private TiledTextureRegion playerTextureRegion;
	/** the players sprite */
	private AnimatedSprite player;
	
	/** the TMX tiled map */
	private TMXTiledMap tmxTiledMap;
	/** the first and only layer of the TMX map */
	private TMXLayer tmxLayer;
	
	/**
	 * the controller responsible for communication between the Activity and the Algorithm 
	 * and for getting and setting of other technical information
	 */
	private Controller controller;
	
	private PinchZoomDetector pinchZoomDetector;
	private float initialTouchZoomFactor;

	/**
	 * sets up the camera and engine options like screen orientation 
	 * and ratio resolution policy
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		camera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_CAMERA_VELOCITY, MAX_CAMERA_VELOCITY, MAX_ZOOM_FACTOR_CHANGE);
		camera.setBounds(0, 0, 200*32, 200*32);
		camera.setBoundsEnabled(true);
		
		controller = new Controller();

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
	}

	/**
	 * creates a scene and its children and adds them to the scene
	 */
	@Override
	public Scene onCreateScene() {
		/* create the scene */
		final Scene scene = new Scene();
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		/* load the tmx tiled map */
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(TMXTiledMap tmxTiledMap, TMXLayer tmxLayer, TMXTile tmxTile, TMXProperties<TMXTileProperty> tmxTileProperties) {
					// TODO nothing...					
				}				
			});
			this.tmxTiledMap = tmxLoader.loadFromAsset("tmx/mytmx.tmx");
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		tmxLayer = this.tmxTiledMap.getTMXLayers().get(0);
		scene.attachChild(tmxLayer);
		
		/* set the scene's on touch listener to the activity itself */
		scene.setOnSceneTouchListener(this);
		pinchZoomDetector = new PinchZoomDetector(this);
		pinchZoomDetector.setEnabled(true);

		/* Calculate the coordinates for the player sprite, so it's spawned in the center of the camera. */
		final float centerX = (CAMERA_WIDTH - this.playerTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.playerTextureRegion.getHeight()) / 2;

		/* Create the sprite and add it to the scene. */
		player = new AnimatedSprite(centerX, centerY, 48, 64, this.playerTextureRegion, this.getVertexBufferObjectManager());		
		scene.attachChild(player);
		/* let the camera chase the player */
		camera.setChaseEntity(player);
		
		return scene;
	}

	/**
	 * on scene touch listener, called when player touches the screen 
	 * gets the path to the touched tile from the controller 
	 * and starts the movement(if the player is not moving already)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent sceneTouchEvent) {
		pinchZoomDetector.onTouchEvent(sceneTouchEvent);
		if(!controller.isMoving()){
			TMXTile startTile = tmxLayer.getTMXTileAt(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
			TMXTile destinationTile = tmxLayer.getTMXTileAt(sceneTouchEvent.getX(), sceneTouchEvent.getY());
			Path path = controller.getPath(startTile, destinationTile, tmxTiledMap);
			
			startPath(path);
		}		
		return false;
	}

	/**
	 * Responsible fot moving the player along the given path 
	 * and for showing the correct animation
	 * 
	 * @param path - The path the player shall go to
	 */
	private void startPath(final Path path) {
		player.registerEntityModifier(new LoopEntityModifier(new OurPathModifier(50, path, null, new IPathModifierListener() {
			@Override
			/**
			 * called at the beginning of the movement, 
			 * sets the isMoving boolean in the controller 
			 */
			public void onPathStarted(final PathModifier pathModifier, final IEntity entity) {
				controller.animationStarted();
			}

			@Override
			/**
			 * called whenever a new waypoint is started 
			 * gets the correct type of animation from the controller and shows it
			 */
			public void onPathWaypointStarted(final PathModifier pathModifier, final IEntity entity, final int waypointIndex) {				
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
			
			@Override
			/** called whenever a waypoint is finished, stops the animation */
			public void onPathWaypointFinished(final PathModifier pathModifier, final IEntity entity, final int waypointIndex) {
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
			}
		}), 0));
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pinchZoomDetector,
			TouchEvent sceneTouchEvent) {
		initialTouchZoomFactor = camera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pinchZoomDetector,
			TouchEvent touchEvent, float zoomFactor) {
		final float newZoomFactor = initialTouchZoomFactor * zoomFactor;
		if(newZoomFactor < MAX_ZOOM_FACTOR && newZoomFactor > MIN_ZOOM_FACTOR){
			camera.setZoomFactor(newZoomFactor);
		}
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pinchZoomDetector,
			TouchEvent touchEvent, float zoomFactor) {
		// TODO Auto-generated method stub
		
	}

}
