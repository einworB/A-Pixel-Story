package de.projectrpg.game;

import java.util.ArrayList;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import de.projectrpg.algorithm.OurPathModifier;
import de.projectrpg.database.Item;
import de.projectrpg.inventory.InventarActivity;
import de.projectrpg.quest.GetItemQuest;
import de.projectrpg.quest.KillQuest;
import de.projectrpg.quest.TalkToQuest;
import de.projectrpg.save.LoadSavedGame;
import de.projectrpg.save.WriteSaveFile;
import de.projectrpg.scene.OurScene;
import de.projectrpg.scene.QuestScene;
import de.projectrpg.sprites.LootBag;
import de.projectrpg.sprites.NPC;
import de.projectrpg.sprites.Opponent;
import de.projectrpg.sprites.Player;
import de.projectrpg.start.HelpActivity;

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
	
	/** used for loading sprite bitmaps, can hold more than one*/
	private BitmapTextureAtlas tiledBitmapTextureAtlas;
	/** used for loading button bitmaps, can hold more than one*/
	private BitmapTextureAtlas bitmapTextureAtlas;
	
	/** used for loading bitmaps*/
	private TiledTextureRegion playerTextureRegion;
	/** used for loading bitmaps*/
	private TiledTextureRegion opponentTextureRegion;
	/** used for loading bitmaps*/
	private TiledTextureRegion npcTextureRegion;

	/** used for loading bitmaps*/
	private TextureRegion beutelTextureRegion;

	/** the players sprite */
	private Player player;

	/**the controller responsible for communication between the Activity and the Algorithm 
	 * and for getting and setting of other technical information */
	private static Controller controller;
	private QuestScene questScene;

	/** a detector for reacting to pinch zooms */
	private PinchZoomDetector pinchZoomDetector;
	/** boolean to help the onscenetouch listener to decide if the screen was touched to zoom(and not to move the player) */
	private ClickDetector clickDetector;
	
	/** necessary as var to be able to stop an already started path */
	private LoopEntityModifier pathModifier;
	
	/** the font for the dialog text */
	private Font font;
	/** the font for the quest titel */
	private Font fontQuestTitel;
	/** the font for the "how to close quest" text */
	private Font fontQuestHowTo;

	/** tickertext for the dialog window */
	private TickerText text;
	/** text for the name of the quest */
	private Text questName;
	/** text for the task of the quest */
	private Text questTask;
	/** text for the level of the player */
	private Text levelTextPlayer;
	/** text for the level of the actual opponent */
	private Text levelTextOpponent;
	/** text to inform the user how to close a quest */
	private Text howToCloseQuest;

	/** the list of Strings */
	private ArrayList<String> interActionText;

	
	/** the hud for the normal game scenes */
	private HUD hud;
	/** the hud for the questscene */
	private HUD questHud;

	/** the portrait of the player */
	private Sprite portrait;
	/** the portrait of the enemy */
	private Sprite portraitEnemy;
	/** the red life bar of the player */
	private Sprite redBarPlayer;
	/** the red life bar of the enemy */
	private Sprite redBarEnemy;
	/** the background of the dialog */
	private Sprite textScroll;
	/** the background of the exp bar */
	private Sprite expBackground;
	/** the start of the exp bar */
	private Sprite startExpBar;
	/** the exp bar */
	private Sprite expBar;
	/** the button to start the helpActivity */
	private Sprite helpButton;
	/** the button to start the questscene */
	private Sprite questButton;
	/** the button to start the inventory activity */
	private Sprite inventarButton;
	/** the button to go back to game from quest scene */
	private Sprite backToGameButton;
	/** the background of the button to go back to game from quest scene */
	private Sprite backToGameButtonBackground;
	/** the button to go to the next quest in quest scene */
	private Sprite nextQuestButton;
	/** the button to go to the previous quest in quest scene */
	private Sprite prevQuestButton;
	/** show if the button to go to next quest isn't shown */
	private Sprite nextQuestGrayButton;
	/** show if the button to go to previous quest isn't shown */
	private Sprite prevQuestGrayButton;
	
	/** boolean if the inventar was started already. true if it was started, else false. */
	private boolean inventarStarted = false;
	/** boolean if the helpactivity was started already. true if it was started, else false. */
	private boolean helpStarted = false;
	/** boolean if the player is fleeing. True if it is fleeing, else false. */
	private boolean fleeing;
	/** necessary as var to be able to stop an already started path */
	private boolean stop;
	/** boolean stating if the player is interacting at the moment */
	private boolean isInteracting;

	/** var for the zoom factor before the pinch zoom began */
	private float initialTouchZoomFactor;
	/** the number of the actual quest that should be shown */
	private int questcount = 0;

	/** the slot where the game is saved*/
	private int slot;
	/** if the game should be load from the slot or a new game should be started */
	private boolean newGame;
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		slot = (Integer) extras.get("slot");
		newGame = (Boolean) extras.get("newGame");
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

		this.tiledBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256);
		this.playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.tiledBitmapTextureAtlas, this, "player4x4.png", 0, 0, 4, 4);
		this.opponentTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.tiledBitmapTextureAtlas, this, "enemy.png", 128, 0, 4, 4);
		this.npcTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.tiledBitmapTextureAtlas, this, "npc.png", 128, 128, 4, 4);
		this.tiledBitmapTextureAtlas.load();
		
		this.bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024, 512);		
		beutelTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "lootBag.png", 0, 175);
		TextureRegion textScrollTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "dialogHintergrund.png", 0, 0);
		TextureRegion portraitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "portrait.png", 32, 175);
		TextureRegion portraitEnemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "portraitEnemy.png", 82, 175);
		TextureRegion redBarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "roterBalken.png", 132, 175);
		TextureRegion inventarButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "inventarButton.png", 182, 175);
		TextureRegion experienceBackgroundTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "expBackground.png", 236, 175);
		TextureRegion experienceBarTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "expBar.png", 236, 205);
		TextureRegion startExperienceBarTextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "startExpBar.png", 738, 175);

		TextureRegion helpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "helpButton.png", 758, 175);
		TextureRegion questButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "questButton.png", 812, 175);
		TextureRegion backToGameButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "backToGameButton.png", 0, 229);
		TextureRegion backToGameButtonBackgroudTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "backToGameButtonSchatten.png", 408, 229);

		TextureRegion nextQuestButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "nextQuest.png", 300, 229);
		TextureRegion prevQuestButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "prevQuest.png", 354, 229);
		
		TextureRegion nextQuestGrayButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "nextQuestGray.png", 462, 229);
		TextureRegion prevQuestGrayButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bitmapTextureAtlas, this, "prevQuestGray.png", 516, 229);

		this.bitmapTextureAtlas.load();
		
		
		this.font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.font.load();

		this.fontQuestTitel = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 50);
		this.fontQuestTitel.load();
		this.fontQuestHowTo = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 25);
		this.fontQuestHowTo.load();		
		
		
		textScroll = new Sprite(10, CAMERA_HEIGHT-110, CAMERA_WIDTH-40, 175, textScrollTextureRegion, this.getVertexBufferObjectManager());
		
		portrait = new Sprite(2, 2, portraitTextureRegion, getVertexBufferObjectManager());
		portraitEnemy = new Sprite(CAMERA_WIDTH-52, 2, portraitEnemyTextureRegion, getVertexBufferObjectManager());
		
		redBarPlayer = new Sprite(44, 11, 0, 4, redBarTextureRegion, getVertexBufferObjectManager());
		
		redBarEnemy = new Sprite(CAMERA_WIDTH-10, 11, 0, 4, redBarTextureRegion, getVertexBufferObjectManager());
		redBarEnemy.setZIndex(1);
		
		inventarButton = new Sprite(CAMERA_WIDTH-70, CAMERA_HEIGHT-70, 54, 54, inventarButtonTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(!inventarStarted){
					Log.d("RPG", "Inventar touched");
					inventarStarted = true;
					Intent intent = new Intent(LevelActivity.this, InventarActivity.class);
					intent.putExtra("isMerchant", false);
					startActivity(intent);
				}
				return true;
			}
		};
		
		questButton = new Sprite(CAMERA_WIDTH-140, CAMERA_HEIGHT-70, 54, 54, questButtonTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				startQuestScene();
				return true;
			}
		};
		helpButton = new Sprite(CAMERA_WIDTH-210, CAMERA_HEIGHT-70, 54, 54, helpButtonTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(!helpStarted){
					helpStarted = true;
					Intent intent = new Intent(LevelActivity.this, HelpActivity.class);
					startActivity(intent);
				}
				
				return true;
			}
		};
				
		backToGameButton = new Sprite(CAMERA_WIDTH/2 - 150, CAMERA_HEIGHT-150, 300, 100, backToGameButtonTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				OurScene scene = controller.getCurrentScene();
				camera.setHUD(hud);

				LevelActivity.this.mEngine.setScene(scene);
				return true;
			}
		};
		
		backToGameButtonBackground = new Sprite(CAMERA_WIDTH/2 - 152, CAMERA_HEIGHT-152, 302, 102, backToGameButtonBackgroudTextureRegion, getVertexBufferObjectManager());
		
		nextQuestButton = new Sprite(CAMERA_WIDTH/2 + 180, CAMERA_HEIGHT-125, 54, 54, nextQuestButtonTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(questcount + 1 < controller.getActiveQuests().size()) {
					questcount++;
					startQuestScene();
				}
				return true;
			}
		};
		
		prevQuestButton = new Sprite(CAMERA_WIDTH/2 - 234, CAMERA_HEIGHT-125, 54, 54, prevQuestButtonTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(questcount > 0) {
					questcount--;
					startQuestScene();
				}
				return true;
			}
		};
		
		nextQuestGrayButton = new Sprite(CAMERA_WIDTH/2 + 180, CAMERA_HEIGHT-125, 54, 54, nextQuestGrayButtonTextureRegion, getVertexBufferObjectManager());
		prevQuestGrayButton = new Sprite(CAMERA_WIDTH/2 - 234, CAMERA_HEIGHT-125, 54, 54, prevQuestGrayButtonTextureRegion, getVertexBufferObjectManager());
		
		expBar = new Sprite(91, 13, 0, 30, experienceBarTextureRegion, getVertexBufferObjectManager());
		expBackground = new Sprite(70, 12, CAMERA_WIDTH - 140, 32, experienceBackgroundTextureRegion, getVertexBufferObjectManager());
		startExpBar = new Sprite(71, 13, 20, 30, startExperienceBarTextureRegion, getVertexBufferObjectManager());
		
		questName = new Text(20, 20, fontQuestTitel, "", 100, getVertexBufferObjectManager());
		questTask = new Text(20, 90, font, "", 200, getVertexBufferObjectManager());
		howToCloseQuest = new Text(20, CAMERA_HEIGHT - 230, fontQuestHowTo, "Gehe zurück zu der Person die dir den Quest gegeben hat um ihn abzuschließen", new TextOptions(AutoWrap.WORDS, CAMERA_WIDTH-30, HorizontalAlign.LEFT, 5), getVertexBufferObjectManager());
		levelTextPlayer = new Text(3, 50, fontQuestHowTo, "lvl 1", 5, getVertexBufferObjectManager());
		levelTextOpponent = new Text(CAMERA_WIDTH - 51, 50, fontQuestHowTo, "lvl 1", 5, getVertexBufferObjectManager()); //TODO gegner maximal level 9 .. sonst verschieben

		
		Controller.initInstance(this, expBar);
		controller = Controller.getInstance();
	}

	/**
	 * creates a scene and its children and adds them to the scene
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Scene onCreateScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());

		if(newGame) {
			
			int lastLevel = controller.getLastLevel();
			for(int i=1; i<=lastLevel; i++){			
				TMXTiledMap tmxTiledMap = controller.loadTMXMap(getAssets(), this.mEngine, getVertexBufferObjectManager(), i);
				OurScene scene = new OurScene(i, this, tmxTiledMap, controller.getSpawn());
				scene.generateAnimatedSprites(opponentTextureRegion, npcTextureRegion, getVertexBufferObjectManager(), i);
				controller.addSceneToManager(scene);
			}
			int questSceneIndex = lastLevel + 1;
			questScene = new QuestScene(questSceneIndex, controller,getAssets(), this.mEngine, getVertexBufferObjectManager());
			questScene.attachChild(questScene.getMap().getTMXLayers().get(0));
			
			camera.setBounds(0, 0, 30*32, 30*32);	// TODO: insert constants
			camera.setBoundsEnabled(true);
			
			/* set the scene's on touch listener to the activity itself */
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
			player = new Player(spawnX, spawnY, 24, 32, this.playerTextureRegion, this.getVertexBufferObjectManager(), 1);
			player.setZIndex(1);
			int column = spawnTile.getTileColumn();
			int row = spawnTile.getTileRow();
			Log.d("RPG", "COLUMN: "+column+" ROW: "+row);
			if(column==0) player.setCurrentTileIndex(5);
			else if(row==0) player.setCurrentTileIndex(1);
			else if(row==layer.getTileRows()-1) player.setCurrentTileIndex(9);
			else if(column==layer.getTileColumns()-1) player.setCurrentTileIndex(13);
			
			controller.getCurrentScene().attachChild(player);
			controller.setPlayer(player);
			
			/* let the camera chase the player */
			camera.setChaseEntity(player);
			camera.setCenterDirect(spawnX, spawnY);
			
			hud = new HUD();
			questHud = new HUD();
			
			camera.setHUD(hud);
			hud.attachChild(portrait);
			hud.attachChild(redBarPlayer);
			hud.attachChild(redBarEnemy);
			hud.attachChild(inventarButton);
			hud.attachChild(questButton);
			hud.attachChild(helpButton);
			
			hud.attachChild(levelTextPlayer);
	
			hud.attachChild(expBar);
			hud.attachChild(expBackground);
			hud.attachChild(startExpBar);
			
			hud.registerTouchArea(inventarButton);
			hud.registerTouchArea(questButton);
			hud.registerTouchArea(helpButton);
			
			
			questHud.attachChild(backToGameButtonBackground);
			questHud.attachChild(backToGameButton);
			questHud.attachChild(questTask);
			questHud.attachChild(questName);
			questHud.attachChild(howToCloseQuest);
			questHud.attachChild(prevQuestGrayButton);
			questHud.attachChild(nextQuestGrayButton);
			
			
			questHud.registerTouchArea(backToGameButton);
			questHud.registerTouchArea(nextQuestButton);
			questHud.registerTouchArea(prevQuestButton);
			
			interActionText = (ArrayList<String>) controller.getInteractionText(null).clone();
			startInteraction();
			
			return controller.getCurrentScene();
			
		} else {
			LoadSavedGame gameLoader = new LoadSavedGame(this);
			//load game
			gameLoader.loadGame(slot);
			
			
			
		}
		return null;
	}


	private void startQuestScene() {
		
		camera.setHUD(questHud);
		
		questHud.detachChild(nextQuestButton);
		questHud.detachChild(prevQuestButton);
		questHud.detachChild(howToCloseQuest);
		
		questHud.unregisterTouchArea(nextQuestButton);
		questHud.unregisterTouchArea(prevQuestButton);
		
		
		LevelActivity.this.mEngine.setScene(questScene);
		
		if(controller.getActiveQuests().size() != 0) {
			if(controller.getActiveQuests().get(questcount).getNpcID() != 0) {
				questHud.attachChild(howToCloseQuest);
			}

			if((questcount + 1) < controller.getActiveQuests().size()) {
				questHud.attachChild(nextQuestButton);
				questHud.registerTouchArea(nextQuestButton);
			}
			if(questcount > 0) {
				questHud.attachChild(prevQuestButton);
				questHud.registerTouchArea(prevQuestButton);
			}
			
			if(controller.getActiveQuests().size() != 0) {
				questName.setText(questScene.getQuestTitel(controller.getActiveQuests(), questcount)); 

				if(controller.getActiveQuests().get(questcount) instanceof TalkToQuest) {
					questTask.setText(questScene.getTalkToTask((controller.getNPCWithID(controller.getCurrentScene(), controller.getActiveQuests().get(questcount).getNpcID()).getName())));
					
				} else if(controller.getActiveQuests().get(questcount) instanceof GetItemQuest) {
					
					GetItemQuest quest = (GetItemQuest)controller.getActiveQuests().get(questcount);
					questTask.setText(questScene.getItemTask(quest.getItemName(), quest.getItemCount())); 
					
				} else if(controller.getActiveQuests().get(questcount) instanceof KillQuest) {
					questTask.setText(questScene.getKillTask(controller.getKillCount(questcount))); 
				}
			}
		} else {
			questTask.setText("Keine Aktiven Quests!");
			questName.setText("");
		}
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
	 * @param interActionText - the string to show in the dialog
	 */
	private void startInteraction() {
		isInteracting = true;
		
		hud.attachChild(textScroll);
		if(inventarButton.hasParent()) {
			hud.detachChild(inventarButton);
			hud.unregisterTouchArea(inventarButton);
		}
		if(questButton.hasParent()) {
			hud.detachChild(questButton);
			hud.unregisterTouchArea(questButton);
		}
		if(helpButton.hasParent()) {
			hud.detachChild(helpButton);
			hud.unregisterTouchArea(helpButton);
		}
		
		levelTextPlayer.setText("lvl " + player.getLevel());
		
		nextString();
	}
	
	private void nextString(){
		if(!interActionText.isEmpty()){
			if(inventarButton.hasParent()) {
				hud.detachChild(inventarButton);
				hud.unregisterTouchArea(inventarButton);
			}
			if(questButton.hasParent()) {
				hud.detachChild(questButton);
				hud.unregisterTouchArea(questButton);
			}
			
			if(text!=null) if(text.hasParent()) hud.detachChild(text);
			text = new TickerText(textScroll.getX()+40, textScroll.getY()+15, font, interActionText.remove(0), new TickerTextOptions(AutoWrap.WORDS, textScroll.getWidth()-80, HorizontalAlign.LEFT, 15), this.getVertexBufferObjectManager());
			text.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			
			hud.attachChild(text);
		}
		else stopInteraction();
	}
	
	/**
	 * detaches dialog window(consisting of a rect and a tickertext) fromt he hud
	 */
	private void stopInteraction() {
		if(!inventarButton.hasParent()) {
			hud.attachChild(inventarButton);
			hud.registerTouchArea(inventarButton);
		}
		if(!questButton.hasParent()) {
			hud.attachChild(questButton);
			hud.registerTouchArea(questButton);
		}
		if(!helpButton.hasParent()) {
			hud.attachChild(helpButton);
			hud.registerTouchArea(helpButton);
		}
		hud.detachChild(textScroll);
		hud.detachChild(text);
		isInteracting = false;
		if(controller.startMerchant()){
			Intent intent = new Intent(LevelActivity.this, InventarActivity.class);
			intent.putExtra("isMerchant", true);
			startActivity(intent);
		}
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
		int velocity;
		if(fleeing) velocity = 100;
		else velocity = 50;
		pathModifier = new LoopEntityModifier(new OurPathModifier(velocity, path, null, new IPathModifierListener() {

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
									final Item loot = controller.getLoot(((LootBag) child).getLoot());
									if(loot!=null){
											player.addItemToInventory(loot);
											controller.checkQuests(loot);
									}
//									if(player.getEquippedWeapon()!=null) Log.d("RPG", "Equipped Weapon: "+player.getEquippedWeapon().getName());
//									Armor[] equippedArmor = player.getArmor();
//									for(int j=0; j<equippedArmor.length; j++){
//										if(equippedArmor[j]!=null) Log.d("RPG", "EquippedArmorSlot "+j+": "+equippedArmor[j].getName());
//									}
									Log.d("RPG", player.getInventory().toString());
									if(loot!=null){
										runOnUiThread(new Runnable() {	
											@Override
											public void run() {
												Toast.makeText(getApplicationContext(), loot.getName()+" aufgehoben.", Toast.LENGTH_LONG).show();
											}
										});
										break;									
									}
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
				if(divRows<0) sprite.setCurrentTileIndex(9);
				else sprite.setCurrentTileIndex(1);
			} else{
				if(divColumns<0) sprite.setCurrentTileIndex(13);
				else sprite.setCurrentTileIndex(5);
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
				camera.setCenterDirect(spawnX, spawnY);
				int column = spawnTile.getTileColumn();
				int row = spawnTile.getTileRow();
				Log.d("RPG", "COLUMN: "+column+" ROW: "+row);
				if(column==0) player.setCurrentTileIndex(5);
				else if(row==0) player.setCurrentTileIndex(1);
				else if(row==layer.getTileRows()-1) player.setCurrentTileIndex(9);
				else if(column==layer.getTileColumns()-1) player.setCurrentTileIndex(13);
				
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

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickDetector clickDetector, int pointerID,
			float sceneX, float sceneY) {
		if(!controller.isMoving()){
			if(isInteracting){
				nextString();
			} else{
				OurScene scene = controller.getCurrentScene();
				final TMXLayer layer = (TMXLayer) scene.getChildByIndex(0);
				final TMXTile startTile = layer.getTMXTileAt(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
				final TMXTile destinationTile = layer.getTMXTileAt(sceneX, sceneY);
				switch(controller.doAction(startTile, destinationTile, scene.getMap(), scene)){
					case 1:					
						Path path = controller.getPath(startTile, destinationTile, scene.getMap());
						if(path!=null){
							if(portraitEnemy.hasParent()) hud.detachChild(portraitEnemy);
							if(redBarEnemy.hasParent()) hud.detachChild(redBarEnemy);
							if(levelTextOpponent.hasParent()) hud.detachChild(levelTextOpponent);
							startPath(path, destinationTile);
						}
						else Log.d("RPG", "path=null");
						break;
					case 2:
						NPC npc = (NPC) scene.getChildByMatcher(new IEntityMatcher() {							
							@Override
							public boolean matches(IEntity entity) {
								if(layer.getTMXTileAt(entity.getX(), entity.getY()) == destinationTile)	return true;
								else return false;
							}
						});
						controller.checkQuests(npc);
						interActionText = (ArrayList<String>) controller.getInteractionText(npc).clone();
						turnToTile(npc, startTile, destinationTile, scene.getMap());
						turnToTile(player, destinationTile, startTile, scene.getMap());
						startInteraction();
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
						if(!redBarEnemy.hasParent()){
							hud.attachChild(redBarEnemy);
							hud.sortChildren();
						}
						if(!levelTextOpponent.hasParent()){
							hud.attachChild(levelTextOpponent);
							hud.sortChildren();
						}
						levelTextOpponent.setText("lvl " + opponent.getLevel());
						
						turnToTile(player, destinationTile, startTile, scene.getMap());
						turnToTile(opponent, startTile, destinationTile, scene.getMap());
						switch(controller.fight(player, opponent, redBarPlayer, redBarEnemy)){
							case 1:
								fightWon(opponent, destinationTile);
								controller.checkQuests("enemy");
								hud.detachChild(portraitEnemy);
								hud.detachChild(redBarEnemy);
								hud.detachChild(levelTextOpponent);
								break;
							case 2:
								flee();
								break;
						}
						break;
				}	
			}
		} else	if(!fleeing) stopPath();
	}

	private void flee() {
		float[] coords;
		if(controller.getLevel()==1) coords = controller.getCurrentScene().getSpawn("SPAWN");
		else coords = controller.getCurrentScene().getSpawn("LEVEL"+(controller.getLevel()-1));
		TMXLayer layer = controller.getTMXLayer();
		TMXTile startTile = layer.getTMXTileAt(player.getX(), player.getY());
		TMXTile endTile = layer.getTMXTileAt(coords[0], coords[1]);
		Path path = controller.getPath(startTile, endTile, controller.getCurrentScene().getMap());
		if(path!=null) startPath(path, endTile);
		else Log.d("RPG", "path=null");
		hud.detachChild(portraitEnemy);
		hud.detachChild(levelTextOpponent);
		hud.detachChild(redBarEnemy);
		fleeing = true;
	}
	

	private void fightWon(Opponent opponent, TMXTile destinationTile) {
		OurScene scene = controller.getCurrentScene();
		scene.detachChild(opponent);
		Sprite beutel = new LootBag(destinationTile.getTileX(), destinationTile.getTileY(), beutelTextureRegion, getVertexBufferObjectManager(), opponent);
		scene.attachChild(beutel);
		scene.sortChildren();
		controller.addExp(100 * opponent.getLevel());
		levelTextPlayer.setText("lvl " + player.getLevel());
	}

	@Override
	protected synchronized void onResume() {
		super.onResume();
		inventarStarted = false;
		helpStarted = false;
		if(redBarPlayer!=null){
			redBarPlayer.setWidth((float)(100-player.getHealth())/3);
			redBarPlayer.setX(44-redBarPlayer.getWidth());
		}
		
	}
	
	@Override
	public void onBackPressed() {
		this.showDialog(1);
	}
	
	@Override
	protected Dialog onCreateDialog(final int id) {
		switch(id) {
			case 1:
				return new AlertDialog.Builder(this).setMessage("Zurück zum Hauptmenü?")
						.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						})
//						.setNeutralButton("Ja & Speichern", new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								WriteSaveFile writer = new WriteSaveFile(LevelActivity.this);
//								OurScene[] scene = new OurScene[controller.getLastLevel()];
//								for(int i = 1; i <= controller.getLastLevel(); i++) {
//									scene[i - 1] = controller.getScene(i);
//								}
//								
//								writer.createFile(1, controller.getLastLevel(), questcount, scene, player, controller);
//								finish();
//							}
//						})
						.setNegativeButton("Nein", null)
						.create();
			default:
				return null;
		}
	}

}
