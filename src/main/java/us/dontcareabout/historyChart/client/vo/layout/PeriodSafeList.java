package us.dontcareabout.historyChart.client.vo.layout;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import us.dontcareabout.historyChart.client.vo.HasPeriod;

/**
 * 一組安全、不會重疊的 {@link HasPeriod}。
 * 亦即：任意兩個 element 的起訖日期區間都不會重疊（但允許相等）。
 */
public class PeriodSafeList implements HasPeriod {
	private List<HasPeriod> list = Lists.newArrayList();
	private Date startDate;
	private Date endDate;

	/**
	 * 前提假設：傳入 period 的開始時間小於 list 中任一元素的開始時間。
	 */
	public boolean accept(HasPeriod newPeriod) {
		if (list.isEmpty()) {
			list.add(newPeriod);
			startDate = newPeriod.getStartDate();
			endDate = newPeriod.getEndDate();
			return true;
		}

		//只要開始時間比目前的結束時間晚就行
		//如果日期相等也可以接受，所以用 not before
		if (!newPeriod.getStartDate().before(endDate)) {
			list.add(newPeriod);
			endDate = newPeriod.getEndDate();
			return true;
		}

		return false;
	}

	public List<HasPeriod> getList() {
		return list;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}
}
