package nl.everlutions.myradar.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import nl.everlutions.myradar.R;


public class DialogUtil implements Constants {

    public static final String TAG = DialogUtil.class.getSimpleName();

    public static Dialog getLoadingDialog(final Activity activity) {
        View root = activity.getLayoutInflater().inflate(R.layout.dialog_loading, null);

        Dialog loadingDialog = new Dialog(activity);
        loadingDialog.getWindow().requestFeature(DialogFragment.STYLE_NO_TITLE);
        loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog.setCancelable(true);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setContentView(root);

        return loadingDialog;
    }

    public static void showMsg(Context context, int msg) {
        showMsg(context, context.getString(msg));
    }

    public static void showMsg(Context ctx, String msg) {
        Toast t = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
    }
//
//    public static void showAlertDialog(String msg, Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(R.string.ucc_info);
//        builder.setMessage(msg);
//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setCancelable(true);
//        builder.show();
//    }
//
//    public static AlertDialog.Builder getAlertDialog(String msg, Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(R.string.ucc_info);
//        builder.setMessage(msg);
//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setCancelable(true);
//        return builder;
//    }
//
//    public static void showAlertDialog(int resStringId, Context context) {
//        showAlertDialog(context.getString(resStringId), context);
//    }
//
//
//    public static AlertDialog.Builder getPosNegDialog(Context ctx, int titleRes, int messageRes, int posTextRes, int negTextRes, DialogInterface.OnClickListener posClickListener, DialogInterface.OnClickListener negClickListener) {
//        String title = ctx.getString(titleRes);
//        String message = ctx.getString(messageRes);
//        String posText = ctx.getString(posTextRes);
//        String negText = ctx.getString(negTextRes);
//        return getPosNegDialog(ctx, title, message, posText, negText, posClickListener, negClickListener);
//    }
//
//    public static AlertDialog.Builder getPosNegDialog(Context ctx, String title, String message, String posText, String negText, DialogInterface.OnClickListener posClickListener, DialogInterface.OnClickListener negClickListener) {
//        AlertDialog.Builder posNegDialog = new AlertDialog.Builder(ctx);
//        posNegDialog.setTitle(title.isEmpty() ? ctx.getString(R.string.ucc_info) : title);
//        if (!message.isEmpty()) {
//            posNegDialog.setMessage(message);
//        }
//        posNegDialog.setNegativeButton(negText, negClickListener);
//        posNegDialog.setPositiveButton(posText, posClickListener);
//        posNegDialog.setCancelable(true);
//        return posNegDialog;
//    }

//    public static void showErrorMessage(final BaseActivity activity, String errorMsg) {
//        if(activity!=null) {
//            activity.hideLoader();
//            if (errorMsg.contains("Network is not available")) {
//                showMsg(activity, activity.getString(R.string.ucc_msg_network_unavailable));
//            } else if (errorMsg.contains("Exception occurred during invocation of web service.")) {
//                showMsg(activity, activity.getString(R.string.ucc_msg_unknown_error));
//            } else if (errorMsg.equalsIgnoreCase("no_access")) {
//                showLogOutDialog(activity);
//            } else if (errorMsg.contains("Request has been cancelled explicitely.")) {
////            showMsg(activity, activity.getString(R.string.ucc_msg_task_cancelled));
//            } else {
//                showMsg(activity, errorMsg);
//            }
//        }
//    }

//    public static void showErrorMessage(BaseActivity activity, SpiceException exception) {
//        if(activity!=null) {
//            activity.hideLoader();
//            String errorMsg = exception.getMessage();
//            CustomSpiceException customException = getCausingHTTPException(exception);
//            if (customException != null) {
//                errorMsg = customException.getCustomMessage();
//                if (errorMsg.contains(UCC_EXCEPTION_JSON_MAPPING)) {
//                    showMsg(activity, activity.getString(R.string.ucc_msg_error_json_mapping));
//                    Crashlytics.logException(customException);
//                    Crashlytics.getInstance().crash();
//                } else if (errorMsg.contains(UCC_EXCEPTION_ON_SERVER)) {
//                    showMsg(activity, activity.getString(R.string.ucc_msg_error_on_server));
//                } else if (errorMsg.contains(UCC_EXCEPTION_TIME_OUT)) {
//                    showMsg(activity, activity.getString(R.string.ucc_msg_error_timed_out));
//                } else {
//                    getAlertDialog(errorMsg, activity).setTitle(R.string.ucc_error).show();
//                }
//            } else {
//                if (errorMsg.contains("Network is not available")) {
//                    showMsg(activity, activity.getString(R.string.ucc_msg_network_unavailable));
//                } else if (errorMsg.contains("Exception occurred during invocation of web service.")) {
//                    getAlertDialog(activity.getString(R.string.ucc_msg_unknown_error), activity).setTitle(R.string.ucc_error).show();
//                } else if (errorMsg.contains("Request has been cancelled explicitely.")) {
////            showMsg(activity, activity.getString(R.string.ucc_msg_task_cancelled));
//                }
//            }
//        }
//    }

//    public static CustomSpiceException getCausingHTTPException(Exception exception) {
//        if (exception instanceof CustomSpiceException) {
//            return (CustomSpiceException) exception;
//        }
//
//        if (exception.getCause() != null && exception.getCause() instanceof Exception) {
//            return getCausingHTTPException((Exception) exception.getCause());
//        }
//
//        return null;
//    }

//    public static void showErrorMessage(final BaseActivity activity, BaseObject baseObject) {
//        if(activity!=null) {
//            activity.hideLoader();
//            if(baseObject.getCode()!=null && !baseObject.getCode().isEmpty()) {
//                if (baseObject.getCode().contains("ucc.UPDATE")) {
//                    showUpdateApp(activity, baseObject.getUrl());
//                } else if (baseObject.getCode().contains("NO_ACCESS")) {
//                    showLogOutDialog(activity);
//                } else {
//                    AlertDialog.Builder builder = getAlertDialog(baseObject.getMessageError(), activity);
//                    builder.setTitle(R.string.ucc_error).setMessage(baseObject.getMessageError()).show();
//                }
//            }else {
//                AlertDialog.Builder builder = getAlertDialog(baseObject.getMessageError(), activity);
//                builder.setTitle(R.string.ucc_error).show();
//            }
//        }
//    }
}
