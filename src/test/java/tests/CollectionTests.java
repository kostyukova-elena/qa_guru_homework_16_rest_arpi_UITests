package tests;

import io.restassured.response.Response;
import models.AuthData;
import models.BookData;
import models.DeleteBookData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static spec.Specification.bookRequestSpec;

public class CollectionTests extends TestBase {

    private final AuthData authData = new AuthData();
    private final BookData bookData = new BookData();
    private final DeleteBookData deleteBookData = new DeleteBookData();

    @Test
    @DisplayName("Добавление книги в корзину и последующее удаление")
    public void addBookToCollectionAllureTest() {

        Response authResponse = step("Авторизация", () -> given(bookRequestSpec)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response());

        step("Очистка корзины (на всякий случай)", () -> given(bookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(204));

        step("Добавление книги", () -> given(bookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().response());


        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");
        $(".ReactTable").shouldHave(text("Speaking JavaScript"));

        step("Удаление книги", () -> given(bookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(deleteBookData)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .log().status()
                .log().body()
                .statusCode(204));
    }
}
