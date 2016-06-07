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


    public static ViewPropertyAnimator anim_fadeIn(View v, int dur, int delay){
        v.animate().alpha(0);
        return v.animate()
                .alpha(1)
                .setStartDelay(delay)
                .setDuration(dur);
    }

    public static ViewPropertyAnimator anim_fadeOut(View v, int dur, int delay){
        v.animate().alpha(1);
        return v.animate()
                .alpha(0)
                .setStartDelay(delay)
                .setDuration(dur);
    }

    public static ViewPropertyAnimator anim_remove(Context context, final View v){
        return anim_fadeOut(v,
                context.getResources().getInteger(R.integer.update_fade_duration),
                context.getResources().getInteger(R.integer.update_fade_startdelay));
    }

    public static ViewPropertyAnimator anim_add(final Context context, final View v){
        return anim_fadeIn(v,
                context.getResources().getInteger(R.integer.update_fade_duration),
                context.getResources().getInteger(R.integer.update_fade_startdelay));
    }

    public static ViewPropertyAnimator fadeInOut(final View v, final int dur, final int delay, final int durOutfaded, final Runnable onOutfaded){
        final int halfdur = dur / 2;
        return anim_fadeOut(v, halfdur, delay)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if(onOutfaded != null)
                            onOutfaded.run();

                        anim_fadeIn(v, halfdur, durOutfaded)
                                .start();
                    }
                });
    }

    public final static int DIRECTION_RIGHT     = 0;
    public final static int DIRECTION_LEFT      = 1;

    public static ViewPropertyAnimator reganRoll(final Context context, final View v, int direction, final Runnable betweenAnimationAction){
        final float[] xMov = direction == DIRECTION_LEFT ?
                new float[]{-400f, 400f}:
                new float[]{400f, -400f};

        return v.animate()
                .x(xMov[0])
                .alpha(0)
                .setDuration(context.getResources().getInteger(R.integer.swipe_movement_duration))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        betweenAnimationAction.run();

                        v.animate()
                                .x(xMov[1])
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
