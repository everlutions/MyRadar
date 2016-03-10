package nl.everlutions.myradar.comm;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.retry.RetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import nl.everlutions.myradar.utils.Constants;
import nl.everlutions.myradar.utils.Utils;


public abstract class CustomSpiceRequest<T> extends SpiceRequest<T> implements Constants {

    protected ObjectMapper mapper;
    public final String TAG = this.getClass().getSimpleName();
    public JSONObject mJsonPayLoad = new JSONObject();

    public CustomSpiceRequest(Class<T> clazz) {
        super(clazz);
        setRetryPolicy(new UCCRetryPolicy());
        this.mapper = Utils.getObjectMapper();
        this.construct();
    }

    protected void construct() {
    }

    public class UCCRetryPolicy implements RetryPolicy {

        /**
         * The number of retry attempts.
         */
        private int retryCount;
        private long delayBeforeRetry;
        private float backOffMultiplier;

        private int lastStatusCode;

        // ----------------------------------
        // CONSTRUCTORS
        // ----------------------------------
        public UCCRetryPolicy() {
            this.retryCount = 0;
            this.delayBeforeRetry = 2000;
            this.backOffMultiplier = 1.1f;
        }

        // ----------------------------------
        // PUBLIC API
        // ----------------------------------

        @Override
        public int getRetryCount() {
            if (lastStatusCode == 0 || lastStatusCode == 409 || lastStatusCode >= 500) {
                return retryCount;
            } else {
                System.out.println("NO retry on status code: " + lastStatusCode);
                return 0;
            }
        }

        @Override
        public void retry(SpiceException exception) {
            int statusCode = 200;
            if (lastStatusCode == 0 && statusCode == 409) {
                retryCount = 10;
                backOffMultiplier = 1.0f;
            }
            lastStatusCode = statusCode;
            retryCount--;
            delayBeforeRetry = (long) (delayBeforeRetry * backOffMultiplier);
        }

        @Override
        public long getDelayBeforeRetry() {
            return delayBeforeRetry;
        }

    }

    public JSONObject mergeJsonPayload(JSONObject newJsonPayLoad) {
        for (Iterator<String> iter = mJsonPayLoad.keys(); iter.hasNext(); ) {
            String key = iter.next();
            try {
                newJsonPayLoad.put(key, mJsonPayLoad.get(key));
            } catch (JSONException e) {
                Log.e(TAG, "Json exception with merge");
            }
        }
        return newJsonPayLoad;
    }

}
