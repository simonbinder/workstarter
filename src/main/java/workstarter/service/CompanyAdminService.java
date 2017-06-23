package workstarter.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import workstarter.config.Constants;
import workstarter.domain.Authority;
import workstarter.domain.Company;
import workstarter.domain.CompanyAdmin;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.repository.AuthorityRepository;
import workstarter.repository.CompanyAdminRepository;
import workstarter.repository.CompanyRepository;
import workstarter.repository.StudentRepository;
import workstarter.repository.search.CompanyAdminSearchRepository;
import workstarter.repository.search.UserSearchRepository;
import workstarter.security.AuthoritiesConstants;
import workstarter.security.SecurityUtils;
import workstarter.service.dto.CompanyAdminDTO;
import workstarter.service.dto.StudentDTO;
import workstarter.service.util.RandomUtil;

@Service
@Transactional
public class CompanyAdminService {

	private final Logger log = LoggerFactory.getLogger(StudentService.class);
	private final CompanyAdminRepository companyAdminRepository;
	private final PasswordEncoder passwordEncoder;
	private final SocialService socialService;
	private final CompanyAdminSearchRepository companyAdminSearchRepository;
	private final CompanyRepository companyRepository;
	private final AuthorityRepository authorityRepository;

	public CompanyAdminService(CompanyAdminRepository companyAdminRepository, PasswordEncoder passwordEncoder,
			SocialService socialService, CompanyAdminSearchRepository companyAdminSearchRepository, CompanyRepository companyRepository,
			AuthorityRepository authorityRepository) {
		this.companyAdminRepository = companyAdminRepository;
		this.passwordEncoder = passwordEncoder;
		this.socialService = socialService;
		this.companyAdminSearchRepository = companyAdminSearchRepository;
		this.companyRepository = companyRepository;
		this.authorityRepository = authorityRepository;
	}

