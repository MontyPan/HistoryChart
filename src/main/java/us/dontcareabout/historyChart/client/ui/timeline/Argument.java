package us.dontcareabout.historyChart.client.ui.timeline;

import java.util.Date;

import us.dontcareabout.historyChart.client.util.DateUtil;

public class Argument {
	public static double dayUnit = 1.0 / 200;

	public static int padding = 5;

	/** {@link EraLS} 名稱的高度 */
	public static int eraNameHeight = 26;
	public static int incidentHeight = 32;

	public static double toWidth(Date start, Date target) {
		return DateUtil.daysBetween(start, target) * dayUnit;
	}
}
