import api.ApiClient;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class RestSetupTest {

    private ApiClient apiClient;

    private String appName = "UniFi Network";
    private String countryCode = "840";
    private String timezone = "Europe/Riga";
    private String configState = "set-installed";
    private String localAdminRequestBody = """
            {
                "cmd": "add-default-admin",
                "name": "admin",
                "email": "network-admin@gmail.com",
                "x_password": "password"
            }
        """;

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

//        apiClient.postLocalAdmin(localAdminRequestBody);
//        apiClient.postApplicationName(appName);
//        apiClient.postCountryCode(countryCode);
//        apiClient.postTimezone(timezone);
//        apiClient.postConfigState(configState);

        apiClient.getAdmin().then().log().all().body("data[0].name", equalTo("admin"));
        apiClient.getCountry().then().log().all().body("data[0].code", equalTo(countryCode));
    }
}
