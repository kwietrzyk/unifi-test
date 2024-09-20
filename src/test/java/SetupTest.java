import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
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
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.requestSpecification;

public class SetupTest extends BaseTest {

    private final static SetupPage setupPage = page(SetupPage.class);
    private final String serverName = "KasiaNetwork";
    private final String country = "Poland";

    @BeforeEach
    public void openApplication() {

        Configuration.browser = "chrome";
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("no-sandbox");
        chromeOptions.addArguments("disable-default-apps");
        chromeOptions.addArguments("--disable-search-engine-choice-screen");
        Configuration.browserCapabilities = chromeOptions;
        open(Config.BASE_URL);
    }

    @Test
    @DisplayName("GUI setup verification")
    public void shouldSetupServerGui() {

        DashboardPage dashboardPage =
                setupPage
                .setServerName(serverName)
                .setCountry(country)
                .clickLicenceButton()
                .clickNextButton()
                .goToAdvancedSetup()
                .setUserName(Config.USERNAME)
                .setPassword(Config.PASSWORD)
                .confirmPassword(Config.PASSWORD)
                .setEmail(Config.EMAIL)
                .clickFinishButton();

        switchTo().alert().accept();
        dashboardPage.adminName.shouldHave(text(Config.USERNAME));
        dashboardPage.goToSettings()
                .country.shouldHave(value(country));
    }

    @Test
    @DisplayName("REST setup verification")
    public void shouldSetupServerRest() {

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(Config.BASE_URL)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.given().spec(requestSpecification)
                .body("")
                .when()
                .post("/api/cmd/sitemgr")
                .then()
                .statusCode(200);


    }

}
