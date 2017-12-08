package com.ircalc.business;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ircalc.model.CloseTime;
import com.ircalc.model.FinalizedTrade;
import com.ircalc.model.OpenTrade;
import com.ircalc.model.Trade;
import com.ircalc.model.VirtualTrade;
import com.ircalc.repository.FinalizedTradeRepository;
import com.ircalc.repository.OpenTradeRepository;
import com.ircalc.repository.TradeRepository;
import com.ircalc.repository.VirtualTradeRepository;
import com.simplequery.GenericBusiness;

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
		processTrades();
		return  newTrade;
	}

	private void clearTradesStatus() {
		openTradeRepository.deleteAll();
		finalizedTradeRepository.deleteAll();
		virtualTradeRepository.deleteAll();
	}

	@Transactional
	public void processTrades() {
		clearTradesStatus();
		List<Trade> orderedTrades = tradeRepository.findAllByOrderByDateAsc();
		orderedTrades.forEach(trade -> {
			finalizeOpenDayTradesAfterCurrentDate(trade);
			processTrade(new VirtualTrade(trade), false);
		});
	}

	private void finalizeOpenDayTradesAfterCurrentDate(Trade currentTrade) {
		List<OpenTrade> openDayTrades = openTradeRepository.findByCloseTime(CloseTime.DAYTRADE);
		for(OpenTrade openDayTrade : openDayTrades){
			if(openDayTrade.getOpenVirtualTrades().get(0).getDate().before((currentTrade.getDate()))){
				openTradeRepository.delete(openDayTrade);
				List<VirtualTrade> residualTrades = getResidualDayTrades(openDayTrade);
				residualTrades.forEach(residualTrade -> processTrade(residualTrade, true));
			}
		}
	}

	private void processTrade(VirtualTrade virtualTrade, boolean isResidual) {
		OpenTrade ticketOpenTrade = getPriorityOpenTrade(virtualTrade, isResidual);
		if(ticketOpenTrade != null){
			if(ticketOpenTrade.getMarketDirection().equals(virtualTrade.getMarketDirection())){
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

	private OpenTrade getPriorityOpenTrade(VirtualTrade virtualTrade, boolean isResidual) {
		OpenTrade openDayTrade = openTradeRepository.findByTicketAndCloseTime(virtualTrade.getTrade().getTicket(), CloseTime.DAYTRADE);
		if(openDayTrade != null){
			return openDayTrade;
		}
		else{
			OpenTrade openNormalTrade = openTradeRepository.findByTicketAndCloseTime(virtualTrade.getTrade().getTicket(), CloseTime.NORMAL);
			if((hasDayTradeOnTradeDate(virtualTrade) && !isResidual)){
				return null;
			}
			return openNormalTrade;
		}
	}

	private List<VirtualTrade> getResidualDayTrades(OpenTrade openDayTrade) {
		long openQuantity = openDayTrade.getOpenQuantity();
		openDayTrade.getOpenVirtualTrades().sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
		int i = 0;
		List<VirtualTrade> residualTrades = new ArrayList<>();
		while(openQuantity > 0){
			VirtualTrade virtualTrade = openDayTrade.getOpenVirtualTrades().get(i);
			openQuantity -= virtualTrade.getQuantity();
			if(openQuantity >= 0){
				virtualTradeRepository.delete(virtualTrade);
				residualTrades.add(new VirtualTrade(virtualTrade.getTrade(), virtualTrade.getQuantity()));
			}
			else if(openQuantity < 0){
				residualTrades.add(new VirtualTrade(virtualTrade.getTrade(), openQuantity + virtualTrade.getQuantity()));
				virtualTrade.setQuantity(-openQuantity);
			}
			i++;
		}
		return residualTrades;
	}

	private void finalizeTrade(OpenTrade openTrade, VirtualTrade virtualTrade) {
		FinalizedTrade finalizedTrade = new FinalizedTrade();
		finalizedTrade.setOpenVirtualTrades(new ArrayList<>(openTrade.getOpenVirtualTrades()));
		finalizedTrade.setCloseTrade(virtualTrade);
		finalizedTrade.setCloseTime(openTrade.getCloseTime());
		finalizedTradeRepository.save(finalizedTrade);
		openTrade.decreaseOpenQuantity(virtualTrade.getQuantity());
		if(openTrade.getOpenQuantity() <= 0){
			openTradeRepository.delete(openTrade);
			
			if(openTrade.getOpenQuantity() < 0){
				virtualTrade.setQuantity(virtualTrade.getQuantity() + openTrade.getOpenQuantity());
				processTrade(new VirtualTrade(virtualTrade.getTrade(), -openTrade.getOpenQuantity()), false);
			}
		}
		
	}

	public void addOpenTrade(VirtualTrade virtualTrade, boolean isResidual){
		OpenTrade newOpenTrade = new OpenTrade(virtualTrade);
		newOpenTrade.setCloseTime(!hasDayTradeOnTradeDate(virtualTrade) || isResidual ? CloseTime.NORMAL : CloseTime.DAYTRADE);
		openTradeRepository.save(newOpenTrade);
	}

	private boolean hasDayTradeOnTradeDate(VirtualTrade virtualTrade) {
		List<Trade> tradeWithSameTicketAndDayAndMarketDirectionComplement = tradeRepository.findByTicketAndDateAndMarketDirection(virtualTrade.getTicket(), virtualTrade.getDate(), virtualTrade.getTrade().getMarketDirectionComplement());
		return !tradeWithSameTicketAndDayAndMarketDirectionComplement.isEmpty();
	}

	public List<FinalizedTrade> getFinalizedTrades(){
		return finalizedTradeRepository.findAll();
	}
}
