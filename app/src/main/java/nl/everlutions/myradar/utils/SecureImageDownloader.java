package nl.everlutions.myradar.utils;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import nl.everlutions.myradar.comm.HTTPRequest;


public class SecureImageDownloader extends BaseImageDownloader {
    /**
     * {@value}
     */
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
    /**
     * {@value}
     */
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds

    private int connectTimeout;
    private int readTimeout;

    private SSLSocketFactory sf;

    public SecureImageDownloader(Context context) {
        this(context, DEFAULT_HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_READ_TIMEOUT);
    }

    public SecureImageDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;

        initSSLSocketFactory();
    }

    private void initSSLSocketFactory() {
        try {
            sf = HTTPRequest.getSSLSocketFactory();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        URL url = new URL(imageUri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);

        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(sf);
        }

        return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
    }
}
