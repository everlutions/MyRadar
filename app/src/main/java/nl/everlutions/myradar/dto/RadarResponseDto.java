package nl.everlutions.myradar.dto;

import com.octo.android.robospice.persistence.exception.SpiceException;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RadarResponseDto {

    public ArrayList<String> mImageTimes;
    public int mRadarmageWidth;
    public int mRadarImageHeight;
    public ArrayList<Bitmap> radarImageBitmapList;

    public RadarResponseDto(byte[] completeRadarImageByteArray, Map<String, List<String>> headerFields)
            throws SpiceException {
        initImageTimes(headerFields);
        initSize(headerFields);
        initRadarImages(completeRadarImageByteArray);
    }

    private void initImageTimes(Map<String, List<String>> headerFields) {
        mImageTimes = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            try {
                String time = headerFields.get("X-Date-" + i).get(0);
                try {
                    Date date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.UK).parse(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    mImageTimes.add(sdf.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {
                break;
            }
        }
    }

    private void initSize(Map<String, List<String>> headerFields) {
        String[] size = null;
        if (headerFields.get("X-Size") == null) {
            size = headerFields.get("X-Size").get(0).split("x");
        } else {
            size = headerFields.get("x-size").get(0).split("x");
        }
        mRadarmageWidth = Integer.parseInt(size[0]);
        mRadarImageHeight = Integer.parseInt(size[1]);
    }

    public void initRadarImages(byte[] completeRadarImageByteArray) throws SpiceException {
        Bitmap completeRadarImageBitmap = BitmapFactory
                .decodeByteArray(completeRadarImageByteArray, 0, completeRadarImageByteArray.length);
        int numberOfRadarImages = completeRadarImageBitmap.getWidth() / mRadarmageWidth;

        radarImageBitmapList = new ArrayList<>();
        try {
            for (int i = 0; i < numberOfRadarImages; i++) {

                Bitmap singleRadarImageBitmap = null;
                int xOfNextRadarImage = i * mRadarmageWidth;

                singleRadarImageBitmap = Bitmap.createBitmap(completeRadarImageBitmap, xOfNextRadarImage, 0,
                        mRadarmageWidth, mRadarImageHeight);
                radarImageBitmapList.add(singleRadarImageBitmap);
            }
        } catch (Exception e) {
            throw new SpiceException(e.getMessage());
        } catch (OutOfMemoryError e) {
            throw new SpiceException("Out Of Mem");
        }
    }


    // BUG in 4.4 Bitmap transparency is set to black
    // SO what we do here is set all the black pixels to transparent.
    // If the img src is from an URL please add '&ext=png' to the imag url
    // it wil make the img transparency be valid
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static Bitmap eraseBlackFromBitmap(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap b = src.copy(Bitmap.Config.ARGB_8888, true);
        b.setHasAlpha(true);

        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            if (pixels[i] == color) {
                pixels[i] = 0;
            }
        }

        b.setPixels(pixels, 0, width, 0, 0, width, height);

        return b;
    }
}
