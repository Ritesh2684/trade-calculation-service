package com.trade.calculation.service;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeEventVO;
import com.trade.calculation.model.TradeReportType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TradeEventHelper {

	/**
	 * This method updates the trade amount for each participant Id - add amount in
	 * case of New - minus amount in case of cancel
	 * 
	 * @param tradeEventVO
	 * @param stepExecution
	 * @return HashMap<String, Double> participantIdMap
	 */
	public static HashMap<String, Double> updateParticipantIdTradeAmount(TradeEventVO tradeEventVO,
			HashMap<String, Double> participantIdMap) {

		log.info("Started method updateParticipantIdTradeAmount ");

		TradeEvent tradeEvent = tradeEventVO.getTradeEvent();
		TradeEvent otherSideTradeEvent = tradeEventVO.getOtherSideTradeEvent();

		// update trade amount for incoming trade event participant id
		updateTradeAmount(participantIdMap, tradeEvent);

		// update trade amount for other side trade event participant id
		updateTradeAmount(participantIdMap, otherSideTradeEvent);

		log.info("Ended method updateParticipantIdTradeAmount ");

		return participantIdMap;

	}

	private static HashMap<String, Double> updateTradeAmount(HashMap<String, Double> participantIdMap,
			TradeEvent tradeEvent) {

		log.info("Started method updateTradeAmount");

		double totalTradeAmountForParticipantId = 0.0;
		double updatedTradeAmountForParticipantId = 0.0;
		if (participantIdMap.containsKey(tradeEvent.getParticipantId())) {
			// get the existing cumulative amount
			totalTradeAmountForParticipantId = participantIdMap.get(tradeEvent.getParticipantId());
		}

		if (tradeEvent.getTradeReportType().equals(TradeReportType.New)) {
			// add amount in case of New
			updatedTradeAmountForParticipantId = totalTradeAmountForParticipantId + tradeEvent.getTradeAmount();
		} else {
			// minus amount in case of Cancel
			updatedTradeAmountForParticipantId = totalTradeAmountForParticipantId - tradeEvent.getTradeAmount();
		}

		participantIdMap.put(tradeEvent.getParticipantId(), updatedTradeAmountForParticipantId);

		log.info("Method ended updateTradeAmount ");

		return participantIdMap;
	}

}
