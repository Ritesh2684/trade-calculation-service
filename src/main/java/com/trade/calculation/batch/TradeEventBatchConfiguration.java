package com.trade.calculation.batch;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.trade.calculation.constant.ApplicationConstant;
import com.trade.calculation.model.TradeEvent;

@EnableBatchProcessing
@Configuration
public class TradeEventBatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Value("${inputFileLocation}")
	private String inputFileLocation;

	@Autowired
	DataSource dataSource;

	@Bean
	@StepScope
	public FlatFileItemReader<TradeEvent> tradeEventFileReader(@Value("#{stepExecution}") StepExecution stepExecution) {

		Resource inputResource;
		FlatFileItemReader<TradeEvent> itemReader = new FlatFileItemReader<>();

		itemReader.setLineMapper(new TradeEventLineMapper());
		itemReader.setLinesToSkip(1);
		itemReader.setSaveState(false);
		inputResource = new FileSystemResource(inputFileLocation);
		itemReader.setResource(inputResource);
		stepExecution.getJobExecution().getExecutionContext().put(ApplicationConstant.PARTICIPANT_ID_MAP,
				new HashMap<String, Double>());
		return itemReader;
	}

	@Bean
	ItemProcessor<TradeEvent, TradeEvent> tradeEventProcessor() {
		return new TradeEventProcessor();
	}

	@Bean
	public Step tradeCalculationStep(FlatFileItemReader<TradeEvent> tradeEventFileReader,
			TradeEventProcessor tradeProcessor, TradeEventWriter tradeEventWriter,
			TradeEventStepExecutionListener tradeEventStepExecutionListener) {
		return stepBuilderFactory.get("tradeCalculationStep").<TradeEvent, TradeEvent>chunk(1)
				.reader(tradeEventFileReader).processor(tradeProcessor).writer(tradeEventWriter)
				.listener(tradeEventStepExecutionListener).build();
	}

	@Bean
	Job tradeCalculationJob(@Qualifier("tradeCalculationStep") Step tradeCalculationStep) {
		return jobBuilderFactory.get("Trade-Calculation-Job").incrementer(new RunIdIncrementer())
				.flow(tradeCalculationStep).end().build();
	}

}
