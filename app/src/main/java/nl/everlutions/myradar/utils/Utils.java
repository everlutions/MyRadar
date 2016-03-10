package nl.everlutions.myradar.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;


@SuppressLint("SimpleDateFormat")
public class Utils implements Constants {

    public static final String TAG = Utils.class.getSimpleName();

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
        objectMapper.configure(MapperFeature.AUTO_DETECT_CREATORS, false);

        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

        objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // mapper.setDateFormat(df);
    }

    @SuppressLint("SimpleDateFormat")
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String encodeToUTF8(String url) {
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            Log.e(TAG, "fileName encode error: " + url);
        }
        return encodedString;
    }

    public static Calendar parseDateStringYYYYMMddHHmmssToCal(String dateString) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            Log.e(TAG, "dateParse error: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
        return cal;
    }

    public static Calendar parseDateStringYYYYMMddToCal(String dateString) {
        Calendar cal = Calendar.getInstance();
        if (dateString == null) {
            return cal;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            Log.e(TAG, "DATE PARSE error: " + e.getLocalizedMessage());
            e.printStackTrace();
            return Calendar.getInstance();
        }
        return cal;
    }

    public static Calendar parseFormatAndDateStringToCal(String dateString, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            Log.e(TAG, "dateParse error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return cal;
    }

    public static boolean isScreenSizeNormal(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL;
    }

    public static boolean isScreenSizeLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isScreenSizeXLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * @param configOrientation use Confirugration.StaticOrientation reference
     */
    public static boolean isOrientation(int configOrientation, Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == configOrientation;
    }

    public static boolean isTablet(Context context) {
        return isOrientation(Configuration.ORIENTATION_LANDSCAPE, context)
                || isScreenSizeXLarge(context);
    }

    /**
     *
     */
    public static int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public static Uri makeGeoURI(double lat, double lon, String label) {
        // String q = lat + "," + lon + " (" + label + ")";
        // return Uri.parse("geo:" + lat + "," + lon + "?q=" + Uri.encode(q) + "&z=16");
        return Uri.parse("http://maps.google.com/maps?q="
                + Uri.encode("loc:" + lat + "," + lon + " (" + label + ")"));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String formatImageUrl(String url, int w, int h) {
        return url.replace("[format]", w + "x" + h);
    }

    /**
     * Formats a price as display currency
     *
     * @param price
     * @param zeroesToDash replace ,00 with ,-
     * @return
     */
    public static String formatPrice(double price, boolean zeroesToDash) {
        String priceString = formatDecimal(price);
        priceString = zeroesToDash ? priceString + ",-" : priceString + ",00";
        return "\u20AC\u00A0" + priceString;
    }

    public static String formatPrice(double price, boolean zeroesToDash, boolean withDecimals) {
        price = round(price, 2);
        String priceString = formatDecimal(price);
        if (withDecimals) {
            priceString = zeroesToDash ? priceString.replace(",00", ",-") : priceString;
        } else {
            priceString = String.format(new Locale("nl", "NL"), "%.0f", price);
            priceString = zeroesToDash ? priceString + ",-" : priceString + ",00";
        }

        return "\u20AC\u00A0" + priceString;
    }

    public static String getUserAgentString(Context context) {
        String version = "?";
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = manager.versionName;
        } catch (NameNotFoundException e) {
            // Handle exception
        }

        return context.getPackageName() + "/" + version + " (Android; " + Build.VERSION.RELEASE
                + ")";
    }

    public static int getApplicationVersionCode(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }

    }

    public static String minutesToDisplayText(int notifyMinutesBefore) {

        StringBuilder b = new StringBuilder();
        if (notifyMinutesBefore >= 24 * 60) {
            int d = notifyMinutesBefore / (24 * 60);
            b.append(Integer.toString(d));
            b.append(d > 1 ? " dagen" : " dag");
            notifyMinutesBefore %= 24 * 60;
            if (notifyMinutesBefore > 0) {
                b.append(", ");
            }
        }
        if (notifyMinutesBefore >= 60) {
            b.append((notifyMinutesBefore / 60) + " uur");
            notifyMinutesBefore %= 60;
            if (notifyMinutesBefore > 0) {
                b.append(", ");
            }
        }
        if (notifyMinutesBefore > 0) {
            b.append(notifyMinutesBefore + " minuten");
        }
        return b.toString();
    }


    public static boolean isEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    public static void expandView(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
        v.startAnimation(a);
    }

    public static void collapseView(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
        v.startAnimation(a);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
        return connected;
    }

    public static String serialize(Object value, Class<?> clazz) {
        if (value == null)
            return null;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static <T> T deserialize(String value, Class<T> clazz) {
        if (value == null)
            return null;
        try {
            return objectMapper.readValue(value, clazz);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static void lockOrientation(Activity activity) {
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int tempOrientation = activity.getResources().getConfiguration().orientation;
        int orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        switch (tempOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                else
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                else
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        }
        activity.setRequestedOrientation(orientation);
    }

    public static String formatDateUCC(String wrongDateFormat) {
        Calendar cal = Utils.parseDateStringYYYYMMddToCal(wrongDateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        return sdf.format(cal.getTime());
    }

    public static String formatDecimal(double bigNumber) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("nl", "NL"));
        return nf.format(bigNumber);
    }

    public static double round(double dvalue, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(dvalue);
        bd = bd.setScale(places, RoundingMode.DOWN);
        return bd.doubleValue();
    }


    public static String loadJSONFromAsset(Context ctx, String fileName) {
        String json = null;
        try {

            InputStream is = ctx.getAssets().open(fileName + ".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        if (bitmap != null && bitmap.getWidth() > IMAGE_PREFERRED_WIDTH) {
            float scale = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            bitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_PREFERRED_WIDTH, (int) (IMAGE_PREFERRED_WIDTH * scale), false);
        }
        return bitmap;
    }

    public static byte[] getByteArrayFromGalleryIntent(Context ctx, Intent data) {
        try {
            InputStream stream = ctx.getContentResolver().openInputStream(
                    data.getData());
            byte[] byteArray = IOUtils.toByteArray(stream);
            stream.close();
            return byteArray;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decoding the imageByteArray but not allocating any memory while using inJsutDecodeBounds true
     * then check if the outWidth would be larger then the maxImageWidth.
     * If so the bitmap is scaled down with the appropriate InSampleSize using the calculateInSampl-
     * eSize
     *
     * @return
     */
    public static byte[] getScaledImageByteArray(byte[] originalImageByteArray) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(originalImageByteArray, 0, originalImageByteArray.length, options);
        options.inSampleSize = calculateInSampleSize(options, IMAGE_MAX_WIDTH);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(originalImageByteArray, 0, originalImageByteArray.length, options);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayStream);
        originalImageByteArray = byteArrayStream.toByteArray();
        bitmap.recycle();
        return originalImageByteArray;
    }

    public static String getAppVersionName(Context ctx) {
        String versionName = "Error getting version name";
        try {
            versionName = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static Bitmap decodeSampledBitmap(byte[] array,
                                             int reqWidth) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(array, 0, array.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(array, 0, array.length, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth) {
        // Raw height and width of image
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {
            while ((width/ inSampleSize) > reqWidth) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
}
