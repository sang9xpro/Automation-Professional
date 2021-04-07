package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.Social;
import com.automation.professional.domain.enumeration.TaskType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.Tasks} entity.
 */
public class TasksDTO implements Serializable {

    private Long id;

    private String taskName;

    private String description;

    private String source;

    private Double price;

    private Social social;

    private TaskType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Social getSocial() {
        return social;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TasksDTO)) {
            return false;
        }

        TasksDTO tasksDTO = (TasksDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tasksDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TasksDTO{" +
            "id=" + getId() +
            ", taskName='" + getTaskName() + "'" +
            ", description='" + getDescription() + "'" +
            ", source='" + getSource() + "'" +
            ", price=" + getPrice() +
            ", social='" + getSocial() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
