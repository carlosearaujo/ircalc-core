package com.ircalc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Entity @Getter @Setter
@Table(name = "TRADE")
public class Trade {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	private Date date;
	@Column
	private String ticket;
	

}
