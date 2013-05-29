package edu.uc.labs.heartbeat.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class MachineGroup implements Serializable {

    private long id;
    private String shortName;
    private String longName;
    private String description;
    private Machine machine;


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

    public Machine getMachine(){
        return this.machine;
    }

    public void setMachine(Machine machine){
        this.machine = machine;
    }
}
