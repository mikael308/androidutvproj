package com.example.mikael.androidutvproj.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.mikael.androidutvproj.dao.Photo;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public class PhotoHelper extends ChildHelper<Photo> {

    // database attributes
    public static final String COLUMN_DESCRIPTION       = "description";
    public static final String COLUMN_FILESOURCE        = "filesource";
    public static final String COLUMN_LATITUDE          = "latitude";
    public static final String COLUMN_LONGITUDE         = "longtiude";
    public static final String COLUMN_DATE              = "date";

    private static final String SQL_TABLE_ENTRIES =
              COLUMN_ID                     + TEXT_TYPE + NOTNULL   + COMMA_SEP
            + COLUMN_FOREIGN_ID             + TEXT_TYPE + NOTNULL   + COMMA_SEP
            + COLUMN_FILESOURCE             + TEXT_TYPE + NOTNULL   + COMMA_SEP
            + COLUMN_DESCRIPTION            + TEXT_TYPE + NOTNULL   + COMMA_SEP
            + COLUMN_LATITUDE               + INTEGER_TYPE          + COMMA_SEP
            + COLUMN_LONGITUDE              + INTEGER_TYPE          + COMMA_SEP
            + COLUMN_DATE                   + INTEGER_TYPE          ;

    public PhotoHelper(Context context) {
        super(context, "photo", SQL_TABLE_ENTRIES);
    }

    @Override
    public long persist(Photo photo){
        Log.d("photohelper", "perisist " + photo.getLabel());
        Log.d("photohelper", "with fk : " + photo.getForeignKey());
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,                   photo.getId());
        values.put(COLUMN_FOREIGN_ID,           photo.getForeignKey());
        values.put(COLUMN_FILESOURCE,           photo.getPhotoFile().getAbsolutePath());
        values.put(COLUMN_DESCRIPTION,          photo.getDescription());
        if (photo.getLatLng() != null){
            values.put(COLUMN_LATITUDE,         photo.getLatLng().latitude);
            values.put(COLUMN_LONGITUDE,        photo.getLatLng().longitude);
        }
        if (photo.getDate() != null){
            values.put(COLUMN_DATE,             photo.getDate().getTime());
        }

        return mSQLiteDatabase.insert(TABLE_NAME, null, values);
    }


    @Override
    public Photo parse(Cursor c) {
        Photo photo = new Photo(
                c.getString(c.getColumnIndex(RealEstateHelper.  COLUMN_ID)),
                new File(c.getString(c.getColumnIndex(          COLUMN_FILESOURCE))));
        photo.setForeignKey(c.getString(c.getColumnIndex(       COLUMN_FOREIGN_ID)));
        photo.setDescription(c.getString(c.getColumnIndex(      COLUMN_DESCRIPTION)));
        double lat = c.getDouble(c.getColumnIndex(              COLUMN_LATITUDE));
        double lng = c.getDouble(c.getColumnIndex(              COLUMN_LONGITUDE));
        photo.setLatLng(new LatLng(lat, lng));
        photo.setDate(new Date(c.getLong(c.getColumnIndex(      COLUMN_DATE))));
        return photo;
    }

    @Override
    public Cursor getAllRows() throws SQLException {

        Cursor c = mSQLiteDatabase.query(false,
                TABLE_NAME,
                new String[]{
                    COLUMN_ID,
                    COLUMN_FOREIGN_ID,
                    COLUMN_FILESOURCE,
                    COLUMN_DESCRIPTION,
                    COLUMN_LATITUDE,
                    COLUMN_LONGITUDE,
                    COLUMN_DATE
                },
                null, null, null, null,
                RealEstateHelper.COLUMN_ID + " COLLATE NOCASE", // ORDER BY
                null);
        if(c!= null){
            c.moveToFirst();
        }
        return c;
    }

    @Override
    public List<Photo> getAllEntries() throws SQLException {
        Cursor cursor = getAllRows();
        if(cursor == null) return null;

        ArrayList<Photo> photos    = new ArrayList<>(cursor.getCount());;
        do{
            photos.add(parse(cursor));

        } while(cursor.moveToNext());
        cursor.close();

        return photos;
    }
}
