package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Offering.
 */
@Entity
@Table(name = "offering")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "offering")
public class Offering implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offeringvalues")
    private String offeringvalues;

    @ManyToOne
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfferingvalues() {
        return offeringvalues;
    }

    public Offering offeringvalues(String offeringvalues) {
        this.offeringvalues = offeringvalues;
        return this;
    }

    public void setOfferingvalues(String offeringvalues) {
        this.offeringvalues = offeringvalues;
    }

    public Student getStudent() {
        return student;
    }

    public Offering student(Student student) {
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
        Offering offering = (Offering) o;
        if (offering.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, offering.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Offering{" +
            "id=" + id +
            ", offeringvalues='" + offeringvalues + "'" +
            '}';
    }
}
