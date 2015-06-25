package tuandn.com.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends Activity {

    public static final String ConsummerKey = "2OViy7kUg5VvFWAs7ONRgFCaw";
    public static final String ConsummerSecret = "1569OnKRNtfSiGvsK5XB9eTWZmxiLETMBSmsvavIKSiAnhvkDT";

    private SharedPreferences mSharedPreferences;
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(ConsummerKey, ConsummerSecret);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.login_screen);


//        Check if another already logged in
        if (Twitter.getSessionManager().getActiveSession() != null) {
//            Start Main Activity
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {
            loginButton = (TwitterLoginButton)
                    findViewById(R.id.login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
//                    Start Main Activity
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode,
                data);
    }

}
