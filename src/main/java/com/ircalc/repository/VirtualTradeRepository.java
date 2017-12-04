package com.ircalc.repository;

import com.ircalc.model.VirtualTrade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Carlos on 04/12/2017.
 */
public interface VirtualTradeRepository extends JpaRepository<VirtualTrade, Long> {
}
