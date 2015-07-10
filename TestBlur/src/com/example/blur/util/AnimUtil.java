package com.example.blur.util;

import java.util.WeakHashMap;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

public class AnimUtil {
	
	  static WeakHashMap<Animator, Object> sAnimators = new WeakHashMap<Animator, Object>();
	    static Animator.AnimatorListener sEndAnimListener = new Animator.AnimatorListener() {
	        public void onAnimationStart(Animator animation) {
	            sAnimators.put(animation, null);
	        }

	        public void onAnimationRepeat(Animator animation) {
	        }

	        public void onAnimationEnd(Animator animation) {
	            sAnimators.remove(animation);
	        }

	        public void onAnimationCancel(Animator animation) {
	            sAnimators.remove(animation);
	        }
	    };

	    public static void cancelOnDestroyActivity(Animator a) {
	        a.addListener(sEndAnimListener);
	    }
	    
	public static ObjectAnimator ofPropertyValuesHolder(View target,
            PropertyValuesHolder... values) {
        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(target);
        anim.setValues(values);
        cancelOnDestroyActivity(anim);
        new FirstFrameAnimHelper(anim, target);
        return anim;
    }

}
