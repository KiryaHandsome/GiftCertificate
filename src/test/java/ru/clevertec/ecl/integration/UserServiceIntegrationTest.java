package ru.clevertec.ecl.integration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;


    @Nested
    class FindTest {

        @Test
        void checkFindShouldThrowEntityNotFoundException() {
            int id = Integer.MAX_VALUE;

            assertThrows(EntityNotFoundException.class,
                    () -> userService.find(id));
        }

        @Test
        void checkFindShouldReturnExpectedUser() {
            int id = 2;
            User expected = new User(2, "second name");

            User actual = userService.find(id);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class FindAllTest {

        @Test
        void checkFindAllShouldReturnNotEmptyPage() {
            Pageable pageable = PageRequest.of(0, 10);

            Page<User> users = userService.findAll(pageable);

            assertThat(users).isNotNull();
            List<User> userList = users.getContent();
            assertThat(userList).isNotNull();
            assertThat(userList).isNotEmpty();
        }

        @Test
        void checkFindAllShouldReturnEmptyPage() {
            Pageable pageable = PageRequest.of(1, 10);

            Page<User> users = userService.findAll(pageable);

            assertThat(users).isNotNull();
            List<User> userList = users.getContent();
            assertThat(userList).isEmpty();
        }
    }

}
