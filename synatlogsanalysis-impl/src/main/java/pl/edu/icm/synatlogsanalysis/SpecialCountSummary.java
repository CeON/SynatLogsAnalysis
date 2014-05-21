package pl.edu.icm.synatlogsanalysis;

import com.google.protobuf.InvalidProtocolBufferException;
import java.util.HashSet;
import java.util.Set;
import org.apache.hadoop.io.BytesWritable;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.StatisticCalculator;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class SpecialCountSummary implements StatisticCalculator {

    private static final String sessionIdLabel = "sessionId";

    @Override
    public double calculate(Iterable<BytesWritable> messages) {
        double result = 0.0;

        Set<String> sessions = new HashSet<String>();

        for (BytesWritable bw : messages) {
            try {
                StatisticsProtos.InputEntry inputEntry = StatisticsProtos.InputEntry.parseFrom(bw.copyBytes());
                String sessionId = getFieldValue(inputEntry, sessionIdLabel);
                if (!sessionId.isEmpty()) {
                    if (!sessions.contains(sessionId)) {
                        result += 1.0;
                        sessions.add(sessionId);
                    }
                } else {
                    result += 1.0;
                }
            } catch (InvalidProtocolBufferException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        return result;
    }

    @Override
    public void setup(String... params) {
    }

    private String getFieldValue(StatisticsProtos.InputEntry entry, String fieldName) {
        for (StatisticsProtos.KeyValue kv : entry.getFieldList()) {
            if (fieldName.equals(kv.getKey())) {
                return kv.getValue();
            }
        }
        return "";
    }
}
