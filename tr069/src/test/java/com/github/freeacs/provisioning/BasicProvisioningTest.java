package com.github.freeacs.provisioning;

import com.github.freeacs.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = {
        "classpath:application.properties",
        "classpath:application-h2-datasource.properties",
        "classpath:application-basic-security.properties",
        "classpath:application-discovery-mode.properties"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicProvisioningTest extends AbstractProvisioningTest {

    @Before
    public void init() throws SQLException {
        addNonProvisionedUnit();
    }

    @Test
    public void getUnauthorizedOnMissingAuthentication() throws Exception {
        mvc.perform(post("/tr069")).andExpect(status().isUnauthorized());
    }

    @Test
    public void discoverUnitWithDigestAuthentication() throws Exception {
       discoverUnit(httpBasic(UNIT_ID, UNIT_PASSWORD));
    }
}