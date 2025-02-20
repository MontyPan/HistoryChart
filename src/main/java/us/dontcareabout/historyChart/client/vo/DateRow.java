package us.dontcareabout.historyChart.client.vo;

import java.util.Date;

import com.google.common.base.Strings;

import us.dontcareabout.gwt.client.google.sheet.Row;

/**
 * 主要是提供 {@link #compoundDate(String)}。
 * <p>
 * Google Sheet 的日期 cell 無法有效輸入西元前的日期。
 * 另外，如果要 JS 直接處理西元前日期的字串，需要完整符合 expanded years 的格式，
 * 要求使用者輸入該格式太不實際，系統邏輯也應該要允許單純輸入年份而不輸入日期。
 * 因此最合適的方法就是將年代 / 日期分成兩個欄位儲存，在 VO 合併成 {@link Date} 方便後續程式使用。
 * <p>
 * <b>注意：</b>這裡假設年份 / 日期欄位有相同的 header，結尾是 {@link #yearTail}} / {@link #dateTail}。
 */
class DateRow extends Row {
	private static String yearTail = "年份";
	private static String dateTail = "日期";

	protected DateRow() {}

	/**
	 * 如果對應 yearTail 欄位沒有值，則忽略「日期」回傳 null。
	 * 如果對應 dateTail 欄位沒有值，則設定為當年的 1 月 1 號。
	 */
	@SuppressWarnings("deprecation")
	final Date compoundDate(String header) {
		if (Strings.isNullOrEmpty(stringField(header + yearTail))) {
			return null;
		}

		Date result = dateField(header + dateTail);

		if (result == null) { result = new Date(0, 0, 1); }

		result.setYear(intField(header + yearTail) - 1900);

		return result;
	}
}
