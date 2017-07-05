/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twtr;

import java.util.ArrayList;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author 7460r
 */
public class ChromeDriverDispatcher {

    private static ArrayList<ChromeDriver> chromeDrivers;
    public static int ALLOWED_DRIVERS = 5;
    private int cur = 0;
    
    public ChromeDriverDispatcher( int task){
        chromeDrivers = new ArrayList<>();
        
        int num_drivers = ALLOWED_DRIVERS;
        if( task < ALLOWED_DRIVERS )
            num_drivers = task;
        
        for( int i=0; i < num_drivers; i++)
            chromeDrivers.add( new ChromeDriver());   
    }
    
    public ChromeDriver dispatchDriver(){
    	
    	ChromeDriver chromeDriver =  chromeDrivers.get(  this.cur );
        this.cur = this.cur % this.chromeDrivers.size();
        
    	
    	return chromeDriver;
    }

    
}
