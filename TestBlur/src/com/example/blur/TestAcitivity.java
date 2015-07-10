package com.example.blur;

import com.example.blur.util.AnimUtil;
import com.example.blur.util.BlurManager;
import com.example.blur.util.DeviceProfile;
import com.example.blur.util.ScreenshotUtil;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TestAcitivity extends Activity {
	boolean useSystemScreenShot;//是否为系统应用
	boolean sBarTranslucent = false;//状态栏是否透明
	int statusHeight = 0;
	int navigationHeight = 0;
	private FrameLayout frameLayout;
	private ImageView imageView,blackImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		useSystemScreenShot = isSystemApplication(getApplicationContext());
		System.out.println("================berrytao ========isSystemApp:"+useSystemScreenShot);
		frameLayout = (FrameLayout) findViewById(R.id.main);
		imageView = (ImageView) findViewById(R.id.blur_bg);
		blackImageView = (ImageView) findViewById(R.id.blur_black_bg);
		setBlurBackgroud();
		Rect fameRect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(fameRect);
		statusHeight = fameRect.top;//状态栏
		navigationHeight = fameRect.bottom;//导航栏
	}
	
	public void setBlurBackgroud() {		
		PropertyValuesHolder alpha3 = PropertyValuesHolder.ofFloat("alpha", 0,
				0.6f);
		final ObjectAnimator oa3 = AnimUtil.ofPropertyValuesHolder(
				blackImageView, alpha3);
		oa3.setDuration(250);
		oa3.start();
		new BitmapWorkerTask().execute();
		if(useSystemScreenShot) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
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
			Display mDisplay = TestAcitivity.this.getWindowManager()
					.getDefaultDisplay();
			mDisplay.getRealMetrics(mDisplayMetrics);
			int statusH = DeviceProfile.getStatusBarHeight(TestAcitivity.this);
			int navigaH = DeviceProfile.getNavigationHeigth(TestAcitivity.this);
			System.out.println("=========w:"+mDisplayMetrics.widthPixels+",h:"+mDisplayMetrics.heightPixels+",status:"+statusH+",navi:"+navigaH);
			if(useSystemScreenShot) {			
				screenshotBitmap = ScreenshotUtil.takeScreenshot(getApplicationContext());
				//screenshotBitmap = Utilities.takeScreenshot2(getApplicationContext());
				}
			float SCALE = ScreenshotUtil.SCALE;
			if (screenshotBitmap!=null && !sBarTranslucent) {
				screenshotBitmap = Bitmap.createBitmap(screenshotBitmap, 0,
									(int) (statusH * SCALE),
									(int) (mDisplayMetrics.widthPixels * SCALE),
									(int) ((mDisplayMetrics.heightPixels
											-statusH-navigaH) * SCALE));
			}
			
			if (screenshotBitmap!=null) {
				BlurManager stackBlurManager = new BlurManager(screenshotBitmap);
				Bitmap bmp = stackBlurManager.processNatively((int)getResources().getDimension(R.dimen.blur_level));
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
	
	
	public void setBlurBg(Bitmap blur) {	
		if (blur!=null) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(blur);
		}else {
			imageView.setVisibility(View.GONE);
			frameLayout.setBackgroundResource(R.color.bg_color);
		}
		
	}

}
