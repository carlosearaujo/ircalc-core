package com.ircalc.model;

/**
 * Created by Carlos on 25/11/2017.
 */
public enum MarketDirection {
    BUY, SELL;

	public static MarketDirection getComplement(MarketDirection direction) {
		if(MarketDirection.BUY.equals(direction)){
			return MarketDirection.SELL;
		}
		return MarketDirection.BUY;
	}
}
