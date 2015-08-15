package com.danielme.tipsandroid.imageslider;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 
 * @author danielme.com
 *
 */
public class MainActivity extends Activity {
	private ImageSwitcher imageSwitcher;

	private int[] gallery = { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d, R.drawable.e, R.drawable.f };

	private int position;

	private static final Integer DURATION = 2500;

	private Timer timer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		imageSwitcher.setFactory(new ViewFactory() {

			public View makeView() {
				return new ImageView(MainActivity.this);
			}
		});

		// Set animations
		// http://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		imageSwitcher.setInAnimation(fadeIn);
		imageSwitcher.setOutAnimation(fadeOut);
	}

	// ////////////////////BUTTONS
	/**
	 * starts or restarts the slider
	 * 
	 * @param button
	 */
	public void start(View button) {
		if (timer != null) {
			timer.cancel();
		}
		position = 0;
		startSlider();
	}

	public void stop(View button) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void startSlider() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				// avoid exception:
				// "Only the original thread that created a view hierarchy can touch its views"
				runOnUiThread(new Runnable() {
					public void run() {
						imageSwitcher.setImageResource(gallery[position]);
						position++;
						if (position == gallery.length) {
							position = 0;
						}
					}
				});
			}

		}, 0, DURATION);
	}

	// Stops the slider when the Activity is going into the background
	@Override
	protected void onPause() {
		super.onPause();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (timer != null) {
			startSlider();
		}

	}

}