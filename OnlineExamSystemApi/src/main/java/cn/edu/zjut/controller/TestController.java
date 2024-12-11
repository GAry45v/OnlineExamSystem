package cn.edu.zjut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public String testDatabaseConnection() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM some_table", Integer.class);
        return "Total rows: " + count;
    }
}
