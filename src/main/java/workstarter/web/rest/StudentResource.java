package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.Student;

import workstarter.repository.StudentRepository;
import workstarter.repository.search.StudentSearchRepository;
import workstarter.security.SecurityUtils;
import workstarter.service.MailService;
import workstarter.service.StudentService;
import workstarter.service.dto.StudentDTO;
import workstarter.web.rest.util.HeaderUtil;
import workstarter.web.rest.vm.KeyAndPasswordVM;
import workstarter.web.rest.vm.ManagedStudentVM;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	private final StudentService studentService;
	private final MailService mailService;

	public StudentResource(StudentRepository studentRepository, StudentSearchRepository studentSearchRepository,
			StudentService studentService, MailService mailService) {
		this.studentRepository = studentRepository;
		this.studentSearchRepository = studentSearchRepository;
		this.studentService = studentService;
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

	/**
	 * POST /register : register the user.
	 *
	 * @param managedStudentVM
	 *            the managed user View Model
	 * @return the ResponseEntity with status 201 (Created) if the user is
	 *         registered or 400 (Bad Request) if the login or e-mail is already
	 *         in use
	 */
	@PostMapping(path = "/students/register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
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

	/**
	 * POST /account : update the current user information.
	 *
	 * @param studentDTO
	 *            the current user information
	 * @return the ResponseEntity with status 200 (OK), or status 400 (Bad
	 *         Request) or 500 (Internal Server Error) if the user couldn't be
	 *         updated
	 */
	@PostMapping("/students/account")
	@Timed
	public ResponseEntity saveAccount(@Valid @RequestBody StudentDTO studentDTO) {
		Optional<Student> existingUser = studentRepository.findOneByEmail(studentDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(studentDTO.getLogin()))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
					.body(null);
		}
		return studentRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).map(u -> {
			studentService.updateStudent(studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(),
					studentDTO.getLangKey());
			return new ResponseEntity(HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
