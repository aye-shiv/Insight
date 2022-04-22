package com.trivtech.insight.util;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

public class TextToSpeech {
    public static String logTag = "TextToSpeech";

    public android.speech.tts.TextToSpeech ttsSystem;
    private boolean disabled = false;
    private String reason = "";

    private boolean userDisabled = false;

    public TextToSpeech(Context context){
        ttsSystem = new android.speech.tts.TextToSpeech(context, new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == android.speech.tts.TextToSpeech.SUCCESS){
                    int result = ttsSystem.setLanguage(Locale.US);
                    if(result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e(logTag, "(TTS) This language is not supported (" + ttsSystem.getLanguage() + ")");
                        disabled = true;
                        reason = "This language is not supported";
                    }
                } else {
                    Log.e(logTag, "TTS Initialization failed");
                    disabled = true;
                    reason = "Initialization failed";
                }
            }
        });
    }

    public void speak(String text){
        speak(text, false);
    }
    public void speak(String text, boolean force){
        if(isDisabled())
            return;

        if(force)
            ttsSystem.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);
        else
            ttsSystem.speak(text, android.speech.tts.TextToSpeech.QUEUE_ADD, null);

    }


    public boolean isDisabled(){
        if(disabled) {
            Log.e(logTag, "Status - Disabled");
            Log.e(logTag, "Reason: " + reason);
            return true;
        } else if(userDisabled) {
            Log.e(logTag, "Status - User Disabled");
            return true;
        }
        return false;
    }

    public void toggle(){
        if(userDisabled)
            enable();
        else disable();
    }
    private void enable(){
        userDisabled = false;
        Log.i(logTag, "User Enabled");
    }
    private void disable(){
        if(ttsSystem.isSpeaking())
            ttsSystem.stop();
        userDisabled = true;
        Log.i(logTag, "User Disabled");
    }


}
