import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import configuration.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.awaitility.Awaitility.await;

public class BaseTest {

    private static final String EXPECTED_URL_SUFFIX = "/setup/configure/controller-name";
    private static final int TIMEOUT_SECONDS = 60;

    @BeforeEach
    public void prepareApplicationInFactoryDefaultState() throws Exception {
        removeExistingDockerContainers();
        installApplication();
        waitForUrlToBeAvailable();
    }

    @AfterEach
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }

    public static void removeExistingDockerContainers() {
        try {
            ProcessBuilder pb = new ProcessBuilder("wsl", "docker stop $(docker ps -q) && docker rm $(docker ps -a -q)");
            Process process = pb.start();
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void installApplication() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "wsl", "-d", "Ubuntu", "--", "/bin/bash", "-c",
                String.format("cd %s && ./install.sh", Config.SCRIPT_PATH)
        );
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        if (!process.waitFor(10, TimeUnit.MINUTES)) {
            throw new RuntimeException("Script install.sh. timeout exception");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new RuntimeException("Script install.sh failed. Exit code: " + exitCode);
        }
        System.out.println("Script install.sh passed");
    }

    protected void setSelenideConfiguration() {
        Configuration.browser = "chrome";
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("no-sandbox");
        chromeOptions.addArguments("disable-default-apps");
        chromeOptions.addArguments("--disable-search-engine-choice-screen");
        Configuration.browserCapabilities = chromeOptions;
    }

    private void waitForUrlToBeAvailable() {
        waitForPageAccess();
        waitForPageToLoad();
    }

    private void waitForPageAccess() {
        await().atMost(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(() -> {
                    try {
                        open(Config.BASE_URL);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                });
        System.out.println("Page is accessible");
    }

    private void waitForPageToLoad() {
        await().atMost(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> {
                    open(Config.BASE_URL);
                    String currentUrl = url();
                    return currentUrl.contains(EXPECTED_URL_SUFFIX);
                });
        System.out.println("Application panel is loaded");
    }
}
