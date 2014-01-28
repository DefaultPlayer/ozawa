/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder;

/**
 * Created by dkerr on 12/20/13.
 */
import java.util.ArrayList;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.ShowcaseView.ConfigOptions;
import com.ozawa.hextcgdeckbuilder.UI.TutorialEventListener;
import com.ozawa.hextcgdeckbuilder.enums.TutorialType;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FullImageActivity extends Activity implements GestureOverlayView.OnGesturePerformedListener {

	private GestureLibrary gesLibrary;
	private int position;
	private ImageView imageView;
	private int cardCount;
	private boolean isMaster;
	
	// Tutorial
	private static final String PREFS_NAME = "FirstLaunchPref";
	private SharedPreferences mPreferences;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();
        
		gesLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gesLibrary.load()) {
			this.finish();
		}

        // Selected image id
        position = i.getExtras().getInt("id");
        isMaster = i.getExtras().getBoolean("isMaster");

        imageView = (ImageView) findViewById(R.id.full_image_view);
        
        setImage();
        
        imageView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				finish();
			}
		});
        
		GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.fullImageGestureOverlayView);
		gestureOverlayView.addOnGesturePerformedListener(this);	
		gestureOverlayView.setGestureVisible(false);
		
		// Tutorial
		mPreferences = getSharedPreferences(PREFS_NAME, 0);
		boolean firstTime = mPreferences.getBoolean("firstTime", true);
		if (firstTime) { 
			showTutorial();
		}
    }
    
    /**
	 * Show the app's tutorial
	 */
	@SuppressWarnings("deprecation")
	public void showTutorial() {
		ConfigOptions co = new ShowcaseView.ConfigOptions();
		co.shotType = ShowcaseView.TYPE_ONE_SHOT;
		co.centerText = true;
		co.hideOnClickOutside = true;
		ShowcaseView showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(this) / 2, (int)(HexUtil.getScreenHeight(this) / 15), this, "Fullscreen View", 
				"Check out the hi-res on these bad boys. Here is a close up of the card and all it's interesting data.", co);     
		showcaseView.setShowcase(ShowcaseView.NONE);
		showcaseView.setOnShowcaseEventListener(new TutorialEventListener(this,co, TutorialType.FULLSCREEN));        
		showcaseView.show();
	}
    
    @Override
    protected void onPause(){
    	super.onPause();
    	finish();
    }

	@Override
	public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
		ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
		if (predictions.size() > 0) {
			for (Prediction prediction : predictions) {
				if (prediction.score > 1.0) {
					if (prediction.name.equalsIgnoreCase("swipe right")) {
						if(position > 0){
							position --;
							setImage();
						} else{
							Toast.makeText(this, "No more cards, Jumping to the end", Toast.LENGTH_SHORT).show();
							position = cardCount - 1;
							setImage();
						}
					} else if (prediction.name.equalsIgnoreCase("swipe left")) {
						if(position < cardCount - 1){
							position ++;
							setImage();
						} else{
							position = 0;
							Toast.makeText(this, "No more cards, Jumping to the start", Toast.LENGTH_SHORT).show();
							setImage();
						}						
					}else if(prediction.name.equalsIgnoreCase("anti clockwise") || prediction.name.equalsIgnoreCase("clockwise")){
                    	finish();
                    }
				}
			}
		}
	}
	
	private void setImage(){
		if(isMaster){
        	imageView.setImageBitmap(MasterDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
        	cardCount = MasterDeckFragment.cardViewer.getFilteredCardList().size();
        } else{
        	imageView.setImageBitmap(CustomDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
        	cardCount = CustomDeckFragment.cardViewer.getFilteredCardList().size();
        }
	}
}
