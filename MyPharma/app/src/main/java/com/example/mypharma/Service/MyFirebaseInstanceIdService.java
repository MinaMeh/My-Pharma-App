package com.example.mypharma.Service;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mypharma.Controllers.AuthentificationController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;



public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "global";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        AuthentificationController auth= new AuthentificationController();
        SharedPreferences pref= getSharedPreferences("filename",MODE_PRIVATE);
         int nss= pref.getInt("nss",0);
        auth.updateToken(refreshedToken,nss);

        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        AuthentificationController auth= new AuthentificationController();
        SharedPreferences pref= getSharedPreferences("fileName",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.commit();

        Log.d( "test",pref.getString("token", "alger"));


    }
}