package com.example.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;

@RestController
public class LibraryController {

	@Autowired
	private NamedParameterJdbcTemplate a;

	@PostMapping("/book/query")
	public List<Map<String, Object>> query(@RequestBody Map<String, Object> queryMap) {
		String sql = "SELECT * FROM book a inner join inventory b on a.isbn=b.isbn";

		Map<String, Object> map = new HashMap<>();

		List<Map<String, Object>> mapList = a.queryForList(sql, map);
		return mapList;
	}

	@PostMapping("/book/borrow")
	public Map<String, Object> borrow(@RequestBody Map<String, Object> map) {
		String sql = "SELECT * FROM inventory where inventory_id=:inventory_id";

		List<Map<String, Object>> mapList = a.queryForList(sql, map);
		if (mapList.size() > 0) {
			// 判斷是否可借閱
			if ("N".equals(MapUtils.getString(mapList.get(0), "status"))) {
				String inventoryId = MapUtils.getString(map, "inventory_id");
				String userId = MapUtils.getString(map, "user_id");
				borrowBook(inventoryId, userId);
			}
		}
		Map<String, Object> map2 = new HashMap<>();
		map2.put("status", "Y");
		return map2;
	}

	@PostMapping("/book/return")
	public Map<String, Object> borrowRreturn(@RequestBody Map<String, Object> map) {

		String inventoryId = MapUtils.getString(map, "inventory_id");
		String userId = MapUtils.getString(map, "user_id");
		returnBook(inventoryId, userId);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("status", "Y");
		return map2;
	}

	@PostMapping("/record/query")
	public List<Map<String, Object>> queryRecord(@RequestBody User user) {
		String sql = "SELECT * FROM borrowing_record a inner join inventory b on a.inventory_id=b.inventory_id inner join book c on c.isbn=b.isbn "
				+ "where user_id=:user_id";
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", user.getUserId());
		List<Map<String, Object>> mapList = a.queryForList(sql, map);
		return mapList;
	}

	/**
	 * 借書
	 * 
	 * @param inventoryId
	 * @param userId
	 */
	@Transactional
	public void borrowBook(String inventoryId, String userId) {
		// 更新已被借閱
		updateInventoryStatus(inventoryId, "Y");
		addRecord(inventoryId, userId);
	}

	/**
	 * 還書
	 * 
	 * @param inventoryId
	 * @param userId
	 */
	@Transactional
	public void returnBook(String inventoryId, String userId) {
		// 更新已可借閱
		updateInventoryStatus(inventoryId, "N");
		updateRecord(inventoryId, userId);
	}

	/**
	 * 更新庫存狀態
	 * 
	 * @param inventoryId
	 */
	private void updateInventoryStatus(String inventoryId, String status) {
		String sql = "UPDATE inventory SET status=:status WHERE inventory_id = :inventory_id";
		Map<String, Object> map = new HashMap<>();
		map.put("inventory_id", inventoryId);
		map.put("status", status);
		a.update(sql, map);
	}

	/**
	 * 借書紀錄
	 * 
	 * @param inventoryId
	 * @param userId
	 */
	private void addRecord(String inventoryId, String userId) {
		String sql = "INSERT INTO borrowing_record(pk_id,user_id,inventory_id,borrowing_time) value"
				+ " ( :pk_id,:user_id,:inventory_id,:borrowing_time)";
		Map<String, Object> map = new HashMap<>();
		map.put("pk_id", UUID.randomUUID().toString().replace("-", ""));
		map.put("user_id", userId);
		map.put("inventory_id", inventoryId);
		map.put("borrowing_time", new Date());
		a.update(sql, map);
	}

	/**
	 * 還書紀錄
	 * 
	 * @param pkId
	 */
	private void updateRecord(String inventoryId, String userId) {
		String sql = "UPDATE borrowing_record SET return_time=:return_time WHERE inventory_id=:inventory_id and user_id=:user_id and return_time is null";
		Map<String, Object> map = new HashMap<>();
		map.put("inventory_id", inventoryId);
		map.put("user_id", userId);
		map.put("return_time", new Date());
		a.update(sql, map);
	}
}
