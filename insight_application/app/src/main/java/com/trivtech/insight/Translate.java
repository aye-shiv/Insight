package com.trivtech.insight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutioncore.VideoInput;
import com.google.mediapipe.solutions.hands.HandLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Locale;


public class Translate extends Fragment {
    public static String logTag = "Fragment - Translate";

    //======== Views =========

    View fragView;

    LottieAnimationView translateIcon;
    TextView translateMessage;
    
    LottieAnimationView flipCamBtn;
    LottieAnimationView ttsBtn;

    //
    TextToSpeech ttsSystem;

    //========  =========

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

    // Image demo UI and image loader components.
    private ActivityResultLauncher<Intent> imageGetter;
    private HandsResultImageView imageView;
    // Video demo UI and video loader components.
    private VideoInput videoInput;
    private ActivityResultLauncher<Intent> videoGetter;
    // Live camera demo UI and camera components.
    private CameraInput cameraInput;

    private SolutionGlSurfaceView<HandsResult> glSurfaceView;

    //============================


    private boolean iconAnimate = false;


    public Translate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        fragView = view;

        translateIcon = fragView.findViewById(R.id.translateIcon);
        translateMessage = fragView.findViewById(R.id.translateMessage);

        ViewGroup t = fragView.findViewById(R.id.translate_container);
        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGING);
        t.setLayoutTransition(lt);

        flipCamBtn = fragView.findViewById(R.id.flipCameraBtn);
        ttsBtn = fragView.findViewById(R.id.ttsBtn);

        updateLayout();
        setupButtons();
        setupStaticImageDemoUiComponents();
        setupVideoDemoUiComponents();
        setupLiveDemoUiComponents();
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsSystem = new TextToSpeech(requireContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        //Animation.Translate.flipCam(flipCamBtn, cameraFacing);
        //Animation.Translate.TTSBtn(ttsBtn, !ttsSystem.isDisabled());

        if (inputSource == InputSource.CAMERA) {
            return;
        }
        stopCurrentPipeline();
        setupStreamingModePipeline(InputSource.CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (inputSource == InputSource.CAMERA) {
            // Restarts the camera and the opengl surface rendering.
            cameraInput = new CameraInput(requireActivity());
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
            glSurfaceView.post(this::startCamera);
            glSurfaceView.setVisibility(View.VISIBLE);
        } else if (inputSource == InputSource.VIDEO) {
            videoInput.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.setVisibility(View.GONE);
            cameraInput.close();
        } else if (inputSource == InputSource.VIDEO) {
            videoInput.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    public void updateLayout(){

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) translateMessage.getLayoutParams();
        params.setMargins(0, 0, 0, Utils.getScreenHeightPercent(.01));
        params.matchConstraintMaxHeight = Utils.getScreenWidthPercent(.40); //40%
        params.matchConstraintMaxWidth = Utils.getScreenWidthPercent(.95); //95%
        translateMessage.setLayoutParams(params);

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
                    translateMessage.setVisibility(View.GONE);
                    translateIcon.pauseAnimation();
                }
            }
        });
        translateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!translateIcon.isAnimating())
                    translateIcon.resumeAnimation();
            }
        });

        flipCamBtn.setOnClickListener(v -> {

            if(cameraFacing == CameraInput.CameraFacing.BACK)
                cameraFacing = CameraInput.CameraFacing.FRONT;
            else cameraFacing = CameraInput.CameraFacing.BACK;
            Animation.Translate.flipCam(flipCamBtn, cameraFacing);
            stopCurrentPipeline();
            setupStreamingModePipeline(InputSource.CAMERA);
            //
        });

        ttsBtn.setOnClickListener(v -> {
            //Toast.makeText(requireContext(), "Toggle Text-To-Speech", Toast.LENGTH_SHORT).show();

            ttsSystem.toggle();
            Animation.Translate.TTSBtn(ttsBtn, !ttsSystem.isDisabled());

        });

        translateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = Utils.getRandomText();

                //
                String[] line = new String[] { "hello", "goodbye", "hi", "ok", "", "a", "b", "c", "d", "e", "f", "g", "h", "i", "o" };
                text = line[Utils.getRandom(line.length-1)];
                //

                translateMessage.setText(text);
                ttsSystem.speak(text, true);
            }
        });


        TooltipCompat.setTooltipText(translateIcon, "InSight");
        TooltipCompat.setTooltipText(flipCamBtn, "");
        TooltipCompat.setTooltipText(ttsBtn, "");
        Animation.Translate.flipCam(flipCamBtn, cameraFacing);
        Animation.Translate.TTSBtn(ttsBtn, !ttsSystem.isDisabled());
    }


    //====================================================

    private void startCamera() {
        cameraInput.start(
                requireActivity(),
                hands.getGlContext(),
                cameraFacing,
                glSurfaceView.getWidth(),
                glSurfaceView.getHeight()
        );
    }


    /** Sets up the UI components for the static image demo. */
    private void setupStaticImageDemoUiComponents() {
        // The Intent to access gallery and read images as bitmap.
        imageGetter =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap =
                                                downscaleBitmap(
                                                        MediaStore.Images.Media.getBitmap(
                                                                requireContext().getContentResolver(), resultIntent.getData()));
                                    } catch (IOException e) {
                                        Log.e(logTag, "Bitmap reading error:" + e);
                                    }
                                    try {
                                        InputStream imageData =
                                                requireContext().getContentResolver().openInputStream(resultIntent.getData());
                                        bitmap = rotateBitmap(bitmap, imageData);
                                    } catch (IOException e) {
                                        Log.e(logTag, "Bitmap rotation error:" + e);
                                    }
                                    if (bitmap != null) {
                                        hands.send(bitmap);
                                    }
                                }
                            }
                        });

        /*
        Button loadImageButton = fragView.findViewById(R.id.button_load_picture);
        loadImageButton.setOnClickListener(
                v -> {
                    cameraFacing = CameraInput.CameraFacing.FRONT;
                    if (inputSource != InputSource.IMAGE) {
                        stopCurrentPipeline();
                        setupStaticImageModePipeline();
                    }
                    // Reads images from gallery.
                    Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                    pickImageIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                    imageGetter.launch(pickImageIntent);
                });

        */
        imageView = new HandsResultImageView(requireContext());
    }

    /** Sets up core workflow for static image mode. */
    private void setupStaticImageModePipeline() {
        this.inputSource = InputSource.IMAGE;
        // Initializes a new MediaPipe Hands solution instance in the static image mode.
        hands =
                new Hands(
                        requireContext(),
                        HandsOptions.builder()
                                .setStaticImageMode(true)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build()
                );

        // Connects MediaPipe Hands solution to the user-defined HandsResultImageView.
        hands.setResultListener(
                handsResult -> {
                    logWristLandmark(handsResult, /*showPixelValues=*/ true);
                    imageView.setHandsResult(handsResult);
                    requireActivity().runOnUiThread(() -> imageView.update());
                });
        hands.setErrorListener((message, e) -> Log.e(logTag, "MediaPipe Hands error:" + message));

        // Updates the preview layout.
        FrameLayout frameLayout = fragView.findViewById(R.id.preview_display_layout);
        frameLayout.removeAllViewsInLayout();
        imageView.setImageDrawable(null);
        frameLayout.addView(imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    /** Sets up the UI components for the video demo. */
    private void setupVideoDemoUiComponents() {
        // The Intent to access gallery and read a video file.
        videoGetter = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    glSurfaceView.post(
                                            () ->
                                                    videoInput.start(
                                                            requireActivity(),
                                                            resultIntent.getData(),
                                                            hands.getGlContext(),
                                                            glSurfaceView.getWidth(),
                                                            glSurfaceView.getHeight())
                                    );
                                }
                            }
                        });

        /*
        Button loadVideoButton = fragView.findViewById(R.id.button_load_video);
        loadVideoButton.setOnClickListener(
            v -> {
                cameraFacing = CameraInput.CameraFacing.BACK;
                stopCurrentPipeline();
                setupStreamingModePipeline(InputSource.VIDEO);
                // Reads video from gallery.
                Intent pickVideoIntent = new Intent(Intent.ACTION_PICK);
                pickVideoIntent.setDataAndType(MediaStore.Video.Media.INTERNAL_CONTENT_URI, "video/*");
                videoGetter.launch(pickVideoIntent);
            }
        );
        */
    }

    /** Sets up the UI components for the live demo with camera input. */
    private void setupLiveDemoUiComponents() {
        /*Button startCameraButton = fragView.findViewById(R.id.button_start_camera);
        startCameraButton.setOnClickListener(
                v -> {
                    if (inputSource == InputSource.CAMERA) {
                        return;
                    }
                    stopCurrentPipeline();
                    setupStreamingModePipeline(InputSource.CAMERA);
                });*/
        /*
        LottieAnimationView mainButton = fragView.findViewById(R.id.mainBtn);
        mainButton.setOnClickListener(
                v -> {
                    mainButton.playAnimation();
                    if (inputSource == InputSource.CAMERA) {
                        return;
                    }
                    stopCurrentPipeline();
                    setupStreamingModePipeline(InputSource.CAMERA);
                });


         */
    }

    /** Sets up core workflow for streaming mode. */
    private void setupStreamingModePipeline(InputSource inputSource) {
        this.inputSource = inputSource;
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands = new Hands(
                        requireContext(),
                        HandsOptions.builder()
                                .setStaticImageMode(false)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build()
                );
        hands.setErrorListener((message, e) -> Log.e(logTag, "MediaPipe Hands error:" + message));

        if (inputSource == InputSource.CAMERA) {
            cameraInput = new CameraInput(requireActivity());
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
        } else if (inputSource == InputSource.VIDEO) {
            videoInput = new VideoInput(requireActivity());
            videoInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
        }

        // Initializes a new Gl surface view with a user-defined HandsResultGlRenderer.
        glSurfaceView = new SolutionGlSurfaceView<>(requireContext(), hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new HandsResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);
        hands.setResultListener(
            handsResult -> {

                logWristLandmark(handsResult, /*showPixelValues=*/ false);
                glSurfaceView.setRenderData(handsResult);
                glSurfaceView.requestRender();

            }
        );

        // The runnable to start camera after the gl surface view is attached.
        // For video input source, videoInput.start() will be called when the video uri is available.
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.post(this::startCamera);
        }

        translateIcon.bringToFront();
        translateMessage.bringToFront();

        //mainBtn.bringToFront();
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


    private void stopCurrentPipeline() {
        if (cameraInput != null) {
            cameraInput.setNewFrameListener(null);
            cameraInput.close();
        }
        if (videoInput != null) {
            videoInput.setNewFrameListener(null);
            videoInput.close();
        }
        if (glSurfaceView != null) {
            glSurfaceView.setVisibility(View.GONE);
        }
        if (hands != null) {
            hands.close();
        }
    }

    private void logWristLandmark(HandsResult result, boolean showPixelValues) {
        if (result.multiHandLandmarks().isEmpty()) {
            iconAnimate = false;
            return;
        }
        iconAnimate = true;
        if(!translateIcon.isAnimating()){ // Start animation when
            requireActivity().runOnUiThread(() -> {
                translateIcon.resumeAnimation();
            });
        }
        if(translateMessage.getVisibility() == View.INVISIBLE || translateMessage.getVisibility() == View.GONE){
            requireActivity().runOnUiThread(() -> {
                translateMessage.setVisibility(View.VISIBLE);
                translateMessage.setText("•••");
            });
        }

        LandmarkProto.NormalizedLandmark wristLandmark =
                result.multiHandLandmarks().get(0).getLandmarkList().get(HandLandmark.WRIST);
        // For Bitmaps, show the pixel values. For texture inputs, show the normalized coordinates.
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
        if (result.multiHandWorldLandmarks().isEmpty()) {
            return;
        }
        LandmarkProto.Landmark wristWorldLandmark =
                result.multiHandWorldLandmarks().get(0).getLandmarkList().get(HandLandmark.WRIST);
        Log.i(
                logTag,
                String.format(
                        "MediaPipe Hand wrist world coordinates (in meters with the origin at the hand's"
                                + " approximate geometric center): x=%f m, y=%f m, z=%f m",
                        wristWorldLandmark.getX(), wristWorldLandmark.getY(), wristWorldLandmark.getZ()));
    }

    //


    private Bitmap downscaleBitmap(Bitmap originalBitmap) {
        double aspectRatio = (double) originalBitmap.getWidth() / originalBitmap.getHeight();
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        if (((double) imageView.getWidth() / imageView.getHeight()) > aspectRatio) {
            width = (int) (height * aspectRatio);
        } else {
            height = (int) (width / aspectRatio);
        }
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false);
    }

    private Bitmap rotateBitmap(Bitmap inputBitmap, InputStream imageData) throws IOException {
        int orientation =
                new ExifInterface(imageData)
                        .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            return inputBitmap;
        }
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                matrix.postRotate(0);
        }
        return Bitmap.createBitmap(
                inputBitmap, 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight(), matrix, true);
    }

}