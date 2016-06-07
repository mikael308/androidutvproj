package com.example.mikael.androidutvproj;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class Dialog {


    /**
     * compounds list of strings with space between<br>
     *     first letter is uppercase, rest is lowercase
     * @param strings
     * @return
     */
    public static String initialUpper(String... strings){
        String compoundStr = "";
        for(int i = 0; i < strings.length; i++){
            compoundStr += strings[i];
            if(i != strings.length-1)
                compoundStr += " ";
        }

        compoundStr = compoundStr.substring(0, 1).toUpperCase() + compoundStr.substring(1).toLowerCase();

        return compoundStr;
    }


    /**
     * create dialog with null cancelbutton and okbutton
     * @param title
     * @param v
     * @param btn_pos
     * @return
     */
    public static AlertDialog.Builder createDialog(Activity activity, int title, View v, DialogInterface.OnClickListener btn_pos){
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(v)
                .setPositiveButton(activity.getResources().getString(R.string.btn_ok), btn_pos)
                .setNegativeButton(activity.getResources().getString(R.string.btn_cancel), null);
    }


    public static AlertDialog.Builder confirm(Activity activity, String confirmTitleSuffix, String message, DialogInterface.OnClickListener confirmAction){
        String title = initialUpper(activity.getResources().getString(R.string.confirm),
                confirmTitleSuffix);

        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(activity.getResources().getString(R.string.btn_cancel), null)
                .setPositiveButton(activity.getResources().getString(R.string.btn_ok), confirmAction);
    }

    public static AlertDialog.Builder question(Activity activity, int msgResId, DialogInterface.OnClickListener onPositiveClick){
        return new AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.confirm))
                .setMessage(msgResId)
                .setNegativeButton(activity.getResources().getString(R.string.btn_no), null)
                .setPositiveButton(activity.getResources().getString(R.string.btn_yes), onPositiveClick);
    }

    public static AlertDialog removeFile(Activity activity, final File file, final Runnable postRemove){

        return Dialog.question(activity, R.string.delete_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                file.delete();
                if (postRemove != null) postRemove.run();
            }
        }).create();
    }

    public static AlertDialog.Builder optionMenu(Activity activity, String title, DialogInterface.OnClickListener onClick, String... menuOptions){
        ArrayList<String> menulist = new ArrayList<>();
        for(String option : menuOptions)
            menulist.add(option);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, menulist);
        builder
                .setTitle(title)
                .setAdapter(adapter, onClick)
                .setNegativeButton(activity.getResources().getString(R.string.btn_cancel), null);

        return builder;
    }


}
