package com.ircalc.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ircalc.business.FinalizedTradeBusiness;
import com.ircalc.model.FinalizedTrade;
import com.ircalc.model.MonthResult;
import com.simplequery.GenericController;

/**@author carlos.araujo
   @since  27 de nov de 2017*/
@RestController
@RequestMapping(value="finalizedTrade", produces=APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin(origins = "*")
public class FinalizedTradeController extends GenericController<FinalizedTrade> {
	
	@Autowired
	private FinalizedTradeBusiness business;

	
	@Autowired
	public FinalizedTradeController(FinalizedTradeBusiness business) { super(business); }
	
	@RequestMapping(value = "getPeriodResult")
	public List<FinalizedTrade> findByDateRange(@RequestParam Date initInclusive, @RequestParam Date endExclusive){
		return business.findByDateRange(initInclusive, endExclusive);
	}
	
	@RequestMapping(value = "getMonthResult")
	public MonthResult getMonthResult(@RequestParam int month, @RequestParam int year){
		return business.getMonthResult(month, year);
	}
	
	@RequestMapping(value = "getYearResult")
	public List<MonthResult> getYearResult(@RequestParam int year){
		return business.getYearResult(year);
	}

	@RequestMapping(value = "/getFinalizeds")
	public List<FinalizedTrade> getFinalizedTrades(@RequestParam("ticket") String ticket){
		return business.getFinalizedTrades(ticket);
	}

}
