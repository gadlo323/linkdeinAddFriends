package org.linkedinScript;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * Hello world!
 *
 */
public class App 
{
    @Deprecated
    public static void main( String[] args )
    {
        final String URL_TO_CONNECT = "https://www.linkedin.com/home";
        final String LINKEDIN_USER = SystemUtil.LINKEDIN_USER; //Your user here
        final String LINKEDIN_PASSWORD = SystemUtil.LINKEDIN_USER; // Your password here
        int FRIENDS_TO_ADD = 3;
        WebDriver driver = null;
        try {
            driver =  Helper.startChromeDriver(URL_TO_CONNECT);

            //Create a login to a LinkedIn account
            driver.findElement(By.xpath("//a[contains(text(),'Sign in')]")).click();
            driver.findElement(By.id("username")).sendKeys(LINKEDIN_USER);
            driver.findElement(By.id("password")).sendKeys(LINKEDIN_PASSWORD);
            driver.findElement(By.xpath("//button[contains(text(),'Sign in')]")).click();

            WebDriverWait wait = new WebDriverWait(driver, 5000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='My Network']//parent::a"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(@aria-label,'See all People in the Information Technology')])[1]"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("discover-cohort-recommendations-modal__title")));


            List<WebElement> resultConnection = driver.findElements(By.xpath("//li[contains(@class,'ember-view')]"));

            int index = 0;
            JSONArray connectionsList = new JSONArray();
            for (WebElement element : resultConnection) {
                if(FRIENDS_TO_ADD > 0){
                    String profileName = element
                            .findElement(By.xpath(".//*[contains(@class,'discover-person-card__name')]")).getText();
                    String workplace = element
                            .findElement(By.xpath(".//*[contains(@class,'discover-person-card__occupation')]")).getText();
                    String profileImg = element
                            .findElement(By.xpath(".//*[contains(@class,'discover-entity-type-card__image-circle')]")).getAttribute("src");
                    WebElement connectButton = element
                            .findElement(By.xpath(".//*[text()='Connect']//parent::button"));

                    // Creating a JSONObject object
                    JSONObject connectionInfo = new JSONObject();
                    connectionInfo.put("id", index);
                    connectionInfo.put("name", profileName);
                    connectionInfo.put("profileImg", profileImg);
                    connectionInfo.put("workPlace", workplace);


                    JSONObject linkedinObj = new JSONObject();
                    linkedinObj.put("connection", connectionInfo);
                    connectionsList.put(linkedinObj);
//                    connectButton.click();
                    index++;
                    FRIENDS_TO_ADD--;
                }else break;

            }
            Helper.writeToFile(connectionsList,"output.Json");
            driver.quit();

        } catch (Exception e) {
            e.printStackTrace();
            Objects.requireNonNull(driver).quit();
        }

    }


}
