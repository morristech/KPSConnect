package me.msfjarvis.kpsconnect.utils;

import android.util.Log;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;

import org.json.JSONObject;

public class APIThread extends Thread {
    public void run(String BASE_URL, JSONObject postContent){
        try{
            Request request = Bridge
                    .post(BASE_URL)
                    .body(postContent)
                    .header("Content-Type","application/json")
                    .request();
        }catch (BridgeException exc){
            Log.d("Bridge",exc.toString());
        }


    }
}
