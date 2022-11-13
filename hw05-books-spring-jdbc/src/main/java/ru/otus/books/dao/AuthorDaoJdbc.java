package ru.otus.books.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.books.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Author> getAll() {
        return jdbcOperations.query("select id, name from authors order by id", new AuthorMapper());
    }

    @Override
    public Optional<Author> getById(long id) {
        var params = Collections.singletonMap("id", id);
        try {
            return Optional.ofNullable(jdbcOperations.queryForObject("select id, name from authors where id = :id",
                    params, new AuthorMapper()));
        } catch ( EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Author(id, name);
        }
    }

}
