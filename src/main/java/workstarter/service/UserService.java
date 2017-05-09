package workstarter.service;

import workstarter.domain.Authority;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.repository.AuthorityRepository;
import workstarter.config.Constants;
import workstarter.repository.StudentRepository;
import workstarter.repository.UserRepository;
import workstarter.repository.search.StudentSearchRepository;
import workstarter.repository.search.UserSearchRepository;
import workstarter.security.AuthoritiesConstants;
import workstarter.security.SecurityUtils;
import workstarter.service.util.RandomUtil;
import workstarter.service.dto.StudentDTO;
import workstarter.service.dto.UserDTO;

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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	@PersistenceContext
	EntityManager em;
	private final Logger log = LoggerFactory.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SocialService socialService;
	private final UserSearchRepository userSearchRepository;
	private final AuthorityRepository authorityRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			SocialService socialService, UserSearchRepository userSearchRepository,
			AuthorityRepository authorityRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.socialService = socialService;
		this.userSearchRepository = userSearchRepository;
		this.authorityRepository = authorityRepository;
	}

	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return userRepository.findOneByActivationKey(key).map(user -> {
			// activate given user for the registration key.
			user.setActivated(true);
			user.setActivationKey(null);
			userSearchRepository.save(user);
			log.debug("Activated user: {}", user);
			return user;
		});
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key).filter(user -> {
			ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
			return user.getResetDate().isAfter(oneDayAgo);
		}).map(user -> {
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setResetKey(null);
			user.setResetDate(null);
			return user;
		});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findOneByEmail(mail).filter(User::getActivated).map(user -> {
			user.setResetKey(RandomUtil.generateResetKey());
			user.setResetDate(ZonedDateTime.now());
			return user;
		});
	}


	public List<User> getAllUser() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> rootEntry = cq.from(User.class);
		CriteriaQuery<User> all = cq.select(rootEntry);
		TypedQuery<User> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}


	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			socialService.deleteUserSocialConnection(user.getLogin());
			userRepository.delete(user);
			userSearchRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}

	public void changePassword(String password) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			String encryptedPassword = passwordEncoder.encode(password);
			user.setPassword(encryptedPassword);
			log.debug("Changed password for User: {}", user);
		});
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
		return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities(Long id) {
		return userRepository.findOneWithAuthoritiesById(id);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
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
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
			userSearchRepository.delete(user);
		}
	}
}
