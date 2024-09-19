package pages.network;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SettingsPage {

    public SelenideElement system = $("span[data-testid='system']");
}
