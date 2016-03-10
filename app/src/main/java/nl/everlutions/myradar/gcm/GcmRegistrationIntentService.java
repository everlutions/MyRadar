/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.everlutions.myradar.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import nl.everlutions.myradar.R;
import nl.everlutions.myradar.comm.HTTPRequest;
import nl.everlutions.myradar.utils.Constants;
import nl.everlutions.myradar.utils.PrefsUtil;

public class GcmRegistrationIntentService extends IntentService implements Constants {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public GcmRegistrationIntentService() {
        super(TAG);
    }

    /**
     * To Register to gcm you need a configuration file
     *
     * Follow step 2 to get a conifugration file
     * https://developers.google.com/cloud-messaging/
     * put the config file(goole-services.json) in the app/ folder
     *
     * Make sure you have the followin line added to your poject build.gradle file
     * classpath 'com.google.gms:google-services:1.3.0-beta1'
     *
     * Make sure you have add these two dependencies to your app build.gradle file
     * apply plugin: 'com.google.gms.google-services'
     * compile 'com.google.android.gms:play-services-gcm:7.5.+'
     *
     * Dont forget to update you AndroidManifset with the permissions and services and broadcast receviers
     *
     * And last but not least don't forget to put your own base_gcm_sender id in the strings
     *
     * Use the google chrome postman plugin or DHC to make the following request
     *
     *  https://gcm-http.googleapis.com/gcm/send
     *
     *  Content-Type:application/json
     *  Authorization : key=AIzaSyBEiFAZqO0H4aReIojl17QqRUJO9i187DI
     *
     *  {
     *  "to": "[PUT YOUR DEVICE TOKEN HERE]",
     *  "data":{
     *          "message": "This is a GCM Topic Message!",
     *         }
     *  }
     *
     *  you can also use "to":"topics/global",
     *
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            synchronized (TAG) {
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.base_gcm_sender_id),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                sendRegistrationToServer(token);

                // Subscribe to topic channels
                 subscribeTopics(token);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            PrefsUtil.saveBool(this, PREF_KEY_GCM_SUBSCRIBING_SUCCESSFUL, false);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        // Intent registrationComplete = new Intent();
        // LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        JSONObject params = new JSONObject();
        try {
            params.put("pushToken", token);
            params.put("development", "" + DEBUG_MODE);
            params.put("deviceName", Build.MANUFACTURER + " " + Build.MODEL);
            params.put("categories", new JSONArray());
            params.put("counties", new JSONArray());
            params.put("device_type", "android");
        } catch (JSONException e) {
            Log.e(TAG, "ERROR Adding JSON params");
            e.printStackTrace();
        }

        JsonParser jsonParser = HTTPRequest.doRequest(HTTPRequest.PUT_REQUEST, API_BASE_URL + API_PUT_REGISTER_DEVICE, params, null);

        PrefsUtil.saveBool(this, PREF_KEY_GCM_SUBSCRIBING_SUCCESSFUL, false);

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
