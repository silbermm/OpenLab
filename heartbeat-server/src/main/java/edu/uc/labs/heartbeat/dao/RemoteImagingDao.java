package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.models.*;

public interface RemoteImagingDao extends Dao<RemoteImageTask> {
    
    RemoteImageTask getTaskBySerialAndMac(String serial, String mac);
    void expireTasksOlderThan(int minutes);    
}
