package ca.germuth.puzzled;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 * Displays Puzzled splash screen and waits until logging in.
 * Once the app actually has things to load, it will load here, rather
 * than just make the user wait. Current list of things to do here:
 * 
 * 1 - Load Fonts
 * 2 - Load openGL shaders and compile them
 * 3 - Create Database if not created
 * 4 - Get reference to database application wide?
 * 5 - Generically get all subclasses of puzzle, store in application
 * 6 - Load up music files and music players
 * 
 * @author Germuth
 *
 */
public class SplashScreenActivity extends PuzzledActivity{
	private final int SPLASH_DISPLAY_LENGHT = 1000;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
