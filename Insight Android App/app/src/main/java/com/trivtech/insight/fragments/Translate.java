package com.trivtech.insight.fragments;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.hands.HandLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;
import com.trivtech.insight.R;
import com.trivtech.insight.activities.MainActivity;
import com.trivtech.insight.misc.Interpreter;
import com.trivtech.insight.misc.OnSwipeTouchListener;
import com.trivtech.insight.util.Animation;
import com.trivtech.insight.util.TextToSpeech;
import com.trivtech.insight.util.Utils;
import com.trivtech.insight.views.HandsResultGlRenderer;
import com.trivtech.insight.views.HandsResultImageView;


public class Translate extends Fragment {
    public static String logTag = "Fragment - Translate";
    public static boolean active = false;

    /*========  =========*/

    private Hands hands;
    // Run the pipeline and the model inference on GPU or CPU.
    private static final boolean RUN_ON_GPU = false;
    private static CameraInput.CameraFacing cameraFacing = CameraInput.CameraFacing.BACK;

    private enum InputSource {
        UNKNOWN,
        IMAGE,
        VIDEO,
        CAMERA,
    }
    private InputSource inputSource = InputSource.UNKNOWN;

    private HandsResultImageView imageView;
    // Live camera demo UI and camera components.
    private CameraInput cameraInput;

    private SolutionGlSurfaceView<HandsResult> glSurfaceView;

    /*======== Views =========*/

    View fragView;

    LottieAnimationView translateIcon;
    TextView translateSign;
    TextView messageBubble;
    String messageBubbleText = "";
    
    LottieAnimationView flipCamBtn;
    LottieAnimationView ttsBtn;

    //
    TextToSpeech ttsSystem;

    Handler checker = new Handler();
    Interpreter interpreter;

    /*======== Checks =========*/
    private boolean showTranslateSign = false;
    private boolean iconAnimate = false;



    public Translate() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interpreter = new Interpreter();
        ttsSystem = new TextToSpeech(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageView = new HandsResultImageView(requireContext());

        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        fragView = view;

        translateSign = fragView.findViewById(R.id.translateSign);
        translateIcon = fragView.findViewById(R.id.translateIcon);
        messageBubble = fragView.findViewById(R.id.messageBubble);

        ViewGroup t = fragView.findViewById(R.id.translate_container);
        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGING);
        t.setLayoutTransition(lt);
        t.getLayoutTransition().setAnimateParentHierarchy(false);


        flipCamBtn = fragView.findViewById(R.id.flipCameraBtn);
        ttsBtn = fragView.findViewById(R.id.ttsBtn);

