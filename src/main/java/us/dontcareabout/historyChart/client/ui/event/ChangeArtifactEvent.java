package us.dontcareabout.historyChart.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.historyChart.client.ui.event.ChangeArtifactEvent.ChangeArtifactHandler;

public class ChangeArtifactEvent extends GwtEvent<ChangeArtifactHandler> {
	public static final Type<ChangeArtifactHandler> TYPE = new Type<ChangeArtifactHandler>();

	@Override
	public Type<ChangeArtifactHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeArtifactHandler handler) {
		handler.onChangeArtifact(this);
	}

	public interface ChangeArtifactHandler extends EventHandler{
		public void onChangeArtifact(ChangeArtifactEvent event);
	}
}
