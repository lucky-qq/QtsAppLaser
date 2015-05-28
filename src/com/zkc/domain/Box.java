package com.zkc.domain;

public class Box {
	public static final String STATE_CHECKED = "检查完";
	public static final String STATE_IN_WAREHOUSE = "已入库";
	public static final String STATE_MARK_CODE_CREATE = "唛头生成";
	public static final String STATE_CAN_OUT  = "可出库";
	public static final String STATE_OUT = "已出库";
	public static final String STATE_DELETED = "已作废";
	
	private String boxCode;
	private String customer;
	private String product;
	private String heatNo;
	private String state;

	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getHeatNo() {
		return heatNo;
	}

	public void setHeatNo(String heatNo) {
		this.heatNo = heatNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Box) {
			return ((Box) obj).boxCode.equals(boxCode);
		}
		return false;
	}
}
