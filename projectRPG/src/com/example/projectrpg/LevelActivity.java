package com.example.projectrpg;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
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
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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
import org.andengine.opengl.texture.region.TextureRegion;
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
	
	/** used for loading bitmaps, can hold more than one*/
	private BitmapTextureAtlas playerTiledBitmapTextureAtlas;
	/** used for loading bitmaps*/
	private TiledTextureRegion playerTextureRegion;
	/** the players sprite */
	private Player player;
	/** used for loading bitmaps*/
	private BitmapTextureAtlas opponentTiledBitmapTextureAtlas;
	/** used for loading bitmaps*/
	private TiledTextureRegion opponentTextureRegion;
	
	
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
	private boolean fleeing;

	private Font lifeFont;
	private TextureRegion beutelTextureRegion;
	private BitmapTextureAtlas bitmapTextureAtlas;

	private TextureRegion portraitTextureRegion;
	private Sprite portrait;
	private TextureRegion redBarTextureRegion;
	private Sprite redBarPlayer;
	private TextureRegion portraitEnemyTextureRegion;
	private Sprite portraitEnemy;
	private Sprite redBarEnemy;
	


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

		this.playerTiledBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 128);
		this.playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.playerTiledBitmapTextureAtlas, this, "player4x4.png", 0, 0, 4, 4);
		this.opponentTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.playerTiledBitmapTextureAtlas, this, "enemy.png", 128, 0, 4, 4);
		this.playerTiledBitmapTextureAtlas.load();
		
		this.bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 128);		
		this.beutelTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "beutel.png", 0, 0);
		this.portraitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "portrait.png", 32, 0);
		this.portraitEnemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "portraitEnemy.png", 82, 0);
		this.redBarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "roterBalken.png", 132, 0);
		this.bitmapTextureAtlas.load();
		
		
		this.font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.font.load();
		this.lifeFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 12);
		this.lifeFont.load();
		
		
		rect = new Rectangle(10, CAMERA_HEIGHT-110, CAMERA_WIDTH-20, 100, this.getVertexBufferObjectManager());
		rect.setColor(Color.WHITE);
		portrait = new Sprite(2, 2, portraitTextureRegion, getVertexBufferObjectManager());
		portraitEnemy = new Sprite(CAMERA_WIDTH-52, 2, portraitEnemyTextureRegion, getVertexBufferObjectManager());
		redBarPlayer = new Sprite(44, 11, 0, 4, redBarTextureRegion, getVertexBufferObjectManager());
		redBarEnemy = new Sprite(CAMERA_WIDTH-10, 11, 0, 4, redBarTextureRegion, getVertexBufferObjectManager());
		redBarEnemy.setZIndex(1);
		

		controller = new Controller(this);
		
		
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

		camera.setBounds(0, 0, 30*32, 30*32);	// TODO: insert constants
		camera.setBoundsEnabled(true);
		
		/* set the scene's on touch listener to the activity itself */
