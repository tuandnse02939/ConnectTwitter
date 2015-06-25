package tuandn.com.twitter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.twitter.sdk.android.core.TwitterSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import Adapter.FriendListAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display Friend List
        displayFriendList();
    }


    private void displayFriendList() {
        session =
                com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
        userID = session.getUserId();
        twitter = new TwitterFactory().getInstance();

        //Set Twitter Authentication
        twitter.setOAuthConsumer(LoginActivity.ConsummerKey, LoginActivity.ConsummerSecret);
        twitter4j.auth.AccessToken accessToken = new AccessToken(AccessToken, AccessSecret);
        twitter.setOAuthAccessToken(accessToken);

        friendList = new ArrayList<Friend>();

        new GetFriends().execute();
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
                    f.setName(users.get(i).getName());
                    f.setImage(users.get(i).getBiggerProfileImageURL());
                    friendList.add(f);
                }
            } catch (TwitterException e){
                e.printStackTrace();
            }
            return friendList;
        }

        protected void onPostExecute(ArrayList<Friend> frList) {
            setListAdapter(new FriendListAdapter(DisplayFriendListActivity.this, frList));
        }
    }
}