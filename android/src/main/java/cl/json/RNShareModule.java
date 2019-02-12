package cl.json;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cl.json.social.GenericShare;
import cl.json.social.ShareIntent;


public class RNShareModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private enum SHARES {
        generic;
        public static ShareIntent getShareClass(ReactApplicationContext reactContext) {
            return new GenericShare(reactContext);
        }
    };

    public RNShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
    return "RNShare";
    }

    @javax.annotation.Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        for (SHARES val: SHARES.values()) {
            constants.put(val.toString().toUpperCase(), val.toString());
        }
        return constants;
    }

    @ReactMethod
    public void open(ReadableMap options, @Nullable Callback failureCallback, @Nullable Callback successCallback) {
        try{

            JSONArray options = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("package", "com.google.android.apps.plus");
            item.put("defaultWebLink", "https://plus.google.com/share?url={url}");
            item.put("playStoreLink", "market://details?id=com.google.android.apps.plus");
            options.add(item);
            item.put("br.com.bb.android", "com.google.android.apps.plus");
            item.put("defaultWebLink", "");
            item.put("playStoreLink", "market://details?id=br.com.bb.android");
            options.add(item);

            GenericShare share = new GenericShare(this.reactContext);
            share.open(options);
            successCallback.invoke("OK");
        }catch(ActivityNotFoundException ex) {
            System.out.println("ERROR");
            System.out.println(ex.getMessage());
            failureCallback.invoke("not_available");
        }catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
            failureCallback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void shareSingle(ReadableMap options, @Nullable Callback failureCallback, @Nullable Callback successCallback) {
        System.out.println("SHARE SINGLE METHOD");
        // if (ShareIntent.hasValidKey("social", options) ) {
            try{
                ShareIntent shareClass = SHARES.getShareClass(this.reactContext);
                if (shareClass != null && shareClass instanceof ShareIntent) {
                    shareClass.open(options);
                    successCallback.invoke("OK");
                } else {
                    throw new ActivityNotFoundException("Invalid share activity");
                }
            }catch(ActivityNotFoundException ex) {
                System.out.println("ERROR");
                System.out.println(ex.getMessage());
                failureCallback.invoke(ex.getMessage());
            }catch (Exception e) {
                System.out.println("ERROR");
                System.out.println(e.getMessage());
                failureCallback.invoke(e.getMessage());
            }
        // } else {
        //     failureCallback.invoke("key 'social' missing in options");
        // }
    }

    @ReactMethod
    public void isPackageInstalled(String packagename, @Nullable Callback failureCallback, @Nullable Callback successCallback) {
        try{
            boolean res = ShareIntent.isPackageInstalled(packagename, this.reactContext);
            successCallback.invoke(res);
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            failureCallback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void isBase64File(String url, @Nullable Callback failureCallback, @Nullable Callback successCallback) {
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if((scheme != null) && scheme.equals("data")) {
                successCallback.invoke(true);
            } else {
                successCallback.invoke(false);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
            failureCallback.invoke(e.getMessage());
        }
    }
}
