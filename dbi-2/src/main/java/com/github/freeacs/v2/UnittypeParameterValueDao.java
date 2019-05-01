package com.github.freeacs.v2;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class UnittypeParameterValueDao {

    private final JdbcTemplate jdbcTemplate;

    public UnittypeParameterValueDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // TODO
}
