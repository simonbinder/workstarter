package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;

import workstarter.domain.CompanyAdmin;
import workstarter.domain.Jobadvertisment;
import workstarter.domain.Keywords;
import workstarter.domain.Profession;
import workstarter.domain.School;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.repository.CompanyAdminRepository;
import workstarter.repository.JobadvertismentRepository;
import workstarter.repository.StudentRepository;
import workstarter.repository.search.CompanyAdminSearchRepository;
import workstarter.repository.search.StudentSearchRepository;
import workstarter.repository.search.UserSearchRepository;
import workstarter.security.SecurityUtils;
import workstarter.service.MailService;
import workstarter.service.StudentService;
import workstarter.service.dto.StudentDTO;
import workstarter.web.rest.util.HeaderUtil;
import workstarter.web.rest.vm.ManagedStudentVM;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Student.
 */
@RestController
@RequestMapping("/api")
public class StudentResource {

	private final Logger log = LoggerFactory.getLogger(StudentResource.class);

	private static final String ENTITY_NAME = "student";

	private final StudentRepository studentRepository;
	private final StudentSearchRepository studentSearchRepository;
	private final CompanyAdminSearchRepository companyAdminSearchRepository;
	private final StudentService studentService;
	private final CompanyAdminRepository companyAdminRepository;
	private final JobadvertismentRepository jobadvertismentRepository;
	private final MailService mailService;

	public StudentResource(StudentRepository studentRepository, StudentSearchRepository studentSearchRepository,
			CompanyAdminSearchRepository companyAdminSearchRepository, StudentService studentService,
			CompanyAdminRepository companyAdminRepository, JobadvertismentRepository jobadvertismentRepository,
			MailService mailService) {
		this.studentRepository = studentRepository;
		this.studentSearchRepository = studentSearchRepository;
		this.companyAdminSearchRepository = companyAdminSearchRepository;
		this.studentService = studentService;
		this.companyAdminRepository = companyAdminRepository;
		this.jobadvertismentRepository = jobadvertismentRepository;
		this.mailService = mailService;
	}

	/**
	 * POST /students : Create a new student.
	 *
	 * @param student
	 *            the student to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new student, or with status 400 (Bad Request) if the student has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/students")
	@Timed
	public ResponseEntity<Student> createStudent(@RequestBody Student student) throws URISyntaxException {
		log.debug("REST request to save Student : {}", student);
		if (student.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new student cannot already have an ID"))
					.body(null);
		}
		Student result = studentRepository.save(student);
		studentSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/students/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /students : Updates an existing student.
	 *
	 * @param student
	 *            the student to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         student, or with status 400 (Bad Request) if the student is not
	 *         valid, or with status 500 (Internal Server Error) if the student
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/students")
	@Timed
	public ResponseEntity<Student> updateStudent(@RequestBody Student student) throws URISyntaxException {
		log.debug("REST request to update Student : {}", student);
		if (student.getId() == null) {
			return createStudent(student);
		}
		Student result = studentRepository.save(student);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, student.getId().toString()))
				.body(result);
	}

	/**
	 * GET /students/schools : get all the students.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of students
	 *         in body
	 */
	@GetMapping("/students/{id}/schools")
	@Timed
	public List<School> getAllSchools(@PathVariable Long id) {
		log.debug("REST request to get all Schools for one Student");
		List<School> schools = studentService.getSchools(id);
		return schools;
	}

