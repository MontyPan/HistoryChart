package us.dontcareabout.historyChart.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.historyChart.client.data.event.GroupReadyEvent.GroupReadyHandler;

public class GroupReadyEvent extends GwtEvent<GroupReadyHandler> {
	public static final Type<GroupReadyHandler> TYPE = new Type<GroupReadyHandler>();

	@Override
	public Type<GroupReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GroupReadyHandler handler) {
		handler.onGroupReady(this);
	}

	public interface GroupReadyHandler extends EventHandler{
		public void onGroupReady(GroupReadyEvent event);
	}
}
