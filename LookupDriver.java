import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.w3c.dom.Text;

import java.io.IOException;

public class LookupDriver {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: LookupDriver <input path> <output path> <target id>");
            System.exit(-1);
        }
        Configuration conf = new Configuration();
        conf.set("target.id", args[2]);
        try {
            Job job = Job.getInstance(conf, "Lookup Job");
            job.setJarByClass(LookupDriver.class);
            job.setMapperClass(LookupMapper.class);
            job.setReducerClass(LookupReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            job.setNumReduceTasks(1);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}