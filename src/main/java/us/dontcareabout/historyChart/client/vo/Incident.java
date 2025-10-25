package us.dontcareabout.historyChart.client.vo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.Validator;

/**
 * 對應 Google Sheet 的「事件」工作表的一筆資料。
 * <p>
 * 開始時間（{@link #getStartDate()}）與結束時間（{@link #getEndDate()}）必然有值。
 * 開始時間是透過 {@link IncidentValidator} 與 {@link SheetDto} 確保；
 * 如果原始資料沒有給結束時間，則 {@link #getEndDate()} 會回傳開始時間。
 */
public final class Incident extends DateRow implements HasPeriod {
	protected Incident() {}

	public String getName() {
		return stringField("名稱");
	}

	@Override
	public Date getStartDate() {
		return compoundDate("開始");
	}

	@Override
	public Date getEndDate() {
		Date result = compoundDate("結束");
		return result == null ? getStartDate() : result;
	}

	public String getParent () {
		return stringField("所屬事件");
	}

	public String getColor () {
		return stringField("顏色");
	}

	public static class IncidentValidator implements Validator<Incident> {
		@Override
		public List<Throwable> validate(Incident entry) {
			if (entry.getStartDate() == null) {
				return Arrays.asList(new Throwable("沒有開始日期"));
			}

			if (entry.getStartDate().after(entry.getEndDate())) {
				return Arrays.asList(new Throwable("結束日期大於開始日期"));
			}

			return null;
		}
	}
}
