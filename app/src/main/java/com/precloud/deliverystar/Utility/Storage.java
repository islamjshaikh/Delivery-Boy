package com.precloud.deliverystar.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Storage {

    private static final String TAG = "Storage";
    public static Storage instance;
    static SharedPreferences pref;
    public static final String DocumentId = "DocumentId";
    public static final String REferenceNo = "REferenceNo";
    public static final String Name = "Name";
    public static final String Email_id = "Email_id";
    /**
     * Constructor to create shared preferences object.
     *
     */
    public Storage(Context context) {
        pref = context.getSharedPreferences("precloud", Context.MODE_PRIVATE);
    }

    /*
     * single instance of storage
     */
    // public static Storage getinstance() {
    // if (instance == null) {
    // instance = new Storage(Dashboard.getActivity());
    // return instance;
    // } else
    // return instance;
    // }

    public static Storage getInstance() {
        if (instance == null)
            instance = new Storage(DeliveryStarApplication.getContextInstance());
        return instance;
    }

    /**
     * Method Set String
     *
     */
    public void setString(String key, String value) {
        Editor prefEdit = pref.edit();
        prefEdit.putString(key, value);
        prefEdit.apply();
    }

    /**
     * Method Get String
     *
     */
    public String getString(String key) {
        return pref.getString(key, null);
    }

    /**
     * Method Set Integer
     *
     */
    public void setInteger(String key, int value) {
        Editor prefEdit = pref.edit();
        prefEdit.putInt(key, value);
        prefEdit.apply();
    }

    /**
     * Method Get Integer
     *
     */
    public int getInteger(String key) {
        return pref.getInt(key, 0);
    }

    /**
     * Method Set Long
     *
     */
    public void setLong(String key, long value) {
        Editor prefEdit = pref.edit();
        prefEdit.putLong(key, value);
        prefEdit.apply();
    }

    /**
     * Method Get Long
     *
     */
    public long getLong(String key) {
        return pref.getLong(key, 0);
    }

    /**
     * Method Set Boolean
     *
     */
    public void setBoolean(String key, Boolean value) {
        Editor prefEdit = pref.edit();
        prefEdit.putBoolean(key, value);
        prefEdit.apply();
    }

    /**
     * Method Get Boolean
     *
     */
    public Boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    /**
     * Method to remove Storage
     *
     */
    public void removeStroage() {
        Editor prefEdit = pref.edit();
        prefEdit.clear();
        prefEdit.apply();
    }

    /**
     * Method Set Collection
     *
     */
    public void setCollection(String key, String set) {
        Editor prefEdit = pref.edit();
        prefEdit.putString(key, set);
        prefEdit.apply();
    }

    /**
     * Method Get Collection
     *
     */
    public String getCollection(String key) {

        return pref.getString(key, null);
    }

    public void setTabList(String key, Set<String> set) {
        Editor prefEdit = pref.edit();
        prefEdit.putStringSet("Tab", set);
        prefEdit.apply();
    }

    public String getTabList(String key) {

        return pref.getString(key, null);
    }

    public void setId(int id) {
        Editor prefEdit = pref.edit();
        prefEdit.putInt("NotificationId", id);
        prefEdit.apply();
    }

    public int getId(String key) {
        return pref.getInt(key, 0);
    }

    /**
     * Method to remove Key
     *
     */
    public void removeKey(String key) {
        Editor prefEdit = pref.edit();
        prefEdit.remove(key);
        prefEdit.apply();
    }

    public SharedPreferences getPref() {
        return null;
    }

    public boolean isContainKey(String key) {
        Map<String, ?> atc = pref.getAll();
        return atc.containsKey(key);

    }

    //public void storeArrayList(String key, ArrayList<DealerCardModel> list) {
    //	try {
    //		Gson gson = new Gson();
    //		String jsonText = gson.toJson(list);
    //		Editor prefEdit = pref.edit();
    //		prefEdit.putString(key, jsonText);
    //		prefEdit.commit();
    //	} catch (Exception e) {
    //		e.printStackTrace();
    //		// TODO: handle exception
    //	}
    //
    //}

    public ArrayList<?> retrieveArrayList(String key) {
        ArrayList<?> text = new ArrayList<>();
        try {
            Gson gson = new Gson();
            // Editor prefEdit = pref.edit();
            String jsonText = pref.getString(key, null);
            text = gson.fromJson(jsonText, ArrayList.class);

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return text;
    }



}
