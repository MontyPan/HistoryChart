package us.dontcareabout.historyChart.client.vo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class Artifact extends DateRow {
	protected Artifact() {}

	public String getName() {
		return stringField("名稱");
	}

	//會給結束時間表示不確定確切時間，但還是得給定一個時間，所以就取中點 XD
	public Date getDate() {
		return new Date((getStartDate().getTime() + getEndDate().getTime()) / 2);
	}

	public String getDescription() {
		return stringField("說明");
	}

	public List<String> getImgList() {
		return Arrays.asList(stringField("圖檔").split("\n"));
	}

	public List<String> getRefList() {
		return Arrays.asList(stringField("參考資料").split("\n"));
	}

	private Date getStartDate() {
		return compoundDate("開始");
	}

	private Date getEndDate() {
		Date result = compoundDate("結束");
		return result == null ? getStartDate() : result;
	}
}
