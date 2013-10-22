package co.silbersoft.openlab.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "labs_currentTasks")
public class MachineTask {
    
    private Long id;
    private String name;
    private String description;
    private String status;
    private Long completionPercentage;
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column
    public Long getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Long completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
    
    
    
}
