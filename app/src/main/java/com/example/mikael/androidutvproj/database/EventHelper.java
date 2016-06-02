package com.example.mikael.androidutvproj.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.mikael.androidutvproj.dao.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventHelper extends ChildHelper<Event> {


    // database attributes
    public static final String COLUMN_OPENHOUSE         = "showing";

    private static final String SQL_TABLE_ENTRIES =
              COLUMN_ID                     + NOTNULL           + COMMA_SEP
            + COLUMN_FOREIGN_ID             + NOTNULL           + COMMA_SEP
            + COLUMN_OPENHOUSE              + INTEGER_TYPE;


    public EventHelper(Context context) {
        super(context, "event", SQL_TABLE_ENTRIES);
    }


    @Override
    public long persist(Event entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,               entry.getId());
        values.put(COLUMN_FOREIGN_ID,       entry.getForeignKey());
        values.put(COLUMN_OPENHOUSE,        entry.getDate().getTime());

        return mSQLiteDatabase.insert(TABLE_NAME, null, values);
    }

    @Override
    public Event parse(Cursor c) {
        Event event = new Event(c.getString(c.getColumnIndex(   COLUMN_ID)));
        event.setForeignKey(c.getString(c.getColumnIndex(       COLUMN_FOREIGN_ID)));
        event.setDate(new Date(c.getLong(c.getColumnIndex(      COLUMN_OPENHOUSE))));

        return event;
    }

    @Override
    public Cursor getAllRows() {
        Log.d("hal", "getAllRows in " + Thread.currentThread().getName());
        Cursor c = mSQLiteDatabase.query(false,
                TABLE_NAME,
                new String[]{
                        COLUMN_ID,
                        COLUMN_FOREIGN_ID,
                        COLUMN_OPENHOUSE},
                null, null, null, null,
                COLUMN_ID + " COLLATE NOCASE",
                null);
        if(c!= null){
            c.moveToFirst();
        }
        return c;
    }

    @Override
    public List<Event> getAllEntries() throws SQLException {
        Cursor cursor = getAllRows();
        if(cursor == null) return null;

        ArrayList<Event> events    = new ArrayList<>(cursor.getCount());;
        do{
            events.add(parse(cursor));

        } while(cursor.moveToNext());
        cursor.close();

        return events;
    }
}
