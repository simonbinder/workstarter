package workstarter.service;

import workstarter.WorkstarterApp;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.config.Constants;
import workstarter.repository.StudentRepository;
import workstarter.service.dto.StudentDTO;
import java.time.ZonedDateTime;
import workstarter.service.util.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see StudentService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
@Transactional
public class UserServiceIntTest {

    @Autowired
    private StudentRepository userRepository;

    @Autowired
    private StudentService userService;

    @Test
    public void assertThatUserMustExistToResetPassword() {
        Optional<? extends User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();

        maybeUser = userService.requestPasswordReset("admin@localhost");
        assertThat(maybeUser.isPresent()).isTrue();

        assertThat(maybeUser.get().getEmail()).isEqualTo("admin@localhost");
        assertThat(maybeUser.get().getResetDate()).isNotNull();
        assertThat(maybeUser.get().getResetKey()).isNotNull();
    }

    @Test
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        Student student = userService.createStudent("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "http://placehold.it/50x50", "en-US");
        Optional<? extends User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(student);
    }

    @Test
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Student student = userService.createStudent("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "http://placehold.it/50x50", "en-US");

        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        String resetKey = RandomUtil.generateResetKey();
        student.setActivated(true);
        student.setResetDate(daysAgo);
        student.setResetKey(resetKey);

        userRepository.save(student);

        Optional<? extends User> maybeUser = userService.completePasswordReset("johndoe2", student.getResetKey());

        assertThat(maybeUser.isPresent()).isFalse();

        userRepository.delete(student);
    }

    @Test
    public void assertThatResetKeyMustBeValid() {
        Student student = userService.createStudent("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "http://placehold.it/50x50", "en-US");

        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        student.setActivated(true);
        student.setResetDate(daysAgo);
        student.setResetKey("1234");
        userRepository.save(student);
        Optional<? extends User> maybeUser = userService.completePasswordReset("johndoe2", student.getResetKey());
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(student);
    }

    @Test
    public void assertThatUserCanResetPassword() {
        Student student = userService.createStudent("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "http://placehold.it/50x50", "en-US");
        String oldPassword = student.getPassword();
        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(2);
        String resetKey = RandomUtil.generateResetKey();
        student.setActivated(true);
        student.setResetDate(daysAgo);
        student.setResetKey(resetKey);
        userRepository.save(student);
        Optional<? extends User> maybeUser = userService.completePasswordReset("johndoe2", student.getResetKey());
        assertThat(maybeUser.isPresent()).isTrue();
        assertThat(maybeUser.get().getResetDate()).isNull();
        assertThat(maybeUser.get().getResetKey()).isNull();
        assertThat(maybeUser.get().getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(student);
    }

    @Test
    public void testFindNotActivatedUsersByCreationDateBefore() {
        userService.removeNotActivatedUsers();
        ZonedDateTime now = ZonedDateTime.now();
        List<? extends User> studends = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        assertThat(studends).isEmpty();
    }

    @Test
    public void assertThatAnonymousUserIsNotGet() {
        final PageRequest pageable = new PageRequest(0, (int) userRepository.count());
        final Page<StudentDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream()
            .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin())))
            .isTrue();
    }
}
