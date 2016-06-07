package com.example.mikael.androidutvproj.details;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mikael.androidutvproj.OnSwipeListener;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.anim.Animators;

/**
 * displays ImageView, uses callback methods on swipe left or right.<br>
 *     Calling activity must implement {@link ImageNavigatorListener}
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class ImageNavigatorFragment extends DialogFragment {


    public interface ImageNavigatorListener  {

        /**
         * called between animations when user navigates left
         *     navigate left implies decreasing index of items
         */
        void onNavigateLeft();
        /**
         * called between animations when user navigates right<br>
         *     navigate right implies increasing index of items
         */
        void onNavigateRight();

    }

    /**
     * main setViewAnimated image
     */
    private ImageView mImageDisplay;
    /**
     * button used to navigate left
     */
    private ImageButton mBtn_nav_left;
    /**
     * button used to navigate right
     */
    private ImageButton mBtn_nav_right;

    private boolean mNavigateEnabled = false;

    private OnSwipeListener mOnSwipeListener = new OnSwipeListener(getActivity()) {

        @Override
        public void onSwipeRight() {
            if (navigateEnabled())
                navigateRight();
        }

        @Override
        public void onSwipeLeft() {
            if (navigateEnabled())
                navigateLeft();
        }

    };


    /**
     * calling activity
     */
    private ImageNavigatorListener mImageNavigatorListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (context instanceof Activity)
                mImageNavigatorListener = (ImageNavigatorListener) context;

        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement " + ImageNavigatorListener.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.layout_image_navigator, container);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init_navigation();
        mImageDisplay = (ImageView) getView().findViewById(R.id.imagedisplay);
    }

    private float getResourceFloat(int res){
        return Float.parseFloat(getResources().getString(res));
    }

    /**
     * initialize this navigation controllers: Swipe, Buttons
     */
    private void init_navigation(){

        // NAVIGATION SWIPE
        ///////////////////////////

        FrameLayout rootLayout = (FrameLayout) getView().findViewById(R.id.root);
        rootLayout.setOnTouchListener(mOnSwipeListener);

        // NAVIGATION BUTTONS
        ////////////////////////////

        mBtn_nav_left = (ImageButton) getView().findViewById(R.id.btn_navigate_left);
        mBtn_nav_left.setAlpha(0f);
        mBtn_nav_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigateEnabled())
                    navigateRight();
            }
        });

        mBtn_nav_right = (ImageButton) getView().findViewById(R.id.btn_navigate_right);
        mBtn_nav_right.setAlpha(0f);
        mBtn_nav_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigateEnabled())
                    navigateLeft();
            }
        });

    }

    public boolean navigateEnabled(){
        return mNavigateEnabled;
    }

    public void setNavigateEnabled(boolean enabled){
        mNavigateEnabled = enabled;

        int alphaRes = navigateEnabled() ? R.string.navigation_btn_alpha : R.string.navigation_btn_alpha_disabled;
        float alphaVal = getResourceFloat(alphaRes);

        mBtn_nav_left.setAlpha(alphaVal);
        mBtn_nav_right.setAlpha(alphaVal);
    }

    /**
     * navigate this setViewAnimated to the left<br>
     * animations is run and calls callback method in {@link ImageNavigatorListener}
     */
    public void navigateLeft(){
        Animators.reganRoll(getActivity(), mImageDisplay, Animators.DIRECTION_LEFT, new Runnable() {
            @Override
            public void run() {
                mImageNavigatorListener.onNavigateRight();
            }
        }).start();
    }

    /**
     * navigate this setViewAnimated to the right<br>
     * animations is run and calls callback method in {@link ImageNavigatorListener}
     */
    public void navigateRight(){
        Animators.reganRoll(getActivity(), mImageDisplay, Animators.DIRECTION_RIGHT, new Runnable() {
            @Override
            public void run() {
                mImageNavigatorListener.onNavigateLeft();
            }
        }).start();
    }

    public ViewPropertyAnimator getNavigateLeftAnimation(Runnable postAnim){
        return Animators.reganRoll(getActivity(), mImageDisplay, Animators.DIRECTION_RIGHT, postAnim);
    }
    public ViewPropertyAnimator getNavigateRightAnimation(Runnable postAnim){
        return Animators.reganRoll(getActivity(), mImageDisplay, Animators.DIRECTION_LEFT, postAnim);
    }

    /**
     * setViewAnimated bitmap
     * @param bitmap
     */
    public void display(Bitmap bitmap){
        mImageDisplay.setImageBitmap(bitmap);
    }

    /**
     * setViewAnimated image from resource
     * @param resId
     */
    public void display(int resId){
        mImageDisplay.setImageResource(resId);
    }


}
