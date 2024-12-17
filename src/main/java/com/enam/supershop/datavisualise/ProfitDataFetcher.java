package com.enam.supershop.datavisualise;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProfitDataFetcher {

    public List<ProfitData> getProfitDataForToday() throws SQLException {
        List<ProfitData> profitDataList = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/your_database_name";
        String username = "your_username";
        String password = "your_password";

        // Get today's date in the format YYYY-MM-DD
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query = "SELECT amountTaka, date FROM sold WHERE DATE(date) = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, todayDate);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double amountTaka = rs.getDouble("amountTaka");
                Timestamp timestamp = rs.getTimestamp("datetime");

                profitDataList.add(new ProfitData(amountTaka, timestamp.toLocalDateTime()));
            }
        }

        return profitDataList;
    }

    public static class ProfitData {
        double amountTaka;
        LocalDateTime dateTime;

        public ProfitData(double amountTaka, LocalDateTime dateTime) {
            this.amountTaka = amountTaka;
            this.dateTime = dateTime;
        }

        public double getAmountTaka() {
            return amountTaka;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }
    }
}

