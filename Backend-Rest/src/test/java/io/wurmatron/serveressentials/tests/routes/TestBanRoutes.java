package io.wurmatron.serveressentials.tests.routes;

import io.wurmatron.serveressentials.ServerEssentialsRest;
import io.wurmatron.serveressentials.models.Ban;
import io.wurmatron.serveressentials.tests.sql.TestBans;
import io.wurmatron.serveressentials.tests.utils.HTTPRequests;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestBanRoutes {

    @BeforeAll
    public static void setup() throws IOException, SQLException {
        ServerEssentialsRest.main(new String[]{});
        HTTPRequests.BASE_URL = "http://" + ServerEssentialsRest.config.server.host + ":" + ServerEssentialsRest.config.server.port + "/";
        // TODO Add Authentication or force config.testing when running tests
    }

    @Test
    @Order(1)
    public void testAddBanEntry() throws IOException {
        Ban newEntry = HTTPRequests.postWithReturn("ban", TestBans.TEST_BAN, Ban.class);
        assertNotNull(newEntry, "Entry was added successfully");
        // Make sure the ban was added.
    }

    @Test
    @Order(2)
    public void testGetBanEntry() throws IOException {
        Ban entry = HTTPRequests.get("ban/" + TestBans.TEST_BAN.banID, Ban.class);
        assertEquals(TestBans.TEST_BAN, entry, "Entry exists");
    }

    @Test
    @Order(2)
    public void testGetBans() throws IOException {
        Ban[] bans = HTTPRequests.get("ban", Ban[].class);
        boolean exists = false;
        for(Ban ban : bans)
            if(TestBans.TEST_BAN.equals(ban)) {
                 exists = true;
                 break;
            }
        assertTrue(exists, "Ban Entry Exists");
    }

    @Test
    @Order(2)
    public void testUpdateBan() throws IOException {
        TestBans.TEST_BAN.banReason ="Stupidity";
        HTTPRequests.put("ban/" + TestBans.TEST_BAN.banID, TestBans.TEST_BAN.banID);
        // Make sure it was updated
        Ban entry = HTTPRequests.get("ban/" + TestBans.TEST_BAN.banID, Ban.class);
        assertEquals(TestBans.TEST_BAN, entry, "Entry was updated");
    }

    @Test
    @Order(3)
    public void testDeleteBan() throws IOException {
        Ban deletedBan = HTTPRequests.deleteWithReturn("ban/" + TestBans.TEST_BAN.banID,TestBans.TEST_BAN, Ban.class);
        assertNotNull(deletedBan, "Ban was deleted");
    }
}