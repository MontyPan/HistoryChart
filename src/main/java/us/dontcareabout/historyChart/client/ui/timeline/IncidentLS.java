package us.dontcareabout.historyChart.client.ui.timeline;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.historyChart.client.util.DateUtil;
import us.dontcareabout.historyChart.client.vo.layout.IncidentNode;

public 	class IncidentLS extends TextLS {
	public IncidentLS(IncidentNode in) {
		super(in.instance.getName());

		setBg(new RGB(in.instance.getColor()));
		resize(
			DateUtil.daysBetween(in.getStartDate(), in.getEndDate()) * Argument.dayUnit,
			Argument.incidentHeight
		);
	}
}