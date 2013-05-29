package edu.uc.labs.heartbeat.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "labs_machines")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Machine implements Serializable {

    private long id;
    private String uid;
    private String name;
    private String serialNumber;
    private String mac;
    private String ipAddress;
    private String os;
    private String osVersion;
    private String manufacturer;
    private String model;
    private Date lastSeen;
    private MachineGroup group;
    private String currentUser;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }


    @Column(name = "uid", unique = true, nullable = false)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(name = "Machine")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ServiceTag")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "MAC")
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

    @Column(name = "os")
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Column(name = "osVersion")
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Column(name = "Manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Column(name = "Model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "LastSeen")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "machine", cascade = CascadeType.ALL)
    public MachineGroup getGroup() {
        return group;
    }

    public void setGroup(MachineGroup group) {
        this.group = group;
    }

    @Column(name = "InUse")
    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tComputer Name = ").append(this.name).append("\n");
        sb.append("\tMac Addresses = ").append(this.mac).append("\n");
        sb.append("\tOS = ").append(this.os).append("\n");
        sb.append("\tOSVersion = ").append(this.osVersion).append("\n");
        sb.append("\tManufacturer = ").append(this.manufacturer).append("\n");
        sb.append("\tModel = ").append(this.model).append("\n");
        sb.append("\tSerial Number = ").append(this.serialNumber).append("\n");
        sb.append("\tUUID = ").append(this.uid).append("\n");
        sb.append("\tUser = ").append(this.currentUser).append("\n");
        sb.append("\tID = ").append(this.id).append("\n");
        return sb.toString();
    }
}
