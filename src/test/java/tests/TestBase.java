package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";
    }

    @AfterEach
    void shutdown(){
        closeWebDriver();
    }
}
