import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import java.io.IOException;
public class LookupMapper extends Mapper<LongWritable, Text, Text, Text> {
    private Text targetId;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");
        if (fields.length == 4) {
            String id = fields[0].trim();
            Text tgt = getTargetId();
            if (tgt != null && id.equals(tgt.toString())) {
            context.write(new Text(id), new Text(fields[1].trim() + "," + fields[2].trim() + "," + fields[3].trim()));
            }
        }
    }
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        String tgt = context.getConfiguration().get("target.id");
        if (tgt != null) {
            setTargetId(new Text(tgt));
        }
    }
    public Text getTargetId() {
        return targetId;
    }
    public void setTargetId(Text targetId) {
        this.targetId = targetId;
    }
    
}
