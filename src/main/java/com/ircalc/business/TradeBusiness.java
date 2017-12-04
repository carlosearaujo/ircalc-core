package com.ircalc.business;

import com.ircalc.model.CloseTime;
import com.ircalc.model.FinalizedTrade;
import com.ircalc.model.OpenTrade;
import com.ircalc.repository.FinalizedTradeRepository;
import com.ircalc.repository.OpenTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ircalc.model.Trade;
import com.ircalc.repository.TradeRepository;
import com.simplequery.GenericBusiness;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Service
public class TradeBusiness extends GenericBusiness<Trade> {
	
	@Autowired private TradeRepository tradeRepository;
	@Autowired private OpenTradeRepository openTradeRepository;
	@Autowired private FinalizedTradeRepository finalizedTradeRepository;

	@Transactional
	public Trade save(Trade newTrade) {
		tradeRepository.save(newTrade);
		clearTradesStatus();
		processTrades();
		return  newTrade;
	}

	private void clearTradesStatus() {
		openTradeRepository.deleteAll();
		finalizedTradeRepository.deleteAll();
	}

	private void processTrades() {
		List<Trade> orderedTrades = tradeRepository.findAllByOrderByDateAsc();
		orderedTrades.forEach(trade -> {
			processTrade(trade);
		});
	}

	private void processTrade(Trade trade) {
		OpenTrade ticketOpenTrade = openTradeRepository.findByTicket(trade.getTicket());
		Trade tradeWithSameTicketAndDay = tradeRepository.findByTicketAndDateAndMarketDirection(trade.getTicket(), trade.getDate(), trade.getMarketDirectionComplement());
		if(tradeWithSameTicketAndDay != null && trade.getId() > tradeWithSameTicketAndDay.getId()){
			finalizeDayTrade(trade, tradeWithSameTicketAndDay);
		}
		else if(ticketOpenTrade != null){
			if(ticketOpenTrade.getMarketDirection().equals(trade.getMarketDirection())){
				ticketOpenTrade.addNewReference(trade);
			}
			else{
				finalizeTrade(ticketOpenTrade, trade);
			}
		}
		else{
			addOpenTrade(trade);
		}
	}

	private void finalizeDayTrade(Trade trade, Trade tradeWithSameTicketAndDay) {
		FinalizedTrade finalizedTrade = new FinalizedTrade();
		finalizedTrade.setCloseTime(CloseTime.DAYTRADE);
		finalizedTrade.setCloseTrade(trade);
		finalizedTrade.setReferencedTrades(Arrays.asList(tradeWithSameTicketAndDay));
		finalizedTradeRepository.save(finalizedTrade);
	}

	private void finalizeTrade(OpenTrade openTrade, Trade trade) {
		FinalizedTrade finalizedTrade = new FinalizedTrade(openTrade, trade);
		finalizedTradeRepository.save(finalizedTrade);
		openTrade.decreaseOpenQuantity(trade.getQuantity());
		if(openTrade.getOpenQuantity() == 0){
			openTradeRepository.delete(openTrade);
		}
	}

	public void addOpenTrade(Trade trade){
		OpenTrade newOpenTrade = new OpenTrade(trade);
		openTradeRepository.save(newOpenTrade);
	}

	public List<FinalizedTrade> getFinalizedTrades(){
		return finalizedTradeRepository.findAll();
	}
}
