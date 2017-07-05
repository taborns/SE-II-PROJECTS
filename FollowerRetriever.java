/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twtr;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author 7460r
 */
public class FollowerRetriever implements Runnable{
    private String twtr_handle;
    private ChromeDriver chromeDriver;
    private String follower_url;
    private boolean isURL = false;
    private ArrayList<WebElement> followers;
    
    public FollowerRetriever( String twtr_handle, ChromeDriver chromeDriver ){
        
        this.twtr_handle = twtr_handle;
        this.chromeDriver = chromeDriver;
    }
    
    public  FollowerRetriever( String URL, ChromeDriver chromeDriver, boolean isURL){
        this.follower_url = URL;
        this.chromeDriver = chromeDriver;
        this.isURL = isURL;
    }
   
    
    public ArrayList<String> retrive_all(){
        
        if( !this.isURL ){
            follower_url = String.format(Config.FOLLOWER_URL, this.twtr_handle);
            follower_url =  Config.URL + follower_url;
        }
        
        System.out.println("Retrieving from ... : " + this.follower_url);
        
        this.chromeDriver.get( follower_url ); 
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<WebElement> followers_ = new ArrayList<>();
        
        
        followers_ = (ArrayList<WebElement>)chromeDriver.findElementsByCssSelector( Config.FOLLOWER_SEL );
        
        for( WebElement follower : followers_)
            followers.add(follower.getText());
        
        return followers;
    }
    
    
    public void run(){
        Twtr.my_follower_followers.set(this.twtr_handle, this.retrive_all());
    }
    
}
