package com.saltech.event_processor.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJobService {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Autowired
    private JobRepository jobRepository;


//    @Scheduled(fixedRate = 5000, initialDelay = 5000)
//    public void startBatchJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        JobParameters params = new JobParametersBuilder()
//                .addString("job", String.valueOf(System.currentTimeMillis()))
//                .toJobParameters();
//        jobLauncher.run(job, params);
//    }

    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        JobParameters params = new JobParametersBuilder()
                .addString("job", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job,params);
        return jobLauncher;
    }
}
