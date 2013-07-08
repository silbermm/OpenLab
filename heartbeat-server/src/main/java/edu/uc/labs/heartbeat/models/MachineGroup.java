package edu.uc.labs.heartbeat.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "machine_groups")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@group_id")
public class MachineGroup implements Serializable {

    private long groupId;
    private String name;
    private String description;
    private Set<Machine> machines;
		private double xCoordinate;
		private double yCoordinate;

    @Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false, unique = true)
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

		@Column(name = "xCoordinate")
		public void setXCoordinate(double xCoordinate){
			this.xCoordinate = xCoordinate;
		}

		public double getXCoordinate(){
			return this.xCoordinate;
		}
		
		@Column(name = "yCoordinate")
		public void setYCoordinate(double yCoordinate){
			this.yCoordinate = yCoordinate;
		}

		public double getYCoordinate(){
			return this.yCoordinate;
		}

		@OneToMany(mappedBy="group",cascade={CascadeType.ALL})	
    public Set<Machine> getMachines(){
        return this.machines;
    }

    public void setMachines(Set<Machine> machines){
        this.machines = machines;
    }
}
