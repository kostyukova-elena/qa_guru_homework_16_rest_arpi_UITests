package tests;

import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class UIPageObjectsTest {

    private final SelenideElement reactTable = $(".ReactTable");

    public UIPageObjectsTest openPage() {
        open("/favicon.ico");
        return this;
    }

    public UIPageObjectsTest openProfile() {
        open("/profile");
        return this;
    }

    public UIPageObjectsTest cookieAuthorization(Response authResponse) {
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));
        return this;
    }

    public UIPageObjectsTest checkElementIsPresent() {
        reactTable.shouldHave(text("Speaking JavaScript"));
        return this;
    }

    public UIPageObjectsTest checkElementIsMissing() {
        reactTable.shouldNotHave(text("Speaking JavaScript"));
        return this;
    }
}
