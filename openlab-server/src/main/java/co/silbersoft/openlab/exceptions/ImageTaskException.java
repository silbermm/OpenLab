package co.silbersoft.openlab.exceptions;

import co.silbersoft.openlab.models.RemoteImageTask;


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
