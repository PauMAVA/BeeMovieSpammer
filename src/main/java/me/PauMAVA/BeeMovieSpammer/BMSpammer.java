package me.PauMAVA.BeeMovieSpammer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

public class BMSpammer {

    private final ScriptTokenizer tokenizer;

    private WebDriver driver;

    public static void main(String[] args) {
        new BMSpammer(
                "C:/Users/Pau/Desktop/Programming/BeeMovieSpammer/src/main/resources/beemovie.script",
                SeparationType.WORD
        ).startSpamming();
    }

    public BMSpammer(String filePath, SeparationType type) {
        tokenizer = new ScriptTokenizer(filePath, type);
    }

    public void startSpamming() {
        openBrowser();
        openChat("BRAÇ DE MONIATO");
        int i = 0;
        try {
            String token;
            while ((token = tokenizer.nextToken()) != null) {
                System.out.println("Sending token: " + token);
                sendMessage(token);
                SleepUtil.sleep(500);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent " + i + " tokens!");
    }

    private void openBrowser() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("incognito");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriver.exe");
        this.driver = new ChromeDriver(capabilities);
        driver.get("https://web.whatsapp.com");
        SleepUtil.sleep(2000);
        while(!isLoggedIn()) {
            System.out.println("Not logged in, waiting for login...");
            SleepUtil.sleep(1000);
        }
        System.out.println("Logged in!");
        SleepUtil.sleep(6000);
    }

    private void openChat(String chatTitle) {
        WebElement chat = driver.findElement(By.cssSelector("[title*='" + chatTitle + "']"));
        chat.click();
    }

    private void sendMessage(String message) {
        writeText(message);
        SleepUtil.sleep(100);
        sendMessage();
    }

    private void writeText(String text) {
        WebElement textBox = driver.findElement(By.cssSelector("[data-tab='1']"));
        textBox.click();
        textBox.sendKeys(text);
    }

    private void sendMessage() {
        WebElement button = driver.findElement(By.className("_1U1xa"));
        button.click();
    }

    private boolean isLoggedIn() {
        try {
            driver.findElement(By.cssSelector("[aria-label='Scan me!']"));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    static class SleepUtil {

        public static void sleep(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
