package nl.everlutions.myradar.comm;

import com.octo.android.robospice.persistence.exception.SpiceException;

public class CustomSpiceException extends SpiceException {


    private String mDetailMessage;

    public CustomSpiceException(String detailMessage) {
        super(detailMessage);
        mDetailMessage = detailMessage;
    }

    public CustomSpiceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CustomSpiceException(Throwable throwable) {
        super(throwable);
    }

    public String getCustomMessage() {
        return mDetailMessage;
    }
}
