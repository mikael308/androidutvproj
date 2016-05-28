package com.example.mikael.androidutvproj.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mikael.androidutvproj.dao.DataAccessObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * class contains attributes for column data-types
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class DatabaseHelper<T extends DataAccessObject> extends SQLiteOpenHelper {


    /**
     * this name of database table
     */
    public String TABLE_NAME;

    protected static final String COLUMN_ID         = "id";

    protected static final String COMMA_SEP         = " , ";
    protected static final String NOTNULL           = " NOT NULL ";
    protected static final String TEXT_TYPE         = " TEXT ";
    protected static final String INTEGER_TYPE      = " INTEGER ";
    protected static final String REAL_TYPE         = " REAL ";

    /**
     * SQL create table query
     */
    private String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s ( %s )";
    /**
     * SQL drop table query
     */
    private String SQL_DROP_TABLE = "DROP TABLE IF EXISTS %s";
    /**
     * name of database
     */
    public static final String DATABASE_NAME = "androidutvprojdb";
    /**
     * current database version
     */
    protected static int DATABASE_VERSION = 1;

    protected static SQLiteDatabase mSQLiteDatabase;


    /**
     *
     * @param context
     * @param tablename
     * @param entries
     */
    public DatabaseHelper(Context context, String tablename, String entries){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        TABLE_NAME          = tablename;
        SQL_CREATE_TABLE    = String.format(SQL_CREATE_TABLE, tablename, entries);
        SQL_DROP_TABLE      = String.format(SQL_DROP_TABLE, TABLE_NAME);

    }

    public void init(){
        mSQLiteDatabase = this.getWritableDatabase();
        createTable();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     * creates this table
     */
    public void createTable(){
        mSQLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }


    /**
     * persists entry in database
     * @param entry entry to persist
     * @return
     */
    public abstract long persist(T entry);

    /**
     * delete entry in database
     * @param entry entry to delete
     * @return
     */
    public long delete(T entry) {
        String selection        = String.format("%s LIKE ?", COLUMN_ID);
        String[] selectionArgs  = {
                entry.getId()
        };

        return mSQLiteDatabase.delete(TABLE_NAME, selection, selectionArgs);
    }

    /**
     * edit entry in database
     * @param entry
     */
    public long edit(T entry){
        delete(entry);
        return persist(entry);
    }

    /**
     * format from Cursor to DataAccessObject T
     * @param c Cursor to format from
     * @return instance of T
     */
    public abstract T parse(Cursor c);

    /**
     * get all rows of database entries
     * @return Cursor to first entry
     * @throws SQLException
     */
    public abstract Cursor getAllRows() throws SQLException;

    /**
     * get Cursor equal to entry
     * @param entry
     * @return Cursor to specified entry<br>return null if entry is not found
     */
    public Cursor getRow(T entry){
        Cursor c = getAllRows();
        c.moveToFirst();
        if (c.isAfterLast()) return null;

        do {
            if (c.getString(c.getColumnIndex(COLUMN_ID)).equals(entry.getId())){
                return c;
            }

        } while(c.moveToNext());

        return null;
    }

    /**
     * get entries read from database table
     * @return entries list of entries, if no entries found: empty list is returned
     * @throws SQLException
     */
    public abstract List<T> getAllEntries() throws SQLException;

    public boolean dropTable(){
        try {
            mSQLiteDatabase.execSQL(SQL_DROP_TABLE);
            return true;

        } catch (SQLException e){
            Log.e("hal", e.getMessage());
            return false;
        }
    }


    /**
     * get all rows grouped by specific database column
     * @param matchColumn
     * @return map with keyes of matchColumn values, pointing to collection of Entries matching key column
     */
    public Map<String, ArrayList<T>> getRowsGroupByColumn(String matchColumn){
        Cursor c = getAllRows();
        if (c == null || c.getCount() == 0) return null;

        Map<String, ArrayList<T>> resList = new HashMap<>();
        do {
            String itemFK = c.getString(c.getColumnIndex(matchColumn));
            ArrayList<T> tmpEntryList;
            if ((tmpEntryList = resList.get(itemFK)) == null){ // if FK was new value
                tmpEntryList = new ArrayList<>(2);
                resList.put(itemFK, tmpEntryList);
            }

            tmpEntryList.add(parse(c));

        } while (c.moveToNext());

        return resList;
    }


}
