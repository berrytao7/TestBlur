package com.example.blur.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class ScreenshotUtil {
	public static final float SCALE = 0.125f;   	
	
	public static Bitmap takeScreenshot(Context context) { 
	    	WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
	        Display mDisplay = mWindowManager.getDefaultDisplay();  
	        DisplayMetrics mDisplayMetrics = new DisplayMetrics();  
	        mDisplay.getRealMetrics(mDisplayMetrics);  
	        Matrix mDisplayMatrix = new Matrix();  
	        float[] dims = { mDisplayMetrics.widthPixels,  
	                mDisplayMetrics.heightPixels };  
	  
	        int value = mDisplay.getRotation();  
	        Object[] args = { "ro.sf.hwrotation", "0" };
	        String hwRotation = (String)invokeUtil.invokeStaticMethod("android.os.SystemProperties", "get", args); 
	        if (hwRotation.equals("270") || hwRotation.equals("90")) {  
	            value = (value + 3) % 4;  
	        }  
	        float degrees = getDegreesForRotation(value);  
	  
	        boolean requiresRotation = (degrees > 0);  
	        if (requiresRotation) {  
	            // Get the dimensions of the device in its native orientation  
	            mDisplayMatrix.reset();  
	            mDisplayMatrix.preRotate(-degrees);  
	            mDisplayMatrix.mapPoints(dims);  
	  
	            dims[0] = Math.abs(dims[0]);  
	            dims[1] = Math.abs(dims[1]);  
	        }  
	  
	        Object[] args2 = {(int)dims[0], (int) dims[1]};//android.view.SurfaceControl
	        Bitmap mScreenBitmap = null;
	        if (android.os.Build.VERSION.SDK_INT <18 ) {
	        	mScreenBitmap = (Bitmap)invokeUtil.invokeStaticMethod("android.view.Surface", "screenshot", args2); 	        	
			}else {
				mScreenBitmap = (Bitmap)invokeUtil.invokeStaticMethod("android.view.SurfaceControl", "screenshot", args2);  
			}
	        if (mScreenBitmap!=null) {
	        	mScreenBitmap =createScaleBitmap(mScreenBitmap, SCALE);
			}	
	     // If we couldn't take the screenshot, notify the user  
	        if (mScreenBitmap == null) {  
	            return null;  
	        } 
	        if (requiresRotation) {  
	            // Rotate the screenshot to the current orientation  
	            Bitmap ss = Bitmap.createBitmap((int)(mDisplayMetrics.widthPixels*SCALE),  
	                    (int)(mDisplayMetrics.heightPixels*SCALE), Bitmap.Config.ARGB_8888);  
	            Canvas c = new Canvas(ss);  
	            c.translate(ss.getWidth() / 2, ss.getHeight() / 2);  
	            c.rotate(degrees);  
	            c.translate(-dims[0]*SCALE / 2, -dims[1]*SCALE / 2);
	            c.drawBitmap(mScreenBitmap, 0, 0, null);  
	            c.setBitmap(null);  
	            mScreenBitmap = ss;  
	        }  
	  
	         
	  
	        // Optimizations  
	        mScreenBitmap.setHasAlpha(false);  
	        mScreenBitmap.prepareToDraw(); 
	        return mScreenBitmap;
	    } 
			
	   public static Bitmap createScaleBitmap( Bitmap source , float scale ){
			if (scale <= 0 || scale == 1) {
				return source;
			}

			return Bitmap.createScaledBitmap(source,
					(int) (source.getWidth() * scale),
					(int) (source.getHeight() * scale), false);
		}
	   
	   private static float getDegreesForRotation(int value) {  
	        switch (value) {  
	        case Surface.ROTATION_90:  
	            return 360f - 90f;  
	        case Surface.ROTATION_180:  
	            return 360f - 180f;  
	        case Surface.ROTATION_270:  
	            return 360f - 270f;  
	        }  
	        return 0f;  
	    }  
	   
	
}
