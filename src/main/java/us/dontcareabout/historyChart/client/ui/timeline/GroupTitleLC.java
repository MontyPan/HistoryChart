package us.dontcareabout.historyChart.client.ui.timeline;

import java.util.HashMap;

import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.Rotation;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;

import us.dontcareabout.gxt.client.util.ColorUtil;
import us.dontcareabout.historyChart.client.data.DataCenter;
import us.dontcareabout.historyChart.client.util.FontUtil;
import us.dontcareabout.historyChart.client.vo.Group;
import us.dontcareabout.historyChart.client.vo.layout.GroupNode;

//Group 的字串要轉九十度才符合 UI 需求
//但是 GF Layer 沒辦法處理 rotation，所以直接用 DrawComponent 上
/**
 * 需要 {@link TimelineLC} 的 {@link EraLS} 資訊來決定各個 {@link Group} 有多高。
 */
public class GroupTitleLC extends DrawComponent {
	private HashMap<GroupNode, EraLS> eraMap;

	public GroupTitleLC() {}

	public void refresh(HashMap<GroupNode, EraLS> map) {
		this.eraMap = map;

		clearSurface();

		for (GroupNode gn : DataCenter.groupTree.getRootList()) {
			draw(gn, 0);
		}
	}

	private double draw(GroupNode gn, int level) {
		EraLS eraLS = eraMap.get(gn);

		RectangleSprite bg = new RectangleSprite(
			(DataCenter.groupTree.getMaxLevel() - level) * Argument.groupWidth,
			eraLS.getHeight(),
			level * Argument.groupWidth,
			eraLS.getY()
		);
		bg.setFill(gn.getColor());
		addSprite(bg);

		TextSprite name = new TextSprite(gn.getName());
		name.setFontSize(FontUtil.suggestSize(Argument.groupNameWidth));
		name.setX(level * Argument.groupWidth + Argument.groupNameWidth);
		name.setY(eraLS.getY() + Argument.padding);
		name.setRotation(new Rotation(name.getX(), name.getY(), 90));
		name.setFill(ColorUtil.blackOrWhite(gn.getColor()));
		addSprite(name);

		if (gn.isLeaf()) { return bg.getHeight(); }

		double h = bg.getHeight();

		for (GroupNode child : gn.getGroupChildren()) {
			h = h + draw(child, level + 1);
		}

		bg.setHeight(h);
		return bg.getHeight();
	}
}
