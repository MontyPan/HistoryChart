package us.dontcareabout.historyChart.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.gst.client.GSTEP;
import us.dontcareabout.historyChart.client.data.DataCenter;

public class HistoryChartEP extends GSTEP {
	public HistoryChartEP() {
		super("HistoryChart-Key", "1eeFbwHueLDK7E6Bohr040D6aNY51Yi99wHIyvJGc4pM");
	}

	@Override
	protected String version() { return "0.0.1"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }

	@Override
	protected void featureFail() {
		Window.alert("這個瀏覽器我不尬意，不給用..... \\囧/");
	}

	@Override
	protected void start() {
	}
}
