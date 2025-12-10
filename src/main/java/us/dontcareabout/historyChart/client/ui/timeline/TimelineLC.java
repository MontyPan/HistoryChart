package us.dontcareabout.historyChart.client.ui.timeline;

import static us.dontcareabout.historyChart.client.ui.timeline.Argument.timePeriodAmount;
import static us.dontcareabout.historyChart.client.ui.timeline.Argument.timePeriodUnit;
import static us.dontcareabout.historyChart.client.ui.timeline.Argument.timeTextHeight;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.util.DateWrapper;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.historyChart.client.data.DataCenter;
import us.dontcareabout.historyChart.client.util.DateUtil;
import us.dontcareabout.historyChart.client.vo.layout.Era;
import us.dontcareabout.historyChart.client.vo.layout.GroupNode;

public class TimelineLC extends LayerContainer {
	private Date start = new Date();
	private Background bg = new Background(start, start);
	private HashMap<GroupNode, EraLS> eraLSMap = new HashMap<>();
	private double bgWidth;

	public TimelineLC() {
		addLayer(bg);
	}

	public void refresh() {
		clear();

		start = new DateWrapper(DataCenter.start).add(timePeriodUnit, -timePeriodAmount / 2).asDate();
		Date end = new DateWrapper(DataCenter.end).addYears(timePeriodAmount / 2).asDate();
		bgWidth = Argument.toWidth(start, end);

		bg = new Background(start, end);
		addLayer(bg);

		double height = timeTextHeight;
		height = convertGroupNode(GroupNode.DEFAULT, height);

		for (GroupNode gn : DataCenter.groupTree.getRootList()) {
			height = traverseGroupNode(gn, height);
		}

		setPixelSize(
			(int)bgWidth,
			(int)(height + timeTextHeight * 2)
		);
	}

	public HashMap<GroupNode, EraLS> getEraLSMap() {
		return eraLSMap;
	}


	@Override
	protected void adjustMember(int width, int height) {
		bg.resize(width, height);
	}

	/**
	 * 將 {@link GroupNode} 變成 {@link EraLS} 並加到畫面上。
	 * @return 產生的 {@link EraLS} 加入後，目前的高度值
	 */
	private double convertGroupNode(GroupNode gn, double height) {
		Era era = new Era(gn.getIncidentChildren());
		EraLS eraLS = new EraLS(era, new RGB(gn.getIncidentColor()));
		eraLS.setLX(Argument.toWidth(start, era.getStartDate()));
		eraLS.setLY(height);

		LayerSprite ls = new LayerSprite();
		ls.setBgColor(gn.isDefault() ? Color.NONE : gn.getColor());
		ls.setLX(0);
		ls.setLY(eraLS.getLY());
		ls.resize(bgWidth, eraLS.getHeight());

		//ls 要先加才會壓在下面
		//XXX 用 setLZIndex 的方式，ls 會整個不見... =.=?
		addLayer(ls);
		addLayer(eraLS);
		eraLSMap.put(gn, eraLS);

		return height + eraLS.getHeight();
	}

	private double traverseGroupNode(GroupNode gn, double height) {
		//先將自己的畫完、再畫小孩
		double h = convertGroupNode(gn, height);

		if (gn.isLeaf()) { return h; }

		for (GroupNode child : gn.getGroupChildren()) {
			h = traverseGroupNode(child, h);
		}

		return h;
	}

	/**
	 * 負責畫時間文字以及垂直線
	 */
	class Background extends LayerSprite {
		List<LRectangleSprite> lineList = Lists.newArrayList();
		List<TextLS> textList = Lists.newArrayList();

		Background(Date start, Date end) {
			int sy = (DateUtil.year(start) / timePeriodAmount - 1) * timePeriodAmount;
			int ey = (DateUtil.year(end) / timePeriodAmount + 1) * timePeriodAmount;

			for (int i = sy; i <= ey; i = i + timePeriodAmount) {
				Date date = new DateWrapper(i, 0, 1).asDate();
				LRectangleSprite line = new LRectangleSprite(1, 1);
				line.setFill(RGB.DARKGRAY);
				line.setLX(Argument.toWidth(start, date));
				line.setLY(0);
				lineList.add(line);
				add(line);

				TextLS year = new TextLS(DateUtil.toY(date));
				year.setTopMargin(2);
				year.setLX(Argument.toWidth(start, date) - 10);
				year.setLY(0);
				year.resize(80, timeTextHeight);
				textList.add(year);
				add(year);
			}
		}

		@Override
		protected void adjustMember() {
			double y = getHeight() - timeTextHeight;
			lineList.forEach(line -> {
				line.setHeight(y);
			});

			textList.forEach(text -> {
				text.setLY(y);
			});
		}
	}
}