package com.trade.calculation.batch;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.trade.calculation.constant.ApplicationConstant;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(value = "classpath:application-test.properties")
public class TradeEventBatchTest {

	@Autowired
	private Job tradeCalculationJob;

	@Autowired
	private JobLauncher jobLauncher;

	@Resource
	private JobLocator locator;

	@SuppressWarnings("unchecked")
	@Test
	public void testBatchConfiguration() throws Exception {

		HashMap<String, Double> participantIdMap = null;

		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(tradeCalculationJob, jobParameters);

		if (jobExecution.getExecutionContext().containsKey(ApplicationConstant.PARTICIPANT_ID_MAP)) {
			participantIdMap = (HashMap<String, Double>) jobExecution.getExecutionContext()
					.get(ApplicationConstant.PARTICIPANT_ID_MAP);
		}

		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		Assert.assertNotNull(participantIdMap);
		Assert.assertEquals(5216.75, participantIdMap.get("Firm 1").doubleValue(), 0);
		Assert.assertEquals(6575.0, participantIdMap.get("Firm 2").doubleValue(), 0);
		Assert.assertEquals(4377.5, participantIdMap.get("Firm 3").doubleValue(), 0);
		Assert.assertEquals(1430.0, participantIdMap.get("Firm 4").doubleValue(), 0);
		Assert.assertEquals(501.75000000000006, participantIdMap.get("Firm 5").doubleValue(), 0);
		Assert.assertEquals(429.0, participantIdMap.get("Firm 6").doubleValue(), 0);
		Assert.assertEquals(8096.5, participantIdMap.get("Firm 7").doubleValue(), 0);

	}

}
