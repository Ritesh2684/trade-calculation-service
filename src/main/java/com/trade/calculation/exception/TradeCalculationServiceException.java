package com.trade.calculation.exception;

public class TradeCalculationServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public static final String COLON = ":";

	public TradeCalculationServiceException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

	public TradeCalculationServiceException(String errorCode, String errorMessage) {
		super(errorCode + COLON + errorMessage);
	}

}