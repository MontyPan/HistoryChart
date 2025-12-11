package us.dontcareabout.historyChart.client.ui.timeline;

import static us.dontcareabout.historyChart.client.ui.timeline.Argument.padding;

import java.util.Date;
import java.util.List;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LCircleSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.historyChart.client.ui.UiCenter;
import us.dontcareabout.historyChart.client.vo.Artifact;

public class ArtifactLS extends LayerSprite {
	private static final int size = 15;

	public final Date start;
	public final Date end;

	//TODO 目前沒有處理 Artifact 重疊的問題
	public ArtifactLS(List<Artifact> list) {
		start = list.get(0).getDate();
		end = list.get(list.size() - 1).getDate();

		list.forEach(a -> add(new Point(a)));
		resize(Argument.toWidth(start, end), (size + padding) * 2);
	}

	//要多包一層 LayerSprite 才能有 addSprite*Handler()
	class Point extends LayerSprite {
		Point(Artifact a) {
			LCircleSprite point = new LCircleSprite(size);
			point.setFill(RGB.RED);
			point.setLX(Argument.toWidth(start, a.getDate()));
			point.setLY(size + padding);
			this.add(point);

			addSpriteSelectionHandler(e -> UiCenter.changeArtifact(a));
		}
	}
}
