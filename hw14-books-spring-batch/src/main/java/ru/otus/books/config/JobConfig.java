package ru.otus.books.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import ru.otus.books.domain.*;
import ru.otus.books.repositories.TempAuthorRepository;
import ru.otus.books.repositories.TempBookRepository;
import ru.otus.books.repositories.TempGenreRepository;
import ru.otus.books.services.MigrateService;

import java.util.HashMap;

@Configuration
public class JobConfig {
    private final Logger logger = LoggerFactory.getLogger("Batch");
    private static final int CHUNK_SIZE = 3;
    public static final String MIGRATE_JOB_NAME = "migrateBooks";


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MigrateService migrateService;
    private final MongoOperations mongoOperations;
    private final TempAuthorRepository tempAuthorRepository;
    private final TempGenreRepository tempGenreRepository;
    private final TempBookRepository tempBookRepository;

    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MigrateService migrateService, MongoOperations mongoOperations, TempAuthorRepository tempAuthorRepository, TempGenreRepository tempGenreRepository, TempBookRepository tempBookRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.migrateService = migrateService;
        this.mongoOperations = mongoOperations;
        this.tempAuthorRepository = tempAuthorRepository;
        this.tempGenreRepository = tempGenreRepository;
        this.tempBookRepository = tempBookRepository;
    }

    @Bean
    public MongoItemReader<Author> authorReader() {
        return new MongoItemReaderBuilder<Author>()
                .template(mongoOperations)
                .name("authors")
                .jsonQuery("{ }")
                .targetType(Author.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public MongoItemReader<Genre> genreReader() {
        return new MongoItemReaderBuilder<Genre>()
                .template(mongoOperations)
                .name("genres")
                .jsonQuery("{ }")
                .targetType(Genre.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public MongoItemReader<Book> bookReader() {
        return new MongoItemReaderBuilder<Book>()
                .template(mongoOperations)
                .name("books")
                .jsonQuery("{ }")
                .targetType(Book.class)
                .sorts(new HashMap<>())
                .saveState(false)
                .build();
    }

    @Bean
    public RepositoryItemWriter<TempAuthor> authorWriter() {
        return new RepositoryItemWriterBuilder<TempAuthor>()
                .repository(tempAuthorRepository)
                .build();
    }

    @Bean
    public RepositoryItemWriter<TempGenre> genreWriter() {
        return new RepositoryItemWriterBuilder<TempGenre>()
                .repository(tempGenreRepository)
                .build();
    }

    @Bean
    public RepositoryItemWriter<TempBook> bookWriter() {
        return new RepositoryItemWriterBuilder<TempBook>()
                .repository(tempBookRepository)
                .build();
    }

    @Bean
    ItemProcessor<Author, TempAuthor> authorConverter() {
        return migrateService::convertAuthor;
    }

    @Bean
    ItemProcessor<Genre, TempGenre> genreConverter() {
        return migrateService::convertGenre;
    }

    @Bean
    ItemProcessor<Book, TempBook> bookConverter() {
        return migrateService::convertBook;
    }

    @Bean
    public MethodInvokingTaskletAdapter prepareScriptTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(migrateService);
        adapter.setTargetMethod("prepareTables");
        return adapter;
    }

    @Bean
    public MethodInvokingTaskletAdapter migrateScriptTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(migrateService);
        adapter.setTargetMethod("executeMigrateScript");
        return adapter;
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpScriptTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(migrateService);
        adapter.setTargetMethod("executeCleanupScript");
        return adapter;
    }

    @Bean
    public Step prepareStep(MethodInvokingTaskletAdapter prepareScriptTasklet) {
        return this.stepBuilderFactory.get("prepareStep")
                .tasklet(prepareScriptTasklet)
                .build();
    }

    @Bean
    public Step migrateStep() {
        return this.stepBuilderFactory.get("migrateStep")
                .tasklet(migrateScriptTasklet())
                .build();
    }

    @Bean
    public Step cleanUpStep() {
        return this.stepBuilderFactory.get("cleanUpStep")
                .tasklet(cleanUpScriptTasklet())
                .build();
    }

    @Bean
    public Step loadAuthorsStep(MongoItemReader<Author> reader, ItemWriter<TempAuthor> writer,
                                     ItemProcessor<Author, TempAuthor> itemProcessor) {
        return stepBuilderFactory.get("loadAuthorsStep")
                .<Author, TempAuthor>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step loadGenresStep(ItemReader<Genre> genreReader, ItemWriter<TempGenre> genreWriter,
                                ItemProcessor<Genre, TempGenre> itemProcessor) {
        return stepBuilderFactory.get("loadGenresStep")
                .<Genre, TempGenre>chunk(CHUNK_SIZE)
                .reader(genreReader)
                .processor(itemProcessor)
                .writer(genreWriter)
                .build();
    }

    @Bean
    public Step loadBooksStep(MongoItemReader<Book> reader, ItemWriter<TempBook> writer,
                               ItemProcessor<Book, TempBook> itemProcessor) {
        return stepBuilderFactory.get("loadBooksStep")
                .<Book, TempBook>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job migrateJob( Step prepareStep, Step loadGenresStep, Step loadAuthorsStep,
                           Step loadBooksStep, Step migrateStep, Step cleanUpStep ) {
        return jobBuilderFactory.get(MIGRATE_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(prepareStep)
                .next(loadAuthorsStep)
                .next(loadGenresStep)
                .next(loadBooksStep)
                .next(migrateStep)
                .next(cleanUpStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

}
