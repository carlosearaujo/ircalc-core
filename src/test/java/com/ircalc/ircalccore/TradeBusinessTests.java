package com.ircalc.ircalccore;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**@author carlos.araujo
   @since  5 de dez de 2017*/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TradeBusinessTests {
	
	@Test
	public void testeOne(){
		assertTrue(false);
	}
}
