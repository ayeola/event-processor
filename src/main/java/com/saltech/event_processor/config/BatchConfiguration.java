package com.saltech.event_processor.config;

import com.saltech.event_processor.batch.CustomItemWriter;
import com.saltech.event_processor.batch.CustomerItemProcessor;
import com.saltech.event_processor.batch.JobCompletionNotificationListener;
import com.saltech.event_processor.model.Subscriber;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
public class BatchConfiguration {

    @Autowired
    private  KafkaProperties properties;

    @Autowired
    private Environment environment;

    public Properties consumerConfigs() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.cloud.stream.kafka.binder.brokers"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                true);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"customer-group");
        return props;
    }

    @Bean
    public Job job(JobRepository jobRepository,
                             JobCompletionNotificationListener listener,
                             Step step) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager
            transactionManager, DataSource dataSource) {
        return new StepBuilder("step", jobRepository)
                .<String, Subscriber> chunk(10, transactionManager)
                .reader(kafkaItemReader())
                .processor(processor())
                .writer(writer1(dataSource))
                .build();
    }

    @Bean
  public   KafkaItemReader<String, String> kafkaItemReader() {
        Properties props = new Properties();
        List<String> bootstrapServers = new ArrayList<>();
        bootstrapServers.add("localhost:9092");
        properties.setBootstrapServers(bootstrapServers);
        props.putAll(this.properties.getProperties());
        props.put("bootstrap.server","localhost:9092");


        KafkaItemReader kafkaItemReader = new KafkaItemReaderBuilder<String, String>()
                .partitions(0)
                .consumerProperties(props)
                .name("customer-reader")
                .saveState(true)
                .topic("bulk_customer_topic")
                .consumerProperties(consumerConfigs())
                .build();
        kafkaItemReader.open(new ExecutionContext());

        return kafkaItemReader;
    }


    @Bean
    public VirtualThreadTaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor("virtual-thread-executor");
    }



    public JdbcBatchItemWriter writer(){
        return new CustomItemWriter();
    }

    @Bean
    public JdbcBatchItemWriter writer1(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO customer (reference_id,nin, bvn, msisdn,first_name,last_name,dob,address,email,gender,status,created_by,created_date) " +
                        "VALUES (:referenceId,:nin, :bvn, :msisdn,:firstName,:lastName,:dob,:address,:email,:gender,:status,:createdBy,:createdDate)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public CustomerItemProcessor processor(){
        return new CustomerItemProcessor();
    }
}
