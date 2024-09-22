package pages.settings;

import com.codeborne.selenide.SelenideElement;
import pages.settings.system.GeneralSettings;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SystemSettings {

    public SelenideElement general = $("button[data-testid='general']");

    public GeneralSettings goToGeneral() {
        general.shouldBe(visible).click();
        return page(GeneralSettings.class);
    }
}
