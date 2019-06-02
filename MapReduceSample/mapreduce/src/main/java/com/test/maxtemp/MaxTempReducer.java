package com.test.maxtemp;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTempReducer extends
Reducer<Text, IntWritable, Text, IntWritable> {

public void reduce(Text text, Iterable<IntWritable> values, Context context)
    throws IOException, InterruptedException {
int maxTemperature = 0;
for (IntWritable value : values) {
    if(maxTemperature<value.get())
    	maxTemperature = value.get();
}
context.write(text, new IntWritable(maxTemperature));
}
}
