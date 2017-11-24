package com.ircalc.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ircalc.model.Trade;
import com.ircalc.repository.TradeRepository;
import com.simplequery.GenericBusiness;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Service
public class TradeBusiness extends GenericBusiness<Trade> {
	
	@Autowired private TradeRepository tradeRepository;

	public Trade save(Trade trade) {
		return tradeRepository.save(trade);
	}
}
