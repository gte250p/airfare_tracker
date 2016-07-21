package lee

import org.openqa.selenium.*
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

public class AirFareCheck {

    static String departDate = "10/15/2016";
    static String returnDate = "10/23/2016";

    public static void main(String[] args) {
        if( args?.length > 0 ){
            for( String arg : args ){
                if( arg.toLowerCase().startsWith("--departs=") ){
                    departDate =
                }
            }
        }

        String EXPEDIA_URL = "https://www.expedia.com/Flights-Search?trip=roundtrip&leg1=from:ATL,to:OGG,departure:${departDate}TANYT&leg2=from:OGG,to:ATL,departure:${returnDate}TANYT&passengers=children:0,adults:2,seniors:0,infantinlap:Y&mode=search";


        WebDriver driver = new FirefoxDriver();
        driver.get(EXPEDIA_URL);

        System.out.println("Page title is: " + driver.getTitle());

        try {
            (new WebDriverWait(driver, 30)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.findElement(By.id("stopsFilterList")).isDisplayed();
                }
            });
            System.out.println("Successfully found stopsFilterList")
        }catch(Throwable t){
            errorAndDie("Timed out waiting for stopsFilterList!", t);
        }

        println("Finding all flight-module...");
        List<WebElement> flightModules = driver.findElements(By.cssSelector("article.flight-module"));
        int flightModuleCount = 0;
        for( WebElement we : flightModules ){
            if( flightModuleCount > 20 )
                break;
            try {
                String price = we.findElement(By.cssSelector("div.price-column")).getAttribute("data-test-price-per-traveler")
                String departureTime = we.findElement(By.cssSelector("span.departure-time")).getText()?.trim()
                String arrivalTime = we.findElement(By.cssSelector("span.arrival-time")).getText()?.trim()
                println("Found Flight! price: " + price +", departs: "+departureTime+", arrives: "+arrivalTime)
                flightModuleCount++;
            }catch(Throwable t){
                System.err.println("Error finding price: "+t);
            }
        }


        //Close the browser
        driver.quit();

        System.out.println("Succesfully Executed.");


    }

    static def errorAndDie(String msg){
        System.err.println(msg);
        System.exit(1);
    }
    static def errorAndDie(String msg, Throwable t){
        System.err.println(msg);
        t.printStackTrace(System.err);
        System.exit(1);
    }




}
