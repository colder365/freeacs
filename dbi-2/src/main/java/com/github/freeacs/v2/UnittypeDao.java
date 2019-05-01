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
import java.util.Optional;

public class UnittypeDao {

    private static final String INSERT_SQL = "insert into unit_type(unit_type_name, description, protocol) values(?, ?, ?)";
    private static final String UPDATE_SQL = "update unit_type set unit_type_name = ?, description = ?, protocol = ? where unit_type_id = ?";
    private static final String READ_IDS_SQL = "select unit_type_id from unit_type";
    private static final String READ_ALL_SQL = "select unit_type_id, unit_type_name, description, protocol from unit_type";
    private static final String READ_SQL = READ_ALL_SQL + " where unit_type_id = ?";
    private static final String DELETE_SQL = "delete from unit_type where unit_type_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public UnittypeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Unittype addOrChange(Unittype unittype) {
        if (unittype.getId() == null) {
            long newId = add(unittype);
            return unittype.withId(newId);
        }
        update(unittype);
        return unittype;
    }

    public void delete(Unittype unittype) {
        if (unittype.getId() == null) {
            throw new IllegalArgumentException("Cannot delete a unittype with no id");
        }
        jdbcTemplate.update(DELETE_SQL, unittype.getId());
    }

    public List<Unittype> readAll() {
        return jdbcTemplate.query(READ_ALL_SQL, getRowMapper());
    }

    public List<Long> readAllKeys() {
        return jdbcTemplate.queryForList(READ_IDS_SQL, Long.class);
    }

    public Optional<Unittype> read(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot read a unittype with no id");
        }
        return Optional.ofNullable(jdbcTemplate.queryForObject(READ_SQL, new Object[]{id}, getRowMapper()));
    }

    private void update(Unittype toUpdate) {
        jdbcTemplate.update(getPreparedStatementCreator(toUpdate, toUpdate.getId(), UPDATE_SQL));
    }

    private Long add(Unittype toAdd) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getPreparedStatementCreator(toAdd, null, INSERT_SQL), keyHolder);
        return (Long) keyHolder.getKey();
    }

    private PreparedStatementCreator getPreparedStatementCreator(Unittype toAdd, Long id, String sql) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, toAdd.getName());
            ps.setString(2, toAdd.getDescription());
            ps.setString(3, toAdd.getProtocol().name());
            if (id != null) {
                ps.setLong(4, id);
            }
            return ps;
        };
    }

    private RowMapper<Unittype> getRowMapper() {
        return (resultSet, i) -> Unittype.builder()
                .id(resultSet.getLong(1))
                .name(resultSet.getString(2))
                .description(resultSet.getString(3))
                .protocol(Protocol.valueOf(resultSet.getString(4)))
                .build();
    }
}
