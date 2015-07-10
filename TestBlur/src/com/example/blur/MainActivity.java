package com.example.blur;

import java.lang.ref.WeakReference;

import com.example.blur.util.AnimUtil;
import com.example.blur.util.BlurManager;
import com.example.blur.util.DeviceProfile;
import com.example.blur.util.ScreenshotUtil;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static String TAG = "MainActivity";
	private FrameLayout main;
	private ImageView imageView;
	private ImageView imageViewbg;
	boolean useSystemScreenShot;//是否为系统应用
	boolean sBarTranslucent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		useSystemScreenShot = isSystemApplication(MainActivity.this.getApplicationContext());
		
		main = (FrameLayout) findViewById(R.id.main);
		imageView = (ImageView) findViewById(R.id.blur_bg);
		imageViewbg = (ImageView) findViewById(R.id.blur_black_bg);
		setBlurBackgroud();

		showBackground();
	}
	
	
	void showBackground() {
		WeakReference<Drawable> wallPaper = new WeakReference<Drawable>(WallpaperManager.getInstance(this).getFastDrawable());	
		if(wallPaper.get() != null){
			main.setBackground(wallPaper.get());
		}else{
			main.setBackgroundResource(R.drawable.bg);
		}	
				}
	
	private boolean isSystemApp = false;
    private boolean isBarTranslucent = false;
    int statusH =0;
    int navigaH = 0; 
    private void setBlurBackgroud(){
        isSystemApp = isSystemApplication(getApplicationContext());
        PropertyValuesHolder alpha3 = PropertyValuesHolder.ofFloat("alpha", 0,
                0.4f);
        final ObjectAnimator oa3 = AnimUtil.ofPropertyValuesHolder(
          imageViewbg, alpha3);
        oa3.setDuration(250);
        oa3.start();
        new BitmapWorkerTask().execute();
        if (isSystemApp) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
	
	 class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
       Bitmap screenshotBitmap;
       public BitmapWorkerTask() { 
       }  
       
       @Override
       protected Bitmap doInBackground(Void... params) {
           DisplayMetrics mDisplayMetrics = new DisplayMetrics();
           Display mDisplay = MainActivity.this.getWindowManager()
                   .getDefaultDisplay();
           mDisplay.getRealMetrics(mDisplayMetrics);
           statusH= DeviceProfile.getStatusBarHeight(getApplicationContext());
           navigaH = DeviceProfile.getNavigationHeigth(getApplicationContext());
           if(isSystemApp) { //有系统权限，直接调用系统截屏             
               screenshotBitmap = ScreenshotUtil.takeScreenshot(getApplicationContext());
               if(screenshotBitmap == null){
                   WeakReference<Drawable> wallPaper = new WeakReference<Drawable>(WallpaperManager.getInstance(MainActivity.this).getFastDrawable());
                   Drawable wallDrawable = wallPaper.get();
                   screenshotBitmap = drawableToBitmap(wallDrawable);      
               }
           }else {//无系统权限,获取壁纸,同时将之前的截图与壁纸重叠组合成一张图片
               WeakReference<Drawable> wallPaper = new WeakReference<Drawable>(WallpaperManager.getInstance(MainActivity.this).getFastDrawable());
               Drawable wallDrawable = wallPaper.get();
               screenshotBitmap = drawableToBitmap(wallDrawable);      
           }           
           if (screenshotBitmap!=null) {
               float SCALE = ScreenshotUtil.SCALE;
               if (isBarTranslucent) {
                   screenshotBitmap =  Bitmap.createBitmap(screenshotBitmap, 0,
                           (int) (statusH * SCALE),
                           (int) (mDisplayMetrics.widthPixels * SCALE),
                           (int) ((mDisplayMetrics.heightPixels-navigaH) * SCALE));
               }else {
                   screenshotBitmap =  Bitmap.createBitmap(screenshotBitmap, 0,
                           (int) (statusH * SCALE),
                           (int) (mDisplayMetrics.widthPixels * SCALE),
                           (int) ((mDisplayMetrics.heightPixels
                                   -statusH-navigaH) * SCALE));
               }
             //模糊处理
               BlurManager stackBlurManager = new BlurManager(screenshotBitmap);
               Bitmap bmp = stackBlurManager.processNatively(12);
               return bmp; 
           }
           return screenshotBitmap;            
       }

       @Override
       protected void onPostExecute(Bitmap result) {
           setBlurBg(result);
       };

   };
	
	public static boolean isSystemApplication(Context context) {
	  PackageManager packageManager = context.getPackageManager();
      String packageName = context.getPackageName();
      if (packageManager == null || packageName == null || packageName.length() == 0) {
          return false;
      }

      try {
          ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
          return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
      } catch (NameNotFoundException e) {
          e.printStackTrace();
      }
      return false;
    }
	private Bitmap drawableToBitmap(Drawable drawable) { 
      Bitmap bitmap = Bitmap.createBitmap(
              drawable.getIntrinsicWidth(),
              drawable.getIntrinsicHeight(),
              drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
      drawable.draw(canvas);
      return bitmap;
  }
	
	public void setBlurBg(Bitmap blur) {	
		imageView.setImageBitmap(blur);
	}
}
