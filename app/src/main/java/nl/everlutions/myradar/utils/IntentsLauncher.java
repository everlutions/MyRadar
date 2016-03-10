package nl.everlutions.myradar.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class IntentsLauncher implements Constants {

    public static final String TAG = IntentsLauncher.class.getSimpleName();

    public static void sendMail(Context ctx, String to, String subj, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        intent.putExtra(Intent.EXTRA_SUBJECT, subj);
        intent.setData(Uri.parse("mailto:"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(intent);
        } else {
            Log.e(TAG, "no such application listening to intent action");
        }
    }

    public static void openUrl(Context ctx, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(intent);
        } else {
            Log.e(TAG, "no such application listening to intent action");
        }
    }

    /**
     * You can use ACTION_CALL instead of ACTION_DIAL to directly place a phone call
     * to do this you must implement
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     */
    public static void dialPhone(Context ctx, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(intent);
        }
    }
}
