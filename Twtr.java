/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twtr;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author 7460r
 */
public class Twtr {
    private ChromeDriver chromeDriver;
    private WebElement username_field;
    private WebElement password_field;
    private String username;
    private ArrayList<String> my_followers;
    public static FollowerAC my_follower_followers = new FollowerAC();
    
    
    public void setup(){
        System.setProperty("webdriver.chrome.driver", Config.DRIVERADDR);
        ChromeDriverDispatcher driverDispatcher = new ChromeDriverDispatcher(1);
        chromeDriver = driverDispatcher.dispatchDriver(); //new ChromeDriver();
        
        
    }
    
    public void perform_login(){
        
        chromeDriver.get(Config.URL);
        
        this.username_field = chromeDriver.findElement( By.id(Config.EMAILID ) );
        this.password_field = chromeDriver.findElement( By.id( Config.PASSID) );
        
        this.username_field.sendKeys( Config.EMAIL);
        this.password_field.sendKeys( Config.PASSWORD );
        this.password_field.sendKeys( Keys.ENTER );
    }
    
    public String get_twitter_handle(){
        this.username = Config.EMAIL;
        
        //check if it is email 
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher( this.username );
        
        if ( mat.matches() ){
            WebElement user_fields = chromeDriver.findElement( By.className("DashboardProfileCard-userFields"));
            WebElement username = user_fields.findElement( By.cssSelector(".DashboardProfileCard-screenname .u-linkComplex-target") );
            this.username = username.getText();
        }
        
        return this.username;
    }
    
    public void get_following(){
        String follower_url = String.format(Config.FOLLOWER_URL, this.username);
        chromeDriver.get( follower_url );
    }
    
    public void get_followers(){
        
        //get all followers for the logged in user
        FollowerRetriever followerRetriever = new FollowerRetriever(this.username, this.chromeDriver); 

        this.my_followers = followerRetriever.retrive_all();
        
        String my_follower;
        
        for( int i=0; i < this.my_followers.size(); i++){
               my_follower = this.my_followers.get(i);
               
               if( my_follower.trim().isEmpty() || my_follower.trim().equals(this.username)){
                   this.my_followers.remove(i);
                   continue;
               }   
               
               
        }
    }
    
    public void getFollowers_follower() throws InterruptedException{
        
        String twtr_handle;
        ChromeDriver chromeDriver = new ChromeDriver();

        // create ExecutorService to manage threads
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
        ChromeDriverDispatcher driverDispatcher = new ChromeDriverDispatcher( this.my_followers.size() );
        for( int i=0; i < this.my_followers.size();){
            
            twtr_handle = this.my_followers.get(i);
            
            FollowerRetriever followerRetriever = new FollowerRetriever( twtr_handle, driverDispatcher.dispatchDriver() ); 
            
            //followerRetriever.run();
            threadExecutor.execute(followerRetriever);
            i++;
        }
        
        threadExecutor.shutdown();
        threadExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        
        my_follower_followers.dump_to_csv();
    }
    
    public static void main(String[] args) throws InterruptedException {
        Twtr twtr = new Twtr();
        twtr.setup();
        twtr.perform_login();
        twtr.get_twitter_handle();
        twtr.get_followers();
        System.out.println(twtr.my_followers.size());
        twtr.getFollowers_follower();
        
            Thread.sleep(10000);

        
        
    }
    
}
