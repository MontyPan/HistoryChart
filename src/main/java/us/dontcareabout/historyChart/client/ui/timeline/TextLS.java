package us.dontcareabout.historyChart.client.ui.timeline;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;

import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.util.ColorUtil;
import us.dontcareabout.historyChart.client.util.FontUtil;

/**
 * 提供有 {@link TextSprite} 的 {@link LayerSprite}。
 * <p>
 * 特性如下：
 * <ul>
 * 	<li>文字靠左排列</li>
 * 	<li>以高度調整文字大小</li>
 * 	<li>{@link #setBg(RGB)} 會改變文字與外框顏色</li>
 * </ul>
 */
public class TextLS extends LayerSprite {
	private LTextSprite textSprite = new LTextSprite();
	private int topMargin = 5;
	private int leftMargin = 5;

	public TextLS(String text) {
		textSprite.setText(text);
		add(textSprite);
	}

	public void setBg(RGB color) {
		setBgColor(color);
		Color bOrW = ColorUtil.blackOrWhite(color);
		setBgStrokeColor(bOrW);
		textSprite.setFill(bOrW);
	}

	public void setTopMargin(int margin) {
		this.topMargin = margin;
		adjustMember();
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
		adjustMember();
	}

	@Override
	protected void adjustMember() {
		textSprite.setLX(leftMargin);
		textSprite.setLY(topMargin);
		textSprite.setFontSize(
			FontUtil.suggestSize((int)(getHeight() - topMargin * 2))
		);
	}
}
