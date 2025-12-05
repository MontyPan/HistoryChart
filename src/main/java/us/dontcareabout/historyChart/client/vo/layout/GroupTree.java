package us.dontcareabout.historyChart.client.vo.layout;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import us.dontcareabout.historyChart.client.vo.Group;

public class GroupTree {
	private HashMap<String, GroupNode> nameMap = new HashMap<>();
	private List<Group> nameConflictList = Lists.newArrayList();
	private List<GroupNode> rootList = Lists.newArrayList();
	private List<GroupNode> errorRootList = Lists.newArrayList();
	private int maxLevel;

	public GroupTree(List<Group> data) {
		data.stream().forEach(i -> {
			if (nameMap.get(i.getName()) == null) {
				nameMap.put(i.getName(), new GroupNode(i));
			} else {
				nameConflictList.add(i);
			}
		});

		data.stream().forEach(i -> {
			String parent = i.getParent();
			GroupNode g = nameMap.get(i.getName());

			if (parent.isEmpty()) {
				rootList.add(g);
			} else {
				GroupNode parentNode = nameMap.get(parent);
				if (parentNode == null) {	//parent 不存在
					errorRootList.add(g);
					rootList.add(g);	//改成 root
				} else {
					parentNode.addGroupChild(g);
				}
			}
		});

		maxLevel = 0;

		for (GroupNode gn : rootList) {
			int level = traverseLevel(gn, 0);
			if (level > maxLevel) { maxLevel = level; }
		}
	}

	public void addIncident(IncidentNode in) {
		String parent = in.instance.getParent();
		GroupNode gn = parent.isEmpty() || nameMap.get(parent) == null ?
			GroupNode.DEFAULT : nameMap.get(parent);
		gn.addIncidentChild(in);
	}

	public List<GroupNode> getRootList() {
		return rootList;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	private int traverseLevel(GroupNode gn, int parentLevel) {
		int result = parentLevel + 1;

		if (gn.isLeaf()) { return result; }

		for (GroupNode child : gn.getGroupChildren()) {
			int level = traverseLevel(child, parentLevel + 1);
			if (level > result) { result = level; }
		}

		return result;
	}
}
