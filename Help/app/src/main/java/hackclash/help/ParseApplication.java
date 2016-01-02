package hackclash.help;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Mohit on 1/2/2016.
 */
public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
