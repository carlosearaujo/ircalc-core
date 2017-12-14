package com.ircalc.model;

import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import static com.ircalc.model.CloseTime.*;

/**@author carlos.araujo
   @since  14 de dez de 2017*/
@NoArgsConstructor @Getter
public class MonthResult {
	
	private List<FinalizedTrade> monthTrades;
	
	public MonthResult(List<FinalizedTrade> monthTrades) {
		this.monthTrades = monthTrades;
	}

	public Double getNormalResult(){
		Stream<FinalizedTrade> normalTradesStream = monthTrades.stream().filter(trade -> NORMAL.equals(trade.getCloseTime()));
		return normalTradesStream.mapToDouble(finalizedTrade -> finalizedTrade.getResult()).sum();
	}
	
	public Double getDayTradeResult(){
		Stream<FinalizedTrade> dayTradesStream = monthTrades.stream().filter(trade -> DAYTRADE.equals(trade.getCloseTime()));
		return dayTradesStream.mapToDouble(finalizedTrade -> finalizedTrade.getResult()).sum();
	}
}
