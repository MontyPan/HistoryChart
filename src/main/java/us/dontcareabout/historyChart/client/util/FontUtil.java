package us.dontcareabout.historyChart.client.util;

import java.util.HashMap;

public class FontUtil {
	private static HashMap<Integer, Integer> heightMap = new HashMap<>();
	static {
		heightMap.put(22, 16);
	}

	public static int suggestSize(int height) {
		Integer result = heightMap.get(height);
		return result == null ? 10 : result;
	}
}
