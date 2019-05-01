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

public class UnittypeParameterDaoTest {

    private EmbeddedDatabase dataSource;
    private UnittypeDao unittypeDao;
    private UnittypeParameterDao unittypeParameterDao;

    @Before
    public void init() {
        dataSource = new EmbeddedDatabaseBuilder()
                .setName("testDB;MODE=MySQL")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("h2-schema.sql")
                .build();
        unittypeDao = new UnittypeDao(dataSource);
        unittypeParameterDao = new UnittypeParameterDao(dataSource);
    }

    @After
    public void destroy() {
        dataSource.shutdown();
    }

    @Test
    public void crudOperations() {
        unittypeDao.addOrChange(Unittype.builder().name("Test").description("Test").protocol(Protocol.TR069).build());
        UnittypeParameter unittypeParameter = UnittypeParameter.builder().name("Test").flag("RW").unittypeId(1L).build();
        unittypeParameter = unittypeParameterDao.addOrChange(unittypeParameter);
        assertEquals(1, unittypeParameter.getId().longValue());
        unittypeParameter = unittypeParameter.withFlag("RA");
        unittypeParameter = unittypeParameterDao.addOrChange(unittypeParameter);
        assertEquals(unittypeParameter.withFlag("RA"), unittypeParameter);
        assertEquals(Collections.singletonList(1L), unittypeParameterDao.readAllKeys());
        assertEquals(Collections.singletonList(unittypeParameter), unittypeParameterDao.readAll());
        assertEquals(Optional.of(unittypeParameter), unittypeParameterDao.read(unittypeParameter.getId()));
        unittypeParameterDao.delete(unittypeParameter);
        assertEquals(Collections.emptyList(), unittypeParameterDao.readAllKeys());
        assertEquals(Collections.emptyList(), unittypeParameterDao.readAll());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenDeletingUnittypeWithNoId() {
        unittypeParameterDao.delete(UnittypeParameter.builder().build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenSupplyingNulltoReadUnittype() {
        unittypeParameterDao.read(null);
    }
}
