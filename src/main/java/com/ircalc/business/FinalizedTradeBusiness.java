package com.ircalc.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ircalc.model.FinalizedTrade;
import com.ircalc.model.MonthResult;
import com.ircalc.repository.FinalizedTradeRepository;
import com.simplequery.GenericBusiness;

/**@author carlos.araujo
   @since  27 de nov de 2017*/
@Service
public class FinalizedTradeBusiness extends GenericBusiness<FinalizedTrade> {
	
	@Autowired private FinalizedTradeRepository finalizedTradeRepository;

	public List<FinalizedTrade> getFinalizedTrades(String ticket){
		if(ticket == null){
			return finalizedTradeRepository.findAll();
		}
		return finalizedTradeRepository.findAllByCloseTrade_Trade_Ticket(ticket);
	}
	
	public List<FinalizedTrade> findByMonth(int month, int year) {
		year = month <= 12 ? year : year + 1;
		int nextMonth = month < 12 ? month + 1 : 1;
		
		DateTime initMonth = new DateTime(year, month, 1, 0, 0);
		DateTime finalMonth = new DateTime(year, nextMonth, 1, 0, 0);
		return findByDateRange(initMonth.toDate(), finalMonth.toDate());
	}
	
	public List<FinalizedTrade> findByDateRange(Date initInclusive, Date endExclusive) {
		return finalizedTradeRepository.findByDateRange(initInclusive, endExclusive);
	}
	
	public MonthResult getMonthResult(int month, int year){
		return new MonthResult(findByMonth(month, year));
	}
	
	public MonthResult getMonthResult(Date initInclusive, Date endExclusive){
		return new MonthResult(findByDateRange(initInclusive, endExclusive));
	}
	
	public List<MonthResult> getYearResult(int year){
		List<MonthResult> results = new ArrayList<>();
		for(int i = 1; i <= 12; i++){
			results.add(getMonthResult(i, year));
		}
		return results;
	}
}
