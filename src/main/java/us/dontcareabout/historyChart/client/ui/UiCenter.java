package us.dontcareabout.historyChart.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.historyChart.client.ui.event.ChangeArtifactEvent;
import us.dontcareabout.historyChart.client.ui.event.ChangeArtifactEvent.ChangeArtifactHandler;
import us.dontcareabout.historyChart.client.vo.Artifact;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static Artifact artifact;

	public static void changeArtifact(Artifact a) {
		artifact = a;
		eventBus.fireEvent(new ChangeArtifactEvent());
	}

	public static HandlerRegistration addChangeArtifact(ChangeArtifactHandler handler) {
		return eventBus.addHandler(ChangeArtifactEvent.TYPE, handler);
	}
}
