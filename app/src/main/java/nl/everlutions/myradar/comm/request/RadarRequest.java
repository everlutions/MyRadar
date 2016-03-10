package nl.everlutions.myradar.comm.request;

import com.octo.android.robospice.persistence.exception.SpiceException;

import org.json.JSONObject;

import nl.everlutions.myradar.comm.CustomSpiceRequest;
import nl.everlutions.myradar.comm.HTTPRadarRequest;
import nl.everlutions.myradar.dto.RadarResponseDto;

public class RadarRequest extends CustomSpiceRequest<RadarResponseDto> {

    public final String TAG = this.getClass().getSimpleName();
    private final String mRadarUrl;
    private JSONObject mJsonPayLoad;

    public RadarRequest(String radarUrl) {
        super(RadarResponseDto.class);
        mRadarUrl = radarUrl;
    }

    @Override
    public RadarResponseDto loadDataFromNetwork() throws SpiceException {
        try {
            RadarResponseDto radarData = HTTPRadarRequest.doRequest("GET",
                    mRadarUrl, null, null);
            return radarData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SpiceException(e);
        }
    }
}