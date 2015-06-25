package tuandn.com.twitter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.FriendListAdapter;
import Model.Friend;

/**
 * Created by Anh Trung on 6/24/2015.
 */
public class DisplayFriendListActivity extends ListActivity {

    private static final String FriendLimit = "250";

    private ArrayList<Friend> friendList;
    private FriendListAdapter adapter;
    private ListView mListView;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display Friend List
        displayFriendList();
    }

    private void displayFriendList() {
        //Read Friend Data
        String url = "/me/taggable_friends";
        getFriends(url);
    }

    public void getFriends(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("limit",FriendLimit);


//        GraphRequestBatch batch = new GraphRequestBatch(new GraphRequest(AccessToken.getCurrentAccessToken(),url,null,null))
    }

    private String cutString(String dataResponse) {
        int check = 0;
        for (int i = 0; i < dataResponse.length(); i++) {
            if (dataResponse.charAt(i) == '{')
            {
                check++;
                if(check==2){
                    dataResponse = dataResponse.substring(i,dataResponse.length()-1);
                    return dataResponse;
                }
            }
        }
        return null;
    }
}