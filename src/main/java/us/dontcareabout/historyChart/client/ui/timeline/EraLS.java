package us.dontcareabout.historyChart.client.ui.timeline;

import java.util.List;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.util.ColorUtil;
import us.dontcareabout.historyChart.client.util.DateUtil;
import us.dontcareabout.historyChart.client.vo.HasPeriod;
import us.dontcareabout.historyChart.client.vo.layout.Era;
import us.dontcareabout.historyChart.client.vo.layout.IncidentNode;
import us.dontcareabout.historyChart.client.vo.layout.PeriodSafeList;

/*
TODO layout 優化
目前下圖中的 WTF 不會跟左邊的 EraLS 以及下方的 foo 在同一個 track
而是另外成一個 track
+------------------------------------------+
| +-------------------+                    |
| |      +-----+      |       +-----+      |
| |      |     |      |       | WTF |      |
| |      +-----+      |       +-----+      |
| |  +-----+ +-----+  |  +-----+ +-----+   |
| |  |     | |     |  |  | foo | | foo |   |
| |  +-----+ +-----+  |  +-----+ +-----+   |
| +-------------------+                    |
+------------------------------------------+
 */
public 	class EraLS extends LayerSprite {
	private final Era era;
	private final RGB gnColor;

	public EraLS(Era era, RGB gnColor) {
		this.era = era;
		this.gnColor = gnColor;

		double ly = 0;
		List<PeriodSafeList> rowList = era.getRowList();

		//因為 Era 是從 index 小的 row 開始試著塞，所以這邊倒過來排
		for (int i = rowList.size() - 1; i >= 0; i--) {
			ly = ly + process(rowList.get(i), ly);
		}

		double width = Argument.dayUnit * DateUtil.daysBetween(era.getStartDate(), era.getEndDate());

		if (era.parent != null) {
			RGB color = era.parent.instance.getColor().isEmpty() ? gnColor : new RGB(era.parent.instance.getColor());
			setBgColor(color);
			setBgStrokeColor(ColorUtil.blackOrWhite(color));

			TextLS name = new TextLS(era.parent.instance.getName());
			name.setTopMargin(2);
			name.setBg(color);
			name.setLX(0);
			name.setLY(ly + Argument.padding);
			name.resize(width, Argument.eraNameHeight);
			add(name);
			ly = ly + Argument.eraNameHeight;
		}

		resize(width, ly + Argument.padding);
	}

	private double process(PeriodSafeList row, double ly) {
		double result = 0;

		for (HasPeriod p : row.getList()) {
			LayerSprite newLS;

			if (p instanceof Era) {
				Era childEra = (Era) p;
				newLS = new EraLS(childEra, gnColor);
			} else {
				IncidentNode childIN = (IncidentNode) p;
				newLS = new IncidentLS(childIN, gnColor);
			}

			newLS.setLX(Argument.dayUnit * DateUtil.daysBetween(era.getStartDate(), p.getStartDate()));
			newLS.setLY(ly + Argument.padding);
			add(newLS);

			if (newLS.getHeight() > result) {
				result = newLS.getHeight();
			}
		}

		return Argument.padding + result;
	}
}
