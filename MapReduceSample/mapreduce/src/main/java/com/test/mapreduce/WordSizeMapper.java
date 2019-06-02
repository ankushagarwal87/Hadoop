package com.test.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordSizeMapper extends Mapper<Object,Text,IntWritable,IntWritable> 
{
	private final IntWritable ONE=new IntWritable(1);
	private IntWritable wordLength;
	
	public void map(Object key,Text value,Context context)throws IOException,InterruptedException
	{
		String line=value.toString();
		StringTokenizer tokenizer=new StringTokenizer(line," ");
		while(tokenizer.hasMoreTokens())
		{
			String word=tokenizer.nextToken();
			wordLength =new IntWritable(word.length());
			context.write(wordLength, ONE);
		}
	}
}
