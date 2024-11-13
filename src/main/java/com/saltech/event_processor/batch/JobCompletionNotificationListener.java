package com.saltech.event_processor.batch;

import com.saltech.event_processor.model.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            String query = "SELECT reference_id,nin, bvn, msisdn,first_name,last_name,dob,address," +
                    "email,gender,status,created_date,created_by FROM customer";
            jdbcTemplate.query(query, (rs, row) ->
                            new Subscriber(rs.getString("reference_id"),
                                    rs.getString("nin"),
                                    rs.getString("bvn"),
                                    rs.getString("msisdn"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getDate("dob"),
                                    rs.getString("address"),
                                    rs.getString("email"),
                                    rs.getString("gender"),
                                    rs.getString("status"),
                                    rs.getDate("created_date"),
                                    rs.getString("created_by")))
                    .forEach(customer -> log.info("Found < {} > in the database.", customer));
        }
    }
}
