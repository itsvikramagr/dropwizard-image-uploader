package com.demo.uploader.core;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PhotoMapper implements ResultSetMapper<Photo> {
    public Photo map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        Photo p = new Photo();

        p.setId(r.getLong("id"));
        p.setLength(r.getInt("length"));
        p.setWidth(r.getInt("width"));
        p.setSize(r.getLong("size"));
        p.setFileName(r.getString("filename"));        
        p.setCreatedAt(convertToDateTime(r.getTimestamp("created_at")));
        p.setUpdatedAt(convertToDateTime(r.getTimestamp("updated_at")));

        return p;
    }

    private DateTime convertToDateTime(Timestamp ts) {
        return ts == null ? null : new DateTime(ts).withZone(DateTimeZone.UTC);
    }
}
