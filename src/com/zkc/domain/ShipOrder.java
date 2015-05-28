package com.zkc.domain;

/**
 * 出货单
 * @author IT
 *
 */
public class ShipOrder {
	public static final String STATE_PREPARE = "预出货";
	public static final String STATE_IN = "出货中";
	public static final String STATE_FINISH = "出货完";
	
	private String orderId;
	private String orderName;
	private String orderDate;
	private int markPage;
	private String orderOperator;
	private String orderNote;
	private String orderState;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public int getMarkPage() {
		return markPage;
	}

	public void setMarkPage(int markPage) {
		this.markPage = markPage;
	}

	public String getOrderOperator() {
		return orderOperator;
	}

	public void setOrderOperator(String orderOperator) {
		this.orderOperator = orderOperator;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ShipOrder) {
			return ((ShipOrder) obj).orderId.equals(orderId);
		}
		return false;
	}
}
