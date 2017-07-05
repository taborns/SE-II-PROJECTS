/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twtr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.WebElement;

/**
 *
 * @author 7460r
 */
public class FollowerAC {

    public static int FOLLOWERSPERFILE = 10;

    private  Map<String, ArrayList<String>> followers = new HashMap<>();
    
    public synchronized void set(String twtr_handle, ArrayList<String> followers){
        this.followers.put(twtr_handle, followers);
    }
    
    public ArrayList<String> get(String twtr_handle){
        return this.followers.get(twtr_handle);
    }
    
    public Map<String, ArrayList<String>> get_all(){
        return this.followers;
    }
    
    public void printAll(){
       Iterator iter = (Iterator) followers.keySet().iterator();
       
       while( iter.hasNext()){
           
       }
    }

    public void dump_to_csv(){
        
        Set<String> keys = this.followers.keySet();
        int curColumn = 0;
        int fileNum = 0;
        CSVGenerator csvGen = new CSVGenerator( fileNum );
        
        for(String key : keys){
            
            if( curColumn > 0 && (curColumn % 10 == 0) ){
                csvGen.flush();
                csvGen = new CSVGenerator( ++fileNum);
            }
            
            csvGen.setCurColumnData( this.followers.get( key) );
            curColumn++;
            
        }
        
        if( curColumn > 0 && (curColumn % 10 != 0) )
            csvGen.flush();
        
    }
    
}

