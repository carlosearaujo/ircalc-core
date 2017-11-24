package com.ircalc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ircalc.model.Trade;

/**@author carlos.araujo
   @since  24 de nov de 2017*/
@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>  {

}
