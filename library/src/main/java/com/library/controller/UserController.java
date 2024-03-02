package com.library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;


@RestController
public class UserController {

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Autowired
	private NamedParameterJdbcTemplate a;

	@PostMapping("/user2/register")
	public String insert(@RequestBody User user) {
		String sql = "INSERT INTO user(user_id,phone_number,password,user_name,registration_time,last_login_time) value"
				+ " ( :user_id,:phone_number,:password,:user_name,:registration_time,:last_login_time)";

		Map<String, Object> map = new HashMap<>();
		map.put("password", encryptPassword(user.getPassword()));
		//pk_id
		map.put("user_id", UUID.randomUUID().toString().replace("-", ""));
		map.put("phone_number", user.getPhoneNumber());
		map.put("user_name", user.getUserName());
		map.put("registration_time", user.getRegistrationTime());
		map.put("last_login_time", user.getLastLoginTime());

		a.update(sql, map);

		return "執行 INSERT sql";
	}

	/**
	 * 登入
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/user2/login")
	public String login(@RequestBody User user) {
		String sql = "SELECT * FROM user WHERE user_id = :userId";

		Map<String, Object> map = new HashMap<>();
		map.put("userId", user.getUserId());

		List<Map<String, Object>> mapList = a.queryForList(sql, map);

		if (mapList.size() > 0) {

			String ps = MapUtils.getString(mapList.get(0), "password");
			// 比較密碼是否一樣
			if (ps.equals(encryptPassword(user.getPassword()))) {

			}
		}
		return "執行 INSERT sql";
	}

	/**
	 * 處理密碼
	 * 
	 * @param pw
	 */
	public String encryptPassword(String pw) {
		// 对密码进行延迟哈希
		String hashedPassword = pw;//passwordEncoder.encode(pw);
		return hashedPassword;
	}

//	@DeleteMapping("/students/{studentId}")
//	public String delete(@PathVariable Integer studentId) {
//		String sql = "DELETE FROM student where id = :studentId";
//		Map<String, Object> map = new HashMap<>();
//		map.put("studentId", studentId);
//		a.update(sql, map);
//		return "執行 DELETE sql";
//	}
}
