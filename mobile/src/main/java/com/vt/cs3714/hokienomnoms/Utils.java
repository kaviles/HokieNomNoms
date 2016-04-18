package com.vt.cs3714.hokienomnoms;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Kelvin on 4/17/16.
 */
public class Utils {

    public void SlideUp(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_down));
    }

    public void SlideDown(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_up));
    }
}