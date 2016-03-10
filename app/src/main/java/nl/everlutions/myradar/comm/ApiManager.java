package nl.everlutions.myradar.comm;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;

import nl.everlutions.myradar.comm.request.RadarRequest;
import nl.everlutions.myradar.dto.RadarResponseDto;
import nl.everlutions.myradar.utils.Constants;

@SuppressLint("SimpleDateFormat")
public class ApiManager implements Constants {

    private SpiceManager mSpiceManager;
    private Context mContext;

    public final String TAG = this.getClass().getSimpleName();
    private ArrayList<SpiceRequest> mCurrentTasks = new ArrayList<SpiceRequest>();

    public ApiManager(Context context, SpiceManager manager) {
        mSpiceManager = manager;
        mContext = context;
    }

    public SpiceRequest getRadarImages(String radarUrl, RequestListener<RadarResponseDto> requestListener) {
        SpiceRequest request = new RadarRequest(radarUrl);
        mSpiceManager.execute(request, requestListener);
        mCurrentTasks.add(request);
        return request;
    }

    public void cancelCurrentTasks() {
        for (int i = 0; i < mCurrentTasks.size(); i++) {
            mSpiceManager.cancel(mCurrentTasks.get(i));
        }
        mCurrentTasks.clear();
    }
}
