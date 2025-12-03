package us.dontcareabout.historyChart.client.vo;

import us.dontcareabout.gwt.client.google.sheet.Row;

public final class Group extends Row {
	protected Group() {}

	public String getName() {
		return stringField("名稱");
	}

	public String getParent () {
		return stringField("所屬分區");
	}

	public String getColor () {
		return stringField("顏色");
	}

	public String getIncidentColor () {
		return stringField("顏色");
	}
}
