package com.trade.calculation.batch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeEventVO;
import com.trade.calculation.repository.TradeEventRepository;
import com.trade.calculation.service.TradeCalculationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope
@Component
public class TradeEventProcessor implements ItemProcessor<TradeEvent, TradeEvent> {

	@Autowired
	TradeCalculationService tradeCalculationService;

	@Autowired
	TradeEventRepository tradeEventRepository;

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@Override
	public TradeEvent process(TradeEvent tradeEvent) throws Exception {

		log.info("Trade processing started for trade event " + tradeEvent);

		if (null != tradeEvent) {

			TradeEventVO tradeEventVO = new TradeEventVO();
			tradeEventVO.setTradeEvent(tradeEvent);

			// check and get the other side Trade Event
			tradeEventVO = tradeCalculationService.getOtherSideTradeEvent(tradeEventVO);

			if (tradeEventVO.isOtherTradeSideExists()) {

				log.info("other side Trade event received " + tradeEventVO.getOtherSideTradeEvent());
				// if trade Event exists, update participant Id amounts and store the events
				// with isTraded = 'Y'
				tradeEventVO = tradeCalculationService.processNewAndOtherSideTradeEvent(tradeEventVO, stepExecution);

			} else {

				// if other side doesn't exists, store the trade event with isTraded as 'N'
				tradeEvent = tradeCalculationService.processNewTradeEvent(tradeEvent);

			}
		}

		log.info("Trade processing completed for trade event " + tradeEvent);

		return tradeEvent;
	}

}
