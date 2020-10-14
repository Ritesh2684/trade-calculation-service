package com.trade.calculation.batch;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trade.calculation.model.TradeEvent;

@Service
@StepScope
@Transactional()
public class TradeEventWriter implements ItemWriter<TradeEvent>{


	@Override
	public void write(List<? extends TradeEvent> items) throws Exception {
		
		
	}

}
