package com.example.projectrpg;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
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
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LevelActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener{

	// constants
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// fields
	private RepeatingSpriteBackground mGrassBackground;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	private AnimatedSprite player;
	
	private ITexture obstacleTexture;
	private ITextureRegion obstacleTextureRegion;
	private Sprite obstacle;


	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "player.png", 0, 0, 3, 4);
		this.mGrassBackground = new RepeatingSpriteBackground(CAMERA_WIDTH, CAMERA_HEIGHT, this.getTextureManager(), AssetBitmapTextureAtlasSource.create(this.getAssets(), "gfx/background_grass.png"), this.getVertexBufferObjectManager());
		this.mBitmapTextureAtlas.load();
		try {
			this.obstacleTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/grauerKlotz.png");
				}
			});

			this.obstacleTexture.load();
			this.obstacleTextureRegion = TextureRegionFactory.extractFromTexture(this.obstacleTexture);
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		final Scene scene = new Scene();
		scene.setBackground(this.mGrassBackground);
		
		scene.setOnSceneTouchListener(this);

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getHeight()) / 2;

		/* Create the sprite and add it to the scene. */
		player = new AnimatedSprite(centerX, centerY, 48, 64, this.mPlayerTextureRegion, this.getVertexBufferObjectManager());		

		obstacle = new Sprite(50, 50, obstacleTextureRegion, this.getVertexBufferObjectManager());
		
		scene.attachChild(player);
		scene.attachChild(obstacle);
		
		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		Path path = Algorithm.calculatePath(player.getX(), player.getY(), pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), pScene);
		startPath(pScene, path);
		return false;
	}

	private void startPath(Scene scene, final Path path) {
		player.registerEntityModifier(new LoopEntityModifier(new OurPathModifier(50, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				float startX = path.getCoordinatesX()[0];
				float startY = path.getCoordinatesY()[0];
				float endX = path.getCoordinatesX()[path.getSize()-1];
				float endY = path.getCoordinatesY()[path.getSize()-1];
				float divX = Math.abs(startX - endX);
				float divY = Math.abs(startY - endY);
				
				if(divX > divY){
					if(startX > endX){
						player.animate(new long[]{200, 200, 200}, 9, 11, true);
					}else {
						player.animate(new long[]{200, 200, 200}, 3, 5, true);
					}
				}else {
					if(startY > endY){
						player.animate(new long[]{200, 200, 200}, 0, 2, true);
					}else {
						player.animate(new long[]{200, 200, 200}, 6, 8, true);
					}
				}
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				player.stopAnimation();
			}
		}), 0));
	}

}
