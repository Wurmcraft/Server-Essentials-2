package io.wurmatron.serveressentials.tests.routes;


import io.wurmatron.serveressentials.ServerEssentialsRest;
import io.wurmatron.serveressentials.models.Currency;
import io.wurmatron.serveressentials.tests.sql.TestCurrencys;
import io.wurmatron.serveressentials.tests.utils.HTTPRequests;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCurrencyRoutes {
    @BeforeAll
    public static void setup() throws IOException, SQLException {
        ServerEssentialsRest.main(new String[]{});
        HTTPRequests.BASE_URL = "http://" + ServerEssentialsRest.config.server.host + ":" + ServerEssentialsRest.config.server.port + "/";
        // TODO Add Authentication or force config.testing when running tests
    }


    @Test
    @Order(1)
    public void testAddCurrency() throws IOException {
        Currency currency = HTTPRequests.postWithReturn("currency", TestCurrencys.TEST_CURRENCY, Currency.class);
        assertNotNull(currency, "Currency was added successfully");
        // Make sure the currency was added correctly
        TestCurrencys.TEST_CURRENCY.id = currency.id;
        currency = HTTPRequests.get("currency/" + currency.id, Currency.class);
        assertEquals(TestCurrencys.TEST_CURRENCY, currency, "Currencies are the same");
    }

    @Test
    @Order(2)
    public void testGetCurrency() throws IOException {
        Currency[] currencies = HTTPRequests.get("currency", Currency[].class);
        boolean exists = false;
        for (Currency currency : currencies)
            if (TestCurrencys.TEST_CURRENCY.equals(currency)) {
                exists = true;
                break;
            }
        assertTrue(exists, "Added Currency Exists");
    }

    @Test
    @Order(2)
    public void testUpdateCurrency() throws IOException {
        TestCurrencys.TEST_CURRENCY.globalWorth = .001;
        HTTPRequests.put("currency/" + TestCurrencys.TEST_CURRENCY.id, TestCurrencys.TEST_CURRENCY);
        // Make sure the currency was updated
        Currency currency = HTTPRequests.get("currency/" + TestCurrencys.TEST_CURRENCY.id, Currency.class);
        assertEquals(TestCurrencys.TEST_CURRENCY, currency, "Currencies are the same");
    }

    @Test
    @Order(3)
    public void testDeleteCurrency() throws IOException {
        Currency deletedCurrency = HTTPRequests.deleteWithReturn("currency/" + TestCurrencys.TEST_CURRENCY.id, TestCurrencys.TEST_CURRENCY, Currency.class);
        assertNotNull(deletedCurrency, "Currency was deleted successfully");
        // Make sure the currency was deleted successfully
        Currency[] currencies = HTTPRequests.get("currency", Currency[].class);
        boolean exists = false;
        for (Currency currency : currencies)
            if (TestCurrencys.TEST_CURRENCY.equals(currency)) {
                exists = true;
                break;
            }
        assertFalse(exists, "Currency does not exist");
    }

}
