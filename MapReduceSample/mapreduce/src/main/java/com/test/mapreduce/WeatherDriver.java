package com.test.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WeatherDriver {

	public static void main(String[] args) throws IOException,InterruptedException,ClassNotFoundException
	{
		//Specify path parameter
		Path inputPath = new Path(args[0]);
		Path outputPath = new Path(args[1]);
		
		//Create a new Job
		@SuppressWarnings("deprecation")
		Job job = new Job(new Configuration());
		job.setJarByClass(WeatherDriver.class);
		
		//Specify various Job Specific parameter
		job.setJobName("Weather Driver");
		
		//Set Mapper-Reducer Class
		job.setMapperClass(WeatherDriverMapper.class);
		job.setReducerClass(WeatherDriverReducer.class);
		
		//Specify Key-value 
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//Input
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, inputPath);
		
		//Output
		job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job,outputPath);
		
		int code=job.waitForCompletion(true)?0:1;
		System.exit(code);
	}
}
