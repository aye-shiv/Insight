package com.trivtech.insight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.TooltipCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.mediapipe.solutioncore.CameraInput;

public class Animation {

    public static class Translate {

        private static class ResizeAnimation extends android.view.animation.Animation {
            private View mView;
            private float mToHeight;
            private float mFromHeight;

            private float mToWidth;
            private float mFromWidth;

            public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
                mToHeight = toHeight;
                mToWidth = toWidth;
                mFromHeight = fromHeight;
                mFromWidth = fromWidth;
                mView = v;
                setDuration(300);
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                float height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
                float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
                ViewGroup.LayoutParams p = mView.getLayoutParams();
                p.height = (int) height;
                p.width = (int) width;
                mView.requestLayout();
            }
        }

        public static void displayMessage(TextView chat, String message){
            TextView temp = new TextView(chat.getContext());
            temp.setLayoutParams(chat.getLayoutParams());
            temp.setText(message);
            //temp.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            temp.measure(View.MeasureSpec.makeMeasureSpec(((View) chat.getParent()).getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(((View) chat.getParent()).getHeight(), View.MeasureSpec.EXACTLY));
            ResizeAnimation animation = new ResizeAnimation(chat, chat.getWidth(), chat.getHeight(), temp.getMeasuredWidth(), temp.getMeasuredHeight());
            animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @Override public void onAnimationStart(android.view.animation.Animation animation) { }
                @Override public void onAnimationRepeat(android.view.animation.Animation animation) { }
                @Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    chat.setText(message);
                }
            });
            chat.startAnimation(animation);
            /*
            int duration = 400;
            TextView temp = new TextView(chat.getContext());
            temp.setLayoutParams(chat.getLayoutParams());
            temp.setText(message);
            temp.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            final int targetWidth = temp.getMeasuredWidth();
            final int targetHeight = temp.getMeasuredHeight();

            ValueAnimator animateWidth = ValueAnimator.ofInt(chat.getMeasuredWidth(), targetWidth);
            animateWidth.setInterpolator(new AccelerateDecelerateInterpolator());
            animateWidth.setDuration(duration);
            animateWidth.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    chat.setText(message);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) chat.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                    chat.setLayoutParams(params);
                }
            });
            animateWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int val = (Integer) animation.getAnimatedValue();

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) chat.getLayoutParams();
                    params.width = val;
                    chat.setLayoutParams(params);
                }
            });


            ValueAnimator animateHeight = ValueAnimator.ofInt(chat.getMeasuredHeight(), targetHeight);
            animateHeight.setInterpolator(new AccelerateDecelerateInterpolator());
            animateHeight.setDuration(duration);
            animateHeight.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) chat.getLayoutParams();
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                    chat.setLayoutParams(params);
                }
            });
            animateWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int val = (Integer) animation.getAnimatedValue();

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) chat.getLayoutParams();
                    params.height = val;
                    chat.setLayoutParams(params);
                }
            });

            animateWidth.start();
            animateHeight.start();
            */
        }

        public static void flipCam(LottieAnimationView cam, CameraInput.CameraFacing facing){
            if(facing == CameraInput.CameraFacing.BACK){
                cam.removeAllAnimatorListeners();
                cam.cancelAnimation();
                cam.setMinAndMaxFrame(59, 80);
                cam.setSpeed(1f);
                cam.setRepeatCount(0);
                TooltipCompat.setTooltipText(cam, "Switch to front Camera");
            } else {
                cam.removeAllAnimatorListeners();
                cam.cancelAnimation();
                cam.setMinAndMaxFrame(130, 150);
                cam.setSpeed(1f);
                cam.setRepeatCount(0);
                TooltipCompat.setTooltipText(cam, "Switch to back Camera");
            }
            cam.playAnimation();
        }

        public static void TTSBtn(LottieAnimationView btn, boolean enable){
            if(enable){
                btn.removeAllAnimatorListeners();
                btn.cancelAnimation();
                btn.setMinAndMaxFrame(42, 82);
                btn.setSpeed(1f);
                btn.setRepeatCount(0);
                TooltipCompat.setTooltipText(btn, "Turn off Text-To-Speech");
            } else {
                btn.removeAllAnimatorListeners();
                btn.cancelAnimation();
                btn.setMinAndMaxFrame(0, 41);
                btn.setSpeed(1f);
                btn.setRepeatCount(0);
                TooltipCompat.setTooltipText(btn, "Turn on Text-To-Speech");
            }
            btn.playAnimation();
        }
    }
}
