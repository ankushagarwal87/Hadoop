package com.test.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WeatherDriverReducer extends Reducer<Text,Text,Text,Text>{

	public void reduce(Text key,Iterable<Text>values,Context context) throws IOException, InterruptedException 
	{
		Text year=new Text();
		Text weather=new Text();
		if(key.toString().equals("HOT DAY"))
		{
			for(Text value:values)
			{
				String data = value.toString();
				String[] dateAndTemp = data.split(" ");
				String maxTemp=dateAndTemp[1];
				year.set(dateAndTemp[0]);
				weather.set(key.toString());
				if(Double.parseDouble(maxTemp)>40)
				{
					context.write(year,weather);
				}
			}	
		}
		if(key.toString().equals("COOL DAY"))
		{
			for(Text value:values)
			{
				String data = value.toString();
				String[] dateAndTemp = data.split(" ");
				String minTemp=dateAndTemp[1];
				year.set(dateAndTemp[0]);
				weather.set(key.toString());
				if(Double.parseDouble(minTemp)<10)
				{
					context.write(year,weather);
				}
			}	
		}
	}
}