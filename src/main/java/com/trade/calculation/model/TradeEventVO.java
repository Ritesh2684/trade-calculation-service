package com.trade.calculation.model;

import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TradeEventVO {

	private TradeEvent tradeEvent;
	private boolean isOtherTradeSideExists;
	private TradeEvent otherSideTradeEvent;
	
}
