package nl.everlutions.myradar.comm;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.octo.android.robospice.persistence.exception.SpiceException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import nl.everlutions.myradar.BaseApplication;
import nl.everlutions.myradar.BuildConfig;
import nl.everlutions.myradar.utils.Constants;


public class HTTPRequest implements Constants {

    public final static String TAG = HTTPRequest.class.getSimpleName();
    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";
    public static final String PUT_REQUEST = "PUT";
    public static final String DELETE_REQUEST = "DELETE_REQUEST";

    private static JsonFactory factory;

    static {
        if (BuildConfig.DEBUG) {
            java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(
                    java.util.logging.Level.FINEST);
        }
        factory = new JsonFactory();
    }

    public static JsonParser doRequest(String requestMethod,String urlString, JSONObject jsonPayload, Map<String, String> extraHeaders) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(urlString);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setReadTimeout(API_CONNECTION_TIMEOUT);
            httpUrlConnection.setConnectTimeout(API_CONNECTION_TIMEOUT);
//            FOR HTTPS SUPPORT
//            httpsUrlConnection.setSSLSocketFactory(getSSLSocketFactory());
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod(requestMethod);
            httpUrlConnection.setRequestProperty("Accept", "application/json");
            if(extraHeaders!=null){
                for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                    httpUrlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if ((requestMethod.equalsIgnoreCase(POST_REQUEST) || requestMethod.equalsIgnoreCase(PUT_REQUEST) || requestMethod.equalsIgnoreCase(DELETE_REQUEST))&& jsonPayload != null) {
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setRequestProperty("Content-Type", "application/json");
                outputStream = httpUrlConnection.getOutputStream();
                outputStream.write(jsonPayload.toString().getBytes());
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            httpUrlConnection.connect();
            int response = httpUrlConnection.getResponseCode();
            inputStream = httpUrlConnection.getInputStream();

            if (response == HttpURLConnection.HTTP_OK) {
                String body = inputStreamToString(inputStream);
                return makeJsonParser(body);
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpiceException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                if(httpUrlConnection!=null)
                    httpUrlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String inputStreamToString(InputStream stream) throws IOException {
        InputStream responseStream = new BufferedInputStream(stream);
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();
        return stringBuilder.toString();
    }

    private static JsonParser makeJsonParser(String body) throws SpiceException, IOException {
        if (DEBUG_MODE) {
            try {
                JSONObject testObj = new JSONObject(body);
                Log.e(TAG, testObj.toString(4));
            } catch (JSONException e) {
                JSONArray testArray = null;
                try {
                    testArray = new JSONArray(body);
                    Log.e(TAG, testArray.toString(4));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return body != null ? factory.createParser(body) : null;
    }

    public static SSLSocketFactory getSSLSocketFactory() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream caInput = new BufferedInputStream(BaseApplication.getAppContext().getAssets().open("www.baseurl.com.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}