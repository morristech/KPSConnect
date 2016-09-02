package me.msfjarvis.kpsconnect.utils;

import android.util.Log;
import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;

public class APIThread extends Thread {
    public void run(String API_URL, String regID){
        try{
            Bridge
                    .post(API_URL)
                    .header("regID",regID)
                    .header("Content-Type","application/json")
                    .request();
        }catch (BridgeException exc){
            Log.d("Bridge",exc.toString());
        }


    }
}
