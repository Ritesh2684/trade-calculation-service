package com.trade.calculation.service;

import java.util.HashMap;

import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trade.calculation.constant.ApplicationConstant;
import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeEventVO;
import com.trade.calculation.model.TradeSide;
import com.trade.calculation.repository.TradeEventRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TradeCalculationService {
	
	@Autowired
	TradeEventRepository tradeEventRepository;
	
	/**
	 * This method save both trade events with flag isTraded as 'Y' and updates the tradeAmount for each participant Id
	 * @param tradeEvent
	 * @param tradeEventVO
	 * @param stepExecution
	 * @return HashMap<String, Double> participantIdMap
	 */
	public TradeEventVO processNewAndOtherSideTradeEvent(TradeEventVO tradeEventVO, StepExecution stepExecution) {
		
		log.info("Started method processNewAndOtherSideTradeEvent ");
		
		TradeEvent otherSideTradeEvent;
		TradeEvent tradeEvent;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Double> participantIdMap = (HashMap<String, Double>) stepExecution
				.getJobExecution().getExecutionContext().get(ApplicationConstant.PARTICIPANT_ID_MAP);
		
		// update trade amount for each participant Id
		participantIdMap = TradeEventHelper.updateParticipantIdTradeAmount(tradeEventVO,participantIdMap);	
		
		log.info("Updated participant Id Map " + participantIdMap);
		
		tradeEvent = tradeEventVO.getTradeEvent();
		otherSideTradeEvent = tradeEventVO.getOtherSideTradeEvent();

		tradeEvent.setIsTraded(ApplicationConstant.Y);
		otherSideTradeEvent.setIsTraded(ApplicationConstant.Y);
		
		// save the incoming trade event with isTraded flat as 'Y'
		tradeEventRepository.save(tradeEvent);
		
		// save the other side with isTraded flag as 'Y'
		tradeEventRepository.save(otherSideTradeEvent);
		
		log.info("Ended method processNewAndOtherSideTradeEvent ");
		
		return tradeEventVO;
	}

	/**
	 * This method saves the new trade event to database
	 * @param tradeEvent
	 * @return savedTradeEvent
	 */
	public TradeEvent processNewTradeEvent(TradeEvent tradeEvent) {
		
		TradeEvent savedTradeEvent = null;		
		savedTradeEvent = tradeEventRepository.save(tradeEvent);
		
		return savedTradeEvent;
	}
	
	/**
	 * This method checks and gets the other side of the trade based on below parameters
	 * TradeID
	 * TradeReportType
	 * Equity
	 * Price
	 * Size
	 * TradeSide - 'S' if the tradeEvent is 'B' and vice versa
	 * @param tradeEventVO
	 * @return TradeEvent otherSideTradeEvent
	 */
	
	public TradeEventVO getOtherSideTradeEvent(TradeEventVO tradeEventVO) {
		
		log.info("Started method getOtherSideTradeEvent ");

		TradeEvent otherSideTradeEvent = null;
		TradeEvent tradeEvent = tradeEventVO.getTradeEvent();

		TradeSide tradeSide = null;		

		if (TradeSide.B.equals(tradeEvent.getTradeSide())) {
			tradeSide = TradeSide.S;
		} else {
			tradeSide = TradeSide.B;
		}

		otherSideTradeEvent = tradeEventRepository.findOtherSideTradeEvent(tradeEvent.getTradeId(),
				tradeEvent.getTradeReportType(), tradeEvent.getEquity(), tradeEvent.getPrice(), tradeEvent.getSize(),
				tradeSide);

		if (null != otherSideTradeEvent) {
			tradeEventVO.setOtherSideTradeEvent(otherSideTradeEvent);
			tradeEventVO.setOtherTradeSideExists(true);
		}
		
		log.info("Method ended getOtherSideTradeEvent ");
		
		return tradeEventVO;

	}

}
