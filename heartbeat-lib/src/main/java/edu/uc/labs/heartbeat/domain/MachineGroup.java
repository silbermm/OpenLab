package edu.uc.labs.heartbeat.domain;

import java.io.Serializable;

public class MachineGroup implements Serializable {

    private long id;
    private String shortName;
    private String longName;
    private String description;
    private ClientMachine machine;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientMachine getMachine(){
        return this.machine;
    }

    public void setMachine(ClientMachine machine){
        this.machine = machine;
    }
}
