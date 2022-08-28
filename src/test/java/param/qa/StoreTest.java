package param.qa;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class StoreTest {

    @ValueSource(strings={"Василий Аксенов","Виктор Пелевин"})
    @ParameterizedTest(name="Check results test are not empty for query {0}")
    void searchBooks(String testdata){
        open("https://www.mybook.ru");
        $(".ant-input").setValue(testdata);
        $$(".e4xwgl-1").shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    @CsvSource(value = {
            "Василий Аксенов, В эту книгу вошел один из самых знаменитых романов Василия Аксенова,",
            "Виктор Пелевин,  …Я обращаюсь к вам, чтобы попытаться вместе ответить на некоторые вопросы.",
    })

    @ParameterizedTest(name="Check results test contain text \"{1}\" for query: \"{0}\"")
    void searchMostPopularBooks(String testdata, String expectedResult){
        open("https://www.mybook.ru");
        $(".ant-input").setValue(testdata);
        $$(".e4xwgl-1")
                .first()
                .shouldHave(Condition.partialText(expectedResult));
    }

    static Stream<Arguments> searchCatalogs() {
        return Stream.of(
                Arguments.of("рус", List.of("О компании", "Рекламодателю", "Контакты")),
                Arguments.of("eng", List.of("About us", "To advertisers", "Contacts")));

    }
    @MethodSource("searchCatalogs")
    @ParameterizedTest(name = "Result for query {0} displays correct items {1}")
    void searchCatalogs(String lang, List<String>expectedItems){
        open("https://zagranitsa.com");
        $$(" div.lang>ul>li>a").find(text(lang)).click();
        $$(".scrolling>ul>li").shouldHave(CollectionCondition.texts(expectedItems));

    }
}
