package com.ircalc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**@author carlos.araujo
   @since  17 de nov de 2017*/
@Entity @Getter @Setter
@Table(name = "TRADE")
public class Trade {
	
	@Id
	@SequenceGenerator(name="sqc_trade", sequenceName="sqc_trade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqc_trade")
	private Long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	@Column
	private Date date;
	@Column
	private String ticket;
	@Column
	private Double price;
	@Column
	private Double quantity;
	
	

}
