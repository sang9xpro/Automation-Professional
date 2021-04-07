package com.automation.professional.domain;

import com.automation.professional.domain.enumeration.Social;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "social")
    private Social social;

    @Column(name = "owner")
    private String owner;

    @OneToMany(mappedBy = "comment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comment", "commentFields" }, allowSetters = true)
    private Set<CommentValues> commentValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "mostOfContValues", "comments", "facebook" }, allowSetters = true)
    private MostOfContent mostOfContent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Comment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Comment image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Comment imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Social getSocial() {
        return this.social;
    }

    public Comment social(Social social) {
        this.social = social;
        return this;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    public String getOwner() {
        return this.owner;
    }

    public Comment owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<CommentValues> getCommentValues() {
        return this.commentValues;
    }

    public Comment commentValues(Set<CommentValues> commentValues) {
        this.setCommentValues(commentValues);
        return this;
    }

    public Comment addCommentValues(CommentValues commentValues) {
        this.commentValues.add(commentValues);
        commentValues.setComment(this);
        return this;
    }

    public Comment removeCommentValues(CommentValues commentValues) {
        this.commentValues.remove(commentValues);
        commentValues.setComment(null);
        return this;
    }

    public void setCommentValues(Set<CommentValues> commentValues) {
        if (this.commentValues != null) {
            this.commentValues.forEach(i -> i.setComment(null));
        }
        if (commentValues != null) {
            commentValues.forEach(i -> i.setComment(this));
        }
        this.commentValues = commentValues;
    }

    public MostOfContent getMostOfContent() {
        return this.mostOfContent;
    }

    public Comment mostOfContent(MostOfContent mostOfContent) {
        this.setMostOfContent(mostOfContent);
        return this;
    }

    public void setMostOfContent(MostOfContent mostOfContent) {
        this.mostOfContent = mostOfContent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", social='" + getSocial() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
