package nl.everlutions.myradar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import nl.everlutions.myradar.R;

public class LoaderView extends RelativeLayout {

    int LOADER_WIDTH_DP = 30;
    int LOADER_HEIGHT_DP = 30;

    public LoaderView(Context context) {
        super(context);
        initView(context);
    }

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                ScreenUtils.convertDpToPx(LOADER_WIDTH_DP, context);
//                ScreenUtils.convertDpToPx(LOADER_HEIGHT_DP, context);
                float width = getWidth();
                float height = getHeight();

                LayoutParams params = new LayoutParams((int) width, (int) height);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView ivLoadingImage = new ImageView(getContext());
                ivLoadingImage.setImageResource(R.mipmap.ic_launcher);
                ivLoadingImage.setLayoutParams(params);

                AnimationFlip animation = new AnimationFlip(0, 360, (int) width / 2, (int) height / 2);
                animation.setDuration(2000);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatMode(Animation.RESTART);
                animation.setRepeatCount(Animation.INFINITE);
                ivLoadingImage.setAnimation(animation);
                addView(ivLoadingImage);

            }
        });

    }

}
