package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A CommentValues.
 */
@Entity
@Table(name = "comment_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "commentvalues")
public class CommentValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commentValues", "mostOfContent" }, allowSetters = true)
    private Comment comment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commentValues" }, allowSetters = true)
    private CommentFields commentFields;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommentValues id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public CommentValues value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Comment getComment() {
        return this.comment;
    }

    public CommentValues comment(Comment comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public CommentFields getCommentFields() {
        return this.commentFields;
    }

    public CommentValues commentFields(CommentFields commentFields) {
        this.setCommentFields(commentFields);
        return this;
    }

    public void setCommentFields(CommentFields commentFields) {
        this.commentFields = commentFields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentValues)) {
            return false;
        }
        return id != null && id.equals(((CommentValues) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentValues{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
