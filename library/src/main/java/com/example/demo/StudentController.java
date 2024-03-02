package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

	@Autowired
	private NamedParameterJdbcTemplate a;

	@PostMapping("/student")
	public String insert(@RequestBody Student student) {
		String sql = "INSERT INTO student(id,name) value ( :studentId, :studentName)";

		Map<String, Object> map = new HashMap<>();
		map.put("studentId", student.getId());
		map.put("studentName", student.getName());
		a.update(sql, map);

		return "執行 INSERT sql";
	}
	
	@DeleteMapping("/students/{studentId}")
	public String delete(@PathVariable Integer studentId) {
		String sql="DELETE FROM student where id = :studentId";
		Map<String,Object> map=new HashMap<>();
		map.put("studentId", studentId);
		a.update(sql,map);
		return "執行 DELETE sql";
	}
}
