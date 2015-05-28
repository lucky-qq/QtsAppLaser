package com.zkc.domain;

import java.util.List;

/**
 * 出货单,含唛头
 * @author IT
 *
 */
public class ShipOrderWithBoxCode extends ShipOrder {
	private List<String> boxCodes;

	public List<String> getBoxCodes() {
		return boxCodes;
	}

	public void setBoxCodes(List<String> boxCodes) {
		this.boxCodes = boxCodes;
	}
}
