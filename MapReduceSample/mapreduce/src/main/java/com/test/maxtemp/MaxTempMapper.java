package com.test.maxtemp;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTempMapper extends
Mapper<Object, Text, Text, IntWritable> {

private IntWritable temperature = new IntWritable();
private Text word = new Text();

public void map(Object key, Text value, Context context)
    throws IOException, InterruptedException {

String line = value.toString();
StringTokenizer tokenizer = new StringTokenizer(line," "); 
while (tokenizer.hasMoreTokens()) { 
	String year= tokenizer.nextToken();
	word.set(year);
	String temp= tokenizer.nextToken().trim();
	temperature.set(Integer.parseInt(temp)); 
	context.write(word,temperature);
} 
}
}
