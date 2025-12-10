package us.dontcareabout.historyChart.client.ui.timeline;

import java.util.Date;

import com.sencha.gxt.core.client.util.DateWrapper.Unit;

import us.dontcareabout.historyChart.client.util.DateUtil;

public class Argument {
	public static double dayUnit = 1.0 / 200;

	public static int padding = 5;

	public static int groupNameWidth = 22;
	public static int groupWidth = groupNameWidth + padding * 2;

	/** {@link EraLS} 名稱的高度 */
	public static int eraNameHeight = 26;
	public static int incidentHeight = 32;

	//==== 會這樣搞是避免閏年之類的曆法導致時間有出入 ====//
	/** 時間軸標示時間（軸線）的間距單位，要搭配 {@link #timePeriodAmount} 服用 */
	public static Unit timePeriodUnit = Unit.YEAR;

	/** 時間軸標示時間（軸線）的間距，要搭配 {@link #timePeriodUnit} 服用 */
	public static int timePeriodAmount = 100;
	// ========= //

	/** 時間軸時間字的高度 */
	public static int timeTextHeight = 26;

	public static double toWidth(Date start, Date target) {
		return DateUtil.daysBetween(start, target) * dayUnit;
	}
}
