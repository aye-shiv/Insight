package com.trivtech.insight.util;

import androidx.appcompat.widget.TooltipCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.mediapipe.solutioncore.CameraInput;

public class Animation {

    public static class Translate {

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
