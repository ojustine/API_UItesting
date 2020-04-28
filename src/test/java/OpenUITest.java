import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class OpenUITest
{
	@BeforeSuite
	public void init()
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ojustine\\Downloads\\chromedriver_win32\\chromedriver.exe");
	}

	@Test
	public void Test() {
		open("https://www.google.ru/");
		$(By.name("q"))
				.setValue("Открытие")
				.pressEnter();
		$$(By.className("r"))
				.findBy(text("www.open.ru"))
				.should(exist)
				.click();
		switchTo().window(1);
		$(By.xpath("//*[@id=\"main\"]/div/div/div[8]/section/div/div/div[1]/div/div/div/div/div[1]/div[1]/h2"))
				.scrollTo()
				.shouldHave(text("Курс обмена в интернет-банке"));
		String dollarBuy = $(By.xpath("//*[@id=\"main\"]/div/div/div[8]/section/div/div/div[1]/div/div/div/div/div[2]/table/tbody/tr[2]/td[2]/div/span"))
				.getText();
		String dollarSell = $(By.xpath("//*[@id=\"main\"]/div/div/div[8]/section/div/div/div[1]/div/div/div/div/div[2]/table/tbody/tr[2]/td[4]/div/span"))
				.getText();
		String euroBuy = $(By.xpath("//*[@id=\"main\"]/div/div/div[8]/section/div/div/div[1]/div/div/div/div/div[2]/table/tbody/tr[3]/td[2]/div/span"))
				.getText();
		String euroSell = $(By.xpath("//*[@id=\"main\"]/div/div/div[8]/section/div/div/div[1]/div/div/div/div/div[2]/table/tbody/tr[3]/td[4]/div/span"))
				.getText();
		Assert.assertTrue(dollarSell.compareTo(dollarBuy) > 0 && euroSell.compareTo(euroBuy) > 0);
	}
}
