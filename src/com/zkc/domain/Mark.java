package com.zkc.domain;

public class Mark {
	private Long id;
	private String boxCode;
	private String orderId;
	private String markProduct;
	private String countNo;
	private int pcs;
	private String caseNo;
	private String lotNo;
	private String markCode;
	private int checkedPage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	public String getMarkProduct() {
		return markProduct;
	}

	public void setMarkProduct(String markProduct) {
		this.markProduct = markProduct;
	}

	public String getCountNo() {
		return countNo;
	}

	public void setCountNo(String countNo) {
		this.countNo = countNo;
	}

	public int getPcs() {
		return pcs;
	}

	public void setPcs(int pcs) {
		this.pcs = pcs;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getMarkCode() {
		return markCode;
	}

	public void setMarkCode(String markCode) {
		this.markCode = markCode;
	}

	public int getCheckedPage() {
		return checkedPage;
	}

	public void setCheckedPage(int checkedPage) {
		this.checkedPage = checkedPage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Mark) {
			return ((Mark) obj).id.equals(id);
		}
		return false;
	}
}
