package com.trade.calculation.batch;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import com.trade.calculation.constant.ApplicationConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TradeEventStepExecutionListener implements StepExecutionListener{

	
	 @Override
	    public void beforeStep(StepExecution stepExecution) {
	        log.info("StepExecutionListener - beforeStep " );

	    }

	    @SuppressWarnings("unchecked")
		@Override
	    public ExitStatus afterStep(StepExecution stepExecution) {	
	    	
	    	HashMap<String,Double> hashMap = null;
    	
	        log.info("------------------------------------------------------------------------------------");
	        log.info("StepExecutionListener - afterStep:getFilterCount=" +  stepExecution.getFilterCount());
	        log.info("StepExecutionListener - afterStep:getProcessSkipCount=" +  stepExecution.getProcessSkipCount());
	        log.info("StepExecutionListener - afterStep:getReadCount=" +  stepExecution.getReadCount());
	        log.info("StepExecutionListener - afterStep:getReadSkipCount=" +  stepExecution.getReadSkipCount());
	        log.info("StepExecutionListener - afterStep:getRollbackCount=" +  stepExecution.getRollbackCount());
	        log.info("StepExecutionListener - afterStep:getWriteCount=" +  stepExecution.getWriteCount());
	        log.info("StepExecutionListener - afterStep:getWriteSkipCount=" +  stepExecution.getWriteSkipCount());
	        log.info("StepExecutionListener - afterStep:getStepName=" +  stepExecution.getStepName());
	        log.info("StepExecutionListener - afterStep:getSummary=" +  stepExecution.getSummary());
	        log.info("StepExecutionListener - afterStep:getStartTime=" +  stepExecution.getStartTime());
	        log.info("StepExecutionListener - afterStep:getStartTime=" +  stepExecution.getEndTime());
	        log.info("StepExecutionListener - afterStep:getLastUpdated=" +  stepExecution.getLastUpdated());
	        log.info("StepExecutionListener - afterStep:getExitStatus=" +  stepExecution.getExitStatus());
	        log.info("StepExecutionListener - afterStep:getFailureExceptions=" +  stepExecution.getFailureExceptions());
		    log.info("------------------------------------------------------------------------------------");
		    log.info("------------------------------------------------------------------------------------");
		    log.info("------------------------------------------------------------------------------------");
		    log.info("--------------TOTAL AMOUNT PER PARTICIPANT ID---------------------------------------");		    
		    
		    if(stepExecution.getJobExecution().getExecutionContext().containsKey(ApplicationConstant.PARTICIPANT_ID_MAP)) {					
				hashMap = (HashMap<String, Double>) stepExecution.getJobExecution().getExecutionContext().get(ApplicationConstant.PARTICIPANT_ID_MAP);
			}
		    
		    hashMap.forEach((k,v) -> System.out.println("ParticipantID :  " + k + ", TotalTradeAmount: " + v));
		    
		    
		    log.info("--------------TOTAL AMOUNT PER PARTICIPANT ID---------------------------------------");
	        
	        return null;
	    }

}