        updateLayout();
        setupButtons();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        if (inputSource == InputSource.CAMERA) {
            return;
        }
        stopCurrentPipeline();
        setupStreamingModePipeline(InputSource.CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();
        active = true;
        if(!MainActivity.checkCameraPermission(requireContext())){
            MainActivity.requestCameraPermission(requireActivity());
        }

        if (inputSource == InputSource.CAMERA) {
            // Restarts the camera and the opengl surface rendering.
            cameraInput = new CameraInput(requireActivity());
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
            glSurfaceView.post(this::startCamera);
            glSurfaceView.setVisibility(View.VISIBLE);
        }
        interpreter.open(requireContext());

        int delay = 300;
        checker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!active)
                    return;
                updateAnimation();
                checker.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    public void onPause() {
        super.onPause();
        active = false;
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.setVisibility(View.GONE);
            cameraInput.close();
        }
        interpreter.close();

    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        active = false;
    }

    /*======== UI Stuff =========*/


    public void updateAnimation(){
        if(!messageBubbleText.isEmpty() ){
            if(messageBubble.getVisibility() != View.VISIBLE)
                messageBubble.setVisibility(View.VISIBLE);
        } else if(messageBubble.getVisibility() != View.GONE) {
            messageBubble.setVisibility(View.GONE);
        }

        if(showTranslateSign){
            if(translateSign.getVisibility() != View.VISIBLE)
                translateSign.setVisibility(View.VISIBLE);
        } else if(translateSign.getVisibility() != View.GONE) {
            translateSign.setVisibility(View.GONE);
            translateSign.setText("");
        }
    }

    public void updateLayout(){

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) messageBubble.getLayoutParams();
        params.setMargins(0, 0, 0, Utils.getScreenHeightPercent(.01));
        params.matchConstraintMaxHeight = Utils.getScreenWidthPercent(.40); //40%
        params.matchConstraintMaxWidth = Utils.getScreenWidthPercent(.95); //95%
        messageBubble.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) translateSign.getLayoutParams();
        params.setMargins(0, Utils.getScreenHeightPercent(.01), 0, 0);
        translateSign.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) translateIcon.getLayoutParams();
        params.height = Utils.getScreenWidthPercent(.25); //25%
        params.width = Utils.getScreenWidthPercent(.25); //25%
        translateIcon.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) flipCamBtn.getLayoutParams();
        params.setMargins(0, 0, Utils.getScreenWidthPercent(.1), 0);
        params.setMarginEnd(Utils.getScreenWidthPercent(.1));
        params.height = Utils.getScreenWidthPercent(.15);
        params.width = Utils.getScreenWidthPercent(.15);
        flipCamBtn.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) ttsBtn.getLayoutParams();
        params.setMargins(Utils.getScreenWidthPercent(.1), 0, 0, 0);
        params.setMarginStart(Utils.getScreenWidthPercent(.1));
        params.height = Utils.getScreenWidthPercent(.15);
        params.width = Utils.getScreenWidthPercent(.15);
        ttsBtn.setLayoutParams(params);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupButtons(){

        //Middle Icon
        translateIcon.removeAllAnimatorListeners();
        translateIcon.cancelAnimation();
        translateIcon.setMinAndMaxFrame(33, 77);
        translateIcon.setSpeed(1f);
        translateIcon.addAnimatorListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) {

            }
            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) {
                if(!iconAnimate) {
                    translateIcon.pauseAnimation();
                    showTranslateSign = false;
                    //translateSign.setText("•••");
                }
            }
        });
        translateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!translateIcon.isAnimating())
                    translateIcon.resumeAnimation();
                appendText();
            }
        });

        flipCamBtn.setOnClickListener(v -> {

            if(cameraFacing == CameraInput.CameraFacing.BACK)
                cameraFacing = CameraInput.CameraFacing.FRONT;
            else cameraFacing = CameraInput.CameraFacing.BACK;
            Animation.Translate.flipCam(flipCamBtn, cameraFacing);
            stopCurrentPipeline();
            setupStreamingModePipeline(InputSource.CAMERA);
        });

        ttsBtn.setOnClickListener(v -> {
            ttsSystem.toggle();
            Animation.Translate.TTSBtn(ttsBtn, !ttsSystem.isDisabled());
        });


        messageBubble.setOnTouchListener(new OnSwipeTouchListener(requireContext()) {
            @Override
            public void onSwipeTop() {
                if(messageBubble != null && !messageBubbleText.isEmpty())
                    messageBubbleText = messageBubbleText.substring(0, messageBubbleText.length()-1);
                messageBubble.setText(messageBubbleText);

            }

            @Override
            public void onSwipeBottom() {
                messageBubbleText = "";
                messageBubble.setText(messageBubbleText);
            }

        });

        messageBubble.setOnClickListener(v -> {
            ttsSystem.speak(messageBubble.getText().toString(), true);
        });

        translateSign.setOnClickListener(v -> {
            appendText();
        });

        TooltipCompat.setTooltipText(messageBubble, "Message Bubble");
        TooltipCompat.setTooltipText(translateSign, "Translated Sign");
        TooltipCompat.setTooltipText(translateIcon, "InSight");
        TooltipCompat.setTooltipText(flipCamBtn, "");
        TooltipCompat.setTooltipText(ttsBtn, "Toggle Text-to-Speech");
        Animation.Translate.flipCam(flipCamBtn, cameraFacing);
        Animation.Translate.TTSBtn(ttsBtn, !ttsSystem.isDisabled());
    }

    public void appendText(){
        String text = translateSign.getText().toString();
        if(text.isEmpty() && !messageBubbleText.isEmpty())
            text = " ";
        else if(text.isEmpty() || text.equals("•••"))
            return;
        messageBubbleText += text;
        messageBubble.setText(messageBubbleText);
        ttsSystem.speak(text, true);
    }

    /*======== Mediapipe Stuff =========*/

    private void startCamera() {
        cameraInput.start(
                requireActivity(),
                hands.getGlContext(),
                cameraFacing,
                glSurfaceView.getWidth(),
                glSurfaceView.getHeight()
        );

    }


    private void stopCurrentPipeline() {
        if (cameraInput != null) {
            cameraInput.setNewFrameListener(null);
            cameraInput.close();
        }
        if (glSurfaceView != null) {
            glSurfaceView.setVisibility(View.GONE);
        }
        if (hands != null) {
            hands.close();
        }
    }


    /** Sets up core workflow for streaming mode. */
    private void setupStreamingModePipeline(InputSource inputSource) {
        this.inputSource = inputSource;
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands = new Hands(
                        requireContext(),
                        HandsOptions.builder()
                                .setStaticImageMode(false)
                                .setMaxNumHands(1)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build()
        );
        hands.setErrorListener((message, e) -> Log.e(logTag, "MediaPipe Hands error:" + message));

        if (inputSource == InputSource.CAMERA) {
            cameraInput = new CameraInput(requireActivity());
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
        }

        // Initializes a new Gl surface view with a user-defined HandsResultGlRenderer.
        glSurfaceView = new SolutionGlSurfaceView<>(requireContext(), hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new HandsResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);
        hands.setResultListener(
            handsResult -> {
                if(interpreter.feed(handsResult)){ //Results are not empty
                    iconAnimate = true;
                    requireActivity().runOnUiThread(() -> {
                        if(!translateIcon.isAnimating()) { // Start animation when
                            translateIcon.resumeAnimation();
                        }
                        if(interpreter.checkerDelayPhase && !interpreter.matchFoundDelayPhase)
                            translateSign.setText("•••");
                    });
                } else {
                    iconAnimate = false;
                }

                if(interpreter.matchFound){
                    showTranslateSign = true;
                    interpreter.matchFound = false;

                    requireActivity().runOnUiThread(() -> {
                        translateSign.setText(interpreter.prediction);
                        interpreter.match = "";
                    });

                }

                logWristLandmark(handsResult, false);
                glSurfaceView.setRenderData(handsResult);
                glSurfaceView.requestRender();
            }
        );

        // The runnable to start camera after the gl surface view is attached.
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.post(this::startCamera);
        }

        translateIcon.bringToFront();
        messageBubble.bringToFront();
        translateSign.bringToFront();

        flipCamBtn.bringToFront();
        ttsBtn.bringToFront();

        // Updates the preview layout.
        FrameLayout frameLayout = fragView.findViewById(R.id.preview_display_layout);
        imageView.setVisibility(View.GONE);
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(glSurfaceView);
        glSurfaceView.setVisibility(View.VISIBLE);
        frameLayout.requestLayout();

    }

    private void logWristLandmark(HandsResult result, boolean showPixelValues) {
        if (result.multiHandLandmarks().isEmpty()) {
            return;
        }

        LandmarkProto.NormalizedLandmark wristLandmark =
                result.multiHandLandmarks().get(0).getLandmarkList().get(HandLandmark.WRIST);

        if (showPixelValues) {
            int width = result.inputBitmap().getWidth();
            int height = result.inputBitmap().getHeight();
            Log.i(
                    logTag,
                    String.format(
                            "MediaPipe Hand wrist coordinates (pixel values): x=%f, y=%f",
                            wristLandmark.getX() * width, wristLandmark.getY() * height));

        } else {
            Log.i(
                    logTag,
                    String.format(
                            "MediaPipe Hand wrist normalized coordinates (value range: [0, 1]): x=%f, y=%f",
                            wristLandmark.getX(), wristLandmark.getY()));

        }

    }

}