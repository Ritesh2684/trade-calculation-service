package com.trade.calculation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.trade.calculation.constant.ApplicationConstant;
import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeEventVO;
import com.trade.calculation.model.TradeReportType;
import com.trade.calculation.model.TradeSide;
import com.trade.calculation.repository.TradeEventRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class TradeCalculationServiceTest {

	@InjectMocks
	TradeCalculationService tradeCalculationService;

	@Mock
	TradeEventRepository tradeEventRepository;
	
	@Mock
	StepExecution stepExecution;
	
	@Mock
	JobExecution jobExecution;
	
	@Mock
	ExecutionContext jobExecutionContext;
	
	TradeEvent tradeEvent;
	

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		tradeEvent = new TradeEvent();
		tradeEvent.setTradeId("AE445511");
		tradeEvent.setId(1L);
		tradeEvent.setTradeReportType(TradeReportType.New);
		tradeEvent.setPrice(100);
		tradeEvent.setTradeSide(TradeSide.B);
		tradeEvent.setSize(30);
		tradeEvent.setParticipantId("Firm 1");
		tradeEvent.setEquity("UNA");
		tradeEvent.setIsTraded('N');
	}

	@Test
	public void getOtherSideTradeEventTestWhenNotFound() throws Exception {
		
		TradeEventVO tradeEventVO = new TradeEventVO();
		
		tradeEventVO.setTradeEvent(tradeEvent);
		
		when(tradeEventRepository
                .findOtherSideTradeEvent(any(String.class), any(TradeReportType.class), any(String.class), any(Double.class), any(Integer.class), any(TradeSide.class))).thenReturn(
                null);
		
		tradeEventVO = tradeCalculationService.getOtherSideTradeEvent(tradeEventVO);
		
		
		Assert.assertEquals(null , tradeEventVO.getOtherSideTradeEvent());
		Assert.assertEquals(false , tradeEventVO.isOtherTradeSideExists());
	}
	
	@Test
	public void getOtherSideTradeEventTestWhenFound() throws Exception {
		
		TradeEventVO tradeEventVO = new TradeEventVO();
		
		tradeEventVO.setTradeEvent(tradeEvent);
		
		when(tradeEventRepository
                .findOtherSideTradeEvent("AE445511", TradeReportType.New, "UNA", 100, 30, TradeSide.S)).thenReturn(
                		otherSideTradeEvent());
		
		tradeEventVO = tradeCalculationService.getOtherSideTradeEvent(tradeEventVO);
		
		
		Assert.assertNotNull(tradeEventVO.getOtherSideTradeEvent());
		Assert.assertEquals(true , tradeEventVO.isOtherTradeSideExists());
	}
	
	@Test
	public void processNewAndOtherSideTradeEvent() throws Exception {
		
		TradeEventVO tradeEventVO = new TradeEventVO();
		
		HashMap<String, Double> participantIdMap = new HashMap<>();
		
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(otherSideTradeEvent());
		
		when(stepExecution
				.getJobExecution()).thenReturn(jobExecution);
		when(stepExecution
				.getJobExecution().getExecutionContext()).thenReturn(jobExecutionContext);
		when(stepExecution
				.getJobExecution().getExecutionContext().get(ApplicationConstant.PARTICIPANT_ID_MAP)).thenReturn(
						participantIdMap);
		
		tradeEventVO = tradeCalculationService.processNewAndOtherSideTradeEvent(tradeEventVO,stepExecution);

		Assert.assertEquals('Y' , tradeEventVO.getTradeEvent().getIsTraded());
		Assert.assertEquals('Y' , tradeEventVO.getOtherSideTradeEvent().getIsTraded());
	}

	
	private TradeEvent otherSideTradeEvent() {
		
		TradeEvent otherSideTradeEvent = new TradeEvent();
		otherSideTradeEvent.setTradeId("AE445511");
		otherSideTradeEvent.setId(2L);
		otherSideTradeEvent.setTradeReportType(TradeReportType.New);
		otherSideTradeEvent.setPrice(100);
		otherSideTradeEvent.setTradeSide(TradeSide.S);
		otherSideTradeEvent.setSize(30);
		otherSideTradeEvent.setParticipantId("Firm 2");
		otherSideTradeEvent.setEquity("UNA");
		otherSideTradeEvent.setIsTraded('N');
		
		return otherSideTradeEvent;
		
	}

	


}

