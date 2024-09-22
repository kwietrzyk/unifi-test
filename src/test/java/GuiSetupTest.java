import configuration.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.network.DashboardPage;
import pages.setup.SetupPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class GuiSetupTest extends GuiBaseTest {

    private final static SetupPage setupPage = page(SetupPage.class);
    private final String serverName = "UniFi Network";
    private final String country = "United States";

    @BeforeEach
    public void openApplication() {
        open(Config.BASE_URL);
    }

    @Test
    @DisplayName("GUI setup verification")
    public void shouldVerifySetup() {

        DashboardPage dashboardPage =
                setupPage.setServerName(serverName)
                .setCountry(country)
                .clickLicenceButton()
                .clickNextButton()
                .goToAdvancedSetup()
                .setUserName(Config.USERNAME)
                .setPassword(Config.PASSWORD)
                .confirmPassword(Config.PASSWORD)
                .setEmail(Config.EMAIL)
                .clickFinishButton();

        dashboardPage.adminName.shouldHave(text(Config.USERNAME));
        dashboardPage.goToSettings()
                .goToSystem()
                .goToGeneral()
                .country.shouldBe(visible).shouldHave(value(country));
    }
}
