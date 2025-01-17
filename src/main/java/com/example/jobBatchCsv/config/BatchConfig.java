package com.example.jobBatchCsv.config;

import com.example.jobBatchCsv.helper.CustomerProcessor;
import com.example.jobBatchCsv.model.Customer;
import com.example.jobBatchCsv.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfig{

    private CustomerRepository customerRepository;

    @Bean
    public FlatFileItemReader<Customer> reader(){
        FlatFileItemReader<Customer> itemReader= new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/csv/customer.csv"));
        itemReader.setName("csvReader"); //any
        itemReader.setLinesToSkip(1); //skip the header part
        itemReader.setLineMapper(lineMapper());

        return itemReader;
    }

    private LineMapper<Customer> lineMapper(){
        DefaultLineMapper<Customer> lineMapper=new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("ID","First Name","Last Name","Gender","Contact No","Location","DOB","Salary"); //any or refer to csv columns

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public CustomerProcessor processor(){ //need to create a file config/CustomerProcessor
        return new CustomerProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> writer(){
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");

        return writer;

    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("csv-step", jobRepository)
                .<Customer,Customer>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor()) //if you want to be async, will become faster but unordered
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("importCustomers", jobRepository)
                .flow(step1(jobRepository,transactionManager))
                //.next(step2()) //if you have other step use next()
                .end()
                .build();
    }

    //if we need to spring batch to execute the rows from the source to destination concurrently
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10); //10 thread to run concurrently
        return asyncTaskExecutor;
    }

}
