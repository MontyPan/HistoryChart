package us.dontcareabout.historyChart.client.ui.component;

import com.google.gwt.resources.client.ImageResource;

import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

//Refactory GF
public 	class ImgBlock extends LayerSprite {
	private int margin = 0;
	private final LImageSprite image;

	public ImgBlock(ImageResource ir) {
		image = new LImageSprite(ir);
		add(image);
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	@Override
	protected void adjustMember() {
		image.setWidth(getWidth() - margin * 2);
		image.setHeight(getHeight() - margin * 2);
		image.setLX(margin);
		image.setLY(margin);
	}
}