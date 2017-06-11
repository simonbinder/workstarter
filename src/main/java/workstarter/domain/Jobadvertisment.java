package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Jobadvertisment.
 */
@Entity
@Table(name = "jobadvertisment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobadvertisment")
public class Jobadvertisment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jobname", nullable = false)
    private String jobname;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "exercises")
    private String exercises;

    @Column(name = "contact")
    private String contact;

    @Column(name = "location")
    private String location;

    @Column(name = "tasks")
    private String tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobname() {
        return jobname;
    }

    public Jobadvertisment jobname(String jobname) {
        this.jobname = jobname;
        return this;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getDescription() {
        return description;
    }

    public Jobadvertisment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Jobadvertisment title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExercises() {
        return exercises;
    }

    public Jobadvertisment exercises(String exercises) {
        this.exercises = exercises;
        return this;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getContact() {
        return contact;
    }

    public Jobadvertisment contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public Jobadvertisment location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTasks() {
        return tasks;
    }

    public Jobadvertisment tasks(String tasks) {
        this.tasks = tasks;
        return this;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jobadvertisment jobadvertisment = (Jobadvertisment) o;
        if (jobadvertisment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jobadvertisment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Jobadvertisment{" +
            "id=" + id +
            ", jobname='" + jobname + "'" +
            ", description='" + description + "'" +
            ", title='" + title + "'" +
            ", exercises='" + exercises + "'" +
            ", contact='" + contact + "'" +
            ", location='" + location + "'" +
            ", tasks='" + tasks + "'" +
            '}';
    }
}
