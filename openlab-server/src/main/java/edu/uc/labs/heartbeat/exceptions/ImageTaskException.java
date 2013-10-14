package edu.uc.labs.heartbeat.exceptions;

import edu.uc.labs.heartbeat.models.RemoteImageTask;


public class ImageTaskException extends RuntimeException{
    public ImageTaskException(String msg){
        super(msg);
    }
    
    public void setRemoteImageTask(RemoteImageTask remoteImageTask){
        this.remoteImageTask = remoteImageTask;
    }
    
    public RemoteImageTask getRemoteImageTask(){
        return this.remoteImageTask;
    }
 
    private RemoteImageTask remoteImageTask;
}
