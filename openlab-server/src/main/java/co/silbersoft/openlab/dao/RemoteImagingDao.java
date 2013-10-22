package co.silbersoft.openlab.dao;

import co.silbersoft.openlab.models.*;

public interface RemoteImagingDao extends Dao<RemoteImageTask> {
    
    RemoteImageTask getTaskBySerialAndMac(String serial, String mac);
    void expireTasksOlderThan(int minutes);    
}
