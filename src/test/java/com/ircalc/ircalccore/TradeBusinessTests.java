package com.ircalc.ircalccore;

import static org.junit.Assert.*;

import com.ircalc.business.TradeBusiness;
import com.ircalc.model.MarketDirection;
import com.ircalc.model.MarketType;
import com.ircalc.model.Trade;
import com.ircalc.repository.TradeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**@author carlos.araujo
   @since  5 de dez de 2017*/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TradeBusinessTests {

	@Autowired
	TradeBusiness tradeBusiness;
	@Autowired
	TradeRepository tradeRepository;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Test
	public void testeOne(){
		tradeBusiness.processTrades();
		Trade trade = new Trade();
		try {
			trade.setDate(simpleDateFormat.parse("01/01/2017"));
			trade.setTicket("CMIG4");
			trade.setPricePerUnit(12.85);
			trade.setQuantity(100L);
			trade.setMarketDirection(MarketDirection.BUY);
			trade.setBrokerTax(1.5);
			trade.setBrokerTaxFee(5D / 100);
			tradeRepository.save(trade);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		assertTrue(false);
	}
}
