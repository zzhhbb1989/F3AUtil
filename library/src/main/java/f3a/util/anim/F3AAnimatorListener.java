package f3a.util.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

/**
 * @author Bob
 * @since 2018-08-02
 */
public abstract class F3AAnimatorListener extends AnimatorListenerAdapter {
    
    boolean isCancelled = false;
    
    @Override
    public final void onAnimationCancel(Animator animation) {
        isCancelled = true;
        onAnimCancel(animation);
    }
    
    public void onAnimCancel(Animator animator) {}
    
    @Override
    public final void onAnimationStart(Animator animation) {
        isCancelled = false;
        onAnimStart(animation);
    }
    
    public void onAnimStart(Animator animator) {}
    
    @Override
    public final void onAnimationEnd(Animator animation) {
        onAnimEnd(animation, isCancelled);
    }
    
    public void onAnimEnd(Animator animation, boolean isCancelled) {}
}
