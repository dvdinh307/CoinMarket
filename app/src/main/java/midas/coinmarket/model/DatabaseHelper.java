package midas.coinmarket.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_HISOTRY);
        // Create tables again
        onCreate(db);
    }

    public long insertHistory(CryptocurrencyObject object) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AppConstants.COLUMNS.ID_COIN.toString(), String.valueOf(object.getId()));
        values.put(AppConstants.COLUMNS.TITLE.toString(), object.getName());
        values.put(AppConstants.COLUMNS.TIME.toString(), new Date().toString());
        // insert row
        long id = db.insert(TBL_HISOTRY, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
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

        // close db connection
        db.close();
        cursor.close();
        // return notes list
        return notes;
    }

    /**
     * Clear all history.
     */
    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TBL_HISOTRY);
    }
}
