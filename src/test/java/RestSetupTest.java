import api.ApiClient;
import com.codeborne.selenide.Configuration;
import configuration.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.network.DashboardPage;
import pages.setup.SetupPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.CoreMatchers.equalTo;

public class RestSetupTest extends BaseTest {

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

    @Test
    @DisplayName("REST setup verification")
    public void shouldSetupServerRest() {

        apiClient.postLocalAdmin(localAdminRequestBody);
        apiClient.postApplicationName(appName);
        apiClient.postCountryCode(countryCode);
        apiClient.postTimezone(timezone);
        apiClient.postConfigState(configState);

        apiClient.getAdmin().then().log().all().body("name", equalTo("admin"));
        apiClient.getCountry().then().log().all().body("code", equalTo(countryCode));

    }

}
