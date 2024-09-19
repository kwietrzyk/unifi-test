package pages.setup;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SetupPage {

    public SelenideElement serverName = $("#controllerName");
    public SelenideElement countryRegion = $(".container__kwqQM4WC.containerReset__kwqQM4WC");
    public SelenideElement licenceCheckbox = $("#tosAndEula");
    public SelenideElement submitButton = $("[type='submit']");
}
