package edu.uc.labs.heartbeat.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "labs_cloneTask")
public class RemoteImageTask implements Serializable {

    private long id;
    private String uuid;
    private String mac;
    private String ipAddress;
    private String serialNumber;    
    private String imageName;
    private Date created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }   

    public void setId(long id) {
        this.id = id;
    }
    
    @Column(name = "uuid")
    public String getUuid(){
        return uuid;
    }
    
    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    @Column(name = "mac")
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Column(name = "ipAddress")
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Column(name = "serialNumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "imageName")
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    @Column(name = "created")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getCreated(){
        return this.created;
    }
    
    public void setCreated(Date created){
        this.created = created;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tuuid = ").append(this.uuid).append("\n");
        sb.append("\tMac Addresses = ").append(this.mac).append("\n");
        sb.append("\tIPAddress = ").append(this.ipAddress).append("\n");
        sb.append("\tSerial Number = ").append(this.serialNumber).append("\n");
        sb.append("\tCreated = ").append(this.created).append("\n");
        sb.append("\tID = ").append(this.id).append("\n");
        return sb.toString();
    }

}
