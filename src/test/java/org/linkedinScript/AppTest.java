package org.linkedinScript;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.remote.http.DumpHttpExchangeFilter.LOG;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    private static WebDriver driver;
    final static String URL_TO_CONNECT = "https://www.linkedin.com/home";
    final static String LINKEDIN_USER = SystemUtil.LINKEDIN_USER; //Your user here
    final static String LINKEDIN_PASSWORD = SystemUtil.LINKEDIN_PASSWORD; // Your password here
    static int  FRIENDS_TO_ADD = 204; // Chane here amount off new connects you want

    static List<WebElement> resultConnection ;

    @BeforeClass
    public static void beforeClassFunction(){
        driver =  Helper.startChromeDriver(URL_TO_CONNECT);
    }

    @AfterClass
    public static void afterClassFunction(){
        driver.quit();
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    @Deprecated
    public void testAMethod()
    {
            //Create a login to a LinkedIn account
            driver.findElement(By.xpath("//a[contains(text(),'Sign in')]")).click();
            driver.findElement(By.id("username")).sendKeys(LINKEDIN_USER);
            driver.findElement(By.id("password")).sendKeys(LINKEDIN_PASSWORD);
            driver.findElement(By.xpath("//button[contains(text(),'Sign in')]")).click();
            LOG.info("Connect to Linkedin successfully. ");

            WebDriverWait wait = new WebDriverWait(driver, 5000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='My Network']//parent::a"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(@aria-label,'See all People in the Information Technology')])[1]"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("discover-cohort-recommendations-modal__title")));


            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            resultConnection = driver.findElements(By.xpath("//div[contains(@class,'artdeco-modal__content')]//li[contains(@class,'ember-view')]"));
            WebElement lastElement = resultConnection.get(resultConnection.size()-1);
        while(  FRIENDS_TO_ADD  >= resultConnection.size()){
                 lastElement.sendKeys("");
                 driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                 resultConnection = driver.findElements(By.xpath("//div[contains(@class,'artdeco-modal__content')]//li[contains(@class,'ember-view')]"));
                 lastElement = resultConnection.get(resultConnection.size()-1);
            }
             LOG.info("Found " + resultConnection.size() + " new friends to connect with. ");

            JSONArray connectionsList = new JSONArray();
            for (WebElement element : resultConnection) {
                if(FRIENDS_TO_ADD > 0){
                    String id = element.getAttribute("id");
                    String profileName = element
                            .findElement(By.className("discover-person-card__name")).getAttribute("innerText");
                    String title = element
                            .findElement(By.className("discover-person-card__occupation")).getAttribute("innerText");
                    String profileLInk = element
                            .findElement(By.xpath(".//*[contains(@class,'discover-entity-type-card__link')]")).getAttribute("href");
                    String profileImg = element
                            .findElement(By.xpath(".//*[contains(@class,'discover-entity-type-card__image-circle')]")).getAttribute("src");
                    WebElement connectButton = element
                            .findElement(By.xpath(".//*[text()='Connect']//parent::button"));

                    // Creating a JSONObject object
                    JSONObject connectionInfo = new JSONObject();
                    connectionInfo.put("id", id);
                    connectionInfo.put("name", profileName);
                    connectionInfo.put("profile_img", profileImg);
                    connectionInfo.put("title", title);
                    connectionInfo.put("profile_link", profileLInk);


                    JSONObject linkedinObj = new JSONObject();
                    linkedinObj.put("requestSendTo", connectionInfo);
                    connectionsList.put(linkedinObj);
                    connectButton.click(); //click on connect button to send request
                    FRIENDS_TO_ADD--;
                }else break;

            }
            Helper.writeToFile(connectionsList,"ToWhomDidISend.Json");//Save as a file all the profiles to which I have sent a membership request
            driver.quit();

    }
}
