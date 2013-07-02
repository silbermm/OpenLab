package edu.uc.labs.heartbeat.service;

import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.exceptions.*;
import edu.uc.labs.heartbeat.dao.*;
//import edu.uc.labs.springzilla.models.ClonezillaSettings;
//import edu.uc.labs.springzilla.models.MulticastSettings;
//import edu.uc.labs.springzilla.json.*;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

@Service
@Transactional
public class ClonezillaService {

    private static final Logger log = Logger.getLogger(ClonezillaService.class);
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

    public String[] getImages() {
        String location = null;
        try {
            location = config.getString("clonezilla.imagehome");
            return imageDao.getImages(location);
        } catch (RuntimeException e){
            throw new ImageListingException(e.getMessage());
        }
    }

    /*public boolean startRestoreSession(CloneJson cloneData) throws SessionException {
        drblDao.startSession(cloneData);
        return true;
    }*/
    //@Autowired private SettingsDao settingsDao;
    //@Autowired private MulticastDao multicastDao;
    @Autowired private ImageDao imageDao;   
    @Autowired private Config config;
    //@Autowired private DrblDao drblDao;
}
