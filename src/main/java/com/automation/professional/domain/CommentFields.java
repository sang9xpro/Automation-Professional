package com.automation.professional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A CommentFields.
 */
@Entity
@Table(name = "comment_fields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "commentfields")
public class CommentFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "commentFields")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comment", "commentFields" }, allowSetters = true)
    private Set<CommentValues> commentValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommentFields id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public CommentFields fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<CommentValues> getCommentValues() {
        return this.commentValues;
    }

    public CommentFields commentValues(Set<CommentValues> commentValues) {
        this.setCommentValues(commentValues);
        return this;
    }

    public CommentFields addCommentValues(CommentValues commentValues) {
        this.commentValues.add(commentValues);
        commentValues.setCommentFields(this);
        return this;
    }

    public CommentFields removeCommentValues(CommentValues commentValues) {
        this.commentValues.remove(commentValues);
        commentValues.setCommentFields(null);
        return this;
    }

    public void setCommentValues(Set<CommentValues> commentValues) {
        if (this.commentValues != null) {
            this.commentValues.forEach(i -> i.setCommentFields(null));
        }
        if (commentValues != null) {
            commentValues.forEach(i -> i.setCommentFields(this));
        }
        this.commentValues = commentValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentFields)) {
            return false;
        }
        return id != null && id.equals(((CommentFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
