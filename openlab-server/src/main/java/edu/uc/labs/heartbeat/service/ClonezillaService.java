package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.exceptions.*;
import edu.uc.labs.heartbeat.dao.*;
import edu.uc.labs.heartbeat.models.RemoteImageTask;
import org.slf4j.Logger;
//import edu.uc.labs.springzilla.models.ClonezillaSettings;
//import edu.uc.labs.springzilla.models.MulticastSettings;
//import edu.uc.labs.springzilla.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class ClonezillaService {

    /*
     public List<ClonezillaSettings> getGeneralSettings()
     {
     List<ClonezillaSettings> lstSettings = settingsDao.getAll();
     if (lstSettings == null || lstSettings.isEmpty())
     {
     return null;
     }
     else
     {
     return lstSettings;
     }
     }

     public void saveGeneralSettings(ClonezillaSettings settings)
     {
     if (settingsDao.isSet(settings.getSettingName()))
     {
     ClonezillaSettings oldSettings = settingsDao.getSettingByName(settings.getSettingName());
     oldSettings.setSettingValue(settings.getSettingValue());
     settingsDao.update(oldSettings);
     }
     else
     {
     settingsDao.create(settings);
     }
     }

     public void saveMulticastSettings(MulticastSettings settings)
     {
     try
     {
     multicastDao.updateSettings(settings);
     }
     catch (IOException e)
     {
     throw new MulticastConfigException(e.getMessage());
     }
     }

     public MulticastSettings getMulticastSettings()
     {
     try
     {
     return multicastDao.getSettings();
     }
     catch (IOException e)
     {
     throw new MulticastConfigException(e.getMessage());
     }
     }
     */
    public RemoteImageTask isTaskPending(String serial, String mac, String ip) {
        RemoteImageTask rt = null;
        try {
            remoteImagingDao.expireTasksOlderThan(config.getInt("clonezilla.expireImageTask"));
            rt = remoteImagingDao.getTaskBySerialAndMac(serial, mac);
            if (rt == null) {
                return null;
            }
            return rt;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            ImageTaskException ie =  new ImageTaskException(e.getMessage());
            ie.setRemoteImageTask(rt);
            throw ie;
        } catch (Exception e) {
            log.error(e.getMessage());
            ImageTaskException ie =  new ImageTaskException(e.getMessage());
            ie.setRemoteImageTask(rt);
            throw ie;
        }
    }

    public boolean setupRemoteImaging(RemoteImageTask task) {
        try {
            log.info("Attempting to create the task in the database: ");
            remoteImagingDao.create(task);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new GenericDataException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GenericDataException(e.getMessage());
        }
        return true;
    }

    public String[] getImages() {
        log.info("Trying to get images from " + config.getString("clonezilla.imageHome"));
        String location = null;
        try {
            location = config.getString("clonezilla.imageHome");
            return imageDao.getImages(location);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new ImageListingException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ImageListingException(e.getMessage());
        }
    }
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private Config config;
    @Autowired
    private RemoteImagingDao remoteImagingDao;
    final private Logger log = LoggerFactory.getLogger(ClonezillaService.class);
}
