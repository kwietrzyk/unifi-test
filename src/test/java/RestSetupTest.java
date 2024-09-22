import api.ApiClient;
import api.LocalAdminDto;
import configuration.Config;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class RestSetupTest extends BaseTest {

    private ApiClient apiClient;

    @BeforeEach
    public void createApiClient() {
        apiClient = new ApiClient();
    }

    @AfterEach
    public void logOut() {
        apiClient.logOut();
    }

    @Test
    @DisplayName("REST setup verification")
    public void shouldSetupServerRest() {

        String appName = "UniFi Network";
        String countryCode = "840";
        String timezone = "Europe/Riga";
        String configState = "set-installed";
        LocalAdminDto localAdmin = new LocalAdminDto("add-default-admin", Config.USERNAME, Config.EMAIL, Config.PASSWORD);

        apiClient.postLocalAdmin(localAdmin);
        apiClient.postApplicationName(appName);
        apiClient.postCountryCode(countryCode);
        apiClient.postTimezone(timezone);
        apiClient.postConfigState(configState);

        apiClient.getAdmin().then().body("data[0].name", equalTo(Config.USERNAME));
        apiClient.getCountry().then().body("data[0].code", equalTo(countryCode));
    }
}