	/**
	 * PUT /students/{id}/schools : Updates an existing student.
	 *
	 * @param school
	 *            the school to add
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         student, or with status 400 (Bad Request) if the student is not
	 *         valid, or with status 500 (Internal Server Error) if the student
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/students/{id}/schools")
	@Timed
	public ResponseEntity<Student> addSchool(@PathVariable Long id, @Valid @RequestBody School school)
			throws URISyntaxException {
		log.debug("REST request to update Student : {}", school);
		Student result = studentService.addSchool(id, school);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@DeleteMapping("/students/{id}/schools/{schoolid}")
	@Timed
	public ResponseEntity<Void> deleteSchool(@PathVariable Long id, @PathVariable Long schoolid) {
		log.debug("REST request to delete School : {}", id);
		Student result = studentService.deleteSchool(id, schoolid);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, result.getId().toString()))
				.build();
	}

	/**
	 * PUT /students/{id}/schools({id} : Updates an existing school for an
	 * existing student.
	 *
	 * @param school
	 *            the school to add
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         student, or with status 400 (Bad Request) if the student is not
	 *         valid, or with status 500 (Internal Server Error) if the student
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/students/{id}/schools/{schoolid}")
	@Timed
	public ResponseEntity<Student> updateSchool(@PathVariable Long id, @PathVariable Long schoolid,
			@Valid @RequestBody School school) throws URISyntaxException {
		log.debug("REST request to update School : {}", school);
		Student result = studentService.updateSchool(id, schoolid, school);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * GET /students/schools : get all the students.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of students
	 *         in body
	 */
	@GetMapping("/students/{id}/professions")
	@Timed
	public List<Profession> getAllProfessions(@PathVariable Long id) {
		log.debug("REST request to get all professions for one Student");
		List<Profession> profession = studentService.getProfessions(id);
		return profession;
	}

	@GetMapping("/students/{id}/keywords")
	@Timed
	public List<Keywords> getAllKeywords(@PathVariable Long id) {
		log.debug("REST request to get all keywords for one Student");
		List<Keywords> keywords = studentService.getKeywords(id);
		return keywords;
	}

