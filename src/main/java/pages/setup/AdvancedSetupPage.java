package pages.setup;

import com.codeborne.selenide.SelenideElement;
import pages.network.DashboardPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class AdvancedSetupPage {

    public SelenideElement username = $("#localAdminUsername");
    public SelenideElement adminPassword = $("#localAdminPassword");
    public SelenideElement confirmPassword = $("#localAdminPasswordConfirm");
    public SelenideElement adminEmail = $("#localAdminEmail");
    public SelenideElement finishButton = $(byText("Finish"));


    public AdvancedSetupPage setUserName(String adminName) {
        username.shouldBe(visible).setValue(adminName);
        return this;
    }

    public AdvancedSetupPage setPassword(String password) {
        adminPassword.shouldBe(visible).setValue(password);
        return this;
    }

    public AdvancedSetupPage confirmPassword(String password) {
        confirmPassword.shouldBe(visible).setValue(password);
        return this;
    }

    public AdvancedSetupPage setEmail(String email) {
        adminEmail.shouldBe(visible).setValue(email);
        return this;
    }

    public DashboardPage clickFinishButton() {
        finishButton.shouldBe(visible).click();
        return page(DashboardPage.class);
    }
}
