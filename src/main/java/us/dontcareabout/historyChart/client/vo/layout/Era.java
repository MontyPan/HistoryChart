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
		decidePeriod(list);
	}

	public Era(IncidentNode parent) {
		this.parent = parent;
		build(parent.getChildren());

		//自己也得放下去一起找除起訖時間
		//這也代表：child 的時間區間大過 parent 也沒關係
		List<IncidentNode> list = Lists.newArrayList();
		list.addAll(parent.getChildren());
		list.add(parent);
		decidePeriod(list);
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

	private void decidePeriod(List<IncidentNode> list) {
		List<Date> period = DateUtil.getFirstLast(list);
		start = period.get(0);
		end = period.get(1);
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
