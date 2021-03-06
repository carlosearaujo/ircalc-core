package com.ircalc.repository;

import com.ircalc.model.CloseTime;
import com.ircalc.model.OpenTrade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Carlos on 25/11/2017.
 */
@Repository
public interface OpenTradeRepository extends JpaRepository<OpenTrade, Long> {

    OpenTrade findByTicket(String ticket);

	OpenTrade findByTicketAndCloseTime(String ticket, CloseTime closeTime);

	List<OpenTrade> findByCloseTime(CloseTime closeTime);
}
