package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;

import static org.openqa.selenium.By.cssSelector;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldReturnSuccessIfFieldsFilledInCorrectly() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='order-success']")).getText().strip();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnSuccessfullyIfSurnameWithHyphen() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Петров Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='order-success']")).getText().strip();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFormIsEmpty() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldSurnameAndNameIsEmpty() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldPhoneEmpty() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfDoNotTick() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='agreement'].input_invalid")).getText().strip();
        String expectedMessage = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfSurnameAndNameInLatin() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Gromov Ivan");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldSurnameAndNameFilledWithNumbers() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("12345");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldSurnameAndNameFilledWithSpecialCharacters() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("!@#$%^&*_+");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldPhoneIsOneNumber() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+7");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfPhoneWithoutPlus() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("79371745355");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldPhoneIsTenNumbers() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+7937174535");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldPhoneIsTwelveNumbers() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+793717453555");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldPhoneFilledIsLetters() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+кенгщатлож");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorMessageIfFieldPhoneFilledIsSpecialCharacters() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(cssSelector("form"));
        driver.findElement(cssSelector("[data-test-id='name'] input")).sendKeys("Громов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("!@#$%^&*_+");
        driver.findElement(cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(cssSelector("button")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().strip();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}