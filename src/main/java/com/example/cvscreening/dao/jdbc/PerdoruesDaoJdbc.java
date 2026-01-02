package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.PerdoruesDao;
import com.example.cvscreening.model.Perdorues;

import java.sql.*;
import java.util.Optional;

public class PerdoruesDaoJdbc implements PerdoruesDao {

    private final Connection conn;

    public PerdoruesDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Perdorues p) {
        String sql = """
      INSERT INTO perdorues
      (emri, mbiemri, adresa_email, fjalekalimi, roli, data_regjistrimit, gjendja)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getEmri());
            ps.setString(2, p.getMbiemri());
            ps.setString(3, p.getAdresaEmail());
            ps.setString(4, p.getFjalekalimi());
            ps.setString(5, p.getRoli());
            ps.setDate(6, p.getDataRegjistrimit() == null ? null : Date.valueOf(p.getDataRegjistrimit()));
            ps.setString(7, p.getGjendja());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create perdorues failed", e);
        }
    }

    @Override
    public Optional<Perdorues> findById(int id) {
        String sql = "SELECT * FROM perdorues WHERE id_perdorues = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find by id failed", e);
        }
    }

    private Perdorues map(ResultSet rs) throws SQLException {
        Perdorues p = new Perdorues();
        p.setIdPerdorues(rs.getInt("id_perdorues"));
        p.setEmri(rs.getString("emri"));
        p.setMbiemri(rs.getString("mbiemri"));
        p.setAdresaEmail(rs.getString("adresa_email"));
        p.setFjalekalimi(rs.getString("fjalekalimi"));
        p.setRoli(rs.getString("roli"));
        p.setGjendja(rs.getString("gjendja"));

        Date d = rs.getDate("data_regjistrimit");
        if (d != null) p.setDataRegjistrimit(d.toLocalDate());
        return p;
    }

    @Override
    public Optional<Perdorues> findByEmail(String email) {
        String sql = "SELECT * FROM perdorues WHERE adresa_email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find perdorues by email failed", e);
        }
    }

    @Override
    public boolean updatePassword(int idPerdorues, String newPassword) {
        String sql = "UPDATE perdorues SET fjalekalimi = ? WHERE id_perdorues = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, idPerdorues);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update password failed", e);
        }
    }
}
