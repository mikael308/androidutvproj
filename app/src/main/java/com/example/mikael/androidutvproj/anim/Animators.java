package com.example.mikael.androidutvproj.anim;

import android.content.Context;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.example.mikael.androidutvproj.R;

/**
 *
 *
 * @author Mikael Holmbom
 * @version 1.0
 * @since 2016-04-12
 */
public class Animators {

    public static ViewPropertyAnimator anim_remove(Context context, View v){
        return v.animate()
                .alpha(0)
                .setStartDelay(context.getResources().getInteger(R.integer.update_fade_startdelay))
                .setDuration(context.getResources().getInteger(R.integer.update_fade_duration));
    }
    public static ViewPropertyAnimator anim_add(Context context, View v){
        return v.animate()
                .alpha(1)
                .setStartDelay(context.getResources().getInteger(R.integer.update_fade_startdelay))
                .setDuration(context.getResources().getInteger(R.integer.update_fade_duration));
    }

    public final static int DIRECTION_RIGHT = 0;
    public final static int DIRECTION_LEFT = 1;

    public static ViewPropertyAnimator reganRoll(final Context context, final View v, int direction, final Runnable betweenAnimationAction){
        final float[] xVals = direction == DIRECTION_LEFT ?
                new float[]{-400f, 400f}:
                new float[]{400f, -400f};

        return v.animate()
                .x(xVals[0])
                .alpha(0)
                .setDuration(context.getResources().getInteger(R.integer.swipe_movement_duration))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        betweenAnimationAction.run();

                        v.animate()
                                .x(xVals[1])
                                .setDuration(0)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        v.animate()
                                                .x(0f)
                                                .alpha(1)
                                                .setDuration(context.getResources().getInteger(R.integer.swipe_movement_duration)).start();
                                    }
                                }).start();

                    }
                });
    }

}
