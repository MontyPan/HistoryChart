package us.dontcareabout.historyChart.client.ui;

import static us.dontcareabout.historyChart.client.ui.timeline.Argument.padding;
import static us.dontcareabout.historyChart.client.util.DateUtil.toY_bc_ad;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.PreciseRectangle;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import us.dontcareabout.gwt.client.util.ImageUtil;
import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.historyChart.client.ui.component.ImgBlock;
import us.dontcareabout.historyChart.client.util.Util;
import us.dontcareabout.historyChart.client.vo.Artifact;

//原本 / 目前是設計給 artifact 用的，所以 API 都是 artifact 的形狀
//但是看起來 incident 也需要用到類似的功能，所以先取名為 DetailPanel
//等 incident 開始用之後再看看會發生什麼事(?)
public class DetailPanel extends BorderLayoutContainer {
	private static final ArtifactTemplate template = GWT.create(ArtifactTemplate.class);
	private static final ArtifactResource resource = GWT.create(ArtifactResource.class);
	static {
		resource.css().ensureInjected();
	}

	private ImageLC imageLC = new ImageLC();
	private RefLC refLC = new RefLC();
	private HTML html = new HTML();

	public DetailPanel() {
		html.getElement().getStyle().setOverflow(Overflow.AUTO);

		VerticalLayoutContainer east = new VerticalLayoutContainer();
		east.add(html, new VerticalLayoutData(1, 1));
		east.add(refLC, new VerticalLayoutData(1, 30));
		BorderLayoutData eastBLD = new BorderLayoutData();

		eastBLD.setFloatable(true);
		eastBLD.setSplit(true);
		eastBLD.setSize(200);
		eastBLD.setMinSize(200);
		eastBLD.setCollapsed(true);
		setCenterWidget(imageLC);
		setEastWidget(east, eastBLD);
	}

	public void refresh(Artifact artifact) {
		imageLC.refresh(artifact.getImgList());
		refLC.refresh(artifact.getRefList());
		html.setHTML(
			template.announce(
				resource.css(),
				artifact.getName(),
				artifactDate(artifact),
				Arrays.asList(artifact.getDescription().split("\n"))
			)
		);
	}

	class ImageLC extends LayerSprite {
		LImageSprite img = new LImageSprite();
		TextButton prev = new TextButton("←");
		TextButton next = new TextButton("→");
		List<String> urlList;
		int nowIndex;

		ImageLC() {
			prev.addSpriteSelectionHandler(e -> changeIndex(nowIndex - 1));
			next.addSpriteSelectionHandler(e -> changeIndex(nowIndex + 1));
			add(img);
			add(prev);
			add(next);
		}

		public void refresh(List<String> list) {
			img.setHidden(list.isEmpty());

			if (list.isEmpty()) {
				prev.setHidden(true);
				next.setHidden(true);
				redraw();
				return;
			}

			urlList = list;
			changeIndex(0);
		}

		@Override
		protected void adjustMember() {
			img.setWidth(getWidth());
			img.setHeight(getHeight());
			prev.setLX(padding);
			prev.setLY(padding);
			prev.resize(30, 30);
			next.setLX(getWidth() - padding - 30);
			next.setLY(padding);
			next.resize(30, 30);
		}

		private void changeIndex(int index) {
			nowIndex = index;
			img.setResource(ImageUtil.toResource(urlList.get(index)));
			prev.setHidden(index == 0);
			next.setHidden(index == urlList.size() - 1);
			adjustMember();
			redraw();
		}
	}

	private static final int ICON_WIDTH = 30;

	class RefLC extends LayerContainer {
		RefLC() {}

		public void refresh(List<String> refList) {
			clear();

			List<String> link = Lists.newArrayList();
			List<String> text = Lists.newArrayList();
			refList.forEach(e -> {
				if (e.startsWith("https://")) {
					link.add(e);
				} else {
					text.add(e);
				}
			});

			int width = 0;

			for (String l : link) {
				ImgBlock ib = new ImgBlock(Util.linkIconIR(l));
				ib.setLX(width);
				ib.setLY(0);
				ib.resize(ICON_WIDTH, ICON_WIDTH);
				ib.addSpriteSelectionHandler(e -> Util.openUrl(l));
				addLayer(ib);
				width += ICON_WIDTH + padding;
			}

			List<RefTextLS> textLSList = Lists.newArrayList();

			for (String t : text) {
				RefTextLS ts = new RefTextLS(t);
				addLayer(ts);
				textLSList.add(ts);
			}

			redrawSurfaceForced();

			for (RefTextLS refText : textLSList) {
				refText.setLX(width);
				refText.setLY(0);
				refText.resize(refText.getBBox().getWidth() + padding * 2, ICON_WIDTH);
				width += refText.getWidth() + padding;
			}

			setPixelSize(width, getOffsetHeight());
		}
	}

	class RefTextLS extends LayerSprite {
		private LTextSprite ts = new LTextSprite();

		public RefTextLS(String text) {
			setBgColor(RGB.LIGHTGRAY);
			setBgRadius(5);
			ts.setFontSize(18);
			ts.setText(text);
			add(ts);
		}

		public PreciseRectangle getBBox() {
			return ts.getBBox();
		}

		@Override
		protected void adjustMember() {
			ts.setLX(padding);
			ts.setLY(padding);
		}
	}

	private static String artifactDate(Artifact a) {
		if (a.getStartDate().equals(a.getEndDate())) {
			return toY_bc_ad(a.getStartDate());
		}

		return toY_bc_ad(a.getStartDate()) + "～" + toY_bc_ad(a.getEndDate());
	}

	interface ArtifactStyle extends CssResource {
		String title();
		String normal();
	}

	interface ArtifactTemplate extends XTemplates {
		@XTemplate(source = "ArtifactDetail.html")
		SafeHtml announce(ArtifactStyle style, String name, String date, List<String> description);
	}

	interface ArtifactResource extends ClientBundle {
		@Source("ArtifactDetail.gss")
		ArtifactStyle css();
	}
}