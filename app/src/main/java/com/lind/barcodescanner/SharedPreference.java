package com.lind.barcodescanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.Set;

public class SharedPreference implements  SharedPreferences{
//	public static final String CLIPBOARDMANAGER = "SHAREDPREFERENCE_CLIPBOARDMANAGER";
	public static final String KEY_UNIQUE_ID="unique_id";
	public static final String KEY_EMAIL="email";
	public static final String KEY_NAME="name";
	public static final String KEY_LOG_IN="isUserLoggedIn";
	public static final String KEY_LOG_OUT="isUserLoggedIOut";
	public  static final    String PREFS_NAME = "List_ProtermLab";
	SharedPreferences SharedPreferences=null; // //transient  SharedPreferences SharedPreferences=null; //
	Gson gson=null;
	//org.jivesoftware.smack.packet.ExtensionElement
	public SharedPreference(Context ctx) {
		if(SharedPreferences == null) {
			SharedPreferences = ctx.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			GsonBuilder gsonBuilder = new GsonBuilder();
				gson = gsonBuilder.create();
		}
	}
	//This four methods are used for maintaining favorites.
	public  synchronized void set_unique_id( String KEY_UNIQUE_ID_) {
		Editor editor =SharedPreferences.edit();
		editor.putString(KEY_UNIQUE_ID, KEY_UNIQUE_ID_);
		editor.commit();
	}

	//This four methods are used for maintaining favorites.
	public  synchronized void set_email( String KEY_EMAIL_) {
		Editor editor =SharedPreferences.edit();
		editor.putString(KEY_EMAIL, KEY_EMAIL_);
		editor.commit();
	}
	//This four methods are used for maintaining favorites.
	public  synchronized void set_name( String KEY_NAME_) {
		Editor editor =SharedPreferences.edit();
		editor.putString(KEY_NAME, KEY_NAME_);
		editor.commit();
	}

	public synchronized  void remove_user(){
			// here you get your prefrences by either of two methods
			Editor editor = SharedPreferences.edit();
			editor.clear();
			editor.commit();
	}
	public  Boolean get_isUserLoggedIn() {
		if (SharedPreferences.contains(KEY_LOG_IN)) {
			return SharedPreferences.getBoolean(KEY_LOG_IN, false);
		} else{
			return false;
		}
	}

	public  String get_name() {
		if (SharedPreferences.contains(KEY_NAME)) {
			return SharedPreferences.getString(KEY_NAME, "");
		} else{
			return "";
		}
	}
	public  String get_unique_id() {
		if (SharedPreferences.contains(KEY_UNIQUE_ID)) {
			return SharedPreferences.getString(KEY_UNIQUE_ID, "");
		} else{
			return "";
		}
	}
	public  String get_email() {
		if (SharedPreferences.contains(KEY_EMAIL)) {
			return SharedPreferences.getString(KEY_EMAIL, "");
		} else{
			return "";
		}
	}

	public  boolean  set_isUserLoggedIn( boolean isUserLoggedIn_) {
		Editor editor =SharedPreferences.edit();
		editor.putBoolean(KEY_LOG_IN, isUserLoggedIn_);
		editor.commit();
		return true;
	}



	@Override
	public Map<String, ?> getAll() {return null;}

	@Nullable
	@Override
	public String getString(String key, String defValue) {return null;}

	@Nullable
	@Override
	public Set<String> getStringSet(String key, Set<String> defValues) {return null;}

	@Override
	public int getInt(String key, int defValue) {return 0;}

	@Override
	public long getLong(String key, long defValue) {return 0;}

	@Override
	public float getFloat(String key, float defValue) {return 0;}

	@Override
	public boolean getBoolean(String key, boolean defValue) {return false;}

	@Override
	public boolean contains(String key) {return false;}

	@Override
	public Editor edit() {return null;}

	@Override
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
}