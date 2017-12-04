package com.ircalc.repository;

import com.ircalc.model.MarketDirection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ircalc.model.Trade;

import java.util.Date;
import java.util.List;

/**@author carlos.araujo
   @since  24 de nov de 2017*/
@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>  {

    List<Trade> findAllByOrderByDateAsc();

    Trade findByTicketAndDate(String ticket, Date date);
}
