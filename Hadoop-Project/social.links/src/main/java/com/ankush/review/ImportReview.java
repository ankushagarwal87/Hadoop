package com.ankush.review;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.mahout.classifier.bayes.XmlInputFormat;

import com.ankush.review.util.WordUtil;

public class ImportReview {

	static String DELIMITER = ",";

	public static class SocialLinksMap extends Mapper<LongWritable, Text, Text, Text> {

		// 1, "one tow three" -> one, 1 and two ,1 and three,1
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException {
			
			XMLParser xP;
			try {
				xP = new XMLParser(value.toString());
				
				List<String> categories = xP.getCategory();
				List<String> reviewLists = xP.getReviews();
				int postive = 0;
				int negative = 0;

				for (String eachReview : reviewLists) {
					if (BadWords.isBad(eachReview)) {
						negative++;
					} else {
						postive++;
					}
				}

				for (String eachCat : categories) {
					String mapOutput = WordUtil.cleanWords(eachCat) + DELIMITER + xP.getHash() + DELIMITER + xP.getUrl()
							+ DELIMITER + postive + DELIMITER + negative + DELIMITER + xP.getUsercount();
					System.out.println(" output " + mapOutput);
					context.write(new Text(""), new Text(mapOutput));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("");
			}
		}

	}

	public static void main(String[] args) throws Exception {

		Configuration config = new Configuration(true);
		config.set(XmlInputFormat.START_TAG_KEY, "<document>");
		config.set(XmlInputFormat.END_TAG_KEY, "</document>");
		@SuppressWarnings("deprecation")
		Job conf = new Job(config, "ImportReview");
		conf.setJarByClass(ImportReview.class);
		//conf.set("fs.default.name", "hdfs://localhost:50070");

		conf.setJobName("importreview");
		conf.setNumReduceTasks(0);
		conf.setMapperClass(SocialLinksMap.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		// how the data will be read
		conf.setInputFormatClass(XmlInputFormat.class);
		conf.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		 // Execute job
        int code = conf.waitForCompletion(true) ? 0 : 1;
        System.exit(code);

	}
}
