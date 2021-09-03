package com.study.springbatchstudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cyj0619 on 2021-09-01
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob")
		                        .start(simpleStep1())
		                        .build();
	}

	@Bean
	public Step simpleStep1() {
		return stepBuilderFactory.get("simpleStep1")
		                         .tasklet(((contribution, chunkContext) -> {
			                         log.info("------ this is Step1");
			                         return RepeatStatus.FINISHED;
		                         }))
		                         .build();
	}
}