//		scene.setOnSceneTouchListener(this);
		pinchZoomDetector = new PinchZoomDetector(this);
		pinchZoomDetector.setEnabled(true);
		clickDetector = new ClickDetector(this);
		clickDetector.setEnabled(true);
		
		/* Calculate the coordinates for the player sprite, so it's spawned in the center of the camera. */
		OurScene scene = controller.getCurrentScene();
		Log.d("RPG", "Scene: "+scene.getID());
		Log.d("RPG", "Hashmap: "+scene.getSpawns());
		float[] coords = scene.getSpawn("SPAWN");
		Log.d("RPG", "Koords: "+coords[0]+","+coords[1]);
		TMXLayer layer = controller.getTMXLayer();
		final TMXTile spawnTile = layer.getTMXTileAt(coords[0], coords[1]);
		final float spawnX = spawnTile.getTileX() + 4;
		final float spawnY = spawnTile.getTileY();

		/* Create the sprite and add it to the scene. */
		player = new Player(spawnX, spawnY, 24, 32, this.playerTextureRegion, this.getVertexBufferObjectManager(), 2);
		player.setZIndex(1);
		int column = spawnTile.getTileColumn();
		int row = spawnTile.getTileRow();
		Log.d("RPG", "COLUMN: "+column+" ROW: "+row);
		if(column==0) player.setCurrentTileIndex(4);
		else if(row==0) player.setCurrentTileIndex(0);
		else if(row==layer.getTileRows()-1) player.setCurrentTileIndex(8);
		else if(column==layer.getTileColumns()-1) player.setCurrentTileIndex(12);
		
		controller.getCurrentScene().attachChild(player);
		final Opponent opponent = new Opponent(layer.getTMXTileAt(40, 40).getTileX()+4, layer.getTMXTileAt(40, 40).getTileY(), 24, 32, this.opponentTextureRegion, this.getVertexBufferObjectManager(), 1);
		controller.getCurrentScene().attachChild(opponent);
		
		/* let the camera chase the player */
		camera.setChaseEntity(player);
		camera.setCenterDirect(spawnX, spawnY);
		
		hud = new HUD();
		camera.setHUD(hud);
		hud.attachChild(portrait);
		hud.attachChild(redBarPlayer);
		hud.attachChild(redBarEnemy);
		
		
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
//		controller.testDatabase();
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
	 * @param destinationTile 
	 */
	private void startPath(final Path path, final TMXTile destinationTile) {
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
							player.animate(new long[]{200, 200, 200, 200}, 12, 15, true);
							break;
						/* move right */
						case 2:
							player.animate(new long[]{200, 200, 200, 200}, 4, 7, true);
							break;
						/* move up */
						case 3:
							player.animate(new long[]{200, 200, 200, 200}, 8, 11, true);
							break;
						/* move down */
						case 4:
							player.animate(new long[]{200, 200, 200, 200}, 0, 3, true);
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
				
				final TMXLayer layer = controller.getTMXLayer();
				final TMXTile endTile = layer.getTMXTileAt(player.getX(), player.getY());
				TMXTiledMap map = controller.getCurrentScene().getMap();
				turnToTile(player, destinationTile, endTile, map);
				if(endTile.getTMXTileProperties(map)!=null){
					TMXProperties<TMXTileProperty> properties = endTile.getTMXTileProperties(map);
					for(int i=0; i<properties.size(); i++) {
						TMXTileProperty property = properties.get(i);
						if(property.getName().contentEquals("TRANSITION") && !property.getValue().contentEquals("SPAWN")) startNewLevel(property.getValue());
					}
				} else{
					final OurScene scene = controller.getCurrentScene();
					runOnUpdateThread(new Runnable() {						
						@Override
						public void run() {
							for(int i=0; i<scene.getChildCount(); i++){
								IEntity child = scene.getChildByIndex(i);
								if(layer.getTMXTileAt(child.getX(), child.getY())==endTile && child instanceof LootBag){
									scene.detachChild(child);
									Object[] loot = controller.getLoot(((LootBag) child).getLoot());
									if(loot!=null){
										for(int j=0; j<3; j++){
											if(loot[j] instanceof Weapon) player.setWeapon((Weapon) loot[j]);
											else if(loot[j] instanceof Armor) player.addArmor((Armor) loot[j]);
										}
									}
									Log.d("RPG", "Equipped Weapon: "+player.getEquippedWeapon().getName());
									break;
								}
							}
						}
					});
					
				}
				if(fleeing) fleeing = false;
			}
		}), 0);
		pathModifier.setAutoUnregisterWhenFinished(false);
		player.registerEntityModifier(pathModifier);
	}
	
	private void turnToTile(AnimatedSprite sprite, TMXTile destinationTile, TMXTile playerTile, TMXTiledMap map) {
		if(destinationTile!=playerTile){
			int divColumns = destinationTile.getTileColumn() - playerTile.getTileColumn();
			int divRows = destinationTile.getTileRow() - playerTile.getTileRow();
			if(divColumns==0){
				if(divRows<0) sprite.setCurrentTileIndex(8);
				else sprite.setCurrentTileIndex(0);
			} else{
				if(divColumns<0) sprite.setCurrentTileIndex(12);
				else sprite.setCurrentTileIndex(4);
			}
		}
	}

	/**
	 * notifies the controller to start a new level and recreate the scene
	 * @param id 
	 */
	private void startNewLevel(final String nextIdString) {
		
		this.runOnUpdateThread(new Runnable() {			
			@Override
			public void run() {
				Log.d("RPG", "NEW LEVEL: "+nextIdString);
				int nextId = Integer.parseInt(nextIdString.substring(nextIdString.length()-1));
				OurScene scene = controller.getCurrentScene();
				int currentId = scene.getID();
				controller.getCurrentScene().detachChild(player);
				if(nextId>currentId) controller.nextLevel();
				else controller.previousLevel();
				
				scene = controller.getCurrentScene();
				controller.getCurrentScene().attachChild(player);
				
		
		
				/* Calculate the coordinates for the player sprite to spawn */
				final float[] coords = scene.getSpawn("LEVEL"+currentId);
				TMXLayer layer = controller.getTMXLayer();
				final TMXTile spawnTile = layer.getTMXTileAt(coords[0], coords[1]);
				final float spawnX = spawnTile.getTileX() + 4;
				final float spawnY = spawnTile.getTileY();
				player.setX(spawnX);
				player.setY(spawnY);
				int column = spawnTile.getTileColumn();
				int row = spawnTile.getTileRow();
				Log.d("RPG", "COLUMN: "+column+" ROW: "+row);
				if(column==0) player.setCurrentTileIndex(4);
				else if(row==0) player.setCurrentTileIndex(0);
				else if(row==layer.getTileRows()-1) player.setCurrentTileIndex(8);
				else if(column==layer.getTileColumns()-1) player.setCurrentTileIndex(12);
				
				final Opponent opponent = new Opponent(layer.getTMXTileAt(40, 40).getTileX()+4, layer.getTMXTileAt(40, 40).getTileY(), 24, 32, playerTextureRegion, getVertexBufferObjectManager(), 2);
				scene.attachChild(opponent);
				LevelActivity.this.mEngine.setScene(scene);
			}
		});	
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
			OurScene scene = controller.getCurrentScene();
			final TMXLayer layer = (TMXLayer) scene.getChildByIndex(0);
			final TMXTile startTile = layer.getTMXTileAt(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
			final TMXTile destinationTile = layer.getTMXTileAt(sceneX, sceneY);
			switch(controller.doAction(startTile, destinationTile, scene.getMap(), scene)){
				case 1:
					if(portraitEnemy.hasParent()) hud.detachChild(portraitEnemy);
					Path path = controller.getPath(startTile, destinationTile, scene.getMap());
					if(path!=null) startPath(path, destinationTile);
					else Log.d("RPG", "path=null");
					break;
				case 2:
					String interActionText = controller.getInteractionText();
					startInteraction(interActionText);
					break;
				case 3:
					Opponent opponent = (Opponent) scene.getChildByMatcher(new IEntityMatcher() {							
						@Override
						public boolean matches(IEntity entity) {
							if(layer.getTMXTileAt(entity.getX(), entity.getY()) == destinationTile)	return true;
							else return false;
						}
					});
					if(!portraitEnemy.hasParent()){
						hud.attachChild(portraitEnemy);
						hud.sortChildren();
					}
					turnToTile(player, destinationTile, startTile, scene.getMap());
					turnToTile(opponent, startTile, destinationTile, scene.getMap());
					switch(controller.fight(player, opponent, redBarPlayer, redBarEnemy)){
						case 1:
							fightWon(opponent, destinationTile);
							hud.detachChild(portraitEnemy);
							hud.detachChild(redBarEnemy);
							break;
						case 2:
							flee();
							break;
					}
					break;
			}					
		} else{
			if(!fleeing) stopPath();
		}
	}

	private void flee() {
		float[] coords;
		if(controller.getLevel()==1) coords = controller.getCurrentScene().getSpawn("SPAWN");
		else coords = controller.getCurrentScene().getSpawn("LEVEL1");
		TMXLayer layer = controller.getTMXLayer();
		TMXTile startTile = layer.getTMXTileAt(player.getX(), player.getY());
		TMXTile endTile = layer.getTMXTileAt(coords[0], coords[1]);
		Path path = controller.getPath(startTile, endTile, controller.getCurrentScene().getMap());
		if(path!=null) startPath(path, endTile);
		else Log.d("RPG", "path=null");
		fleeing = true;
	}

	private void fightWon(Opponent opponent, TMXTile destinationTile) {
		OurScene scene = controller.getCurrentScene();
		scene.detachChild(opponent);
		Sprite beutel = new LootBag(destinationTile.getTileX(), destinationTile.getTileY(), beutelTextureRegion, getVertexBufferObjectManager(), opponent);
		scene.attachChild(beutel);
		scene.sortChildren();
	}
}
