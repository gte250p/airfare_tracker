package lee

import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

public class AirFareCheck {

    static Integer MAX_COUNT = 20;
    static String departDate = "10/15/2016";
    static String returnDate = "10/23/2016";

    public static void main(String[] args) {
        if( args?.length > 0 ){
            for( String arg : args ){
                if( arg.toLowerCase().startsWith("--departs=") ){
                    departDate = arg.substring("--departs=".length());
                }else if( arg.toLowerCase().startsWith("--count=") ){
                    MAX_COUNT = Integer.parseInt(arg.substring("--count=".length()));
                }else if( arg.toLowerCase().startsWith("--returns=") ) {
                    returnDate = arg.substring("--returns=".length());
                }
            }
        }

        Calendar now = Calendar.getInstance();

        String URL = "https://www.expedia.com/Flights-Search?trip=roundtrip&leg1=from:ATL,to:OGG,departure:${departDate}TANYT&leg2=from:OGG,to:ATL,departure:${returnDate}TANYT&passengers=children:0,adults:2,seniors:0,infantinlap:Y&mode=search";
//        String URL = "https://www.kayak.com/flights/ATL-OGG/${departDate}/${returnDate}/2adults"

        def options = new ChromeOptions()
        options.addArguments("--start-maximized")
        WebDriver driver = new ChromeDriver(options);
        println "Loading URL: "+URL
        driver.get(URL);

        Thread.sleep(15000); // Sleep for 15 seconds, period (all data should load during this time)

        println("Finding all flight-module...");
        List<WebElement> flightModules = driver.findElements(By.cssSelector("article.flight-module"));
        int flightModuleCount = 0;
        for( WebElement we : flightModules ){
            if( flightModuleCount > MAX_COUNT )
                break;
            try {
                String price = we.findElement(By.cssSelector("div.price-column")).getAttribute("data-test-price-per-traveler")
                String departureTime = we.findElement(By.cssSelector("span.departure-time")).getText()?.trim()
                String arrivalTime = we.findElement(By.cssSelector("span.arrival-time")).getText()?.trim()
                String duration = we.findElement(By.cssSelector("div.duration-emphasis")).getText()?.trim()
                println("Found Flight! price: " + price +", departs: "+departureTime+", arrives: "+arrivalTime+", duration: "+duration)
                flightModuleCount++;
            }catch(Throwable t){
                System.err.println("Error finding price: "+t);
            }
        }


        //Close the browser
        driver.quit();

        System.out.println("Successfully Executed.");


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
