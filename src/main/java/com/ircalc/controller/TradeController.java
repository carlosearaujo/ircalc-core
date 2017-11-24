package com.ircalc.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ircalc.business.TradeBusiness;
import com.ircalc.model.Trade;
import com.simplequery.GenericController;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@RestController
@RequestMapping(value="trade", produces=APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin(origins = "*")
public class TradeController extends GenericController<Trade> {
	
	@Autowired private TradeBusiness business;

	@Autowired
	public TradeController(TradeBusiness business) {
		super(business);
	}
	
	@RequestMapping(value = "/save")
	public Trade save(@RequestBody Trade trade){
		return business.save(trade);
	}
	
}
