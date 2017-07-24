package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Student.
 */
@Entity(name = "Student")
@DiscriminatorValue("Student")
@Document(indexName = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student extends User {

	private static final long serialVersionUID = 1L;

	@Column(name = "matrikel_nummer")
	private String matrikelNummer;

	@Column(name = "title")
	private String title;

	@Column(name = "slogan")
	private String slogan;

	@OneToOne
	@JoinColumn(unique = true)
	private Portfolio portfolio;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Resume> resumes = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<School> schools = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Keywords> keywords = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Profession> professions = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Project> projects = new ArrayList<>();

	public String getMatrikelNummer() {
		return matrikelNummer;
	}

	public Student matrikelNummer(String matrikelNummer) {
		this.matrikelNummer = matrikelNummer;
		return this;
	}

	public void setMatrikelNummer(String matrikelNummer) {
		this.matrikelNummer = matrikelNummer;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public Student portfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
		return this;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public Set<Resume> getResumes() {
		return resumes;
	}

	public Student resumes(Set<Resume> resumes) {
		this.resumes = resumes;
		return this;
	}

	public Student addResumes(Resume resume) {
		this.resumes.add(resume);
		return this;
	}

	public Student removeResumes(Resume resume) {
		this.resumes.remove(resume);
		return this;
	}

	public void setResumes(Set<Resume> resumes) {
		this.resumes = resumes;
	}

	public List<School> getSchools() {
		return schools;
	}

	public Student addSchools(School school) {
		this.schools.add(school);
		return this;
	}

	public Student updateSchool(School oldSchool, School school) {
		int index = this.schools.indexOf(oldSchool);
		this.schools.set(index, school);
		return this;
	}

	public Student removeSchools(School schools) {
		this.schools.remove(schools);
		return this;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public Student addProfessions(Profession profession) {
		this.professions.add(profession);
		return this;
	}

	public Student updateProfession(Profession oldProfession, Profession profession) {
		int index = this.professions.indexOf(oldProfession);
		this.professions.set(index, profession);
		return this;
	}

	public Student removeProfession(Profession profession) {
		this.professions.remove(profession);
		return this;
	}

	public List<Profession> getProfessions() {
		return professions;
	}

	public void setProfessions(List<Profession> professions) {
		this.professions = professions;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Student addProject(Project project) {
		this.projects.add(project);
		return this;
	}

	public Student updateProjects(Project oldProject, Project project) {
		int index = this.projects.indexOf(oldProject);
		this.projects.set(index, project);
		return this;
	}

	public Student removeProject(Project project) {
		this.projects.remove(project);
		return this;
	}

	public List<Keywords> getKeywords() {
		return keywords;
	}

	public Student addKeywords(Keywords keywords) {
		this.keywords.add(keywords);
		return this;
	}

	public Student removeKeywords(Keywords keywords) {
		this.keywords.remove(keywords);
		return this;
	}

	public void setKeywords(List<Keywords> keywords) {
		this.keywords = keywords;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Student [matrikelNummer=" + matrikelNummer + ", portfolio=" + portfolio + ", resumes=" + resumes
				+ ", schools=" + schools + "]";
	}

}
