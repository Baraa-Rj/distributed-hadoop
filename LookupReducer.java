import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class LookupReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long bestTs = -1;
        String bestPayload = null;
        for (Text value : values) {
            String[] parts = value.toString().split(",");
            if (parts.length != 3) {
                continue; 
            }
            long ts = Long.parseLong(parts[2]);
            String payload = parts[0] + "," + parts[1];

            if (ts > bestTs) {
                bestTs = ts;
                bestPayload = payload;
            }
        }
        if (bestPayload != null) {
            context.write(key, new Text(bestPayload));
        }
    }

}
