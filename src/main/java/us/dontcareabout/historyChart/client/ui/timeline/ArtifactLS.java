package us.dontcareabout.historyChart.client.ui.timeline;

import static us.dontcareabout.historyChart.client.ui.timeline.Argument.padding;
import static us.dontcareabout.historyChart.client.ui.timeline.Argument.toDay;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.util.DateWrapper;

import us.dontcareabout.gxt.client.draw.LCircleSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.historyChart.client.ui.UiCenter;
import us.dontcareabout.historyChart.client.vo.Artifact;
import us.dontcareabout.historyChart.client.vo.HasPeriod;
import us.dontcareabout.historyChart.client.vo.layout.PeriodSafeList;

public class ArtifactLS extends LayerSprite {
	private static final int size = 15;

	public final Date start;
	public final Date end;

	private List<PeriodSafeList> rowList = Lists.newArrayList();

	public ArtifactLS(List<Artifact> list) {
		//目前不正式處理 list 是空的狀態
		//也就是說，當沒有 artifact list 的時候，這個 component 根本不應該存在
		//而這個責任是由 caller 負責... XD
		if (list.isEmpty()) {
			start = null;
			end = null;
			return;
		}

		start = list.get(0).getDate();
		end = list.get(list.size() - 1).getDate();
		rowList.add(new PeriodSafeList());

		list.forEach(a -> process(a));

		int height = 0;

		for (int i = rowList.size() - 1; i >= 0; i--) {
			draw(rowList.get(i), height);
			height += (size + padding) * 2;
		}

		resize(Argument.toWidth(start, end),  height);
	}

	private void draw(PeriodSafeList periodSafeList, int height) {
		periodSafeList.getList().forEach(ps -> {
			ArtifactPeriod p = (ArtifactPeriod)ps;
			add(new Point(p.artifact, height));
		});
	}

	private void process(Artifact a) {
		ArtifactPeriod dp = new ArtifactPeriod(a);

		for (PeriodSafeList psl : rowList) {
			if (psl.accept(dp)) { return; }
		}

		//既有的 psl 都找不到，只能弄個新的
		PeriodSafeList psl = new PeriodSafeList();
		psl.accept(dp);
		rowList.add(psl);
	}

	//要多包一層 LayerSprite 才能有 addSprite*Handler()
	class Point extends LayerSprite {
		Point(Artifact a, int height) {
			LCircleSprite point = new LCircleSprite(size);
			point.setFill(RGB.RED);	//TODO 改成用 Artifact 定義的顏色（如果有）
			point.setLX(Argument.toWidth(start, a.getDate()));
			point.setLY(height);
			this.add(point);

			addSpriteSelectionHandler(e -> UiCenter.changeArtifact(a));
		}
	}

	class ArtifactPeriod implements HasPeriod {
		final Artifact artifact;
		final Date start;
		final Date end;

		ArtifactPeriod(Artifact artifact) {
			this.artifact = artifact;
			start = new DateWrapper(artifact.getDate()).addDays(toDay(-size)).asDate();
			end = new DateWrapper(artifact.getDate()).addDays(toDay(size)).asDate();
		}

		@Override
		public Date getStartDate() {
			return start;
		}

		@Override
		public Date getEndDate() {
			return end;
		}
	}
}
