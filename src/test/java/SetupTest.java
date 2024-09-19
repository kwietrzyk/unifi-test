import com.codeborne.selenide.WebDriverRunner;
import configuration.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.network.DashboardPage;
import pages.setup.SetupPage;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

public class SetupTest {

    private final static SetupPage setupPage = page(SetupPage.class);
    private final String serverName = "KasiaNetwork";
    private final String country = "Poland";

    @BeforeEach
    @Step("Open setup page")
    public void goToApp() {
        open(Config.BASE_URL);
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    @Test
    @DisplayName("GUI setup verification")
    public void shouldSetupServer() {

        DashboardPage dashboardPage = setupPage.setServerName(serverName)
                .setCountry(country)
                .clickLicenceButton()
                .clickNextButton()
                .goToAdvanceSetup()
                .setUserName(Config.USERNAME)
                .setPassword(Config.PASSWORD)
                .confirmPassword(Config.PASSWORD)
                .setEmail(Config.EMAIL)
                .clickFinishButton();
                // alert accept

        dashboardPage.adminName.shouldHave(text(Config.USERNAME));

        dashboardPage.goToSettings()
                .goToGeneralSettings()
                .country.shouldHave(value(country));
    }
}
