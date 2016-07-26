package euphony.com.euphony;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by pR0 on 10-06-2016.
 */
public class ResizeAnimation extends Animation {

    private int startWidth;
    private int startHeight;
    private int endHeight;

    private int deltaWidth;
    private int deltaHeight;// distance between start and end height
    private View view;
    private  String type;

    /**
     * constructor, do not forget to use the setParams(int, int) method before
     * starting the animation
     * @param v
     */
    public ResizeAnimation (View v) {
        this.view = v;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

            view.getLayoutParams().width = (int) (startWidth + deltaWidth * interpolatedTime);
            view.getLayoutParams().height = (int) (startHeight + deltaHeight *interpolatedTime);
            view.requestLayout();
    }

    /**
     * set the starting and ending height for the resize animation
     * starting height is usually the views current height, the end height is the height
     * we want to reach after the animation is completed
     * @param startHeight height in pixels
     * @param endHeight height in pixels
     * @param endWidth width in pixels
     * @param startWidth width in pixels
     */
    public void setParams(int startHeight, int endHeight, int startWidth, int endWidth) {

        this.startWidth = startWidth;
        deltaWidth = endWidth - startWidth;

        this.startHeight = startHeight;
        deltaHeight = endHeight - startHeight;
    }

    /**
     * set the duration for the hideshowanimation
     */
    @Override
    public void setDuration(long durationMillis) {
        super.setDuration(durationMillis);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}