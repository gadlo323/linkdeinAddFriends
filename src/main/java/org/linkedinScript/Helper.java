package org.linkedinScript;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Helper {

    public static WebDriver startChromeDriver(String getUrl){
        WebDriver driver = null;
        try{
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\isaya\\Desktop\\chromeDriver\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get(getUrl);
            return driver;
        }catch (Exception e){
            e.printStackTrace();
            Objects.requireNonNull(driver).quit();
            return  driver;

        }

    }

    public static void writeToFile(Object connectionsList,String fileName) {
        //Change here your desire PATH.
        String PATH = "C:\\Users\\isaya\\IdeaProjects\\linkedInAddingFriends\\src\\resource\\\\\\\\" + fileName;
        try {
            FileWriter file = new FileWriter(PATH, true);
            file.write(connectionsList.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
