package us.dontcareabout.historyChart.client.vo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import us.dontcareabout.gwt.client.google.sheet.Validator;

/**
 * 事件
 * <ul>
 * 	<li>允許沒有開始日期（例：生年不詳）</li>
 * 	<li>允許沒有結束日期（單指某一年 / 某一天）</li>
 * </ul>
 */
public final class Incident extends DateRow {
	protected Incident() {}

	public String getName() {
		return stringField("名稱");
	}

	public Date getStartDate() {
		return compoundDate("開始");
	}

	public Date getEndDate() {
		return compoundDate("結束");
	}

	public String getInterval () {
		return stringField("所在區間");
	}

	public static class IncidentValidator implements Validator<Incident> {
		@Override
		public List<Throwable> validate(Incident entry) {
			//日期的判斷
			//但是兩個都缺就是無效資料
			if (entry.getStartDate() == null && entry.getEndDate() == null) {
				return Arrays.asList(new Throwable("沒有開始日期也沒有結束日期"));
			}

			if (entry.getStartDate() != null && entry.getEndDate() != null &&
					entry.getStartDate().after(entry.getEndDate())) {
				return Arrays.asList(new Throwable("結束日期大於開始日期"));
			}

			return null;
		}
	}
}
