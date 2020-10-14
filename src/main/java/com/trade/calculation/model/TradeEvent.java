package com.trade.calculation.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TradeEvent {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String tradeId;
	
	@Enumerated(EnumType.STRING)
	private TradeReportType tradeReportType;
	private String equity;
	private double price;
	private int size;
	
	@Enumerated(EnumType.STRING)
	private TradeSide tradeSide;
	private String participantId;
	private char isTraded;
	
	public double getTradeAmount() {
		return this.price*this.size;
	}
	

}
