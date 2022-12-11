package ru.otus.books.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.books.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.when;

@DisplayName("UserDetailService ")
@SpringBootTest(classes = UserDetailServiceImpl.class)
class UserDetailServiceImplTest {
    private static final User USER_1 = new User(1L, "user", "user");

    @MockBean
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @DisplayName(" должен возвращать UserDetails для существующего User")
    @Test
    void shouldReturnCorrectUserDetails() {
        when(userService.findByUsername(USER_1.getUsername())).thenReturn(Optional.of(USER_1));
        var expected = org.springframework.security.core.userdetails.User.builder()
                .username(USER_1.getUsername())
                .password(USER_1.getPassword())
                .credentialsExpired(false)
                .accountLocked(false)
                .roles()
                .authorities(List.of())
                .build();

        var ud = userDetailsService.loadUserByUsername(USER_1.getUsername());

        assertThat(ud).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName(" должен возвращать исключение UsernameNotFoundException для несуществующего User")
    @Test()
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        when(userService.findByUsername(USER_1.getUsername())).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = catchThrowableOfType(
                () -> userDetailsService.loadUserByUsername(USER_1.getUsername()),
                UsernameNotFoundException.class
        );

        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);
    }

}