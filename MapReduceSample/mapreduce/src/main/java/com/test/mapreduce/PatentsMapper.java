package com.test.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PatentsMapper extends Mapper<Object,Text,IntWritable,IntWritable>{
	private IntWritable patentId;
	private final IntWritable subPatentId = new IntWritable(1);
	
	
	public void map(Object key,Text value, Context context) throws IOException, InterruptedException
	{
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line," "); 
		while (tokenizer.hasMoreTokens()) { 
			patentId= new IntWritable(Integer.parseInt(tokenizer.nextToken()));
			tokenizer.nextToken();
			context.write(patentId,subPatentId);
		} 
	}

}