	public Optional<CompanyAdmin> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return companyAdminRepository.findOneByActivationKey(key).map(company -> {
			// activate given user for the registration key.
			company.setActivated(true);
			company.setActivationKey(null);
			companyAdminSearchRepository.save(company);
			log.debug("Activated user: {}", company);
			return company;
		});
	}

	public Optional<CompanyAdmin> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return companyAdminRepository.findOneByResetKey(key).filter(company -> {
			ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
			return company.getResetDate().isAfter(oneDayAgo);
		}).map(company -> {
			company.setPassword(passwordEncoder.encode(newPassword));
			company.setResetKey(null);
			company.setResetDate(null);
			return company;
		});
	}

	public Optional<CompanyAdmin> requestPasswordReset(String mail) {
		return companyAdminRepository.findOneByEmail(mail).filter(CompanyAdmin::getActivated).map(company -> {
			company.setResetKey(RandomUtil.generateResetKey());
			company.setResetDate(ZonedDateTime.now());
			return company;
		});
	}

	public CompanyAdmin createCompany(String login, String password, String firstName, String lastName, String email,
			String imageUrl, String langKey) {

		CompanyAdmin newCompany = new CompanyAdmin();
		Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		String encryptedPassword = passwordEncoder.encode(password);
		newCompany.setLogin(login);
		// new user gets initially a generated password
		newCompany.setPassword(encryptedPassword);
		newCompany.setFirstName(firstName);
		newCompany.setLastName(lastName);
		newCompany.setEmail(email);
		newCompany.setImageUrl(imageUrl);
		newCompany.setLangKey(langKey);
		// new user is not active
		newCompany.setActivated(false);
		// new user gets registration key
		newCompany.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newCompany.setAuthorities(authorities);
		companyAdminRepository.save(newCompany);
		companyAdminSearchRepository.save(newCompany);
		log.debug("Created Information for User: {}", newCompany);
		return newCompany;
	}

	public CompanyAdmin createCompany(CompanyAdminDTO companyDTO) {
		CompanyAdmin company = new CompanyAdmin();
		company.setLogin(companyDTO.getLogin());
		company.setFirstName(companyDTO.getFirstName());
		company.setLastName(companyDTO.getLastName());
		company.setEmail(companyDTO.getEmail());
		company.setImageUrl(companyDTO.getImageUrl());
		if (companyDTO.getLangKey() == null) {
			company.setLangKey("de"); // default language
		} else {
			company.setLangKey(companyDTO.getLangKey());
		}
		if (companyDTO.getAuthorities() != null) {
			Set<Authority> authorities = new HashSet<>();
			companyDTO.getAuthorities().forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
			company.setAuthorities(authorities);
		}
		String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
		company.setPassword(encryptedPassword);
		company.setResetKey(RandomUtil.generateResetKey());
		company.setResetDate(ZonedDateTime.now());
		company.setActivated(true);
		companyAdminRepository.save(company);
		companyAdminSearchRepository.save(company);
		log.debug("Created Information for User: {}", company);
		return company;
	}
	
	public CompanyAdmin addCompany(Long id, Company company){
		CompanyAdmin companyAdmin = companyAdminRepository.getOne(id);
		Company searchedCompany = companyRepository.getOne(company.getId());
		companyAdmin.setCompany(searchedCompany);
		companyAdminRepository.save(companyAdmin);
		log.debug("Added company for CompanyAdmin: {}", companyAdmin);
		return companyAdmin;
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
	public void updateCompany(String firstName, String lastName, String email, String langKey) {
		companyAdminRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(company -> {
			company.setFirstName(firstName);
			company.setLastName(lastName);
			company.setEmail(email);
			company.setLangKey(langKey);
			companyAdminSearchRepository.save(company);
			log.debug("Changed Information for User: {}", company);
		});
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param companyDTO
	 *            user to update
	 * @return updated user
	 */
	public Optional<CompanyAdminDTO> updateUser(CompanyAdminDTO companyDTO) {
		return Optional.of(companyAdminRepository.findOne(companyDTO.getId())).map(company -> {
			company.setLogin(companyDTO.getLogin());
			company.setFirstName(companyDTO.getFirstName());
			company.setLastName(companyDTO.getLastName());
			company.setEmail(companyDTO.getEmail());
			company.setImageUrl(companyDTO.getImageUrl());
			company.setActivated(companyDTO.isActivated());
			company.setLangKey(companyDTO.getLangKey());
			Set<Authority> managedAuthorities = company.getAuthorities();
			managedAuthorities.clear();
			companyDTO.getAuthorities().stream().map(authorityRepository::findOne).forEach(managedAuthorities::add);
			log.debug("Changed Information for User: {}", company);
			return company;
		}).map(CompanyAdminDTO::new);
	}

	public void deleteCompany(String login) {
		companyAdminRepository.findOneByLogin(login).ifPresent(company -> {
			socialService.deleteUserSocialConnection(company.getLogin());
			companyAdminRepository.delete(company);
			companyAdminSearchRepository.delete(company);
			log.debug("Deleted User: {}", company);
		});
	}

	public void changePassword(String password) {
		companyAdminRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(company -> {
			String encryptedPassword = passwordEncoder.encode(password);
			company.setPassword(encryptedPassword);
			log.debug("Changed password for User: {}", company);
		});
	}

	@Transactional(readOnly = true)
	public Page<CompanyAdminDTO> getAllManagedUsers(Pageable pageable) {
		return companyAdminRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(CompanyAdminDTO::new);
	}

	@Transactional(readOnly = true)
	public Optional<CompanyAdmin> getUserWithAuthoritiesByLogin(String login) {
		return companyAdminRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public CompanyAdmin getUserWithAuthorities(Long id) {
		return companyAdminRepository.findOneWithAuthoritiesById(id);
	}

	@Transactional(readOnly = true)
	public CompanyAdmin getUserWithAuthorities() {
		return companyAdminRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedCompanies() {
		ZonedDateTime now = ZonedDateTime.now();
		List<CompanyAdmin> companies = companyAdminRepository
				.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (CompanyAdmin company : companies) {
			log.debug("Deleting not activated user {}", company.getLogin());
			companyAdminRepository.delete(company);
			companyAdminSearchRepository.delete(company);
		}
	}
}
