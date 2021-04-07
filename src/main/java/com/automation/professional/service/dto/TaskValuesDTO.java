package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.TaskValues} entity.
 */
public class TaskValuesDTO implements Serializable {

    private Long id;

    private String value;

    private TasksDTO tasks;

    private TaskFieldsDTO taskFields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TasksDTO getTasks() {
        return tasks;
    }

    public void setTasks(TasksDTO tasks) {
        this.tasks = tasks;
    }

    public TaskFieldsDTO getTaskFields() {
        return taskFields;
    }

    public void setTaskFields(TaskFieldsDTO taskFields) {
        this.taskFields = taskFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskValuesDTO)) {
            return false;
        }

        TaskValuesDTO taskValuesDTO = (TaskValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", tasks=" + getTasks() +
            ", taskFields=" + getTaskFields() +
            "}";
    }
}
