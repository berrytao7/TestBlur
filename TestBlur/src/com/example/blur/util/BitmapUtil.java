package com.example.blur.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;

public class BitmapUtil {
	
	 public static Bitmap drawable2bitmap( Drawable dw )
	    {

		Bitmap bg = Bitmap.createBitmap( dw.getIntrinsicWidth( ) , dw.getIntrinsicHeight( ) , Config.ARGB_8888 );

		Canvas canvas = new Canvas( bg );

		dw.setBounds( 0 , 0 , dw.getIntrinsicWidth( ) , dw.getIntrinsicHeight( ) );
		dw.draw( canvas );

		canvas.setBitmap( null );

		return bg;
	    }
	 
	 
	 public static Bitmap drawable2bitmap( Drawable dw, float scale ) {
	    	int w = (int)(dw.getIntrinsicWidth()*scale);
	    	int h = (int)(dw.getIntrinsicHeight()*scale);
			Bitmap bg = Bitmap.createBitmap(w, h, Config.ARGB_8888);

			Canvas canvas = new Canvas(bg);
			canvas.scale(scale, scale);
			dw.setBounds(0, 0, dw.getIntrinsicWidth(), dw.getIntrinsicHeight());
			dw.draw(canvas);
			canvas.setBitmap(null);

			return bg;
		}

}
