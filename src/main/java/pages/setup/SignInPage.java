package pages.setup;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SignInPage {

    public SelenideElement advancedSetupButton = $(byText("Advanced Setup"));
    public SelenideElement skipButton = $(byText("Skip"));

    public AdvancedSetupPage goToAdvancedSetup() {
        advancedSetupButton.shouldBe(visible).click();
        skipButton.shouldBe(visible).click();
        return page(AdvancedSetupPage.class);
    }
}
