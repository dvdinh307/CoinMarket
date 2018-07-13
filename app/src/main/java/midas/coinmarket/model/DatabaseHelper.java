package midas.coinmarket.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import midas.coinmarket.utils.AppConstants;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "coin_market_db";
    private static final String TBL_HISOTRY = "history";
    private static final String TBL_BOOKMARK = "bookmark";

    private final String CREATE_TABLE_HISTORY = "CREATE TABLE " + TBL_HISOTRY + "(" +
            AppConstants.COLUMNS.ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            AppConstants.COLUMNS.ID_COIN.toString() + " TEXT," +
            AppConstants.COLUMNS.TITLE.toString() + " TEXT," +
            AppConstants.COLUMNS.TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    private final String CREATE_TABLE_BOOKMARK = "CREATE TABLE " + TBL_BOOKMARK + "(" +
            AppConstants.COLUMNS.ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            AppConstants.COLUMNS.ID_BOOKMARK.toString() + " TEXT," +
            AppConstants.COLUMNS.CONTENT_BOOK_MARK.toString() + " TEXT," +
            AppConstants.COLUMNS.TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create two table history and bookmark.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_HISOTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BOOKMARK);
        onCreate(db);
    }

    public void insertHistory(CryptocurrencyObject object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.COLUMNS.ID_COIN.toString(), String.valueOf(object.getId()));
        values.put(AppConstants.COLUMNS.TITLE.toString(), object.getName());
        values.put(AppConstants.COLUMNS.TIME.toString(), new Date().toString());
        db.insert(TBL_HISOTRY, null, values);
        db.close();
    }

    /**
     * Get all list history.
     *
     * @return
     */
    public ArrayList<CryptocurrencyObject> getAllHistory() {
        ArrayList<CryptocurrencyObject> notes = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_HISOTRY + " ORDER BY " +
                AppConstants.COLUMNS.TIME.toString() + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CryptocurrencyObject cryobject = new CryptocurrencyObject();
                cryobject.setId(cursor.getInt(cursor.getColumnIndex(AppConstants.COLUMNS.ID_COIN.toString())));
                cryobject.setName(cursor.getString(cursor.getColumnIndex(AppConstants.COLUMNS.TITLE.toString())));
                cryobject.setTime(cursor.getString(cursor.getColumnIndex(AppConstants.COLUMNS.TIME.toString())));
                notes.add(cryobject);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return notes;
    }

    /**
     * Clear all history.
     */
    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TBL_HISOTRY);
    }

    // CREATE FUNCTION FOR BOOKMARK.
    public void insertBookMark(CoinObject coin, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Check exist.
        String sql = "SELECT * FROM " + TBL_BOOKMARK + " WHERE " + AppConstants.COLUMNS.ID_BOOKMARK.toString() + "=" + String.valueOf(coin.getId());
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(AppConstants.COLUMNS.ID_BOOKMARK.toString(), coin.getId());
            values.put(AppConstants.COLUMNS.CONTENT_BOOK_MARK.toString(), title);
            values.put(AppConstants.COLUMNS.TIME.toString(), new Date().toString());
            db.insert(TBL_BOOKMARK, null, values);
        }
        cursor.close();
        db.close();
    }

    /**
     * Get All list book mark.
     *
     * @return
     */
    public ArrayList<CoinObject> getAllBookmark() {
        ArrayList<CoinObject> coins = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_BOOKMARK + " ORDER BY " +
                AppConstants.COLUMNS.TIME.toString() + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex(AppConstants.COLUMNS.CONTENT_BOOK_MARK.toString()));
                try {
                    // TODO : Now only save USD values.
                    JSONObject object = new JSONObject(data);
                    coins.add(CoinObject.parserData(object, ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return coins;
    }

    /**
     * Remove item
     *
     * @param id
     * @return
     */
    public boolean removeItemBookmark(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBL_BOOKMARK, AppConstants.COLUMNS.ID_BOOKMARK + "=" + id, null) > 0;
    }

    /**
     * Pin item to top.
     *
     * @param id
     */
    public boolean pinToTop(String id) {
        ContentValues content = new ContentValues();
        content.put(AppConstants.COLUMNS.TIME.toString(), new Date().toString());
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TBL_BOOKMARK, content, AppConstants.COLUMNS.ID_BOOKMARK + " = " + id, null) > 0;
    }


}
