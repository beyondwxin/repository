package com.newgen.domain.channel;



import java.io.Serializable;

/** 
 * ITEM的对应可序化队列属性
 *  */
public class ChannelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 栏目对应ID
	 *  */
	public Integer id;
	/** 
	 * 栏目对应NAME
	 *  */
	public String name;
	
	public String summary;
	
	public String type;
	
	public Integer showType;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;

	public ChannelItem() {
	}

	public ChannelItem(int id, String name,String summary, int orderId,int selected) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.summary =summary;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
	}

	public ChannelItem(Integer id, String name, String summary, String type,
			Integer orderId, Integer selected) {
		super();
		this.id = id;
		this.name = name;
		this.summary = summary;
		this.type = type;
		this.orderId = orderId;
		this.selected = selected;
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}
	
	

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	@Override
	public String toString() {
		return "ChannelItem [id=" + id + ", name=" + name + ", summary="
				+ summary + ", type=" + type + ", orderId=" + orderId
				+ ", selected=" + selected + "]";
	}
}