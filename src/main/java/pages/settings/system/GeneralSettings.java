package pages.settings.system;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class GeneralSettings {

    public SelenideElement country = $("[id='country.code']");
}
