package cl.json.social;

import android.content.ActivityNotFoundException;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by disenodosbbcl on 23-07-16.
 */
public class GenericShare extends ShareIntent {
    public GenericShare(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public void open(ReadableMap options) throws ActivityNotFoundException {
        super.open(options);
        //  extra params here
        this.openIntentChooser();
    }

    @Override
    protected String getPackage(String packageName) {
        return packageName;
    }

    @Override
    protected String getDefaultWebLink(String defaultWebLink) {
        return defaultWebLink;
    }

    @Override
    protected String getPlayStoreLink(String playStoreLink) {
        return playStoreLink;
    }
}
