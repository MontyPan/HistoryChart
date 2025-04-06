package us.dontcareabout.historyChart.client.vo;

import java.util.Comparator;
import java.util.Date;

public interface HasPeriod {
	Date getStartDate();
	Date getEndDate();

	public class StartDateComparator<T extends HasPeriod> implements Comparator<T> {
		@Override
		public int compare(T o1, T o2) {
			return o1.getStartDate().compareTo(o2.getStartDate());
		}
	}
}
