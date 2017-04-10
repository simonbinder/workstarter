package workstarter.service;

import workstarter.WorkstarterApp;
import workstarter.domain.Authority;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.repository.AuthorityRepository;
import workstarter.repository.StudentRepository;
import workstarter.repository.search.UserSearchRepository;
import workstarter.service.MailService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
@Transactional
public class SocialServiceIntTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository userRepository;
    @Autowired
    private UserSearchRepository userSearchRepository;


    @Mock
    private MailService mockMailService;

    @Mock
    private UsersConnectionRepository mockUsersConnectionRepository;

    @Mock
    private ConnectionRepository mockConnectionRepository;

    private SocialService socialService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockMailService).sendSocialRegistrationValidationEmail(anyObject(), anyString());
        doNothing().when(mockConnectionRepository).addConnection(anyObject());
        when(mockUsersConnectionRepository.createConnectionRepository(anyString())).thenReturn(mockConnectionRepository);

        socialService = new SocialService(mockUsersConnectionRepository, authorityRepository,
                passwordEncoder, userRepository, mockMailService, userSearchRepository);
    }

    @Test
    public void testDeleteUserSocialConnection() throws Exception {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");
        socialService.createSocialUser(connection, "fr");
        MultiValueMap<String, Connection<?>> connectionsByProviderId = new LinkedMultiValueMap<>();
        connectionsByProviderId.put("PROVIDER", null);
        when(mockConnectionRepository.findAllConnections()).thenReturn(connectionsByProviderId);

        // Exercise
        socialService.deleteUserSocialConnection("@LOGIN");

        // Verify
        verify(mockConnectionRepository, times(1)).removeConnections("PROVIDER");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSocialUserShouldThrowExceptionIfConnectionIsNull() {
        // Exercise
        socialService.createSocialUser(null, "fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSocialUserShouldThrowExceptionIfConnectionHasNoEmailAndNoLogin() {
        // Setup
        Connection<?> connection = createConnection("",
            "",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSocialUserShouldThrowExceptionIfConnectionHasNoEmailAndLoginAlreadyExist() {
        // Setup
        Student student = createExistingStudent("@LOGIN",
            "mail@mail.com",
            "OTHER_FIRST_NAME",
            "OTHER_LAST_NAME",
            "OTHER_IMAGE_URL");
        Connection<?> connection = createConnection("@LOGIN",
            "",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        try {
            // Exercise
            socialService.createSocialUser(connection, "fr");
        } finally {
            // Teardown
            userRepository.delete(student);
        }
    }

    @Test
    public void testCreateSocialUserShouldCreateUserIfNotExist() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        final Optional<Student> student = userRepository.findOneByEmail("mail@mail.com");
        assertThat(student).isPresent();

        // Teardown
        userRepository.delete(student.get());
    }

    @Test
    public void testCreateSocialUserShouldCreateUserWithSocialInformation() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        Student student = userRepository.findOneByEmail("mail@mail.com").get();
        assertThat(student.getFirstName()).isEqualTo("FIRST_NAME");
        assertThat(student.getLastName()).isEqualTo("LAST_NAME");
        assertThat(student.getImageUrl()).isEqualTo("IMAGE_URL");

        // Teardown
        userRepository.delete(student);
    }

    @Test
    public void testCreateSocialUserShouldCreateActivatedUserWithRoleUserAndPassword() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        Student student = userRepository.findOneByEmail("mail@mail.com").get();
        assertThat(student.getActivated()).isEqualTo(true);
        assertThat(student.getPassword()).isNotEmpty();
        Authority userAuthority = authorityRepository.findOne("ROLE_USER");
        assertThat(student.getAuthorities().toArray()).containsExactly(userAuthority);

        // Teardown
        userRepository.delete(student);
    }

    @Test
    public void testCreateSocialUserShouldCreateUserWithExactLangKey() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        final Student student = userRepository.findOneByEmail("mail@mail.com").get();
        assertThat(student.getLangKey()).isEqualTo("fr");

        // Teardown
        userRepository.delete(student);
    }

    @Test
    public void testCreateSocialUserShouldCreateUserWithLoginSameAsEmailIfNotTwitter() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER_OTHER_THAN_TWITTER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        Student student = userRepository.findOneByEmail("mail@mail.com").get();
        assertThat(student.getLogin()).isEqualTo("mail@mail.com");

        // Teardown
        userRepository.delete(student);
    }

    @Test
    public void testCreateSocialUserShouldCreateUserWithSocialLoginWhenIsTwitter() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "twitter");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        Student student = userRepository.findOneByEmail("mail@mail.com").get();
        assertThat(student.getLogin()).isEqualToIgnoringCase("@LOGIN");

        // Teardown
        userRepository.delete(student);
    }

    @Test
    public void testCreateSocialUserShouldCreateSocialConnection() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        verify(mockConnectionRepository, times(1)).addConnection(connection);

        // Teardown
        Student studentToDelete = userRepository.findOneByEmail("mail@mail.com").get();
        userRepository.delete(studentToDelete);
    }

    @Test
    public void testCreateSocialUserShouldNotCreateUserIfEmailAlreadyExist() {
        // Setup
        createExistingStudent("@OTHER_LOGIN",
            "mail@mail.com",
            "OTHER_FIRST_NAME",
            "OTHER_LAST_NAME",
            "OTHER_IMAGE_URL");
        long initialUserCount = userRepository.count();
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        assertThat(userRepository.count()).isEqualTo(initialUserCount);

        // Teardown
        Student studentToDelete = userRepository.findOneByEmail("mail@mail.com").get();
        userRepository.delete(studentToDelete);
    }

    @Test
    public void testCreateSocialUserShouldNotChangeUserIfEmailAlreadyExist() {
        // Setup
        createExistingStudent("@OTHER_LOGIN",
            "mail@mail.com",
            "OTHER_FIRST_NAME",
            "OTHER_LAST_NAME",
            "OTHER_IMAGE_URL");
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        Student studentToVerify = userRepository.findOneByEmail("mail@mail.com").get();
        assertThat(studentToVerify.getLogin()).isEqualTo("@other_login");
        assertThat(studentToVerify.getFirstName()).isEqualTo("OTHER_FIRST_NAME");
        assertThat(studentToVerify.getLastName()).isEqualTo("OTHER_LAST_NAME");
        assertThat(studentToVerify.getImageUrl()).isEqualTo("OTHER_IMAGE_URL");
        // Teardown
        userRepository.delete(studentToVerify);
    }

    @Test
    public void testCreateSocialUserShouldSendRegistrationValidationEmail() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN",
            "mail@mail.com",
            "FIRST_NAME",
            "LAST_NAME",
            "IMAGE_URL",
            "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        //Verify
        verify(mockMailService, times(1)).sendSocialRegistrationValidationEmail(anyObject(), anyString());

        // Teardown
        Student studentToDelete = userRepository.findOneByEmail("mail@mail.com").get();
        userRepository.delete(studentToDelete);
    }

    private Connection<?> createConnection(String login,
                                           String email,
                                           String firstName,
                                           String lastName,
                                           String imageUrl,
                                           String providerId) {
        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getEmail()).thenReturn(email);
        when(userProfile.getUsername()).thenReturn(login);
        when(userProfile.getFirstName()).thenReturn(firstName);
        when(userProfile.getLastName()).thenReturn(lastName);

        Connection<?> connection = mock(Connection.class);
        ConnectionKey key = new ConnectionKey(providerId, "PROVIDER_USER_ID");
        when(connection.fetchUserProfile()).thenReturn(userProfile);
        when(connection.getKey()).thenReturn(key);
        when(connection.getImageUrl()).thenReturn(imageUrl);

        return connection;
    }

    private Student createExistingStudent(String login,
                                    String email,
                                    String firstName,
                                    String lastName,
                                    String imageUrl) {
        Student student = new Student();
        student.setLogin(login);
        student.setPassword(passwordEncoder.encode("password"));
        student.setEmail(email);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setImageUrl(imageUrl);
        return userRepository.saveAndFlush(student);
    }
}
