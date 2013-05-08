package edu.uc.labs.heartbeat.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "labs_machines")
public class Machine {

    @Id
    private long id;


}
