package com.ircalc.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import static com.ircalc.model.MarketDirection.*;

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
		List<Trade> orderedTrades = tradeRepository.findAllByOrderByDateAscTicketAsc();
		Iterator<Trade> iterator = orderedTrades.iterator();
		while(iterator.hasNext()){
			Trade trade = iterator.next();
			VirtualTrade virtualTrade = new VirtualTrade(trade);
			if(hasDayTradeOnTradeDate(virtualTrade)){
				List<Trade> dayTradeOperations = orderDayTradeOperations(extractDayTradeOperations(trade, iterator));
				dayTradeOperations.forEach(dayTradeOperation ->{
					processTrade(new VirtualTrade(dayTradeOperation, true));
				});
				finalizeResidualDayTrade(trade.getTicket());
			}
			else{
				processTrade(new VirtualTrade(trade));
			}
		}
	}
	
    private void finalizeResidualDayTrade(String ticket) {
    	OpenTrade openDayTrade = openTradeRepository.findByTicketAndCloseTime(ticket, CloseTime.DAYTRADE);
    	if(openDayTrade != null){
    		openTradeRepository.delete(openDayTrade);
        	List<VirtualTrade> residualTrades = getResidualDayTrades(openDayTrade);
        	residualTrades.forEach(residualTrade -> processTrade(residualTrade));
    	}
    }
    
    private List<VirtualTrade> getResidualDayTrades(OpenTrade openDayTrade) {
        long openQuantity = openDayTrade.getOpenQuantity();
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


	private List<Trade> orderDayTradeOperations(List<Trade> operations) {
		List<Trade> buyOperations = operations.stream().filter(trade -> trade.getMarketDirection().equals(BUY)).collect(Collectors.toList());
		List<Trade> sellOperations = operations.stream().filter(trade -> trade.getMarketDirection().equals(SELL)).collect(Collectors.toList());
		long buyOperationsQuantity = buyOperations.stream().mapToLong(o -> o.getQuantity()).sum();
		long sellOperationsQuantity = buyOperations.stream().mapToLong(o -> o.getQuantity()).sum();
		if(buyOperationsQuantity > sellOperationsQuantity){
			return Stream.concat(buyOperations.stream(), sellOperations.stream()).collect(Collectors.toList());
		}
		return Stream.concat(sellOperations.stream(), buyOperations.stream()).collect(Collectors.toList());
	}

	private List<Trade> extractDayTradeOperations(Trade source, Iterator<Trade> iterator) {
		List<Trade> dayTradeOperations = tradeRepository.findByTicketAndDate(source.getTicket(), source.getDate());
		for(int i = 0 ; i < dayTradeOperations.size() - 1 ; i++){
			iterator.next();
		}
		return dayTradeOperations;
	}

	private void processTrade(VirtualTrade virtualTrade) {
		OpenTrade ticketOpenTrade = getPriorityOpenTrade(virtualTrade);
		if(ticketOpenTrade != null){
			if(ticketOpenTrade.getMarketDirection().equals(virtualTrade.getMarketDirection())){
				ticketOpenTrade.addNewReference(virtualTrade);
			}
			else{
				finalizeTrade(ticketOpenTrade, virtualTrade);
			}
		}
		else{
			addOpenTrade(virtualTrade);
		}
	}

	private OpenTrade getPriorityOpenTrade(VirtualTrade virtualTrade) {
		OpenTrade openDayTrade = openTradeRepository.findByTicketAndCloseTime(virtualTrade.getTicket(), CloseTime.DAYTRADE);
		if(openDayTrade != null || virtualTrade.isDayTrade()){
			return openDayTrade;
		}
		else{
			OpenTrade openNormalTrade = openTradeRepository.findByTicketAndCloseTime(virtualTrade.getTrade().getTicket(), CloseTime.NORMAL);
			return openNormalTrade;
		}
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
				processTrade(new VirtualTrade(virtualTrade.getTrade(), -openTrade.getOpenQuantity()));
			}
		}
		
	}

	public OpenTrade addOpenTrade(VirtualTrade virtualTrade){
		OpenTrade newOpenTrade = new OpenTrade(virtualTrade);
		newOpenTrade.setCloseTime(virtualTrade.isDayTrade() ? CloseTime.DAYTRADE : CloseTime.NORMAL);
		return openTradeRepository.save(newOpenTrade);
	}

	private boolean hasDayTradeOnTradeDate(VirtualTrade virtualTrade) {
		List<Trade> tradeWithSameTicketAndDayAndMarketDirectionComplement = tradeRepository.findByTicketAndDateAndMarketDirection(virtualTrade.getTicket(), virtualTrade.getDate(), virtualTrade.getMarketDirectionComplement());
		return !tradeWithSameTicketAndDayAndMarketDirectionComplement.isEmpty();
	}

	public List<OpenTrade> getOpenTrades() {
		return openTradeRepository.findAll();
	}
}
