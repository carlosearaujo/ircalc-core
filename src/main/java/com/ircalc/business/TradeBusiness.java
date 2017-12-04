package com.ircalc.business;

import com.ircalc.model.*;
import com.ircalc.repository.FinalizedTradeRepository;
import com.ircalc.repository.OpenTradeRepository;
import com.ircalc.repository.VirtualTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ircalc.repository.TradeRepository;
import com.simplequery.GenericBusiness;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Service
public class TradeBusiness extends GenericBusiness<Trade> {
	
	@Autowired private TradeRepository tradeRepository;
	@Autowired private OpenTradeRepository openTradeRepository;
	@Autowired private FinalizedTradeRepository finalizedTradeRepository;
	@Autowired private VirtualTradeRepository virtualTradeRepository;

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
			processTrade(new VirtualTrade(trade), false);
		});
	}

	private void processTrade(VirtualTrade virtualTrade, boolean isResidual) {
		OpenTrade ticketOpenTrade = getPriorityOpenTrade(virtualTrade);
		if(ticketOpenTrade != null){
			if(ticketOpenTrade.getMarketDirection().equals(virtualTrade.getTrade().getMarketDirection())){
				ticketOpenTrade.addNewReference(virtualTrade);
			}
			else{
				finalizeTrade(ticketOpenTrade, virtualTrade);
			}
		}
		else{
			addOpenTrade(virtualTrade, isResidual);
		}
	}

	private OpenTrade getPriorityOpenTrade(VirtualTrade virtualTrade) {
		OpenTrade openDayTrade = openTradeRepository.findByTicketAndCloseTime(virtualTrade.getTrade().getTicket(), CloseTime.DAYTRADE);
		openDayTrade.getReferenceTrades().forEach(openReference ->{
			if(!openReference.getTrade().getDate().equals(virtualTrade)){
				openTradeRepository.delete(openDayTrade);
				List<VirtualTrade> residualTrades = getResidualDayTrades(openDayTrade);
				residualTrades.forEach(residualTrade -> processTrade(residualTrade, true));
			}
		});
		return openDayTrade != null ? openDayTrade : openTradeRepository.findByTicketAndCloseTime(virtualTrade.getTrade().getTicket(), CloseTime.NORMAL);
	}

	private List<VirtualTrade> getResidualDayTrades(OpenTrade openDayTrade) {
		long openQuantity = openDayTrade.getOpenQuantity();
		openDayTrade.getReferenceTrades().sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
		int i = 0;
		List<VirtualTrade> residualTrades = new ArrayList<>();
		while(openQuantity > 0){
			if(i > 0){
				VirtualTrade virtualTrade = openDayTrade.getReferenceTrades().get(i - 1);
				virtualTradeRepository.delete(virtualTrade);
				residualTrades.add(virtualTrade);
			}
			VirtualTrade virtualTrade = openDayTrade.getReferenceTrades().get(i);
			virtualTrade.setQuantity(virtualTrade.getQuantity() - openQuantity);
		}
		VirtualTrade lastVirtualTrade = openDayTrade.getReferenceTrades().get(i);
		if(lastVirtualTrade.getQuantity() != 0){
			residualTrades.add(new VirtualTrade(lastVirtualTrade.getTrade(), lastVirtualTrade.getTrade().getQuantity() - lastVirtualTrade.getQuantity()));
		}
		return residualTrades;
	}

	private void finalizeTrade(OpenTrade openTrade, VirtualTrade trade) {
		FinalizedTrade finalizedTrade = new FinalizedTrade();
		finalizedTrade.setReferencedTrades(new ArrayList<>(openTrade.getReferenceTrades()));
		finalizedTrade.setCloseTrade(trade);
		finalizedTrade.setCloseTime(openTrade.getCloseTime());
		finalizedTradeRepository.save(finalizedTrade);
		openTrade.decreaseOpenQuantity(trade.getQuantity());
		if(openTrade.getOpenQuantity() == 0){
			openTradeRepository.delete(openTrade);
		}
	}

	public void addOpenTrade(VirtualTrade virtualTrade, boolean isResidual){
		OpenTrade newOpenTrade = new OpenTrade(virtualTrade);
		Trade tradeWithSameTicketAndDay = tradeRepository.findByTicketAndDate(virtualTrade.getTrade().getTicket(), virtualTrade.getTrade().getDate());
		newOpenTrade.setCloseTime(tradeWithSameTicketAndDay == null || isResidual ? CloseTime.NORMAL : CloseTime.DAYTRADE);
		openTradeRepository.save(newOpenTrade);
	}

	public List<FinalizedTrade> getFinalizedTrades(){
		return finalizedTradeRepository.findAll();
	}
}
