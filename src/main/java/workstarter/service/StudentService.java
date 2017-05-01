package workstarter.service;

import workstarter.domain.Authority;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.repository.AuthorityRepository;
import workstarter.config.Constants;
import workstarter.repository.StudentRepository;
import workstarter.repository.search.StudentSearchRepository;
import workstarter.repository.search.UserSearchRepository;
import workstarter.security.AuthoritiesConstants;
import workstarter.security.SecurityUtils;
import workstarter.service.util.RandomUtil;
import workstarter.service.dto.StudentDTO;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class StudentService {

	private final Logger log = LoggerFactory.getLogger(StudentService.class);
	private final StudentRepository studentRepository;
	private final PasswordEncoder passwordEncoder;
	private final SocialService socialService;
	private final StudentSearchRepository studentSearchRepository;
	private final AuthorityRepository authorityRepository;

	public StudentService(StudentRepository userRepository, PasswordEncoder passwordEncoder,
			SocialService socialService, StudentSearchRepository studentSearchRepository,
			AuthorityRepository authorityRepository) {
		this.studentRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.socialService = socialService;
		this.studentSearchRepository = studentSearchRepository;
		this.authorityRepository = authorityRepository;
	}

	public Optional<Student> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return studentRepository.findOneByActivationKey(key).map(student -> {
			// activate given user for the registration key.
			student.setActivated(true);
			student.setActivationKey(null);
			studentSearchRepository.save(student);
			log.debug("Activated user: {}", student);
			return student;
		});
	}

	public Optional<Student> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return studentRepository.findOneByResetKey(key).filter(user -> {
			ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
			return user.getResetDate().isAfter(oneDayAgo);
		}).map(user -> {
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setResetKey(null);
			user.setResetDate(null);
			return user;
		});
	}

	public Optional<Student> requestPasswordReset(String mail) {
		return studentRepository.findOneByEmail(mail).filter(User::getActivated).map(user -> {
			user.setResetKey(RandomUtil.generateResetKey());
			user.setResetDate(ZonedDateTime.now());
			return user;
		});
	}

	public Student createStudent(String login, String password, String firstName, String lastName, String email,
			String imageUrl, String langKey) {

		Student newStudent = new Student();
		Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		String encryptedPassword = passwordEncoder.encode(password);
		newStudent.setLogin(login);
		// new user gets initially a generated password
		newStudent.setPassword(encryptedPassword);
		newStudent.setFirstName(firstName);
		newStudent.setLastName(lastName);
		newStudent.setEmail(email);
		newStudent.setImageUrl(imageUrl);
		newStudent.setLangKey(langKey);
		// new user is not active
		newStudent.setActivated(false);
		// new user gets registration key
		newStudent.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newStudent.setAuthorities(authorities);
		studentRepository.save(newStudent);
		studentSearchRepository.save(newStudent);
		log.debug("Created Information for User: {}", newStudent);
		return newStudent;
	}

	public Student createStudent(StudentDTO studentDTO) {
		Student student = new Student();
		student.setLogin(studentDTO.getLogin());
		student.setFirstName(studentDTO.getFirstName());
		student.setLastName(studentDTO.getLastName());
		student.setEmail(studentDTO.getEmail());
		student.setImageUrl(studentDTO.getImageUrl());
		if (studentDTO.getLangKey() == null) {
			student.setLangKey("de"); // default language
		} else {
			student.setLangKey(studentDTO.getLangKey());
		}
		if (studentDTO.getAuthorities() != null) {
			Set<Authority> authorities = new HashSet<>();
			studentDTO.getAuthorities().forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
			student.setAuthorities(authorities);
		}
		String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
		student.setPassword(encryptedPassword);
		student.setResetKey(RandomUtil.generateResetKey());
		student.setResetDate(ZonedDateTime.now());
		student.setActivated(true);
		studentRepository.save(student);
		studentSearchRepository.save(student);
		log.debug("Created Information for User: {}", student);
		return student;
	}

	/**
	 * Update basic information (first name, last name, email, language) for the
	 * current user.
	 *
	 * @param firstName
	 *            first name of user
	 * @param lastName
	 *            last name of user
	 * @param email
	 *            email id of user
	 * @param langKey
	 *            language key
	 */
	public void updateStudent(String firstName, String lastName, String email, String langKey) {
		studentRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(student -> {
			student.setFirstName(firstName);
			student.setLastName(lastName);
			student.setEmail(email);
			student.setLangKey(langKey);
			studentSearchRepository.save(student);
			log.debug("Changed Information for User: {}", student);
		});
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param studentDTO
	 *            user to update
	 * @return updated user
	 */
	public Optional<StudentDTO> updateUser(StudentDTO studentDTO) {
		return Optional.of(studentRepository.findOne(studentDTO.getId())).map(student -> {
			student.setLogin(studentDTO.getLogin());
			student.setFirstName(studentDTO.getFirstName());
			student.setLastName(studentDTO.getLastName());
			student.setEmail(studentDTO.getEmail());
			student.setImageUrl(studentDTO.getImageUrl());
			student.setActivated(studentDTO.isActivated());
			student.setLangKey(studentDTO.getLangKey());
			Set<Authority> managedAuthorities = student.getAuthorities();
			managedAuthorities.clear();
			studentDTO.getAuthorities().stream().map(authorityRepository::findOne).forEach(managedAuthorities::add);
			log.debug("Changed Information for User: {}", student);
			return student;
		}).map(StudentDTO::new);
	}

	public void deleteUser(String login) {
		studentRepository.findOneByLogin(login).ifPresent(student -> {
			socialService.deleteUserSocialConnection(student.getLogin());
			studentRepository.delete(student);
			studentSearchRepository.delete(student);
			log.debug("Deleted User: {}", student);
		});
	}

	public void changePassword(String password) {
		studentRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(student -> {
			String encryptedPassword = passwordEncoder.encode(password);
			student.setPassword(encryptedPassword);
			log.debug("Changed password for User: {}", student);
		});
	}

	@Transactional(readOnly = true)
	public Page<StudentDTO> getAllManagedUsers(Pageable pageable) {
		return studentRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(StudentDTO::new);
	}

	@Transactional(readOnly = true)
	public Optional<Student> getUserWithAuthoritiesByLogin(String login) {
		return studentRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public Student getUserWithAuthorities(Long id) {
		return studentRepository.findOneWithAuthoritiesById(id);
	}

	@Transactional(readOnly = true)
	public Student getUserWithAuthorities() {
		return studentRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		ZonedDateTime now = ZonedDateTime.now();
		List<Student> students = studentRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (Student student : students) {
			log.debug("Deleting not activated user {}", student.getLogin());
			studentRepository.delete(student);
			studentSearchRepository.delete(student);
		}
	}
}
