package nl.everlutions.myradar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import nl.everlutions.myradar.R;

public class CustomTextView extends TextView {

    private static final String TAG = CustomTextView.class.getSimpleName();

    public static final int ROBOTO = -1;
    public static final int QUICKSAND_REGULAR = 0;
    public static final int QUICKSAND_BOLD = 1;
    public static final int MUSEO300_REGULAR = 2;
    public static final int MUSEO700_REGULAR = 3;

    /**
     * To add a new font:
     * 1. add the .ttf fonttype file to the assets/fonts folder
     * 2. add the name of the fonttype to the attrs.xml customFont enum of CustomTextView
     * 3. add the enum int value as a constant value in this class
     * 4. add the constant value to {@link #getCustomFont(int)}
     * 5. add xmlns:app="http://schemas.android.com/apk/res-auto" to the layout file if used in xml
     */
    public CustomTextView(Context context) {
        super(context);
        if (!isInEditMode())
            initStyle(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initStyle(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initStyle(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode())
            initStyle(context, attrs);
    }

    private void initStyle(Context context, AttributeSet attrs) {
        int fontInt = ROBOTO;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);
            fontInt = ta.getInt(R.styleable.CustomTextView_customFont, ROBOTO);
        }
        setCustomFont(getCustomFont(fontInt), ".ttf");
    }

    /**
     * use the public static class vars for font int
     */
    public void setCustomFont(Context context, int font) {
        setCustomFont(getCustomFont(font), ".ttf");
    }

    private void setCustomFont(String fontFamily, String fontExtension) {
        if (!fontFamily.equalsIgnoreCase("ROBOTO")) {
            Typeface font = null;
            try {
                font = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontFamily + fontExtension);
                setTypeface(font);
                setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.DEV_KERN_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } catch (Exception e) {
                setCustomFont(fontFamily, ".otf");
            }
        } else {
            Log.e(TAG, "Custom font not loaded, check the names and location of your fonts");
        }
    }

    public static String getCustomFont(int font) {
        String customFont = "";

        switch (font) {
            case ROBOTO:
                customFont = "ROBOTO";
                break;
            case QUICKSAND_REGULAR:
                customFont = "Quicksand-Regular";
                break;
            case QUICKSAND_BOLD:
                customFont = "Quicksand-Bold";
                break;
            case MUSEO300_REGULAR:
                customFont = "Museo300-Regular";
                break;
            case MUSEO700_REGULAR:
                customFont = "Museo700-Regular";
                break;
        }

        return customFont;
    }
}
