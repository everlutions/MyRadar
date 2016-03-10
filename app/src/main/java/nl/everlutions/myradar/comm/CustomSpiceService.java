package nl.everlutions.myradar.comm;

import android.app.Application;
import android.util.Log;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

import java.util.ArrayList;
import java.util.List;

import nl.everlutions.myradar.BuildConfig;
import nl.everlutions.myradar.dto.BaseObjectDto;
import roboguice.util.temp.Ln;

public class CustomSpiceService extends SpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        List<Class<?>> cacheableClasses = new ArrayList<Class<?>>();
        cacheableClasses.add(BaseObjectDto.class);


        Ln.getConfig().setLoggingLevel(BuildConfig.DEBUG ? Log.DEBUG : Log.WARN);

        // // init
        Jackson2ObjectPersisterFactory jacksonObjectPersisterFactory = new Jackson2ObjectPersisterFactory(
                application, cacheableClasses);
        cacheManager.addPersister(jacksonObjectPersisterFactory);

        return cacheManager;
    }

    @Override
    public int getThreadCount() {
        return 3;
    }
}