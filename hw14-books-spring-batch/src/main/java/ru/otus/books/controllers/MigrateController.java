package ru.otus.books.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MigrateController {
    private final Logger logger = LoggerFactory.getLogger(MigrateController.class);

    private final JobLauncher jobLauncher;
    private final Job migrateJob;

    public MigrateController(JobLauncher jobLauncher, Job migrateJob) {
        this.jobLauncher = jobLauncher;
        this.migrateJob = migrateJob;
    }

    @ShellMethod(value = "Start migration", key = {"start", "sm"})
    public void startMigration() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobExecution execution = jobLauncher.run(migrateJob, new JobParametersBuilder()
                .toJobParameters());

        logger.info("{}", execution);
    }

}
