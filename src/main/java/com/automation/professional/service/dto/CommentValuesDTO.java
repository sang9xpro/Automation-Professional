package com.automation.professional.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.CommentValues} entity.
 */
public class CommentValuesDTO implements Serializable {

    private Long id;

    private String value;

    private CommentDTO comment;

    private CommentFieldsDTO commentFields;

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

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    public CommentFieldsDTO getCommentFields() {
        return commentFields;
    }

    public void setCommentFields(CommentFieldsDTO commentFields) {
        this.commentFields = commentFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentValuesDTO)) {
            return false;
        }

        CommentValuesDTO commentValuesDTO = (CommentValuesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentValuesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentValuesDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", comment=" + getComment() +
            ", commentFields=" + getCommentFields() +
            "}";
    }
}
