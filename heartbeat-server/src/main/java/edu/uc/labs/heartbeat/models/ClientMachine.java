package edu.uc.labs.heartbeat.models;

public class ClientMachine {

    private String uuid;
    private String name;
    private String serialNumber;
    private String mac;
    private String os;
    private String osVersion;
    private String manufacturer;
    private String model;
    private String currentUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tComputer Name = ").append(this.name).append("\n");
        sb.append("\tMac Addresses = ").append(this.mac).append("\n");
        sb.append("\tOS = ").append(this.os).append("\n");
        sb.append("\tOSVersion = ").append(this.osVersion).append("\n");
        sb.append("\tManufacturer = ").append(this.manufacturer).append("\n");
        sb.append("\tModel = ").append(this.model).append("\n");
        sb.append("\tSerial Number = ").append(this.serialNumber).append("\n");
        sb.append("\tUUID = ").append(this.uuid).append("\n");
        sb.append("\tUser = ").append(this.currentUser).append("\n");
        return sb.toString();
    }


}
