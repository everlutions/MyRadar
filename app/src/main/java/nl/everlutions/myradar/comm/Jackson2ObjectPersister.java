package nl.everlutions.myradar.comm;

import android.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.springandroid.SpringAndroidObjectPersister;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import nl.everlutions.myradar.utils.Utils;


public final class Jackson2ObjectPersister<T> extends SpringAndroidObjectPersister<T> {

    public final ObjectMapper mJsonMapper;

    public Jackson2ObjectPersister(Application application, Class<T> clazz, File cacheFolder) throws CacheCreationException {
        super(application, clazz, cacheFolder);
        mJsonMapper = Utils.getObjectMapper();
    }

    public Jackson2ObjectPersister(Application application, Class<T> clazz) throws CacheCreationException {
        this(application, clazz, null);
    }

    // ============================================================================================
    // METHODS
    // ============================================================================================

    @Override
    protected T deserializeData(String json) throws CacheLoadingException {
        try {
            return mJsonMapper.readValue(json, getHandledClass());
        } catch (Exception e) {
            throw new CacheLoadingException(e);
        }
    }

    @Override
    protected void saveData(T data, Object cacheKey) throws IOException, CacheSavingException {
        String resultJson;
        // transform the content in json to store it in the cache
        resultJson = mJsonMapper.writeValueAsString(data);

        synchronized (getCacheFile(cacheKey).getAbsolutePath().intern()) {
            // finally store the json in the cache
            if (!StringUtils.isEmpty(resultJson)) {
                FileUtils.writeStringToFile(getCacheFile(cacheKey), resultJson, CharEncoding.UTF_8);
            } else {
                throw new CacheSavingException("Data was null and could not be serialized in json");
            }
        }
    }

}
