package nl.everlutions.myradar.comm;

import nl.everlutions.myradar.utils.Constants;


public class HTTPRequestOld implements Constants {

//    public final static String TAG = HTTPRequestOld.class.getSimpleName();
//    private static JsonFactory factory;
//
//    static {
//        if (BuildConfig.DEBUG) {
//            java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(
//                    java.util.logging.Level.FINEST);
//        }
//        factory = new JsonFactory();
//    }
//
//    private static JsonParser makeJsonParser(String body) throws SpiceException {
//        try {
////            if (DEBUG_MODE) {
////                JSONObject testObj = new JSONObject(body);
////                Log.e(TAG, testObj.toString(4));
////            }
//            return body != null ? factory.createParser(body) : null;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            throw new SpiceException(e);
//        }
//    }
//
//    /**
//     * Almost each request is a POST request containing a Json Payload. A StringEntity is used
//     * setting ContentType to json
//     */
//    public static JsonParser doRequest(String method, String urlPath, JSONObject payload,
//                                       List<BasicNameValuePair> extraHeaders) throws SpiceException, JSONException {
//        StringEntity entity;
//        try {
//            if (DEBUG_MODE) {
////                int maxLogStringSize = 1000;
////                for(int i = 0; i <= payload.toString(4).length() / maxLogStringSize; i++) {
////                    int start = i * maxLogStringSize;
////                    int end = (i+1) * maxLogStringSize;
////                    end = end > payload.toString(4).length() ? payload.toString(4).length() : end;
////                    Log.e(TAG, payload.toString(4).substring(start, end));
////                }
//            }
//            entity = new StringEntity(payload.toString(), HTTP.UTF_8);
//            entity.setContentEncoding(HTTP.UTF_8);
//            entity.setContentType("application/json");
//            String body = doRawRequest(method, urlPath, null, entity, extraHeaders);
//            return makeJsonParser(body);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            throw new SpiceException(e);
//        }
//    }
//
//    /**
//     * A Post byteArrayEntity with urlParams is used to send the server an imgage.
//     * The contentType is therefore set to imgage/jpg.
//     *
//     * @param byteData image byteArray
//     */
//    public static JsonParser doRequest(String method, String urlPath, byte[] byteData,
//                                       List<BasicNameValuePair> params) throws SpiceException {
//
//        try {
//
//            ByteArrayEntity entity = new ByteArrayEntity(byteData);
//            entity.setContentType("image/jpg");
//            String body = doRawRequest(method, urlPath, params, entity, null);
//            return makeJsonParser(body);
//        } catch (SpiceException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//
//
//    private static String doRawRequest(String method, String urlPath, List<BasicNameValuePair> params,
//                                       AbstractHttpEntity requestEntity,
//                                       List<BasicNameValuePair> extraHeaders) throws SpiceException {
//        String urlParams = "";
//
//        if (params != null) {
//            urlParams = "&" + URLEncodedUtils.format(params, "utf-8");
//        }
//
//        HttpUriRequest request;
//        if(urlPath.contains(API_CHECK_TOKEN_URL)){
//            request = new HttpPost(urlPath);
//        }else if (method.equalsIgnoreCase(HttpGet.METHOD_NAME)) {
//            request = new HttpGet(API_BASE_URL + urlPath);
//        } else if (method.equalsIgnoreCase(HttpPut.METHOD_NAME)) {
//            request = new HttpPut(API_BASE_URL + urlPath + urlParams);
//        } else if (method.equalsIgnoreCase(HttpDelete.METHOD_NAME)) {
//            request = new HttpDelete(API_BASE_URL + urlPath + urlParams);
//        } else if (method.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
//            request = new HttpPost(API_BASE_URL + urlPath + urlParams);
//        } else {
//            throw new Error("Invalid request method");
//        }
//
//
//        if ((params != null || requestEntity != null) && !method.equals(HttpGet.METHOD_NAME)) {
//            ((HttpEntityEnclosingRequestBase) request).setEntity(requestEntity);
//        }
//
//        HttpClient client = getHttpClient(requestEntity instanceof ByteArrayEntity);
//
//        if (extraHeaders != null) {
//            for (BasicNameValuePair pair : extraHeaders) {
//                request.setHeader(pair.getName(), pair.getValue());
//            }
//        }
//
//        try {
//            HttpResponse response = client.execute(request);
//
//            String body = null;
//            int statusCode = response.getStatusLine().getStatusCode();
//
//            HttpEntity entity = response.getEntity();
//            if (entity != null) { // No content
//                BufferedReader reader = new BufferedReader(new InputStreamReader(
//                        entity.getContent(), "UTF-8"));
//
//                body = convertReaderToString(reader);
//                entity.consumeContent();
//                if (DEBUG_MODE) {
////                    Log.e(TAG, "Response body: " + body);
//                }
//            }
//
//            if (statusCode >= 400) {
//                throw new CustomSpiceException(UCC_EXCEPTION_ON_SERVER);
//            }
//            return body;
//
//        } catch (SocketTimeoutException e) {
//            throw new CustomSpiceException(UCC_EXCEPTION_TIME_OUT);
//        } catch (ConnectTimeoutException e) {
//            throw new CustomSpiceException(UCC_EXCEPTION_TIME_OUT);
//        } catch (SpiceException e) {
//            Log.e(TAG, ".doRequest(): " + e.getLocalizedMessage());
//            e.printStackTrace();
//            throw e;
//
//        } catch (Throwable e) {
//            Log.e(TAG, ".doRequest(): " + e.getLocalizedMessage());
//            e.printStackTrace();
//            throw new SpiceException(e);
//        }
//    }
//
//    private static HttpClient getHttpClient(boolean isFotoRequest) {
//        int timeOut = isFotoRequest ? UCC_CONNECTION_TIMEOUT*6: UCC_CONNECTION_TIMEOUT;
//        HttpParams httpParameters = new BasicHttpParams();
//        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
//        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
//        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
//        HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
//        HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
//        return getAllTrustingHttpClient(httpParameters);
//    }
//
//    public static HttpClient getAllTrustingHttpClient(HttpParams httpParameters) {
//        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
//
//            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//            SchemeRegistry registry = new SchemeRegistry();
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//            registry.register(new Scheme("https", sf, 443));
//
//            ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);
//
//            return new DefaultHttpClient(ccm, httpParameters);
//        } catch (Exception e) {
//            return new DefaultHttpClient(httpParameters);
//        }
//    }
//
//    public static class MySSLSocketFactory extends SSLSocketFactory {
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//
//        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
//                UnrecoverableKeyException {
//            super(truststore);
//
//            TrustManager tm = new X509TrustManager() {
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            };
//
//            sslContext.init(null, new TrustManager[]{tm}, null);
//        }
//
//        @Override
//        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
//            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//        }
//
//        @Override
//        public Socket createSocket() throws IOException {
//            return sslContext.getSocketFactory().createSocket();
//        }
//    }
//
//
//    public static String convertReaderToString(Reader reader) throws IOException {
//        StringWriter writer = new StringWriter();
//        org.apache.commons.io.IOUtils.copy(reader, writer);
//        return writer.toString();
//    }
}