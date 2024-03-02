package com.example.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.security.crypto.password.PasswordEncoder;
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

	@PostMapping("/user/register")
	public Map<String, Object> insert(@RequestBody User user) {
		String sql = "INSERT INTO user(user_id,phone_number,password,user_name,registration_time) value"
				+ " ( :user_id,:phone_number,:password,:user_name,:registration_time)";

		Map<String, Object> map = new HashMap<>();
		map.put("password", encryptPassword(user.getPassword()));

		map.put("user_id", UUID.randomUUID().toString().replace("-", ""));
		map.put("phone_number", user.getPhoneNumber());
		map.put("user_name", user.getUserName());
		map.put("registration_time", new Date());

		a.update(sql, map);
		map = new HashMap<>();
		map.put("status", "Y");
		return map;
	}

	/**
	 * 登入
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/user/login")
	public Map<String, Object> login(@RequestBody User user) {
		String sql = "SELECT * FROM user WHERE phone_number = :phone_number";
		String sql2 = "UPDATE user SET last_login_time= :last_login_time WHERE phone_number = :phone_number";
		Map<String, Object> map = new HashMap<>();
		map.put("phone_number", user.getPhoneNumber());

		List<Map<String, Object>> mapList = a.queryForList(sql, map);
		Map<String, Object> mapReturn = new HashMap<>();
		mapReturn.put("status", "N");

		if (mapList.size() > 0) {
			String ps = MapUtils.getString(mapList.get(0), "password");
			// 比較密碼是否一樣
			if (ps.equals(encryptPassword(user.getPassword()))) {
				map.put("last_login_time", new Date());
				a.update(sql2, map);
				mapReturn.put("status", "Y");
				mapReturn.put("user_id", MapUtils.getString(mapList.get(0), "user_id"));
			}
		}

		return mapReturn;
	}

	/**
	 * 處理密碼
	 * 
	 * @param pw
	 */
	public String encryptPassword(String pw) {
		// 对密码进行延迟哈希
		String hashedPassword = pw;// passwordEncoder.encode(pw);
		return hashedPassword;
	}

}
