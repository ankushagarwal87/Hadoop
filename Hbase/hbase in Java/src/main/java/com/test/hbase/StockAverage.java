package com.test.hbase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class StockAverage {

	private static Configuration conf;
    static HTable table;
    static HashMap<String, Double> stock_average = new HashMap<String, Double>();
    public StockAverage(String tableName, String colFams) throws IOException 
    {
    	conf = HBaseConfiguration.create();
        createTable(tableName, colFams);
        table = new HTable(conf, tableName);
    }
    public void createTable(String tableName, String colFams) throws IOException 
    {
        HBaseAdmin hbase = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor(tableName);
        HColumnDescriptor meta = new HColumnDescriptor(colFams.getBytes());
        desc.addFamily(meta);
        hbase.createTable(desc);
    }
    public static void addColumnEntry(String tableName, String row,String colFamilyName, String colName, String values) throws IOException 
    {
    	byte[] rowKey = Bytes.toBytes(row);
    	Put putdata = new Put(rowKey);
    	putdata.add(Bytes.toBytes(colFamilyName), Bytes.toBytes(colName),Bytes.toBytes(values));
    	table.put(putdata);
    }
    public static void getAllRecord(String tableName) throws IOException
    {
    	    //Get s = new Get(Bytes.toBytes("row"));
    	  //  Result result = table.get(s);
    	  //  byte [] value = result.getValue(Bytes.toBytes("data"),Bytes.toBytes("stock"));
    	  //  String stock=Bytes.toString(value);
    	  //  System.out.println(stock);
    	//	conf = HBaseConfiguration.create();
    	//	table = new HTable(conf, tableName);
    	    Scan s1 = new Scan();
    	    s1.addColumn(Bytes.toBytes("data"), Bytes.toBytes("stock"));
    	    s1.addColumn(Bytes.toBytes("data"), Bytes.toBytes("stock_price_open"));
    	    ResultScanner scanner = table.getScanner(s1);
    	    try {
    	    	for (Result rr = scanner.next(); rr != null; rr = scanner.next()) 
    	    	{
    	    		byte [] stock_name = rr.getValue(Bytes.toBytes("data"),Bytes.toBytes("stock"));
    	      	    String stock=Bytes.toString(stock_name);
    	      	    byte [] stock_open_price = rr.getValue(Bytes.toBytes("data"),Bytes.toBytes("stock_price_open"));
    	      	    String price_open=Bytes.toString(stock_open_price);
    	      	    System.out.println(stock+" "+price_open);
    	      	    if(!price_open.startsWith("s"))
    	      	    {
    	      	    	if(stock_average.containsKey(stock))
    	      	    	{
    	      	    		Double value = stock_average.get(stock);
    	      	    		value = value + Double.parseDouble(price_open);
    	      	    		value = value / 2;
    	      	    		stock_average.put(stock, value);
    	      	    //		System.out.println(stock+" "+stock_average.get(stock));
    	      	    	}
    	      	    	else
    	      	    	{
    	      	    		stock_average.put(stock,Double.parseDouble(price_open));
    	      	    	}
    	    		//System.out.println("Found row: " + rr);
    	      	    }
    	    	}   
    	    	} 
    	    finally 
    	    {
    	    	scanner.close();
    	    }
    	    Iterator<String> keySetIterator = stock_average.keySet().iterator();
    	    while(keySetIterator.hasNext())
    	    {
    	      String key = keySetIterator.next();
    	      System.out.println("key: " + key + " value: " + stock_average.get(key));
    	    }

   }
    public static void main(String[] args) throws IOException 
    {
        String tableName = "Stock_Prices_Test";
        String colFamilyNames = "data";
        StockAverage test = new StockAverage(tableName,colFamilyNames);
        String fileName = "/home/hduser/hbase/NYSE3.csv";
        String line = null;
        try {	
        	FileReader fileReader = new FileReader(fileName);
        	BufferedReader bufferedReader = new BufferedReader(fileReader);
             while ((line = bufferedReader.readLine()) != null)
             {
            	 String[] values = line.split(",");
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stock",values[1]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"date",values[2]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stock_price_open",values[3]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stoc_price_high",values[4]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stock_price_low",values[5]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stock_price_close",values[6]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stock_volume",values[7]);
                 addColumnEntry(tableName,values[1] + "-" + values[2],colFamilyNames,"stock_price_adj_close",values[8]);
              }
            bufferedReader.close();
        	} 
        catch (FileNotFoundException ex) 
        {
              System.out.println("Unable to open file " + fileName);
        } 
        catch (IOException ex)
        {
              System.out.println("Error reading file " + fileName);
        }
        
       getAllRecord(tableName);
    }

}

