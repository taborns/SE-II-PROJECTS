/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twtr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author 7460r
 * accepts array list and generater csv file 
 */
public class CSVGenerator {
    private int curColumn = 0;
    private ArrayList<String> curColumnData;
    private String curColumnLable;
    private static String csvFileName = "twitterfollowerlist";
    private File csvFile;
    private PrintWriter writter;
    private int curFile = 0;
    private int ALLOWED_COLUMN_NUM = 10;
    
    private ArrayList<String> allColumnData;
    
    public CSVGenerator(int fileNum){
        allColumnData = new ArrayList<>();
        this.curFile = fileNum;
    }
    
    /** set current column data **/
    public void setCurColumnData( ArrayList<String> curColumnData ){
        this.curColumnData  = curColumnData;
    }
    
    /** set curent column label **/
    public void setCurColumnLable( String curColumnLable ){
        this.curColumnLable = curColumnLable;
    }
    
    /** get the filename for the csv file **/
    private String getFileName(){
        return this.csvFileName + this.curFile + ".data";
    }
    
    /** create the csv file **/
    public void createCSVFile() throws FileNotFoundException{
        
        this.csvFile = new File( this.getFileName() );
        this.writter = new PrintWriter( csvFile );    
    }
    
    /** store the data to be dumped to CSV temporarily in array list **/
    public void bufferData(){
        int i = 0;
        boolean theFirst;
        StringBuilder sb;
        
        for( String curDataRow : curColumnData ){
            
            theFirst = false;
            
            sb = new StringBuilder();
            //checks if this data is the first in its row
            try{
                allColumnData.get( i );
                sb.append( allColumnData.get( i) );
            }catch( IndexOutOfBoundsException ie){
                theFirst = true;
            }
            
            //if it is the first in the row, prepend all commas for the empty
            //columns
            if( theFirst ){
                
                for( int j=0; j < this.curColumn; j++)
                    sb.append(",");
            }
            else{
                sb.append(",");
            }
            
            sb.append( curDataRow );
            //System.out.println( sb.toString());
            
            if( theFirst)
                allColumnData.add(i, sb.toString());
            else
                allColumnData.set(i, sb.toString());
            
            i++;
        }
        
        while( i < allColumnData.size() ){
            String curRowData = allColumnData.get(i);
            allColumnData.set( i, curRowData + "," );
            i++;
        }
        
        this.curColumn++;
    }
    
    /** save the data in the buffer to the csv file **/
    public void flush(){
        
        try{
            this.createCSVFile();
        }catch( FileNotFoundException fe){
            return;
        }
        
        for( String curColumn : allColumnData){
            this.writter.write(curColumn + "\n");
        }
        this.writter.close();
        
    }
    
}
