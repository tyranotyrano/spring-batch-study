package com.study.springbatchstudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cyj0619 on 2021-09-06
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job stepNextConditionalJob() {
		/**
		 * step1 실패 시나리오: step1 -> step3
		 * step1 성공 시나리오: step1 -> step2 -> step3
		 * */
		return jobBuilderFactory.get("stepNextConditionalJob")
		                        .start(conditionalJobStep1())
		                            .on("FAILED") // on() : 캐치할 ExitStatus 상태를 지정. BatchStatus 아님에 주의!
		                            .to(conditionalJobStep3())
		                            .on("*")
		                            .end()
		                        .from(conditionalJobStep1())
		                            .on("*")
		                            .to(conditionalJobStep2())
		                            .next(conditionalJobStep3())
		                            .on("*")
		                            .end()
		                        .end()
		                        .build();
	}

	@Bean
	public Step conditionalJobStep1() {
		return stepBuilderFactory.get("conditionalJobStep1")
		                         .tasklet((contribution, chunkContext) -> {
			                         log.info(">>>>> This is stepNextConditionalJob Step1");
			                         // ExitStatus 를 FAILED 로 지정한다. 해당 status 를 보고 flow 가 진행된다.
			                         //contribution.setExitStatus(ExitStatus.FAILED);

			                         return RepeatStatus.FINISHED;
		                         })
		                         .build();
	}

	@Bean
	public Step conditionalJobStep2() {
		return stepBuilderFactory.get("conditionalJobStep2")
		                         .tasklet((contribution, chunkContext) -> {
			                         log.info(">>>>> This is stepNextConditionalJob Step2");
			                         return RepeatStatus.FINISHED;
		                         })
		                         .build();
	}

	@Bean
	public Step conditionalJobStep3() {
		return stepBuilderFactory.get("conditionalJobStep3")
		                         .tasklet((contribution, chunkContext) -> {
			                         log.info(">>>>> This is stepNextConditionalJob Step3");
			                         return RepeatStatus.FINISHED;
		                         })
		                         .build();
	}
}
