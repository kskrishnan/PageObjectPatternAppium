package com.appium.config;

import com.annotation.values.ElementDescription;
import com.annotation.values.PageName;
import com.appium.manager.AppiumParallelTest;
import com.relevantcodes.extentreports.LogStatus;
import com.report.factory.ExtentTestManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public class PageBase {
    public AppiumDriver driver;
    public AppiumParallelTest appiumParallelTest = new AppiumParallelTest();
    Logger logger = Logger.getLogger(PageBase.class);

    public PageBase(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
    }

    public void waitForPageToLoad(WebElement id) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.elementToBeClickable(id));
    }

    public void waitForElementToDisAppear(String id) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
    }

    public WebElement waitForElement(WebElement arg) {
        waitForPageToLoad(arg);
        WebElement el = arg;
        return el;
    }

    public void swipeRightUntilTextExists(String expected) {
        do {
            swipeRight();
        } while (!driver.getPageSource().contains(expected));
    }

    public void swipeLeftUntilTextExists(String expected) {
        do {
            swipeLeft();
        } while (!driver.getPageSource().contains(expected));
    }

    public void swipeRight() {
        Dimension size = driver.manage().window().getSize();
        int startx = (int) (size.width * 0.9);
        int endx = (int) (size.width * 0.20);
        int starty = size.height / 2;
        driver.swipe(startx, starty, endx, starty, 1000);
    }

    public void swipeLeft() {
        Dimension size = driver.manage().window().getSize();
        int startx = (int) (size.width * 0.8);
        int endx = (int) (size.width * 0.20);
        int starty = size.height / 2;
        driver.swipe(startx, starty, endx, starty, 1000);
    }

    public void scrollDirection(MobileElement Id, SwipeElementDirection arg) {
        MobileElement e = Id;
        e.swipe(arg, 1000);
    }

    /**
     * method to set the context to required view.
     *
     * @param context view to be set
     */
    public void setContext(String context) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
            System.out.println(contextName); //prints out something like NATIVE_APP \n WEBVIEW_1
        }
        driver.context((String) contextNames.toArray()[1]); // set context to WEBVIEW_1

        logger.info("Current context" + driver.getContext());
    }

    public void clickBackButton() {
        driver.navigate().back(); //Closes keyboard
        //driver.navigate().back(); //Comes out of edit mode
    }


    public String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public void logStepIntoExtentReport(String elementDescription, String action,String typeString) {
        ExtentTestManager.getTest().log(LogStatus.INFO, withBoldHTML(action),
            elementDescription + "; " + withBoldHTML("Text") + ": " + typeString);
    }

    public String withBoldHTML(String string) {
        if (!string.trim().isEmpty()) {
            return "<b>" + string + "</b>";
        } else {
            return "";
        }
    }

    public String getPageObjectElemetDescription(Object pageObject, String fieldName) {
        try {
            return this.getClass().getAnnotation(PageName.class).value() + "::" +
                pageObject.getClass().getField(fieldName).getAnnotation(ElementDescription.class)
                .value();
        } catch (NoSuchFieldException e) {

            e.printStackTrace();
        }
        return "";
    }
}
