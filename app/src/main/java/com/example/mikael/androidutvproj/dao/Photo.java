

package com.example.mikael.androidutvproj.dao;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.mikael.androidutvproj.PhotoFile;
import com.example.mikael.androidutvproj.tool.RandomString;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Date;

/**
 * @author Mikael Holmbom - miho1202
 * @version 1.0
 * class used for holding information about a Photo
 * containing information :
 *	- content description
 *	- destination of photo
 *	- date photo was taken
 */
public class Photo extends ChildDataAccessObject{


    /**
     * this photo file
     */
    private String mPhotoFilePath;

    /**
     * description of the content of this photo
     * */
    private String  mDescription;
    /**
     * destination of this photo
     */
    private LatLng  mLatLng = null;
    /**
     * date this photo was taken
     */
    private Date    mDate = null;


    public Photo(){
        super("photo-"+ RandomString.getRandomAlphaString(20));
    }

    public Photo(String id){
        super(id);
    }

    public Photo(String id, File photoFile) {
        super(id);
        mPhotoFilePath = photoFile.getAbsolutePath();
    }

    public Photo(File photoFile){
        super("photo-"+RandomString.getRandomAlphaString(20));
        mPhotoFilePath = photoFile.getAbsolutePath();
    }
    

    /**
     * std ctor
     * @param photofile file containing photo
     * @param description description of this photo
     * @param latlng destination of this photo
     * @param date date this photo was taken
     */
    public Photo(File photofile, String description, LatLng latlng, Date date){
        super("photo-"+RandomString.getRandomAlphaString(20));

        mPhotoFilePath  = photofile.getAbsolutePath();
        mDescription    = description;
        mLatLng         = latlng;
        mDate           = date;
    }


    /**
     * get this photofile
     * @return  photofile
     */
    public Bitmap getPhotoBitmap(){
        return BitmapFactory.decodeFile(mPhotoFilePath);
    }

    /**
     * get this photofile
     * @return  photofile
     */
    public PhotoFile getPhotoFile(){

        PhotoFile f = new PhotoFile(mPhotoFilePath);
        if(f.exists())
            return f;
        else
            return null;

    }

    public String getPhotoPath(){
        return mPhotoFilePath;
    }

    /**
     * get this description
     * @return description
     */
    public String getDescription(){
        return mDescription;
    }
    /**
     * gets this LatLng where this photo were taken
     * @return LatLng of this photo
     */
    public LatLng getLatLng(){
        return mLatLng;
    }

    /**
     * get this date
     * @return this date of photo
     */
    public Date getDate(){
        return mDate;
    }

    public Photo setPhotoFile(File f){
        mPhotoFilePath = f.getAbsolutePath();
        return this;
    }

    public Photo setPhotoFile(String filepath){
        mPhotoFilePath = filepath;
        return this;
    }

    public Photo setDescription(String description){
        mDescription = description;
        return this;
    }

    public Photo setLatLng(LatLng latLng){
        mLatLng = latLng;
        return this;
    }

    public Photo setDate(Date d){
        mDate = d;
        return this;
    }

    public Photo setThumbNail(Bitmap thumbnail){
        mThumbnail = thumbnail;
        return this;
    }

    public String toString(){
        return mPhotoFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Photo)
            return getId().equals(((Photo) o).getId());

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);

        parcel.writeString(getDescription());
        if (getPhotoFile() != null){
            parcel.writeString(getPhotoFile().getAbsolutePath());
        }

        if(getDate() != null)
            parcel.writeLong(getDate().getTime());
        else
            parcel.writeLong(0);

        parcel.writeTypedObject(getLatLng(), 0);

    }

    protected Photo(Parcel in){
        super(in);

        setDescription(in.readString());
        setPhotoFile(new File(in.readString()));

        long datetime = in.readLong();
        if(datetime > 0)
            setDate(new Date(datetime));

        LatLng latLng = in.readTypedObject(LatLng.CREATOR);
        setLatLng(latLng);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };


    /**
     * generate a file with unique filename within directory param, file is created in directory and returned
     * @param directory what directory to add file
     * @return created file
     */
    private static File getNewFile(Activity activity, String directory){
        int nExtra = 0;
        File f;
        while(true) {
            String fileNum = String.format("%03d", DataMapper.getCurrentRealEstate().getPhotos().size() + 1 + nExtra);
            String filename = String.format("img_%s_%s_%s.jpg",
                    activity.getString(R.string.app_name),
                    DataMapper.getCurrentRealEstate().toString(),
                    fileNum);

            f = new File(directory, filename);

            if(f.exists())
                nExtra++;
            else
                return f;
        }
    }

    /**
     * Save Bitmap to file
     * @param bm
     * @return
     */
    public static File saveToFile(Activity activity, Bitmap bm){
        String stdImgDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        String imgDir = activity.getSharedPreferences(Settings.SHAREDPREFKEY_SETTINGS, activity.MODE_PRIVATE).getString(Settings.SHAREDPREFKEY_PHOTOSOURCE, stdImgDir);

        File f = getNewFile(activity, imgDir);
        try{
            if(f.createNewFile()){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                return f;
            }

        } catch(Exception e){
            Log.e("error", "exception : " + e.getMessage());
        }

        return null;
    }

    @Override
    public String getLabel() {
        return getPhotoFile().getAbsolutePath();
    }

    @Override
    public Photo clone() {
        Photo clone = new Photo(getId())
                .setDate(           getDate())
                .setLatLng(         getLatLng())
                .setPhotoFile(      getPhotoFile())
                .setDescription(    getDescription());

        return clone;
    }
}
