package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A School.
 */
@Entity
@Table(name = "school")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "school")
public class School implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "core_subject", nullable = false)
	private String coreSubject;

	@Column(name = "target_degree")
	private String targetDegree;

	@Column(name = "start", nullable = false)
	private Date start;

	@Column(name = "end", nullable = false)
	private Date end;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public School name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoreSubjects() {
		return coreSubject;
	}

	public School coreSubjects(String coreSubjects) {
		this.coreSubject = coreSubjects;
		return this;
	}

	public void setCoreSubjects(String coreSubjects) {
		this.coreSubject = coreSubjects;
	}

	public String getTargetDegree() {
		return targetDegree;
	}

	public void setTargetDegree(String targetDegree) {
		this.targetDegree = targetDegree;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		School school = (School) o;
		if (school.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, school.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + ", coreSubject=" + coreSubject + ", targetDegree=" + targetDegree
				+ ", start=" + start + ", end=" + end + "]";
	}

}
