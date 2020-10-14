package com.trade.calculation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trade.calculation.model.TradeEvent;
import com.trade.calculation.model.TradeReportType;
import com.trade.calculation.model.TradeSide;

@Repository
public interface TradeEventRepository extends JpaRepository<TradeEvent, Long> {

	@Query("SELECT tradeEvent FROM TradeEvent tradeEvent where tradeEvent.tradeId=:tradeId and tradeEvent.tradeReportType=:tradeReportType and "
			+ "tradeEvent.equity=:equity and tradeEvent.price=:price and tradeEvent.size=:size and tradeEvent.tradeSide=:tradeSide and "
			+ " tradeEvent.isTraded=\'N\'")
	public TradeEvent findOtherSideTradeEvent(@Param("tradeId") String tradeId,
			@Param("tradeReportType") TradeReportType tradeReportType, @Param("equity") String equity,
			@Param("price") double price, @Param("size") int size, @Param("tradeSide") TradeSide tradeSide);

}
