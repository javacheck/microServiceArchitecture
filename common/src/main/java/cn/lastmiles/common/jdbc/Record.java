package cn.lastmiles.common.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class Record implements Serializable {
	private static final long serialVersionUID = -195998352857021633L;
	private Map<String, Object> row;

	public Object get(String key) {
		return row.get(key.toUpperCase());
	}

	public Integer getInteger(String key) {
		return Integer.valueOf(get(key).toString());
	}

	public Long getLong(String key) {
		return Long.valueOf(get(key).toString());
	}

	public String getString(String key) {
		return (String) get(key);
	}

	public Date getDate(String key) {
		return (Date) get(key);
	}

	private Record(Map<String, Object> row) {
		this.row = row;
	}

	public static Record getRecord(Map<String, Object> map) {
		return new Record(map);
	}

	public static List<Record> getRecordList(List<Map<String, Object>> list) {
		List<Record> result = new ArrayList<Record>();
		for (Map<String, Object> map : list) {
			result.add(new Record(map));
		}
		return result;
	}
}
