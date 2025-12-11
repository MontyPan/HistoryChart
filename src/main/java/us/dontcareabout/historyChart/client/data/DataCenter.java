package us.dontcareabout.historyChart.client.data;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gst.client.data.ApiKey;
import us.dontcareabout.gst.client.data.SheetIdDao;
import us.dontcareabout.gwt.client.google.sheet.Sheet;
import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.SheetDto.Callback;
import us.dontcareabout.gwt.client.util.TaskSet;
import us.dontcareabout.historyChart.client.data.event.ArtifactReadyEvent;
import us.dontcareabout.historyChart.client.data.event.ArtifactReadyEvent.ArtifactReadyHandler;
import us.dontcareabout.historyChart.client.data.event.GroupReadyEvent;
import us.dontcareabout.historyChart.client.data.event.GroupReadyEvent.GroupReadyHandler;
import us.dontcareabout.historyChart.client.data.event.IncidentReadyEvent;
import us.dontcareabout.historyChart.client.data.event.IncidentReadyEvent.IncidentReadyHandler;
import us.dontcareabout.historyChart.client.data.event.InitFinishEvent;
import us.dontcareabout.historyChart.client.data.event.InitFinishEvent.InitFinishHandler;
import us.dontcareabout.historyChart.client.vo.Artifact;
import us.dontcareabout.historyChart.client.vo.Group;
import us.dontcareabout.historyChart.client.vo.HasPeriod.StartDateComparator;
import us.dontcareabout.historyChart.client.vo.Incident;
import us.dontcareabout.historyChart.client.vo.Incident.IncidentValidator;
import us.dontcareabout.historyChart.client.vo.layout.GroupTree;
import us.dontcareabout.historyChart.client.vo.layout.IncidentNode;
import us.dontcareabout.historyChart.client.vo.layout.IncidentTree;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	//level 1 data
	public static List<Incident> incidentList;
	public static List<Group> groupList;
	public static List<Artifact> artifactList;

	//level 2 data
	public static Date start;
	public static Date end;
	public static GroupTree groupTree;
	public static IncidentTree incidentTree;

	public static void init() {
		TaskSet ts = new TaskSet();
		ts.addAsyncTask(
			() -> wantGroup(),
			addGroupReady(e -> ts.check())
		).addAsyncTask(
			() -> wantIncident(),
			addIncidentReady(e -> ts.check())
		).addAsyncTask(
			() -> wantArtifact(),
			addArtifactReady(e -> ts.check())
		).addFinalTask(() -> process())
		.start();
	}

	public static HandlerRegistration addInitFinish(InitFinishHandler handler) {
		return eventBus.addHandler(InitFinishEvent.TYPE, handler);
	}

	public static void wantArtifact() {
		new SheetDto<Artifact>().key(ApiKey.jsValue())
				.sheetId(SheetIdDao.priorityValue()).tabName("作品")
				.fetch(
			new Callback<Artifact>() {
				@Override
				public void onSuccess(Sheet<Artifact> gs) {
					artifactList = gs.getRows();
					artifactList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
					eventBus.fireEvent(new ArtifactReadyEvent());
				}
			}
		);
	}

	public static HandlerRegistration addArtifactReady(ArtifactReadyHandler handler) {
		return eventBus.addHandler(ArtifactReadyEvent.TYPE, handler);
	}

	public static void wantGroup() {
		new SheetDto<Group>().key(ApiKey.jsValue())
				.sheetId(SheetIdDao.priorityValue()).tabName("分區")
				.fetch(
			new Callback<Group>() {
				@Override
				public void onSuccess(Sheet<Group> gs) {
					groupList = gs.getRows();
					eventBus.fireEvent(new GroupReadyEvent());
				}

				@Override
				public void onError(Throwable exception) {
					loadError();
				}
			}
		);
	}

	public static HandlerRegistration addGroupReady(GroupReadyHandler handler) {
		return eventBus.addHandler(GroupReadyEvent.TYPE, handler);
	}

	public static void wantIncident() {
		new SheetDto<Incident>().key(ApiKey.jsValue())
				.sheetId(SheetIdDao.priorityValue()).tabName("事件")
				.validator(new IncidentValidator())
				.fetch(
			new Callback<Incident>() {
				@Override
				public void onSuccess(Sheet<Incident> gs) {
					incidentList = gs.getRows();
					incidentList.sort(new StartDateComparator<Incident>());
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

	private static void process() {
		groupTree = new GroupTree(groupList);
		incidentTree = new IncidentTree(incidentList);
		start = incidentTree.getStartDate();
		end = incidentTree.getEndDate();

		for (IncidentNode in : incidentTree.getRootList()) {
			groupTree.addIncident(in);
		}

		eventBus.fireEvent(new InitFinishEvent());
	}

	private static void loadError() {
		//TODO
	}
}