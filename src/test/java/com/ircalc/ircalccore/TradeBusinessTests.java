package com.ircalc.ircalccore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import com.ircalc.business.TradeBusiness;
import com.ircalc.model.CloseTime;
import com.ircalc.model.FinalizedTrade;
import com.ircalc.model.MarketDirection;
import com.ircalc.model.MarketType;
import com.ircalc.model.Trade;
import com.ircalc.repository.FinalizedTradeRepository;
import com.ircalc.repository.OpenTradeRepository;
import com.ircalc.repository.TradeRepository;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**@author carlos.araujo
   @since  5 de dez de 2017*/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TradeBusinessTests {

	@Autowired private TradeRepository tradeRepo;
	@Autowired private OpenTradeRepository openTradeRepo;
	@Autowired private FinalizedTradeRepository finalizedTradeRepo;

	@Autowired TradeBusiness tradeBusiness;

	@After
	public void clearTrades(){
		tradeRepo.deleteAll();
	}

	private Trade buildTrade(Trade propertiesToChange) {
		Trade trade = new Trade(null, new DateTime(2017, 5, 5,0,0).toDate(), "CMIG4", 12.85, 100L, MarketType.DEFAULT, MarketDirection.BUY, 1.5, 5D / 100);
		if(propertiesToChange != null){
			copyNotNullFields(trade, propertiesToChange);
		}
		return trade;
	}

	private List<Trade> buildDayTradeWithResidualList() {
		return Arrays.asList(new Trade[]{buildTrade(new Trade(100)), buildTrade(new Trade(100)),
													   buildTrade(new Trade(50, MarketDirection.SELL)),
													   buildTrade(new Trade(new DateTime(2017, 6, 5, 0, 0).toDate()))});
	}
	
	private void copyNotNullFields(Object target, Object source){
		try {
			PropertyUtils.describe(source).entrySet().stream().filter(e -> e.getValue() != null).filter(e -> ! e.getKey().equals("class"))
	    	.forEach(e -> {
		    	try {
		    		PropertyUtils.setProperty(target, e.getKey(), e.getValue());
		    	} catch (Exception ex) {
		        }	
	    	});
		} catch (Exception ex) {
            throw new RuntimeException(ex);
        }
	}
	
	@Test
	public void testAddNewOpenTrade(){
		Trade trade = buildTrade(null);
		tradeRepo.save(trade);
		tradeBusiness.processTrades();
		assertTrue(openTradeRepo.count() == 1);
	}
	
	@Test
	public void testTradeFinalization(){
		tradeRepo.save(Arrays.asList(buildTrade(new Trade(MarketDirection.BUY)), buildTrade(new Trade(MarketDirection.SELL))));
		tradeBusiness.processTrades();
		
		assertEquals(finalizedTradeRepo.count(), 1L);
		assertEquals(openTradeRepo.count(), 0L);
	}
	
	@Test
	public void testDayTradeWithResidualNormal(){
		tradeRepo.save(buildDayTradeWithResidualList());
		testDayTradeWithResidual();
	}
	
	@Test
	public void testDayTradeWithResidualInverse(){
		List<Trade> dayTradeTestList = buildDayTradeWithResidualList();
		dayTradeTestList.set(1, dayTradeTestList.get(2));
		tradeRepo.save(dayTradeTestList);
		testDayTradeWithResidual();
	}

	@Test
	public void testDayTradeWithOpenTradeInSameDirectionFirst(){
		Date dayTradeDate = new DateTime(2017, 10, 10, 0, 0).toDate();
		Trade[] trades = new Trade[]{ buildTrade(new Trade(100)),
									  buildTrade(new Trade(MarketDirection.BUY, dayTradeDate)),
									  buildTrade(new Trade(MarketDirection.SELL, dayTradeDate))};
		tradeRepo.save(Arrays.asList(trades));
		tradeBusiness.processTrades();

		List<FinalizedTrade> finalizedTrades = finalizedTradeRepo.findAll();
		assertThat(finalizedTrades).hasSize(1).first().hasFieldOrPropertyWithValue("closeTime", CloseTime.DAYTRADE);
	}
	
	@Test
	public void testDirectionChangeInOneTrade(){
		Trade[] trades = new Trade[]{buildTrade(null), buildTrade(new Trade(200, MarketDirection.SELL))};
		tradeRepo.save(Arrays.asList(trades));
		tradeBusiness.processTrades();
		
		List<FinalizedTrade> finalizedTrades = finalizedTradeRepo.findAll();
		assertThat(finalizedTrades).hasSize(1);
		FinalizedTrade finalizedTrade = finalizedTrades.get(0);
		assertThat(finalizedTrade.getCloseTrade()).hasFieldOrPropertyWithValue("quantity", 100L);
		assertThat(openTradeRepo.findAll()).hasSize(1).first().hasFieldOrPropertyWithValue("marketDirection", MarketDirection.SELL);
	}

	private void testDayTradeWithResidual() {
		tradeBusiness.processTrades();
		List<FinalizedTrade> finalizedTrades = finalizedTradeRepo.findAll();
		assertThat(finalizedTrades).hasSize(1).first().hasFieldOrPropertyWithValue("closeTime", CloseTime.DAYTRADE);
		assertThat(finalizedTrades.get(0).getOpenVirtualTrades()).hasSize(1).first().hasFieldOrPropertyWithValue("quantity", 50L);
		assertThat(finalizedTrades.get(0).getCloseTrade()).hasFieldOrPropertyWithValue("quantity", 50L);
		assertThat(openTradeRepo.findAll()).hasSize(1).first().hasFieldOrPropertyWithValue("closeTime", CloseTime.NORMAL);
	}
}
