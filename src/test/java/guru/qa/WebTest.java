package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {
    @ValueSource(strings = {"Selenide", "Allure"})
    @ParameterizedTest(name = "Результаты поиска не пустые для запроса {0}")
    void commonSearchTest(String testData) {
        open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item").shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    @CsvSource(value = {
            "Selenide,  это фреймворк для автоматизированного тестирования веб-приложений",
            "Allure java,   integrations for",
    })
    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса: \"{0}\"")
    void commonComplexSearchTest(String testData, String expectedResult) {
        open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item").filter(not(text("Реклама")))
                .first()
                .shouldHave(partialText(expectedResult));
    }

    static Stream<Arguments> dataProviderForSelenideSiteMenuTest() {
        return Stream.of(
                Arguments.of(Lang.EN, List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes")),
                Arguments.of(Lang.RU, List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"))
        );
    }
    @MethodSource("dataProviderForSelenideSiteMenuTest")
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки {1}")
    void selenideSiteMenuTest(Lang lang, List<String> expectedButtons) {
        open("https://selenide.org/");
        $$("#languages a").find(text(lang.name())).click();
        $$(".main-menu-pages a")
                .filter(visible)
                .shouldHave(CollectionCondition.texts(expectedButtons));
    }

    @EnumSource(Lang.class)
    @ParameterizedTest
    void selenideSiteMenuEnumTest(Lang lang) {
        open("https://selenide.org/");
        $$("#languages a").find(text(lang.name())).click();
        $("#selenide-logo").shouldBe(visible);
    }
}

