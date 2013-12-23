package dbfit.util;

import java.util.*;
import java.sql.*;

public class DataRow {
    private Map<String, Object> values = new HashMap<String, Object>();
    private boolean processed = false;

    public Set<String> getColumnNames() {
        return values.keySet();
    }

    public DataRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
        for(int i = 1; i <= rsmd.getColumnCount(); i++) {
            Object val = rs.getObject(i);

            // Log.log("loading data from "+rsmd.getColumnName(i) +" = "+
            // val == null?"NULL":(val.getClass() + " " + val));
            values.put(
                    NameNormaliser.normaliseName(rsmd.getColumnLabel(i)),
                    DbParameterAccessor.normaliseValue(val));
        }
    }

    public String getStringValue(String columnName) {
        Object o = values.get(columnName);
        if (o == null) {
            return "null";
        }

        return o.toString();
    }

    public boolean matches(Map<String, Object> keyProperties) {
        for (String key: keyProperties.keySet()) {
            String normalisedKey = NameNormaliser.normaliseName(key);

            if (!values.containsKey(normalisedKey)) {
                return false;
            }

            if (!equals(keyProperties.get(key), values.get(normalisedKey))) {
                return false;
            }
        }

        return true;
    }

    private boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.equals(b);
    }

    public Object get(String key) {
        String normalisedKey = NameNormaliser.normaliseName(key);
        return values.get(normalisedKey);
    }

    public void markProcessed() {
        processed = true;
    }

    public boolean isProcessed() {
        return processed;
    }
}

