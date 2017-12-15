package com.ircalc.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import static com.ircalc.model.CloseTime.*;
import static com.ircalc.model.MarketType.*;


/**@author carlos.araujo
   @since  14 de dez de 2017*/
@NoArgsConstructor @Getter
public class MonthResult {
	
	private List<FinalizedTrade> monthTrades;
	@JsonIgnore
	private Map<String, Stream<FinalizedTrade>> splitResultByMarketType;
	
	public MonthResult(List<FinalizedTrade> monthTrades) {
		this.monthTrades = monthTrades;
	}

	public Map<String, Double> getNormalResult(){
		Map<String, Stream<FinalizedTrade>> tradesSplit = splitResultsByMarketType();
		
		return ImmutableMap.of(DEFAULT.toString(), tradesSplit.get(NORMAL.toString() + DEFAULT.toString()).mapToDouble(finalizedTrade -> finalizedTrade.getResult()).sum(),
							   OPTION.toString(), tradesSplit.get(NORMAL.toString() + OPTION.toString()).mapToDouble(finalizedTrade -> finalizedTrade.getResult()).sum());
	}

	private Map<String, Stream<FinalizedTrade>> splitResultsByMarketType() {
		Stream<FinalizedTrade> normalDefaultStream = monthTrades.stream().filter(finalizedTrade -> { 
			return NORMAL.equals(finalizedTrade.getCloseTime())  && MarketType.DEFAULT.equals(finalizedTrade.getMarketType());
		});
		Stream<FinalizedTrade> dayTradeDefaulStream = monthTrades.stream().filter(finalizedTrade -> { 
			return DAYTRADE.equals(finalizedTrade.getCloseTime())  && MarketType.DEFAULT.equals(finalizedTrade.getMarketType());
		});
		Stream<FinalizedTrade> normalOptionStream = monthTrades.stream().filter(finalizedTrade -> { 
			return NORMAL.equals(finalizedTrade.getCloseTime())  && MarketType.OPTION.equals(finalizedTrade.getMarketType());
		});
		Stream<FinalizedTrade> dayTradeOptionStream = monthTrades.stream().filter(finalizedTrade -> { 
			return DAYTRADE.equals(finalizedTrade.getCloseTime())  && MarketType.OPTION.equals(finalizedTrade.getMarketType());
		});
		return	ImmutableMap.of(NORMAL.toString() + DEFAULT.toString(), normalDefaultStream, 
							   	DAYTRADE.toString() + DEFAULT.toString(), dayTradeDefaulStream,
							   	NORMAL.toString() + OPTION.toString(), normalOptionStream,
							   	DAYTRADE.toString() + OPTION.toString(), dayTradeOptionStream);
	}
	
	public Map<String, Double> getDayTradeResult(){
		Map<String, Stream<FinalizedTrade>> tradesSplit = splitResultsByMarketType();
		return ImmutableMap.of(DEFAULT.toString(), tradesSplit.get(DAYTRADE.toString() + DEFAULT.toString()).mapToDouble(finalizedTrade -> finalizedTrade.getResult()).sum(),
				   			   OPTION.toString(), tradesSplit.get(DAYTRADE.toString() + OPTION.toString()).mapToDouble(finalizedTrade -> finalizedTrade.getResult()).sum());
	}
	
	public boolean isFreeFeeToNormalDefault(){
		double totalMonthSell = 0D;
		Map<String, Stream<FinalizedTrade>> tradesSplit = splitResultsByMarketType();
		Stream<FinalizedTrade> normalDefaultTrades = tradesSplit.get(NORMAL.toString() + DEFAULT.toString());
		for(FinalizedTrade finalizedTrade : normalDefaultTrades.collect(Collectors.toList())){
			totalMonthSell += finalizedTrade.getTotalSell();
		}
		return totalMonthSell <= 20000;
	}
}
