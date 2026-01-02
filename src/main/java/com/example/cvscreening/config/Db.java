package com.example.cvscreening.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class Db {

    private Db() {}

    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                    "sa",
                    ""
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    public static void runSchema(Connection conn) {
        try {
            InputStream in =
                    Db.class.getClassLoader().getResourceAsStream("schema.sql");

            String sql =
                    new String(in.readAllBytes(), StandardCharsets.UTF_8);

            try (Statement st = conn.createStatement()) {
                for (String s : sql.split(";")) {
                    if (!s.trim().isEmpty()) {
                        st.execute(s);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to run schema", e);
        }
    }
}