	@PostMapping("/students/{id}/keywords")
	@Timed
	public ResponseEntity<Student> addKeyword(@PathVariable Long id, @Valid @RequestBody Keywords keywords)
			throws URISyntaxException {
		log.debug("REST request to save Keywords : {}", keywords);
		Student result = studentService.addKeyword(id, keywords);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@DeleteMapping("/students/{id}/keywords/{keywordid}")
	@Timed
	public ResponseEntity<Void> deleteKeyword(@PathVariable Long id, @PathVariable Long keywordid) {
		log.debug("REST request to delete keyword : {}", id);
		Student result = studentService.deleteKeyword(id, keywordid);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, result.getId().toString()))
				.build();
	}

	@PostMapping("/students/{id}/profession")
	@Timed
	public ResponseEntity<Student> addProfession(@PathVariable Long id, @Valid @RequestBody Profession profession)
			throws URISyntaxException {
		log.debug("REST request to update Student : {}", profession);
		Student result = studentService.addProfession(id, profession);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@PutMapping("/students/{id}/profession/{professionid}")
	@Timed
	public ResponseEntity<Student> updateProfession(@PathVariable Long id, @PathVariable Long professionid,
			@Valid @RequestBody Profession profession) throws URISyntaxException {
		log.debug("REST request to update School : {}", profession);
		Student result = studentService.updateProfession(id, professionid, profession);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@DeleteMapping("/students/{id}/profession/{professionid}")
	@Timed
	public ResponseEntity<Void> deleteProfession(@PathVariable Long id, @PathVariable Long professionid) {
		log.debug("REST request to delete profession : {}", id);
		Student result = studentService.deleteProfession(id, professionid);
		studentSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, result.getId().toString()))
				.build();
	}

	@GetMapping("/students/{id}/slogan")
	@Timed
	public String getSlogan(@PathVariable Long id) {
		log.debug("REST request to get slogan for one Student");
		String slogan = studentService.getSlogan(id);
		return slogan;
	}

	@PutMapping("/students/{id}/slogan")
	@Timed
	public ResponseEntity<Student> updateSlogan(@PathVariable Long id, @Valid @RequestBody String slogan)
			throws URISyntaxException {
		log.debug("REST request to update School : {}", slogan);
		Student result = studentService.updateSlogan(id, slogan);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * GET /students : get all the students.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of students
	 *         in body
	 */
	@GetMapping("/students")
	@Timed
	public List<Student> getAllStudents() {
		log.debug("REST request to get all Students");
		List<Student> students = studentService.getAllStudents();
		return students;
	}

	/**
	 * GET /students/:id : get the "id" student.
	 *
	 * @param id
	 *            the id of the student to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         student, or with status 404 (Not Found)
	 */
	@GetMapping("/students/{id}")
	@Timed
	public ResponseEntity<Student> getStudent(@PathVariable Long id) {
		log.debug("REST request to get Student : {}", id);
		Student student = studentRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(student));
	}

	/**
	 * DELETE /students/:id : delete the "id" student.
	 *
	 * @param id
	 *            the id of the student to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/students/{id}")
	@Timed
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		log.debug("REST request to delete Student : {}", id);
		studentRepository.delete(id);
		studentSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/students?query=:query : search for the student
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the student search
	 * @return the result of the search
	 */
	@GetMapping("/_search/students")
	@Timed
	public List<Student> searchStudents(@RequestParam String query) {
		log.debug("REST request to search Students for query {}", query);
		return StreamSupport.stream(studentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

	@GetMapping("/_search/allaccounts")
	@Timed
	public List<User> searchAllAccounts(@RequestParam String query) {
		log.debug("REST request to search Users for query {}", query);
		List<User> students = StreamSupport
				.stream(studentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		List<User> allAccounts = StreamSupport
				.stream(companyAdminSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		allAccounts.addAll(students);
		return allAccounts;
	}

	/**
	 * POST /register : register the user.
	 *
	 * @param managedStudentVM
	 *            the managed user View Model
	 * @return the ResponseEntity with status 201 (Created) if the user is
	 *         registered or 400 (Bad Request) if the login or e-mail is already
	 *         in use
	 */
	@PostMapping(path = "/students/register", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.TEXT_PLAIN_VALUE })
	@Timed
	public ResponseEntity registerAccount(@Valid @RequestBody ManagedStudentVM managedStudentVM) {

		HttpHeaders textPlainHeaders = new HttpHeaders();
		textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

		return studentRepository.findOneByLogin(managedStudentVM.getLogin().toLowerCase())
				.map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
				.orElseGet(() -> studentRepository.findOneByEmail(managedStudentVM.getEmail())
						.map(user -> new ResponseEntity<>("e-mail address already in use", textPlainHeaders,
								HttpStatus.BAD_REQUEST))
						.orElseGet(() -> {
							Student student = studentService.createStudent(managedStudentVM.getLogin(),
									managedStudentVM.getPassword(), managedStudentVM.getFirstName(),
									managedStudentVM.getLastName(), managedStudentVM.getEmail().toLowerCase(),
									managedStudentVM.getImageUrl(), managedStudentVM.getLangKey());

							mailService.sendActivationEmail(student);
							return new ResponseEntity<>(HttpStatus.CREATED);
						}));
	}

	@PostMapping(path = "/students/apply/{companyadminid}/jobadvertisment/{jobid}")
	@Timed
	public ResponseEntity sendApplicationMail(@PathVariable Long companyadminid, @PathVariable Long jobid,
			@RequestBody String message) {
		CompanyAdmin companyAdmin = companyAdminRepository.findOne(companyadminid);
		Jobadvertisment jobadvertisment = jobadvertismentRepository.getOne(jobid);
		Optional<Student> student = studentRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Student user = student.get();
		mailService.sendApplicationEmail(companyAdmin, user, jobadvertisment, message);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * POST /account : update the current user information.
	 *
	 * @param student
	 *            the current user information
	 * @return the ResponseEntity with status 200 (OK), or status 400 (Bad
	 *         Request) or 500 (Internal Server Error) if the user couldn't be
	 *         updated
	 */
	@PostMapping("/students/account")
	@Timed
	public ResponseEntity saveAccount(@RequestBody Student student) {
		Student existingUser = studentRepository.findOne(student.getId());
		if (!existingUser.getLogin().equalsIgnoreCase(student.getLogin())) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
					.body(null);
		}
		return studentRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).map(u -> {
			studentService.updateStudent(student.getFirstName(), student.getLastName(), student.getEmail(),
					student.getTitle(), student.getSlogan());
			return new ResponseEntity(HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	  @Autowired
	    private HttpServletRequest request;
	
	@RequestMapping(value = "/students/{id}/updatefile", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity handleFileUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		// TODO check if file already exists
        if (!file.isEmpty()) {
            try {
                String uploadsDir = "/uploads/";
                String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
                if(! new File(realPathtoUploads).exists())
                {
                    new File(realPathtoUploads).mkdir();
                }
                log.info("realPathtoUploads = {}", realPathtoUploads);
                String orgName = file.getOriginalFilename();
                String filePath = realPathtoUploads + "\\" +  orgName;
                File dest = new File(filePath);
                file.transferTo(dest);
                studentService.updateImage(id, dest.getAbsolutePath());
            } catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return null;
	}
}
