package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Model.Friend;

/**
 * Created by Anh Trung on 6/30/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FRIEND_LIST";

    // Table Names
    private static final String TABLE_FRIEND_LIST = "TABLE_FRIEND_LIST";

    // Friend List Table - column names
    private static final String COLUMN_FRIEND_ID = "COLUMN_FRIEND_ID";
    private static final String COLUMN_FRIEND_IMAGE = "COLUMN_FRIEND_IMAGE";
    private static final String COLUMN_FRIEND_NAME = "COLUMN_FRIEND_NAME";

    // Friend List Table create statement
    private static final String CREATE_TABLE_FRIEND_LIST = "CREATE TABLE "
            + TABLE_FRIEND_LIST + "(" + COLUMN_FRIEND_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_FRIEND_IMAGE + " TEXT," + COLUMN_FRIEND_NAME
            + " TEXT" + ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FRIEND_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND_LIST,"1",null);
        closeDatabse();
    }

    public void closeDatabse() {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //Insert a new friend to DB
    public void addFriend(Friend friend){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIEND_ID, friend.getId());
        values.put(COLUMN_FRIEND_IMAGE, friend.getImage());
        values.put(COLUMN_FRIEND_NAME, friend.getName());
        db.insert(TABLE_FRIEND_LIST, null, values);
        closeDatabse();
    }

    //Get List of Friends from DB
    public ArrayList<Friend> getFriends(){
        ArrayList<Friend> listFriends = new ArrayList<Friend>();
        String selectQuery = "SELECT  * FROM " + TABLE_FRIEND_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Friend f = new Friend();
                f.setId(c.getLong(c.getColumnIndex(COLUMN_FRIEND_ID)));
                f.setName((c.getString(c
                        .getColumnIndex(COLUMN_FRIEND_NAME))));
                f.setImage(c.getString(c
                        .getColumnIndex(COLUMN_FRIEND_IMAGE)));
                listFriends.add(f);
            } while (c.moveToNext());
        }
        closeDatabse();
        return listFriends;
    }

    //check if a friend is already existed in DB
    public boolean isFriendExist(long id){
        String selectQuery = "SELECT * FROM " + TABLE_FRIEND_LIST + " WHERE "
                + COLUMN_FRIEND_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst())
        {
            return true;
        }
        else {
            return false;
        }
    }

    //Update Information of a friend
    public void updateFriend(Friend friend){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIEND_NAME, friend.getName());
        values.put(COLUMN_FRIEND_IMAGE, friend.getImage());
        db.update(TABLE_FRIEND_LIST, values, COLUMN_FRIEND_ID + " = ?",
                new String[] { String.valueOf(friend.getId()) });
        closeDatabse();
    }
}
