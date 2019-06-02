package com.test.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordSizeDriver {

	public static void main(String args[])throws IOException,InterruptedException,ClassNotFoundException
	{
		//Specify Path parameter
		Path input = new Path(args[0]);
		Path output = new Path(args[1]);
		
		//Create a new job
		Job job= new Job(new Configuration());
		job.setJarByClass(WordSizeDriver.class);
		job.setJobName("WordSize");
		
		//Set mapper-reducer class
		job.setMapperClass(WordSizeMapper.class);
		job.setReducerClass(WordSizeReducer.class);
		
		//Specify Key-value class
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		
		//Specify input-output format class
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		//Specify file path
		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job,output);

		int code=job.waitForCompletion(true)?0:1;
		System.exit(code);
	}
}
