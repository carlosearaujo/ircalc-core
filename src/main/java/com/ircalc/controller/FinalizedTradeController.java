package com.ircalc.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ircalc.business.FinalizedTradeBusiness;
import com.ircalc.model.FinalizedTrade;
import com.simplequery.GenericController;

/**@author carlos.araujo
   @since  27 de nov de 2017*/
@RestController
@RequestMapping(value="finalizedTrade", produces=APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin(origins = "*")
public class FinalizedTradeController extends GenericController<FinalizedTrade> {

	
	@Autowired
	public FinalizedTradeController(FinalizedTradeBusiness business) { super(business); }

}
