package tuandn.com.twitter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import Adapter.FriendListAdapter;
import Database.DatabaseHandler;
import Model.Friend;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by Anh Trung on 6/24/2015.
 */
public class DisplayFriendListActivity extends ListActivity {

    public static final String AccessToken = "2166139124-xFf0pSAGwRdKvAcZrfzgoZa0buj4MYZWk4U9IvX";
    public static final String AccessSecret = "TfzCqZFt95A7VnuWKObKPdhiQoftnhDNlX69t2AACQKQy";
    private static final int FriendLimit = 100;

    private ArrayList<Friend> friendList;
    private  TwitterSession session;
    private twitter4j.Twitter twitter;
    private long userID,lCursor = -1;
    private DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new DatabaseHandler(this);

        //Display Friend List
        displayFriendList();
    }


    private void displayFriendList() {
        session =
                com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
        if(session == null){
            Toast.makeText(getApplicationContext(),"Load Session Failed",Toast.LENGTH_LONG).show();
        }
        else {
            userID = session.getUserId();
            twitter = new TwitterFactory().getInstance();

            //Set Twitter Authentication
            twitter.setOAuthConsumer(LoginActivity.ConsummerKey, LoginActivity.ConsummerSecret);
            twitter4j.auth.AccessToken accessToken = new AccessToken(AccessToken, AccessSecret);
            twitter.setOAuthAccessToken(accessToken);

            friendList = new ArrayList<Friend>();

            new GetFriends().execute();
        }
    }

    class GetFriends extends AsyncTask<Void, Void, ArrayList<Friend>> {

        private Exception exception;

        protected ArrayList<Friend> doInBackground(Void... voids) {
            //Get Friend List
            List<twitter4j.User> users;
            try {
                users = twitter.getFriendsList(userID, lCursor, FriendLimit);
                for (int i = 0; i < users.size(); i++) {
                    Friend f = new Friend();
                    f.setId(users.get(i).getId());
                    f.setName(users.get(i).getName());
                    f.setImage(users.get(i).getBiggerProfileImageURL());
                    friendList.add(f);

                    //Save/Update Friend to database
                    if(handler.isFriendExist(f.getId())){
                        handler.updateFriend(f);
                    }
                    else {
                        handler.addFriend(f);
                    }
                }
            } catch (TwitterException e){
                e.printStackTrace();
            }
            return friendList;
        }

        protected void onPostExecute(ArrayList<Friend> frList) {
            if(frList.size() != 0) {
                setListAdapter(new FriendListAdapter(DisplayFriendListActivity.this, frList));
            }
            else {
                frList = handler.getFriends();
                if(frList.size() != 0) {
                    setListAdapter(new FriendListAdapter(DisplayFriendListActivity.this, frList));
                }
                else {
                    Toast.makeText(getApplication(),"Can not get Friends",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}