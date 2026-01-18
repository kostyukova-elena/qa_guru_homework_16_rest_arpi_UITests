package tests;

import io.restassured.response.Response;
import models.AuthData;
import models.BookData;
import models.CollectionOfIsbns;
import models.DeleteBookData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static spec.Specification.bookRequestSpec;
import static spec.Specification.createResponseSpec;

public class CollectionTests extends TestBase {

    private final AuthData authData = new AuthData();
    private final BookData bookData = new BookData();
    private final DeleteBookData deleteBookData = new DeleteBookData();

    @Test
    @DisplayName("Добавление книги в корзину и последующее удаление")
    public void addBookToCollectionAllureTest() {
        bookData.setUserId("602ee7c4-f266-4909-9ad3-97715dce95fe");
        bookData.setCollectionOfIsbns(List.of(new CollectionOfIsbns("9781449365035")));
        deleteBookData.setUserId("602ee7c4-f266-4909-9ad3-97715dce95fe");
        deleteBookData.setIsbn("9781449365035");

        Response authResponse = step("Авторизация", () -> given(bookRequestSpec)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .spec(createResponseSpec(200))
                .extract().response());

        step("Очистка корзины (на всякий случай)", () -> given(bookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .spec(createResponseSpec(204))
                .extract().response());

        step("Добавление книги", () -> given(bookRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .spec(createResponseSpec(201))
                .extract().response());

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
