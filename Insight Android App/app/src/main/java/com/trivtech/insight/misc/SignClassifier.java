package com.trivtech.insight.misc;

import android.content.Context;

import com.trivtech.insight.ml.SignRecog;
import com.trivtech.insight.util.Utils;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class SignClassifier {

    SignRecog model = null;
    public boolean isAvailable = false;

    public SignClassifier(){

    }

    public int classify(List<Float> hand_landmarks){
        ByteBuffer byteBuffer = getByteBuffer(hand_landmarks);
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 42}, DataType.FLOAT32);
        inputFeature0.loadBuffer(byteBuffer);


        // Runs model inference and gets result.
        SignRecog.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
        return argmax(outputFeature0.getFloatArray());
    }

    private ByteBuffer getByteBuffer(List<Float> hand_landmarks){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(42 * Float.BYTES);
        byteBuffer.order(ByteOrder.nativeOrder());
        for(float value: hand_landmarks){
            byteBuffer.putFloat(value);
        }
        return byteBuffer;
    }

    private static int argmax(float[] array){
        float max = array[0];
        int re = 0;
        for(int i = 1; i < array.length; i++){
            if(array[i] > max){
                max = array[i];
                re = i;
            }
        }
        return re;
    }

    public void open(Context context){
        try {
            model = SignRecog.newInstance(context);
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        isAvailable = true;
    }

    public void close() {
        isAvailable = false;
        if(model != null)
            model.close();
    }

}