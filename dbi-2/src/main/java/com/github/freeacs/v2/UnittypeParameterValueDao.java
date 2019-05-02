package com.github.freeacs.v2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class UnittypeParameterValueDao {

    private static final String INSERT_SQL = "insert into unit_type_param(name, flags, unit_type_id) values(?, ?, ?)";
    private static final String UPDATE_SQL = "update unit_type_param set name = ?, flags = ?, unit_type_id = ? where unit_type_param_id = ?";
    private static final String READ_ALL_SQL = "select unit_type_param_id, name, flags, unit_type_id from unit_type_param";
    private static final String DELETE_ALL_SQL = "delete from unit_type_param where unit_type_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public UnittypeParameterValueDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void delete(Long unittypeId) {
        if (unittypeId == null) {
            throw new IllegalArgumentException("Cannot delete unittype parameters with no unittype id");
        }
        jdbcTemplate.update(DELETE_ALL_SQL, unittypeId);
    }

    public List<UnittypeParameterValue> readAll() {
        return jdbcTemplate.query(READ_ALL_SQL, getRowMapper());
    }

    private void update(UnittypeParameterValue unittypeParameter) {
        jdbcTemplate.update(getPreparedStatementCreator(unittypeParameter, UPDATE_SQL));
    }

    private void add(UnittypeParameterValue unittypeParameter) {
        jdbcTemplate.update(getPreparedStatementCreator(unittypeParameter, INSERT_SQL));
    }

    private PreparedStatementCreator getPreparedStatementCreator(UnittypeParameterValue unittypeParameterValue, String sql) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, unittypeParameterValue.getType());
            ps.setString(2, unittypeParameterValue.getValue());
            ps.setInt(3, unittypeParameterValue.getPriority());
            ps.setLong(4, unittypeParameterValue.getUnittypeParameterId());
            return ps;
        };
    }

    private RowMapper<UnittypeParameterValue> getRowMapper() {
        return (resultSet, i) -> UnittypeParameterValue.builder()
                .type(resultSet.getString(1))
                .value(resultSet.getString(2))
                .priority(resultSet.getInt(3))
                .unittypeParameterId(resultSet.getLong(4))
                .build();
    }
}

