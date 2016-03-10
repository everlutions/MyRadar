package nl.everlutions.myradar.activities;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.everlutions.myradar.R;
import nl.everlutions.myradar.dto.RadarResponseDto;


public class MainActivity extends BaseActivity {

    final static String RADAR_URL_NL_3_HOUR_AHEAD
            = "http://api.buienradar.nl/image/image.ashx?k=33&l=2&hist=0&forc=36&step=2&nc=1&h=800";
    final static String RADAR_URL_EU_SATELITE =
            "http://api.buienradar.nl/image/image.ashx?k=3&c=eu&l=2&forc=10&nc=0&step=0&hist=24&h=800";
    final static String RADAR_URL_NL_24_HOUR_AHEAD
            = "http://api.buienradar.nl/image/image.ashx?k=18&l=2&hist=0&forc=24&step=0&nc=1&h=800";
    final static String[] mRadarTitles = new String[]{
            "NL +3 UUR", "EU -3 UUR", "NL +24 UUR"
    };

    final static String[] mRadarUrls = new String[]{
            RADAR_URL_NL_3_HOUR_AHEAD, RADAR_URL_EU_SATELITE, RADAR_URL_NL_24_HOUR_AHEAD
    };
    @Bind(R.id.pager)
    ViewPager mViewPager;
    @Bind(R.id.radar_play)
    ImageView mPlayView;
    @Bind(R.id.radar_image)
    ImageView mRadarImageView;
    @Bind(R.id.radar_seekbar)
    SeekBar mSeekBar;
    @Bind(R.id.radar_time)
    TextView mRadarTimeView;

    private RadarResponseDto mRadarData;
    private boolean mIsPlaying;
//    private View.OnTouchListener mCircleTouchListener = new View.OnTouchListener() {
//        public Boolean mClockWise;
//        public float previousY;
//        public float previousX;
//        public double mRadius;
//        public float downX;
//        public float downY;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getActionMasked()) {
//
//                case MotionEvent.ACTION_DOWN:
//                    downY = event.getY();
//                    downX = event.getX();
//                    previousX = downX;
//                    previousY = downY;
//                    float centerX = mRadarImageView.getWidth() / 2;
//                    float centerY = mRadarImageView.getHeight() / 2;
//                    float xDisToC = Math.abs(centerX - downX);
//                    float yDistToC = Math.abs(centerY - downY);
//                    mRadius = Math.sqrt(Math.pow(xDisToC, 2) + Math.pow(yDistToC, 2));
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float y = event.getY();
//                    float x = event.getX();
//                    if (mClockWise == null) {
//                        mClockWise = y <= previousY && x >= previousX || y >= downY && x < downX;
//                    }
//
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    mClockWise = null;
//                    break;
//
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        mRadarImageView.setOnTouchListener(mCircleTouchListener);

        getRadarData(mRadarUrls[0]);

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup collection, final int position) {
                Context context = collection.getContext();
                TextView radarTitleView = new TextView(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                radarTitleView.setLayoutParams(params);
                radarTitleView.setGravity(Gravity.CENTER);
                radarTitleView.setTextSize(22);
                radarTitleView.setTextColor(Color.BLACK);
                radarTitleView.setText(mRadarTitles[position]);
                radarTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRadarData(mRadarUrls[position]);
                    }
                });
                collection.addView(radarTitleView);
                return radarTitleView;
            }

            @Override
            public void destroyItem(ViewGroup collection, int position, Object view) {
                collection.removeView((View) view);
            }

            @Override
            public int getCount() {
                return mRadarUrls.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getRadarData(mRadarUrls[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

    public void getRadarData(String radarUrl) {
        stopPlayBack();
        showLoader(null);
        mAPI.getRadarImages(radarUrl, new RequestListener<RadarResponseDto>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                mRadarData = null;
                hideLoader();
                mRadarImageView.setImageResource(R.drawable.img_placeholder);
            }

            @Override
            public void onRequestSuccess(RadarResponseDto radarResponseDto) {
                hideLoader();
                if (radarResponseDto != null && radarResponseDto.radarImageBitmapList != null) {
                    mRadarData = radarResponseDto;
                    initRadarData();
                } else {
                    mRadarData = null;
                }
            }
        });
    }

    private void initRadarData() {

        mRadarImageView.setImageBitmap(mRadarData.radarImageBitmapList.get(0));
        mRadarTimeView.setText(mRadarData.mImageTimes.get(0));
        mSeekBar.setMax(mRadarData.radarImageBitmapList.size() - 1);
        mSeekBar.setProgress(0);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRadarImageView.setImageBitmap(mRadarData.radarImageBitmapList.get(progress));
                mRadarTimeView.setText(mRadarData.mImageTimes.get(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopPlayBack();
                mViewPager.animate()
                        .alpha(0).setDuration(300).start();
                mRadarTimeView.animate()
                        .scaleX(3.5f)
                        .scaleY(3.5f)
                        .setDuration(300)
                        .translationX((mRadarImageView.getWidth() / 2) - (mRadarTimeView.getWidth() / 2))
                        .y(mRadarImageView.getY() / 2).start();
                mRadarTimeView.setTextColor(Color.BLACK);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mViewPager.animate()
                        .alpha(1).setDuration(300).start();
                mRadarTimeView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .translationX(mRadarImageView.getX())
                        .translationY(0).start();
                mRadarTimeView.setTextColor(Color.WHITE);
            }
        });
    }


    @OnClick(R.id.radar_play)
    public void onPlayClick(View view) {
        if (mRadarData != null) {
            if (mIsPlaying) {
                stopPlayBack();
            } else {
                mPlayView.setImageResource(android.R.drawable.ic_media_pause);

                mCountDownTimer.start();
            }
            mIsPlaying = !mIsPlaying;
        }
    }

    private void stopPlayBack() {
        mPlayView.setImageResource(android.R.drawable.ic_media_play);
        mCountDownTimer.cancel();
    }

    @Override
    protected void onPause() {
        stopPlayBack();
        super.onPause();
    }

    CountDownTimer mCountDownTimer = new CountDownTimer(300, 300) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            int progress = mSeekBar.getProgress();
            int max = mSeekBar.getMax();
            mSeekBar.setProgress(progress == max ? 0 : progress + 1);
            mCountDownTimer.start();
        }
    };
}
