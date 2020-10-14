package com.trade.calculation.service;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeEventVO;
import com.trade.calculation.model.TradeReportType;
import com.trade.calculation.model.TradeSide;

public class TradeEventHelperTest {

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
	public void updateParticipantIdTradeAmountTestWhenNew() throws Exception {

		TradeEventVO tradeEventVO = new TradeEventVO();
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(getOtherSideTradeEvent());

		HashMap<String, Double> participantIdMap = new HashMap<>();
		participantIdMap.put("Firm 1", 5000.0);
		participantIdMap.put("Firm 2", 6000.0);

		participantIdMap = TradeEventHelper.updateParticipantIdTradeAmount(tradeEventVO, participantIdMap);

		Assert.assertEquals(8000.0, participantIdMap.get("Firm 1").doubleValue(), 0);
		Assert.assertEquals(9000.0, participantIdMap.get("Firm 2").doubleValue(), 0);

	}

	@Test
	public void updateParticipantIdTradeAmountTestWhenCancel() throws Exception {

		TradeEventVO tradeEventVO = new TradeEventVO();
		tradeEvent.setTradeReportType(TradeReportType.Cancel);
		tradeEventVO.setTradeEvent(tradeEvent);
		TradeEvent otherSideTradeEvent = getOtherSideTradeEvent();
		otherSideTradeEvent.setTradeReportType(TradeReportType.Cancel);
		tradeEventVO.setOtherSideTradeEvent(otherSideTradeEvent);

		HashMap<String, Double> participantIdMap = new HashMap<>();
		participantIdMap.put("Firm 1", 5000.0);
		participantIdMap.put("Firm 2", 6000.0);

		participantIdMap = TradeEventHelper.updateParticipantIdTradeAmount(tradeEventVO, participantIdMap);

		Assert.assertEquals(2000.0, participantIdMap.get("Firm 1").doubleValue(), 0);
		Assert.assertEquals(3000.0, participantIdMap.get("Firm 2").doubleValue(), 0);

	}

	@Test
	public void updateParticipantIdTradeAmountTestWhenPIdNotPresent() throws Exception {

		TradeEventVO tradeEventVO = new TradeEventVO();
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(getOtherSideTradeEvent());

		HashMap<String, Double> participantIdMap = new HashMap<>();

		participantIdMap = TradeEventHelper.updateParticipantIdTradeAmount(tradeEventVO, participantIdMap);

		Assert.assertEquals(3000.0, participantIdMap.get("Firm 1").doubleValue(), 0);
		Assert.assertEquals(3000.0, participantIdMap.get("Firm 2").doubleValue(), 0);

	}

	@Test
	public void updateParticipantIdTradeAmountTestWhenOnePIdNotPresent() throws Exception {

		TradeEventVO tradeEventVO = new TradeEventVO();
		tradeEventVO.setTradeEvent(tradeEvent);
		tradeEventVO.setOtherSideTradeEvent(getOtherSideTradeEvent());

		HashMap<String, Double> participantIdMap = new HashMap<>();
		participantIdMap.put("Firm 1", 5000.0);

		participantIdMap = TradeEventHelper.updateParticipantIdTradeAmount(tradeEventVO, participantIdMap);

		Assert.assertEquals(8000.0, participantIdMap.get("Firm 1").doubleValue(), 0);
		Assert.assertEquals(3000.0, participantIdMap.get("Firm 2").doubleValue(), 0);

	}

	private TradeEvent getOtherSideTradeEvent() {

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

}
