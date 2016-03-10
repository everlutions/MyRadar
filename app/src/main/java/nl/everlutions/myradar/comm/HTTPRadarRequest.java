package nl.everlutions.myradar.comm;

import com.octo.android.robospice.persistence.exception.SpiceException;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import nl.everlutions.myradar.dto.RadarResponseDto;
import nl.everlutions.myradar.utils.Constants;


public class HTTPRadarRequest implements Constants {

    public final static String TAG = HTTPRadarRequest.class.getSimpleName();
    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";
    public static final String PUT_REQUEST = "PUT";
    public static final String DELETE_REQUEST = "DELETE_REQUEST";

    public static RadarResponseDto doRequest(String requestMethod, String urlString, JSONObject jsonPayload,
            Map<String, String> extraHeaders) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(urlString);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setReadTimeout(20000);
            httpUrlConnection.setConnectTimeout(API_CONNECTION_TIMEOUT);
//            FOR HTTPS SUPPORT
//            httpsUrlConnection.setSSLSocketFactory(getSSLSocketFactory());
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod(requestMethod);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                    httpUrlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if ((requestMethod.equalsIgnoreCase(POST_REQUEST) || requestMethod.equalsIgnoreCase(PUT_REQUEST)
                    || requestMethod.equalsIgnoreCase(DELETE_REQUEST)) && jsonPayload != null) {
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
                return new RadarResponseDto(IOUtils.toByteArray(inputStream),
                        httpUrlConnection.getHeaderFields());
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpiceException e) {
            new SpiceException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}