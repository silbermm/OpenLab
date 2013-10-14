package edu.uc.labs.heartbeat.dao;

import java.io.*;

public class ImageDaoImpl implements ImageDao {

  public String[] getImages(String location){
    File file = new File(location);
    if(file.exists()){
      String[] directories = file.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return new File(dir, name).isDirectory();
        }
      }); 
      return directories;
    } else {
      throw new RuntimeException("Directory " + location + " doesn't exist!");   
    }	
  }
}
