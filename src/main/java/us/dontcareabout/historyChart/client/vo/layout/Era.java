package us.dontcareabout.historyChart.client.vo.layout;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import us.dontcareabout.historyChart.client.util.DateUtil;
import us.dontcareabout.historyChart.client.vo.HasPeriod;

/**
 * 有 children 的 {@link IncidentNode} 會成為一個 {@link Era}。
 * {@link Era} 會將 children 放入多個 {@link PeriodSafeList}（稱為 rowList）中，
 * 以確保每個 child（可能是 {@link IncidentNode} 或 {@link Era}）的時間區間不會有衝突。
 */
public class Era implements HasPeriod {
	public final IncidentNode parent;

	private List<PeriodSafeList> rowList = Lists.newArrayList();
	private Date start;
	private Date end;

	public Era(List<IncidentNode> list) {
		parent = null;
		build(list);
		List<Date> period = DateUtil.getFirstLast(list);
		start = period.get(0);
		end = period.get(1);
	}

	public Era(IncidentNode parent) {
		this.parent = parent;
		build(parent.getChildren());

		/*
		 * 原本的作法是：child 的時間區間如果大過 parent，以 child 為準。
		 * 但後來發現還是得用 parent 的起訖時間。
		 * 考慮「Foo 建立 WTF 王國」的情況
		 * Foo（Incident）會是 WTF（Incident）的 child
		 * 而 Foo 的出生年勢必比 WTF 的建立時間早
		 * 也就是說，視覺上 Foo 會有一段是超出 WTF 的範圍才合理
		 */
		start = parent.getStartDate();
		end = parent.getEndDate();
	}

	public int getRowAmount() {
		return rowList.size();
	}

	public List<PeriodSafeList> getRowList() {
		return rowList;
	}

	@Override
	public Date getStartDate() {
		return start;
	}

	@Override
	public Date getEndDate() {
		return end;
	}

	private void build(List<IncidentNode> list) {
		rowList.add(new PeriodSafeList());

		for (IncidentNode in : list) {
			if (in.isLeaf()) {
				process(in);
			} else {
				Era my = new Era(in);
				process(my);
			}
		}
	}

	private void process(HasPeriod incident) {
		for (PeriodSafeList psl : rowList) {
			if (psl.accept(incident)) { return; }
		}

		//既有的 psl 都找不到，只能弄個新的
		PeriodSafeList psl = new PeriodSafeList();
		psl.accept(incident);
		rowList.add(psl);
	}
}
