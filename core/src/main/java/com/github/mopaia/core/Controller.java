package com.github.mopaia.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> get() {
        return jdbcTemplate.queryForList("select * from test", Collections.emptyMap());
    }
}
