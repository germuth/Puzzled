package ca.germuth.puzzled.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {
	
	public static final int DIGITALL_FONT = 0;
	public static final int AGENT_ORANGE_FONT = 1;
	
	private static String[] fontPath = {
	    "fonts/Digitall.ttf",
	    "fonts/AgentOrange.ttf"
	};
	private static boolean fontsLoaded = false;
	private static Typeface[] fonts = new Typeface[fontPath.length];

	/**
	 * Returns a loaded custom font based on it's identifier. 
	 * 
	 * @param context - the current context
	 * @param fontIdentifier = the identifier of the requested font
	 * 
	 * @return Typeface object of the requested font.
	 */
	public static Typeface getTypeface(Context context, int fontIdentifier) {
	    if (!fontsLoaded) {
	        loadFonts(context);
	    }
	    return fonts[fontIdentifier];
	}

	
	/**
	 * Loads all the fonts for the entire application into memory
	 * @param context
	 */
	public static void loadFonts(Context context) {
	    for (int i = 0; i < fonts.length; i++) {
	        fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
	    }
	    fontsLoaded = true;
	}
}
