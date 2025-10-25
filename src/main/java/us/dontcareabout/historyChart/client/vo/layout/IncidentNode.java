package us.dontcareabout.historyChart.client.vo.layout;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import us.dontcareabout.historyChart.client.vo.HasPeriod;
import us.dontcareabout.historyChart.client.vo.Incident;

/**
 * 比 {@link Incident} 多記錄其下的 children list。
 */
public class IncidentNode implements HasPeriod {
	public final Incident instance;

	private List<IncidentNode> children = Lists.newArrayList();

	public IncidentNode(Incident i) {
		this.instance = i;
	}

	/**
	 * @return child 的時間區間是否在自己的時間區間內。
	 * 	但無論回傳值為何，都會成功加入 children 當中
	 */
	public boolean addChild(IncidentNode child) {
		//預防性擋一下，懶得噴 error
		if (!child.instance.getParent().equals(instance.getName())) { return false; }

		children.add(child);

		//Incident 確保開始時間一定不大於結束時間
		return !child.getStartDate().before(getStartDate())
			&& !child.getEndDate().after(getEndDate());
	}

	public List<IncidentNode> getChildren() {
		return children;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	@Override
	public Date getStartDate() {
		return instance.getStartDate();
	}

	@Override
	public Date getEndDate() {
		return instance.getEndDate();
	}
}
