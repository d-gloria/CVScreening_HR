package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.RegjisterAuditiDao;
import com.example.cvscreening.model.RegjisterAuditi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegjisterAuditiDaoJdbc implements RegjisterAuditiDao {

    private final Connection conn;

    public RegjisterAuditiDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(RegjisterAuditi a) {
        String sql = """
      INSERT INTO regjister_auditi
      (id_perdorues, objekti, veprimi, data_veprimit, ora_veprimit, pershkrimi_detajuar)
      VALUES (?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdPerdorues());
            ps.setString(2, a.getObjekti());
            ps.setString(3, a.getVeprimi());
            ps.setDate(4, a.getDataVeprimit() == null ? null : Date.valueOf(a.getDataVeprimit()));
            ps.setString(5, a.getOraVeprimit());
            ps.setString(6, a.getPershkrimiDetajuar());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create audit failed", e);
        }
    }

    @Override
    public Optional<RegjisterAuditi> findById(int idVeprimi) {
        String sql = "SELECT * FROM regjister_auditi WHERE id_veprimi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVeprimi);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find audit by id failed", e);
        }
    }

    @Override
    public List<RegjisterAuditi> findByPerdoruesId(int idPerdorues) {
        String sql = "SELECT * FROM regjister_auditi WHERE id_perdorues = ? ORDER BY id_veprimi";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPerdorues);
            try (ResultSet rs = ps.executeQuery()) {
                List<RegjisterAuditi> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find audit by perdorues id failed", e);
        }
    }

    private RegjisterAuditi map(ResultSet rs) throws SQLException {
        RegjisterAuditi a = new RegjisterAuditi();
        a.setIdVeprimi(rs.getInt("id_veprimi"));
        a.setIdPerdorues(rs.getInt("id_perdorues"));
        a.setObjekti(rs.getString("objekti"));
        a.setVeprimi(rs.getString("veprimi"));

        Date d = rs.getDate("data_veprimit");
        if (d != null) a.setDataVeprimit(d.toLocalDate());

        a.setOraVeprimit(rs.getString("ora_veprimit"));
        a.setPershkrimiDetajuar(rs.getString("pershkrimi_detajuar"));
        return a;
    }
}
