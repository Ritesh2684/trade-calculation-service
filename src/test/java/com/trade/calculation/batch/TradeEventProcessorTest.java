package com.trade.calculation.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepExecution;

import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeEventVO;
import com.trade.calculation.model.TradeReportType;
import com.trade.calculation.model.TradeSide;
import com.trade.calculation.service.TradeCalculationService;

@RunWith(MockitoJUnitRunner.class)
public class TradeEventProcessorTest {

	@InjectMocks
	TradeEventProcessor tradeEventProcessor;
	
	@Mock
	TradeCalculationService tradeCalculationService;
	
	@Mock
	StepExecution stepExecution;
	
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
	public void getOtherSideTradeEventTestWhenExists() throws Exception {
		
		TradeEventVO tradeEventVO = new TradeEventVO();		
		
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(otherSideTradeEvent());
		tradeEventVO.setOtherTradeSideExists(true);
		
		when(tradeCalculationService.getOtherSideTradeEvent(any(TradeEventVO.class))).thenReturn(tradeEventVO);
		when(tradeCalculationService.processNewAndOtherSideTradeEvent(any(TradeEventVO.class), any(StepExecution.class))).thenReturn(getProcessedTradeEventVO());
		
		tradeEvent = tradeEventProcessor.process(tradeEvent);		
		
		Assert.assertEquals('Y', tradeEvent.getIsTraded());
	}
	
	@Test
	public void getOtherSideTradeEventTestWhenNotExists() throws Exception {
		
		TradeEventVO tradeEventVO = new TradeEventVO();		
		
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(null);
		tradeEventVO.setOtherTradeSideExists(false);
		
		when(tradeCalculationService.getOtherSideTradeEvent(any(TradeEventVO.class))).thenReturn(tradeEventVO);
		when(tradeCalculationService.processNewTradeEvent(any(TradeEvent.class))).thenReturn(tradeEvent);
		
		tradeEvent = tradeEventProcessor.process(tradeEvent);		
		
		Assert.assertNotNull(tradeEvent);
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
		
		return otherSideTradeEvent;
		
	}
	
	private TradeEventVO getProcessedTradeEventVO() {
		
		TradeEventVO tradeEventVO = new TradeEventVO();		
		
		tradeEvent.setIsTraded('Y');
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(otherSideTradeEvent());
		tradeEventVO.setOtherTradeSideExists(true);
		
		return tradeEventVO;
	}


}

