package workstarter.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import workstarter.domain.Company;
import workstarter.domain.Jobadvertisment;
import workstarter.domain.School;
import workstarter.domain.Student;
import workstarter.repository.CompanyRepository;
import workstarter.repository.JobadvertismentRepository;
import workstarter.repository.search.CompanySearchRepository;

@Service
@Transactional
public class CompanyService {

	private final Logger log = LoggerFactory.getLogger(CompanyService.class);
	private final CompanyRepository companyRepository;
	private final JobadvertismentRepository jobadvertismentRepository;
	private final CompanySearchRepository companySearchRepository;

	public CompanyService(CompanyRepository companyRepository, JobadvertismentRepository jobadvertismentRepository,
			CompanySearchRepository companySearchRepository) {
		this.companyRepository = companyRepository;
		this.jobadvertismentRepository = jobadvertismentRepository;
		this.companySearchRepository = companySearchRepository;
	}

	public List<Jobadvertisment> getJobs(Long id) {
		Company company = companyRepository.getOne(id);
		return company.getJobs();
	}

	public Company addJob(Long id, Jobadvertisment job) {
		Company company = companyRepository.getOne(id);
		jobadvertismentRepository.save(job);
		company.addJob(job);
		companyRepository.save(company);
		log.debug("Added jobad for Company: {}", company);
		return company;
	}
	
	public Company deleteJob(Long id, Long jobid){
		Jobadvertisment jobadvertisment = jobadvertismentRepository.getOne(jobid);
		Company company = companyRepository.getOne(id);
		company.removeJob(jobadvertisment);
		companyRepository.save(company);
		return company;
	}

	public Company updateJob(Long companyID, Long jobID, Jobadvertisment jobadvertisment) {
		Company company = companyRepository.getOne(companyID);
		Jobadvertisment oldJobadvertisment = jobadvertismentRepository.getOne(jobID);
//		company.updateJob(oldJobadvertisment, jobadvertisment);
		oldJobadvertisment.jobname(jobadvertisment.getJobname());
		oldJobadvertisment.description(jobadvertisment.getDescription());
		oldJobadvertisment.title(jobadvertisment.getTitle());
		oldJobadvertisment.exercises(jobadvertisment.getExercises());
		oldJobadvertisment.contact(jobadvertisment.getContact());
		oldJobadvertisment.location(jobadvertisment.getLocation());
		oldJobadvertisment.tasks(jobadvertisment.getTasks());
		companyRepository.save(company);
		log.debug("Updated job for Company: {}", company);
		return company;
	}
}
