package com.april.unomas.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class OrderVO {
	
	private Integer order_code;
	private int user_num;
	private int prod_num;
	private Timestamp order_date;
	private int order_total;
	private String order_status;
	private int order_quantity;
	private String order_postalcode;
	private String order_roadaddr;
	private String order_detailaddr;
	private double user_point;
	
	private String prod_name;
	private int prod_price;
}
