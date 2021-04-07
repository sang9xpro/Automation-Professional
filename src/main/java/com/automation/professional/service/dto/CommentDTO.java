package com.automation.professional.service.dto;

import com.automation.professional.domain.enumeration.Social;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.automation.professional.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {

    private Long id;

    private String content;

    @Lob
    private byte[] image;

    private String imageContentType;
    private Social social;

    private String owner;

    private MostOfContentDTO mostOfContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Social getSocial() {
        return social;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public MostOfContentDTO getMostOfContent() {
        return mostOfContent;
    }

    public void setMostOfContent(MostOfContentDTO mostOfContent) {
        this.mostOfContent = mostOfContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", social='" + getSocial() + "'" +
            ", owner='" + getOwner() + "'" +
            ", mostOfContent=" + getMostOfContent() +
            "}";
    }
}
