package com.trade.calculation.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.stereotype.Component;

import com.trade.calculation.constant.ApplicationConstant;
import com.trade.calculation.exception.Errors;
import com.trade.calculation.exception.TradeCalculationServiceException;
import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeReportType;
import com.trade.calculation.model.TradeSide;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@StepScope
public class TradeEventLineMapper extends DefaultLineMapper<TradeEvent> {
	
	LineTokenizer lineTokenizer;

	FieldSet fieldSet;
	
	@Override
	public TradeEvent mapLine(String line, int lineNumber) throws Exception {

		String[] tokens = line.split(",");

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();

		delimitedLineTokenizer.setNames(ApplicationConstant.TRADE_ID, ApplicationConstant.TRADE_REPORT_TYPE, ApplicationConstant.EQUITY, ApplicationConstant.PRICE, ApplicationConstant.SIZE,
				ApplicationConstant.TRADE_SIDE,ApplicationConstant.PARTICIPANT_ID);
		delimitedLineTokenizer.setIncludedFields(0, 1, 2, 3, 4, 5, 6);
		delimitedLineTokenizer.setStrict(false);
		fieldSet = delimitedLineTokenizer.tokenize(line);
		TradeEvent tradeEvent = getTradeLine(fieldSet, tokens.length);

		setLineTokenizer(delimitedLineTokenizer);

		return tradeEvent;

	}
	
	@Override
	public void setLineTokenizer(LineTokenizer lineTokenizer) {

		super.setLineTokenizer(lineTokenizer);
		this.lineTokenizer = lineTokenizer;
	}
	
	private TradeEvent getTradeLine(FieldSet fieldSet, int length) {

		TradeEvent tradeEvent = null;
		
		tradeEvent = TradeEvent.builder().tradeId(fieldSet.readString(ApplicationConstant.TRADE_ID))
				.tradeReportType(TradeReportType.valueOf(fieldSet.readString(ApplicationConstant.TRADE_REPORT_TYPE)))
				.equity(fieldSet.readString(ApplicationConstant.EQUITY))
				.size(fieldSet.readInt(ApplicationConstant.SIZE))
				.tradeSide(TradeSide.valueOf(fieldSet.readString(ApplicationConstant.TRADE_SIDE)))
				.participantId(fieldSet.readString(ApplicationConstant.PARTICIPANT_ID))
				.price(fieldSet.readDouble(ApplicationConstant.PRICE))
				.isTraded(ApplicationConstant.N)
				.build();
				
		if (length != 7) {
			throw new TradeCalculationServiceException(Errors.INVALID_TRADE_EVENT.getErrorCode(), Errors.INVALID_TRADE_EVENT.getErrorMessage());
		}

		return tradeEvent;

	}

}
