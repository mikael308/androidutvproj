package com.example.mikael.androidutvproj.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.mikael.androidutvproj.dao.RealEstate;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Mikael Holmbom
 * @verison 1.0
 */
public class RealEstateHelper extends DatabaseHelper<RealEstate>{

    private static final String TABLE_NAME              = "realestate";

    // database attributes
    public static final String COLUMN_ID                = TABLE_NAME + "id";
    public static final String COLUMN_ADDRESS           = "address";
    public static final String COLUMN_LAT               = "lat";
    public static final String COLUMN_LNG               = "lng";
    public static final String COLUMN_TYPE              = "type";
    public static final String COLUMN_CONSTRUCTYEAR     = "constructyear";
    public static final String COLUMN_STARTBID          = "startbid";
    public static final String COLUMN_RENT              = "rent";
    public static final String COLUMN_FLOOR             = "floor";
    public static final String COLUMN_ROOMS             = "rooms";
    public static final String COLUMN_LIVINGSPACE       = "livingspace";
    public static final String COLUMN_DESCRIPTION       = "description";

    private static final String SQL_TABLE_ATTRS =
            COLUMN_ID               + TEXT_TYPE + NOTNULL           + COMMA_SEP
            + COLUMN_ADDRESS        + TEXT_TYPE         + COMMA_SEP
            + COLUMN_LAT            + REAL_TYPE         + COMMA_SEP
            + COLUMN_LNG            + REAL_TYPE         + COMMA_SEP
            + COLUMN_TYPE           + INTEGER_TYPE      + COMMA_SEP
            + COLUMN_CONSTRUCTYEAR  + INTEGER_TYPE      + COMMA_SEP
            + COLUMN_STARTBID       + INTEGER_TYPE      + COMMA_SEP
            + COLUMN_RENT           + REAL_TYPE         + COMMA_SEP
            + COLUMN_FLOOR          + REAL_TYPE         + COMMA_SEP
            + COLUMN_ROOMS          + REAL_TYPE         + COMMA_SEP
            + COLUMN_LIVINGSPACE    + REAL_TYPE         + COMMA_SEP
            + COLUMN_DESCRIPTION    + TEXT_TYPE;

    public RealEstateHelper(Context context) {
        super(context, TABLE_NAME, SQL_TABLE_ATTRS);
    }

    @Override
    public long persist(RealEstate entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,               entry.getId());
        values.put(COLUMN_ADDRESS,          entry.getAddress());
        values.put(COLUMN_LAT,              entry.getLatLng().latitude);
        values.put(COLUMN_LNG,              entry.getLatLng().longitude);
        values.put(COLUMN_TYPE,             entry.getType().getId());
        values.put(COLUMN_CONSTRUCTYEAR,    entry.getConstructYear().getTime());
        values.put(COLUMN_STARTBID,         entry.getStartBid());
        values.put(COLUMN_RENT,             entry.getRent());
        values.put(COLUMN_FLOOR,            entry.getFloor());
        values.put(COLUMN_ROOMS,            entry.getRooms());
        values.put(COLUMN_LIVINGSPACE,      entry.getLivingSpace());
        values.put(COLUMN_DESCRIPTION,      entry.getDescription());

        return mSQLiteDatabase.insert(TABLE_NAME, null, values);
    }

    @Override
    public long delete(RealEstate entry) {
        String selection        = String.format("%s LIKE ?", COLUMN_ID);
        String[] selectionArgs  = { entry.getId() };

        return mSQLiteDatabase.delete(TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void edit(RealEstate oldEntry, RealEstate newEntry) {

    }

    @Override
    public RealEstate parse(Cursor c) {
        if(c.isAfterLast()) return null;

        RealEstate a = new RealEstate(c.getString(c.getColumnIndex( COLUMN_ID)));
        a.setAddress(c.getString(c.getColumnIndex(                  COLUMN_ADDRESS)));
        a.setLatLng(new LatLng(
                c.getDouble(c.getColumnIndex(                       COLUMN_LAT)),
                c.getDouble(c.getColumnIndex(                       COLUMN_LNG))));
        a.setType(RealEstate.Type.newType(c.getInt(c.getColumnIndex(COLUMN_TYPE))));
        a.setRent(c.getInt(c.getColumnIndex(                        COLUMN_RENT)));
        a.setConstructYear(c.getLong(c.getColumnIndex(              COLUMN_CONSTRUCTYEAR)));
        a.setFloor(c.getDouble(c.getColumnIndex(                    COLUMN_FLOOR)));
        a.setRooms(c.getDouble(c.getColumnIndex(                    COLUMN_ROOMS)));
        a.setLivingSpace(c.getDouble(c.getColumnIndex(              COLUMN_LIVINGSPACE)));
        a.setStartBid(c.getInt(c.getColumnIndex(                    COLUMN_STARTBID)));
        a.setDescription(c.getString(c.getColumnIndex(              COLUMN_DESCRIPTION)));

        return a;
    }


    @Override
    public Cursor getAllRows() {
        Cursor c = mSQLiteDatabase.query(false,
                TABLE_NAME,
                new String[]{
                        COLUMN_ID,
                        COLUMN_ADDRESS,
                        COLUMN_LAT,
                        COLUMN_LNG,
                        COLUMN_TYPE,
                        COLUMN_CONSTRUCTYEAR,
                        COLUMN_STARTBID,
                        COLUMN_RENT,
                        COLUMN_FLOOR,
                        COLUMN_ROOMS,
                        COLUMN_LIVINGSPACE,
                        COLUMN_DESCRIPTION},
                null, null, null, null,
                COLUMN_ADDRESS,
                null);
        if(c!= null){
            c.moveToFirst();
        }
        return c;
    }

    @Override
    public List<RealEstate> getAllEntries() throws SQLException {
        Cursor cursor = getAllRows();
        if(cursor == null) return null;

        ArrayList<RealEstate> realEstates       = new ArrayList<>(cursor.getCount());

        do{
            RealEstate realEstate = parse(cursor);
            realEstates.add(realEstate);
        } while(cursor.moveToNext());
        cursor.close();

        return realEstates;
    }
}
