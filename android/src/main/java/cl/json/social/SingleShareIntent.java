package cl.json.social;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;
import cl.json.ReactNativeJson;
import cl.json.ShareFile;

/**
 * Created by priscila.rodrigues on 11-02-2019.
 */
public abstract class SingleShareIntent extends ShareIntent {

    protected String playStoreURL = null;
    protected String appStoreURL = null;

    public SingleShareIntent(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    public void open(ReadableMap options) throws ActivityNotFoundException {

        ReactNativeJson rnJson = new ReactNativeJson();
        JSONObject obj = rnJson.convertMapToJson(options);
        System.out.println("obj: " + obj);

        for (Integer i = 0; i < obj.length(); i++) {
            System.out.println(getPackage());
            // check if package is installed
            if (getPackage() != null || getDefaultWebLink() != null || getPlayStoreLink() != null) {
                if (this.isPackageInstalled(getPackage(), reactContext)) {
                    System.out.println("INSTALLED");
                    this.getIntent().setPackage(getPackage());
                    super.open(options);
                } else {
                    System.out.println("NOT INSTALLED");
                    String url = "";
                    if (getDefaultWebLink() != null) {
                        url = getDefaultWebLink().replace("{url}", this.urlEncode(options.getString("url")))
                                .replace("{message}", this.urlEncode(options.getString("message")));
                    } else if (getPlayStoreLink() != null) {
                        url = getPlayStoreLink();
                    } else {
                        // TODO
                    }
                    // open web intent
                    this.setIntent(new Intent(new Intent("android.intent.action.VIEW", Uri.parse(url))));
                }
            }
        }
        // configure default
        super.open(options);
    }

    protected void openIntentChooser() throws ActivityNotFoundException {
        if (this.options.hasKey("forceDialog") && this.options.getBoolean("forceDialog")) {
            Intent chooser = Intent.createChooser(this.getIntent(), this.chooserTitle);
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.reactContext.startActivity(chooser);
        } else {
            this.getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.reactContext.startActivity(this.getIntent());
        }
    }
}