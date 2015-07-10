package com.example.blur.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

public class invokeUtil {
	private static final boolean DEBUG = true;
	private static final String TAG = "invokeUtil";
	
	  public static Object invokeMethod(Object owner, String methodName, Object[] args) {
			Class<?> ownerClass = owner.getClass();
			@SuppressWarnings("rawtypes")
			Class[] methodArgs = new Class[args.length];
			int i = 0;

			for (Object obj : args) {
				if (obj instanceof Integer) {
					methodArgs[i] = int.class;
				} else {
					methodArgs[i] = args[i].getClass();
				}
				i++;
			}

			Method method;
			try {
				method = ownerClass.getMethod(methodName, methodArgs);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Log.w(TAG, "no such method:" + methodName);
				return null;
			}
			
			 Log.v(TAG, "getMethod=" + method + " ownerClass=" + ownerClass);

			try {
				return method.invoke(owner, args);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	  
	  
	  @SuppressWarnings("rawtypes")
	    public static Object invokeStaticMethod(String className, String methodName, Object[] args) {
	    	Class c = null;
			try {
				c = Class.forName(className);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Class[] methodArgs = new Class[args.length];
			int i = 0;

			for (Object obj : args) {
				if (obj instanceof Integer) {
					methodArgs[i] = int.class;
				} else {
					methodArgs[i] = args[i].getClass();
				}
				i++;
			}

			Method method;
			try {
				method = c.getMethod(methodName, methodArgs);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Log.w(TAG, "no such method:" + methodName);
				return null;
			}
			
			 Log.v(TAG, "getMethod=" + method + " Class=" + c);

			try {
				return method.invoke(c, args);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	    

}
