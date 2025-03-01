package us.dontcareabout.historyChart.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

@SuppressWarnings("deprecation")
public class DateUtil {
	/**
	 * JS Date() 的值域範圍（不計正負）。
	 * ref： https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Date
	 */
	public static final long MAX_PERIOD = 86400000L * 100000000L;

	/** JS Date 能處理的第一個日期。 */
	public static final Date FIRST_DATE = new Date(-MAX_PERIOD);

	/** JS Date 能處理的最後日期。 */
	public static final Date LAST_DATE = new Date(MAX_PERIOD);

	public static final String FORMAT_SPLIT = "/";
	public static final DateTimeFormat YMD_FORMAT
		= DateTimeFormat.getFormat("yyyy" + FORMAT_SPLIT + "MM" + FORMAT_SPLIT + "dd");
	public static final DateTimeFormat YM_FORMAT
		= DateTimeFormat.getFormat("yyyy" + FORMAT_SPLIT + "MM");

	public static int year(Date d) {
		return d.getYear() + 1900;
	}

	public static int daysBetween(Date start, Date end) {
		return CalendarUtil.getDaysBetween(start, end);
	}

	public static String toY(Date d) { return "" + year(d); }
	public static String toYM(Date d) { return format(d, YM_FORMAT); }
	public static String toYMD(Date d) { return format(d, YMD_FORMAT); }

	private static String format(Date d, DateTimeFormat fmt) {
		if (d == null) { return ""; }

		String origin = fmt.format(d);
		return year(d) + origin.substring(origin.indexOf(FORMAT_SPLIT));
	}
}
