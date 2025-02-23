package us.dontcareabout.historyChart.client.data;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gst.client.data.ApiKey;
import us.dontcareabout.gst.client.data.SheetIdDao;
import us.dontcareabout.gwt.client.google.sheet.Sheet;
import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.SheetDto.Callback;
import us.dontcareabout.historyChart.client.data.event.IncidentReadyEvent;
import us.dontcareabout.historyChart.client.data.event.IncidentReadyEvent.IncidentReadyHandler;
import us.dontcareabout.historyChart.client.vo.Incident;
import us.dontcareabout.historyChart.client.vo.Incident.IncidentValidator;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static void loadError() {
		//TODO
	}

	public static List<Incident> incidentList;

	public static void wantIncident() {
		new SheetDto<Incident>().key(ApiKey.jsValue())
				.sheetId(SheetIdDao.priorityValue()).tabName("事件")
				.validator(new IncidentValidator())
				.fetch(
			new Callback<Incident>() {
				@Override
				public void onSuccess(Sheet<Incident> gs) {
					incidentList = gs.getRows();
					eventBus.fireEvent(new IncidentReadyEvent());
				}

				@Override
				public void onError(Throwable exception) {
					loadError();
				}
			}
		);
	}

	public static HandlerRegistration addIncidentReady(IncidentReadyHandler handler) {
		return eventBus.addHandler(IncidentReadyEvent.TYPE, handler);
	}
}