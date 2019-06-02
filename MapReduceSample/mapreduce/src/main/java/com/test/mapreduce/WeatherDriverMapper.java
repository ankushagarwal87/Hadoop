package com.test.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WeatherDriverMapper extends Mapper<Object,Text,Text,Text>{
	private Text maxTempMap = new Text();
	private Text minTempMap = new Text();
	private Text HOT_DAY= new Text("HOT DAY");
	private Text COOL_DAY= new Text("COOL DAY");
	
	public void map(Object key,Text value, Context context) throws IOException, InterruptedException
	{
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line," "); 
		while (tokenizer.hasMoreTokens()) { 
			tokenizer.nextToken();
			String date= tokenizer.nextToken();
			tokenizer.nextToken();
			tokenizer.nextToken();
			tokenizer.nextToken();
			String maxTemp= tokenizer.nextToken().trim();
			String minTemp= tokenizer.nextToken().trim();
			maxTempMap.set(date+" "+maxTemp);
			minTempMap.set(date+" "+minTemp);
			context.write(HOT_DAY,maxTempMap);
			context.write(COOL_DAY,minTempMap);
			tokenizer.nextToken("/n");
		} 
	}

}
