package us.dontcareabout.historyChart.client.util;

import static us.dontcareabout.historyChart.client.ImageRS.I;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;

//注意：沒有地方可以去的 method 才放來這邊！ XD
public class Util {
	public static ImageResource linkIconIR(String link) {
		if (link.contains("wikipedia.org")) {
			return I.wikipedia();
		}

		if (link.contains("smarthistory.org")) {
			return I.smartHistory();
		}

		return I.boxArrowUpRight();
	}

	public static void openUrl(String url) {
		Window.open(url, "_blank", null);
	}
}
