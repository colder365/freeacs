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

public class UnittypeParameterDao {

    private static final String INSERT_SQL = "insert into unit_type_param(name, flags, unit_type_id) values(?, ?, ?)";
    private static final String UPDATE_SQL = "update unit_type_param set name = ?, flags = ?, unit_type_id = ? where unit_type_param_id = ?";
    private static final String READ_ALL_SQL = "select unit_type_param_id, name, flags, unit_type_id from unit_type_param where unit_type_id = ?";
    private static final String DELETE_ALL_SQL = "delete from unit_type_param where unit_type_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public UnittypeParameterDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UnittypeParameter addOrChange(UnittypeParameter unittypeParameter) {
        if (unittypeParameter.getId() == null) {
            long newId = add(unittypeParameter);
            return unittypeParameter.withId(newId);
        }
        update(unittypeParameter);
        return unittypeParameter;
    }

    public void deleteAll(Long unittypeId) {
        if (unittypeId == null) {
            throw new IllegalArgumentException("Cannot delete unittype parameters with no unittype id");
        }
        jdbcTemplate.update(DELETE_ALL_SQL, unittypeId);
    }

    public List<UnittypeParameter> readAll(Long unittypeId) {
        if (unittypeId == null) {
            throw new IllegalArgumentException("Cannot read unittype parameters with no unittype id");
        }
        return jdbcTemplate.query(READ_ALL_SQL, new Object[]{unittypeId}, getRowMapper());
    }

    private void update(UnittypeParameter unittypeParameter) {
        jdbcTemplate.update(getPreparedStatementCreator(unittypeParameter, unittypeParameter.getId(), UPDATE_SQL));
    }

    private Long add(UnittypeParameter unittypeParameter) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getPreparedStatementCreator(unittypeParameter, null, INSERT_SQL), keyHolder);
        return (Long) keyHolder.getKey();
    }

    private PreparedStatementCreator getPreparedStatementCreator(UnittypeParameter unittypeParameter, Long id, String sql) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, unittypeParameter.getName());
            ps.setString(2, unittypeParameter.getFlag());
            ps.setLong(3, unittypeParameter.getUnittypeId());
            if (id != null) {
                ps.setLong(4, id);
            }
            return ps;
        };
    }

    private RowMapper<UnittypeParameter> getRowMapper() {
        return (resultSet, i) -> UnittypeParameter.builder()
                .id(resultSet.getLong(1))
                .name(resultSet.getString(2))
                .flag(resultSet.getString(3))
                .unittypeId(resultSet.getLong(4))
                .build();
    }
}
