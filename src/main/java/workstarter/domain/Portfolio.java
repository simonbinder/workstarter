package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "portfolio")
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "title_image")
    private String titleImage;

    @Column(name = "appendix")
    private String appendix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Portfolio title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Portfolio description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public Portfolio titleImage(String titleImage) {
        this.titleImage = titleImage;
        return this;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getAppendix() {
        return appendix;
    }

    public Portfolio appendix(String appendix) {
        this.appendix = appendix;
        return this;
    }

    public void setAppendix(String appendix) {
        this.appendix = appendix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Portfolio portfolio = (Portfolio) o;
        if (portfolio.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, portfolio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", titleImage='" + titleImage + "'" +
            ", appendix='" + appendix + "'" +
            '}';
    }
}
