package com.zkc.domain;

public class BoxWithBundle extends Box {
	private int totalNum;
	private String bundleNo;

//	public static BoxWithBundle fromJson(JSONObject jsonObject) {
//		try {
//			BoxWithBundle boxWithBundle = new BoxWithBundle();
//			boxWithBundle.setBoxCode(jsonObject.getString("boxCode"));
//			boxWithBundle.setCustomer(jsonObject.getString("customer"));
//			boxWithBundle.setProduct(jsonObject.getString("product"));
//			boxWithBundle.setHeatNo(jsonObject.getString("heatNo"));
//			boxWithBundle.setState(jsonObject.getString("state"));
//			boxWithBundle.setTotalNum(Integer.parseInt(jsonObject.getString("totalNum")));
//			boxWithBundle.setBundleNo(jsonObject.getString("bundleNo"));
//			return boxWithBundle;
//		}catch (Exception e) {
//			Log.d("TAG", e.getMessage());
//			return null;
//		}
//	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getBundleNo() {
		return bundleNo;
	}

	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof  BoxWithBundle) {
			return ((BoxWithBundle) obj).getBoxCode().equals(getBoxCode());
		}
		return false;
	}
}
