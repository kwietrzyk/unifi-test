package pages.network;

import com.codeborne.selenide.SelenideElement;
import pages.settings.SystemSettings;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SettingsPage {

    public SelenideElement system = $("span[data-testid='system']");

    public SystemSettings goToSystem() {
        system.shouldBe(visible).click();
        return page(SystemSettings.class);
    }
}
