package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

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

	@NotNull
	@Column(name = "core_subject", nullable = false)
	private String coreSubject;

	@NotNull
	@Column(name = "target_degree")
	private String targetDegree;
	
	@NotNull
	@Column(name = "location")
	private String location;
	
	@Column(name = "fieldOfStudy")
    private String fieldOfStudy;

	@NotNull
	@Column(name = "start", nullable = false)
	private Date start;

	@NotNull
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

	public School coreSubjects(String coreSubjects) {
		this.coreSubject = coreSubjects;
		return this;
	}

	public String getTargetDegree() {
		return targetDegree;
	}

	public void setTargetDegree(String targetDegree) {
		this.targetDegree = targetDegree;
	}
	
	public School targetDegree(String targetDegree){
		this.targetDegree = targetDegree;
		return this;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	public School start(Date start){
		this.start = start;
		return this;
	}
	
	public School end(Date end){
		this.end = end;
		return this;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	public String getCoreSubject() {
		return coreSubject;
	}

	public void setCoreSubject(String coreSubject) {
		this.coreSubject = coreSubject;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public School location(String location){
		this.location = location;
		return this;
	}
	
	public String getFieldOfStudy() {
		return fieldOfStudy;
	}

	public void setFieldOfStudy(String fieldOfStudy) {
		this.fieldOfStudy = fieldOfStudy;
	}

	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + ", coreSubject=" + coreSubject + ", targetDegree=" + targetDegree
				+ ", location=" + location + ", fieldOfStudy=" + fieldOfStudy + ", start=" + start + ", end=" + end
				+ "]";
	}

}
