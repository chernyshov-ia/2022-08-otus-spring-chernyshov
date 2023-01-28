package ru.otus.books.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class MigrateService {
    public static final String FILENAME_SCRIPT_SCHEMA = "1_schema.sql";
    private static final String FILENAME_SCRIPT_TEMP = "2_temp.sql";
    private static final String FILENAME_SCRIPT_MIGRATE = "3_migrate.sql";
    private static final String FILENAME_SCRIPT_CLEANUP = "4_cleanup.sql";

    @PersistenceContext
    private EntityManager manager;

    private String getScript(String filename) {
        var inputStream = MigrateService.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new MigrateServiceException("Error loading script: no resource");
        }

        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String scripts = reader.lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return scripts;
        } catch (IOException e) {
            throw new MigrateServiceException("Error loading script", e);
        }
    }


    private void executeScript(String filename) {
        var script = getScript(filename);
        for (String s : script.split(";")) {
            Query nativeQuery = manager.createNativeQuery(s);
            nativeQuery.executeUpdate();
        }
    }

    @Transactional
    public void prepareTables() {
        executeScript(FILENAME_SCRIPT_SCHEMA);
        executeScript(FILENAME_SCRIPT_TEMP);
    }

    @Transactional
    public void executeMigrateScript() {
        executeScript(FILENAME_SCRIPT_MIGRATE);
    }

    @Transactional
    public void executeCleanupScript() {
        executeScript(FILENAME_SCRIPT_CLEANUP);
    }

    public TempAuthor convertAuthor(Author entity) {
        var temp = new TempAuthor();
        temp.setId(null);
        temp.setOldId(entity.getId());
        temp.setName(entity.getName());
        return temp;
    }

    public TempGenre convertGenre(Genre entity) {
        var temp = new TempGenre();
        temp.setId(null);
        temp.setOldId(entity.getId());
        temp.setName(entity.getName());
        return temp;
    }

    public TempBook convertBook(Book entity) {
        var temp = new TempBook();
        temp.setId(null);
        temp.setOldId(entity.getId());
        temp.setName(entity.getName());
        temp.setGenreId(entity.getGenre().getId());
        temp.setAuthorId(entity.getAuthor().getId());
        return temp;
    }

}
