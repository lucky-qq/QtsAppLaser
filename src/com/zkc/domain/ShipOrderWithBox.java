package com.zkc.domain;

import java.util.List;

/**
 * 出货单
 * @author IT
 *
 */
public class ShipOrderWithBox extends ShipOrder {
	private List<BoxWithMarkPage> boxes;
	
//	public static JsonConfig jsonCfg = new JsonConfig();
//
//	 static {
//	    	jsonCfg.setRootClass(ShipOrderWithBox.class);
//	    	Map<String, Object> classMap = new HashMap<String, Object>();
//	    	classMap.put("boxes", BoxWithMarkPage.class);
//			jsonCfg.setClassMap(classMap);
//			jsonCfg.setCollectionType(List.class);
//	    }

	public List<BoxWithMarkPage> getBoxes() {
		return boxes;
	}

	public void setBoxes(List<BoxWithMarkPage> boxes) {
		this.boxes = boxes;
	}
}
