package com.test.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class WordSizeReducer extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{

	private int sum=0;
	public void reduce(IntWritable key,Iterable<IntWritable> values,Context context)throws IOException,InterruptedException
	{
		for (IntWritable value : values) {
		    sum += value.get();
		}
		context.write(key, new IntWritable(sum));
	}
}
