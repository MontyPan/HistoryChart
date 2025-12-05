package us.dontcareabout.historyChart.client.vo.layout;

import java.util.List;

import com.google.common.collect.Lists;
import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.historyChart.client.vo.Group;

/**
 * {@link Group} 的進化版。
 * 除了記錄有哪些 {@link Group} child，
 * 也紀錄有哪些 {@link IncidentNode} child。
 */
public class GroupNode {
	/**
	 * 預設的 node，
	 * 沒有（或找不到）parent 的 {@link IncidentNode} 會歸在預設 node 底下
	 */
	public static final GroupNode DEFAULT = new GroupNode(null);

	//因為 instance 可以是 null，為了避免 NPE 所以弄成 private 做 delegate
	private final Group instance;

	private List<GroupNode> groupChildren = Lists.newArrayList();
	private List<IncidentNode> incidentChildren = Lists.newArrayList();

	public GroupNode(Group i) {
		this.instance = i;
	}

	public void addGroupChild(GroupNode child) {
		//預防性擋一下，懶得噴 error
		if (!child.instance.getParent().equals(instance.getName())) { return; }

		groupChildren.add(child);
	}

	public void addIncidentChild(IncidentNode in) {
		incidentChildren.add(in);
	}

	public List<GroupNode> getGroupChildren() {
		return groupChildren;
	}

	public List<IncidentNode> getIncidentChildren() {
		return incidentChildren;
	}

	public boolean isLeaf() {
		return groupChildren.size() == 0;
	}

	public boolean isDefault() {
		return instance == null;
	}

	//==== delegate 區 ====//
	public String getName() {
		return isDefault() ? "" : instance.getName();
	}

	public RGB getColor() {
		return new RGB(isDefault() ? "" : instance.getColor());
	}

	public RGB getIncidentColor() {
		return new RGB(isDefault() ? "" : instance.getIncidentColor());
	}
}
