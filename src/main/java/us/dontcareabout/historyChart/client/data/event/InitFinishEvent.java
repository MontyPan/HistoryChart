package us.dontcareabout.historyChart.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.historyChart.client.data.event.InitFinishEvent.InitFinishHandler;

public class InitFinishEvent extends GwtEvent<InitFinishHandler> {
	public static final Type<InitFinishHandler> TYPE = new Type<InitFinishHandler>();

	@Override
	public Type<InitFinishHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InitFinishHandler handler) {
		handler.onInitFinish(this);
	}

	public interface InitFinishHandler extends EventHandler{
		public void onInitFinish(InitFinishEvent event);
	}
}
