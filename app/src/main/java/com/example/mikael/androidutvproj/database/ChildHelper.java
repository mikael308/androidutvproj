package com.example.mikael.androidutvproj.database;

import android.content.Context;

import com.example.mikael.androidutvproj.dao.ChildDataAccessObject;

/**
 * Table with foreign key
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class ChildHelper<EntryType extends ChildDataAccessObject> extends DatabaseHelper<EntryType> {

    public static final String COLUMN_FOREIGN_ID        = "foreignkey_id";

    public ChildHelper(Context context, String tablename, String entries) {
        super(context, tablename, entries);
    }


}
