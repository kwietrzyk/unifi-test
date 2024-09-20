package pages.setup;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SetupPage {

    public SelenideElement serverName = $("#controllerName");
    public SelenideElement countrySelect = $(".container__kwqQM4WC.containerReset__kwqQM4WC");
    public SelenideElement licenceCheckbox = $("#tosAndEula");
    public SelenideElement submitButton = $("[type='submit']");

    public SetupPage setServerName(String name) {
        serverName.shouldBe(visible).sendKeys(Keys.CONTROL, "a");
        serverName.sendKeys(Keys.DELETE);
        serverName.setValue(name);
        return this;
    }

    public SetupPage setCountry(String country) {
        countrySelect.shouldBe(visible).click();
        $$("li").findBy(Condition.text(country)).click();
        return this;
    }

    public SetupPage clickLicenceButton() {
        licenceCheckbox.shouldBe(visible).click();
        return this;
    }

    public SignInPage clickNextButton() {
        submitButton.shouldBe(visible).click();
        return page(SignInPage.class);
    }
}
