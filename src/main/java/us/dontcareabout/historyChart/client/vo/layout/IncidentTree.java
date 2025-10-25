package us.dontcareabout.historyChart.client.vo.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import us.dontcareabout.historyChart.client.util.DateUtil;
import us.dontcareabout.historyChart.client.vo.HasPeriod;
import us.dontcareabout.historyChart.client.vo.Incident;

/**
 * 將一組 {@link Incident} 轉成以 {@link IncidentNode} 組成的 tree 結構。
 */
public class IncidentTree implements HasPeriod {
	private final Date startDate;
	private final Date endDate;

	private HashMap<String, IncidentNode> nameMap = new HashMap<>();
	private List<Incident> nameConflictList = Lists.newArrayList();
	private List<IncidentNode> rootList = Lists.newArrayList();
	private List<IncidentNode> errorRootList = Lists.newArrayList();

	public IncidentTree(List<Incident> data) {
		List<Date> firstLast = DateUtil.getFirstLast(data);
		startDate = firstLast.get(0);
		endDate = firstLast.get(1);

		//先把 nameMap 建起來
		data.stream().forEach(i -> {
			if (nameMap.get(i.getName()) == null) {
				nameMap.put(i.getName(), new IncidentNode(i));
			} else {
				nameConflictList.add(i);
			}
		});

		data.stream().forEach(i -> {
			String parent = i.getParent();
			IncidentNode in = nameMap.get(i.getName());

			if (parent.isEmpty()) {	//不會是 null 可以放心
				rootList.add(in);
			} else {	//掛在 parent node 底下
				IncidentNode parentNode = nameMap.get(parent);
				if (parentNode == null) {	//parent 不存在
					errorRootList.add(in);
					rootList.add(in);	//改成 root
				} else {
					//TODO 收集 child 時間區間超出 parent 的 node
					parentNode.addChild(in);
				}
			}
		});
	}

	public List<IncidentNode> getRootList() {
		return rootList;
	}

	public List<IncidentNode> getErrorRootList() {
		return errorRootList;
	}

	public List<Incident> getNameConflictList() {
		return nameConflictList;
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
