package edu.uc.labs.heartbeat.domain;


public class ClientMachine {

    private String uuid;
    private String name;
    private String serialNumber;
    private String mac;
    private String os;
    private String osVersion;
    private String manufacturer;
    private String model;
    private String facility;
    private String currentUser;
    private String partition1;
    private String partition2;
    private String partition3;
    private String partition4;

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

        /**
     * @return the facility
     */
    public String getFacility() {
        return facility;
    }

    /**
     * @param facility the facility to set
     */
    public void setFacility(String facility) {
        this.facility = facility;
    }

    /**
     * @return the partition1
     */
    public String getPartition1() {
        return partition1;
    }

    /**
     * @param partition1 the partition1 to set
     */
    public void setPartition1(String partition1) {
        this.partition1 = partition1;
    }

    /**
     * @return the partition2
     */
    public String getPartition2() {
        return partition2;
    }

    /**
     * @param partition2 the partition2 to set
     */
    public void setPartition2(String partition2) {
        this.partition2 = partition2;
    }

    /**
     * @return the partition3
     */
    public String getPartition3() {
        return partition3;
    }

    /**
     * @param partition3 the partition3 to set
     */
    public void setPartition3(String partition3) {
        this.partition3 = partition3;
    }

    /**
     * @return the partition4
     */
    public String getPartition4() {
        return partition4;
    }

    /**
     * @param partition4 the partition4 to set
     */
    public void setPartition4(String partition4) {
        this.partition4 = partition4;
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
        sb.append("\tUser = ").append(this.getCurrentUser()).append("\n");
        sb.append("\tPartition1").append(this.getPartition1()).append("\n");
        sb.append("\tPartition2").append(this.getPartition2()).append("\n");
        sb.append("\tPartition3").append(this.getPartition3()).append("\n");
        sb.append("\tPartition4").append(this.getPartition4()).append("\n");
        sb.append("\tFacility").append(this.getFacility()).append("\n");
        return sb.toString();
    }




}
