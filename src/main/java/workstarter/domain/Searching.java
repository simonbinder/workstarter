package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Searching.
 */
@Entity
@Table(name = "searching")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "searching")
public class Searching implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "searchingvalues")
    private String searchingvalues;

    @ManyToOne
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchingvalues() {
        return searchingvalues;
    }

    public Searching searchingvalues(String searchingvalues) {
        this.searchingvalues = searchingvalues;
        return this;
    }

    public void setSearchingvalues(String searchingvalues) {
        this.searchingvalues = searchingvalues;
    }

    public Student getStudent() {
        return student;
    }

    public Searching student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Searching searching = (Searching) o;
        if (searching.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, searching.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Searching{" +
            "id=" + id +
            ", searchingvalues='" + searchingvalues + "'" +
            '}';
    }
}
