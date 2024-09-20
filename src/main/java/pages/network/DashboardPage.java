package pages.network;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    public SelenideElement adminName = $x("//*[contains(text(),'opened UniFi Network via the web.')]");
    public SelenideElement settings = $("[href*='settings']");

    public SettingsPage goToSettings() {
        settings.shouldBe(visible).click();
        return page(SettingsPage.class);
    }
}
