package nl.everlutions.myradar.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import nl.everlutions.myradar.R;
import nl.everlutions.myradar.adapters.TextWatcherAdapter;


/**
 * To change clear icon, set
 * <p/>
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */
public class ClearableEditText extends EditText implements OnTouchListener,
        OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    private static final String TAG = ClearableEditText.class.getSimpleName();

    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Listener listener;

    public ClearableEditText(Context context) {
        super(context);
        init();
        if(!isInEditMode())
            initStyle(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        if(!isInEditMode())
            initStyle(context, attrs);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        if(!isInEditMode())
            initStyle(context, attrs);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            setClearIconVisible(isNotEmpty(text));
        }
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                xD = getResources().getDrawable(R.drawable.ic_launcher, null);
            }else{
                xD = getResources().getDrawable(R.drawable.ic_launcher);
            }
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private void initStyle(Context context, AttributeSet attrs) {
        int fontInt = CustomTextView.MUSEO300_REGULAR;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);
            fontInt = ta.getInt(R.styleable.CustomTextView_customFont, CustomTextView.MUSEO300_REGULAR);
        }
        setCustomFont(CustomTextView.getCustomFont(fontInt), ".ttf");
    }
    /**
     * use the public static class vars for font int
     */
    public void setCustomFont(int font) {
        setCustomFont(CustomTextView.getCustomFont(font),".ttf");
    }

    private void setCustomFont(String fontFamily, String fontExtension) {
        if (!fontFamily.equalsIgnoreCase("ROBOTO")) {
            Typeface font= null;
            try {
                font = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontFamily + fontExtension);
                setTypeface(font);
                setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.DEV_KERN_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }catch (Exception e){
                setCustomFont(fontFamily, ".otf");
            }
        } else {
            Log.e(TAG, "Custom font not loaded, check the names and location of your fonts");
        }
    }
}
