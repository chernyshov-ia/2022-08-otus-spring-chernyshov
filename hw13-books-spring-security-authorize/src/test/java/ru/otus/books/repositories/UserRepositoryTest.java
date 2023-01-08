package ru.otus.books.repositories;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.books.domain.User;
import ru.otus.books.exceptions.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий пользователей ")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @DisplayName("должен возвращать пользователя по имени")
    @Test
    void shouldReturnExistingUserByUsername() {
        List<User> list = repository.findAll();
        System.out.println(list);
        var userExpect = list.stream().findFirst().orElseThrow(NotFoundException::new);
        var userTest = repository.findByUsername(userExpect.getUsername()).orElseThrow(NotFoundException::new);
        assertThat(userExpect).isEqualTo(userTest);
    }

}