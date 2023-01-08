package ru.otus.books.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.books.domain.Authority;
import ru.otus.books.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.when;

@DisplayName("UserDetailService ")
@SpringBootTest(classes = UserDetailServiceImpl.class)
class UserDetailServiceImplTest {
    private static final User USER_1 = new User(1L, "user", "user",
            List.of(new Authority("AUTHORITY_1"), new Authority("AUTHORITY_2"))
    );

    private static final List<GrantedAuthority> GRANTED_AUTHORITIES = USER_1.getAuthorities().stream()
            .map(a -> new SimpleGrantedAuthority(a.getId()))
            .collect(Collectors.toList());


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
                .authorities(GRANTED_AUTHORITIES)
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