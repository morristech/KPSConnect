package me.msfjarvis.kpsconnect.utils;

import android.util.Log;
import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import org.json.JSONObject;

public class APIThread extends Thread {
    public void run(String BASE_URL, JSONObject postContent){
        try{
            Bridge
                    .post(BASE_URL)
                    .body(postContent)
                    .header("Content-Type","application/json")
                    .header("OAuth-Token","test-keys")
                    .request();
        }catch (BridgeException exc){
            Log.d("Bridge",exc.toString());
        }


    }
    public void sendFeedback(JSONObject feedback){
        try{
            Bridge
                    .post("https://api.msfjarvis.me/content/feedback")
                    .header("Content-Type","application/json")
                    .header("OAuth-Token","test-keys")
                    .body(feedback)
                    .request();
        }catch (BridgeException exc){
            Log.d("Bridge",exc.toString());
        }
    }
}
