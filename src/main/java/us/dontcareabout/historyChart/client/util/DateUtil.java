package us.dontcareabout.historyChart.client.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

import us.dontcareabout.historyChart.client.vo.HasPeriod;

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

	/**
	 * 找出 list 中最初 / 最後的日期
	 * @return result.get(0) 是最初日期、result.get(1) 是最後日期
	 */
	public static <T extends HasPeriod> List<Date> getFirstLast(List<T> list) {
		Date start = DateUtil.LAST_DATE;
		Date end = DateUtil.FIRST_DATE;

		for (HasPeriod i : list) {
			Date iStart = i.getStartDate();
			Date iEnd = i.getEndDate();
			if (iStart != null && start.after(iStart)) { start = iStart; }
			if (iEnd != null && start.after(iEnd)) { start = iEnd; }
			if (iStart != null && end.before(iStart)) { end = iStart; }
			if (iEnd != null && end.before(iEnd)) { end = iEnd; }
		}

		return Arrays.asList(start, end);
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
