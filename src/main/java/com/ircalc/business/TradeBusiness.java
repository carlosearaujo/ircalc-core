package com.ircalc.business;

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

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Service
public class TradeBusiness extends GenericBusiness<Trade> {
	
	@Autowired private TradeRepository tradeRepository;
	@Autowired private OpenTradeRepository openTradeRepository;
	@Autowired private FinalizedTradeRepository finalizedTradeRepository;

	@Transactional
	public Trade save(Trade trade) {
		OpenTrade ticketOpenTrade = openTradeRepository.findByTicket(trade.getTicket());
		if(ticketOpenTrade != null){
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
		return tradeRepository.save(trade);
	}

	private void finalizeTrade(OpenTrade openTrade, Trade trade) {
		FinalizedTrade finalizedTrade = new FinalizedTrade(openTrade, trade);
		finalizedTrade.getReferencedTradesAVGPrice();
		finalizedTradeRepository.save(finalizedTrade);
		openTrade.decreaseOpenQuantity(trade.getQuantity());
		if(openTrade.getOpenQuantity() <= 0){
			openTradeRepository.delete(openTrade);
		}
	}

	public void addOpenTrade(Trade trade){
		OpenTrade newOpenTrade = new OpenTrade(trade);
		openTradeRepository.save(newOpenTrade);
	}
}
