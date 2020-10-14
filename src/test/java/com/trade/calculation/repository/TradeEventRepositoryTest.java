package com.trade.calculation.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeReportType;
import com.trade.calculation.model.TradeSide;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TradeEventRepositoryTest {

	@Autowired
	TradeEventRepository tradeEventRepository;
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
	}

	@Test
	public void saveTradeEventTest() {

		TradeEvent storedTradeEvent = null;

		storedTradeEvent = tradeEventRepository.save(tradeEvent);
		Assert.assertEquals(tradeEvent.getEquity(), storedTradeEvent.getEquity());
		// other parameters could be added here

	}

	@Test
	public void findOtherSideTradeEventTest() {

		TradeEvent retrievedTradeEvent = null;
		tradeEventRepository.save(otherSideTradeEvent());

		retrievedTradeEvent = tradeEventRepository.findOtherSideTradeEvent(tradeEvent.getTradeId(),
				tradeEvent.getTradeReportType(), tradeEvent.getEquity(), tradeEvent.getPrice(), tradeEvent.getSize(),
				TradeSide.S);
		Assert.assertNotNull(retrievedTradeEvent);

	}
	
	@Test
	public void findOtherSideTradeEventWhenNotFoundTest() {

		TradeEvent retrievedTradeEvent = null;
		TradeEvent otherSideTradeEvent = otherSideTradeEvent();
		otherSideTradeEvent.setIsTraded('Y');
		tradeEventRepository.save(otherSideTradeEvent);

		retrievedTradeEvent = tradeEventRepository.findOtherSideTradeEvent(tradeEvent.getTradeId(),
				tradeEvent.getTradeReportType(), tradeEvent.getEquity(), tradeEvent.getPrice(), tradeEvent.getSize(),
				TradeSide.S);
		Assert.assertNull(retrievedTradeEvent);

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
