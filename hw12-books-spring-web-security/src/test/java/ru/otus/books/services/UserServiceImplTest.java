package ru.otus.books.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.books.domain.User;
import ru.otus.books.exceptions.NotFoundException;
import ru.otus.books.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {
    private static final User USER = new User(1L, "user", "user");

    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnCorrectExisingUser() {
        when(repository.findByUsername(USER.getUsername())).thenReturn(Optional.of(USER));
        var user = userService.findByUsername(USER.getUsername()).orElseThrow(NotFoundException::new);
        assertThat(user).usingRecursiveComparison().isEqualTo(USER);
    }

}