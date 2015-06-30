package tuandn.com.twitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import Utils.Utils;

/**
 * Created by Anh Trung on 6/22/2015.
 */
public class MainActivity extends Activity {
    private Button post;
    private SharedPreferences mSharedPreferences;
    private EditText username;
    private Context mContext;
    private TextView welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mContext = MainActivity.this;
        post = (Button) findViewById(R.id.post);
        welcome = (TextView) findViewById(R.id.user_name);

        displayUsername();
    }

    private void displayUsername() {
        TwitterSession session =
                Twitter.getSessionManager().getActiveSession();
        if (session == null) {
            Toast.makeText(getApplication(),"Getting session failed",Toast.LENGTH_LONG).show();
        } else {
            welcome.setText("Hello, @" + session.getUserName());
        }
    }


    public void postOnWall(View v){

        EditText status = (EditText) findViewById(R.id.et_status);
        String status_content = status.getText().toString();

        if(status_content.trim().equalsIgnoreCase(null)){
            Toast.makeText(getApplicationContext(),"Please enter status first!",Toast.LENGTH_LONG).show();
        }
        else {
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            StatusesService statusesService = twitterApiClient.getStatusesService();
            statusesService.update(status_content, null, null, null, null, null, null, null, new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    Toast.makeText(getApplication(), "Success", Toast.LENGTH_LONG).show();
                    ;
                }

                @Override
                public void failure(TwitterException e) {
                    Toast.makeText(getApplication(), "Error", Toast.LENGTH_LONG).show();
                    ;
                }
            });
        }
    }

    public void ShowFriendList(View v){
        if(Utils.isConnectingToInternet(MainActivity.this)){
            Intent i = new Intent(MainActivity.this,DisplayFriendListActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(),"You are not connecting to the Internet",Toast.LENGTH_LONG).show();
        }
    }
}

