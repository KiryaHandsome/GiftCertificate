package ru.clevertec.ecl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.TestData;
import ru.clevertec.ecl.util.UserTestBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository mockRepository;
    private UserTestBuilder USER_BUILDER;

    @BeforeEach
    void setUp() {
        USER_BUILDER = new UserTestBuilder();
    }

    @Nested
    class FindTest {

        @Test
        void checkFindShouldThrowEntityNotFoundException() {
            int id = 10;

            doReturn(Optional.empty())
                    .when(mockRepository).findById(id);

            assertThrows(EntityNotFoundException.class,
                    () -> userService.find(id));
        }

        @Test
        void checkFindShouldReturnExpectedUser() {
            User expected = USER_BUILDER.build();

            doReturn(Optional.of(expected))
                    .when(mockRepository).findById(any());

            User actual = userService.find(any());

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class FindAllTest {

        @Test
        void checkFindAllShouldReturnExpectedPage() {
            Pageable pageable = TestData.defaultPageable();
            Page<User> expected = TestData.defaultPageWithUsers();

            doReturn(expected)
                    .when(mockRepository).findAll(pageable);

            Page<User> actual = userService.findAll(pageable);

            assertThat(actual).isEqualTo(expected);
        }
    }
}