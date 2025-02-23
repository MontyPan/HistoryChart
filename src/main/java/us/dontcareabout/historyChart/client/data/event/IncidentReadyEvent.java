package us.dontcareabout.historyChart.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.historyChart.client.data.event.IncidentReadyEvent.IncidentReadyHandler;

public class IncidentReadyEvent extends GwtEvent<IncidentReadyHandler> {
	public static final Type<IncidentReadyHandler> TYPE = new Type<IncidentReadyHandler>();

	@Override
	public Type<IncidentReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(IncidentReadyHandler handler) {
		handler.onRecordReady(this);
	}

	public interface IncidentReadyHandler extends EventHandler{
		public void onRecordReady(IncidentReadyEvent event);
	}
}
