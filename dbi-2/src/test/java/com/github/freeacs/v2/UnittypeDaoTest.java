package com.github.freeacs.v2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UnittypeDaoTest {

    private EmbeddedDatabase dataSource;
    private UnittypeDao unittypeDao;

    @Before
    public void init() {
        dataSource = new EmbeddedDatabaseBuilder()
                .setName("testDB;MODE=MySQL")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("h2-schema.sql")
                .build();
        unittypeDao = new UnittypeDao(dataSource);
    }

    @After
    public void destroy() {
        dataSource.shutdown();
    }

    @Test
    public void crudOperations() {
        Unittype unittype = Unittype.builder().name("Test").description("Test").protocol(Protocol.TR069).build();
        unittype = unittypeDao.addOrChange(unittype);
        assertEquals(1, unittype.getId().longValue());
        unittype = unittype.withDescription("Changed");
        unittype = unittypeDao.addOrChange(unittype);
        assertEquals(unittype.withDescription("Changed"), unittype);
        assertEquals(Collections.singletonList(1L), unittypeDao.readAllKeys());
        assertEquals(Collections.singletonList(unittype), unittypeDao.readAll());
        assertEquals(Optional.of(unittype), unittypeDao.read(unittype.getId()));
        unittypeDao.delete(unittype);
        assertEquals(Collections.emptyList(), unittypeDao.readAllKeys());
        assertEquals(Collections.emptyList(), unittypeDao.readAll());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenDeletingUnittypeWithNoId() {
        unittypeDao.delete(Unittype.builder().build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenSupplyingNulltoReadUnittype() {
        unittypeDao.read(null);
    }
}
