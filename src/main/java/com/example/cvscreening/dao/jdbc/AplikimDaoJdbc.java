package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.AplikimDao;
import com.example.cvscreening.model.Aplikim;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AplikimDaoJdbc implements AplikimDao {

    private final Connection conn;

    public AplikimDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Aplikim a) {
        String sql = """
      INSERT INTO aplikim
      (id_kandidat, id_pozicion, id_cv, data_aplikimit, vleresimi, gjendja, komentet_rekrutuesit)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdKandidat());
            ps.setInt(2, a.getIdPozicion());
            ps.setInt(3, a.getIdCv());
            ps.setDate(4, a.getDataAplikimit() == null ? null : Date.valueOf(a.getDataAplikimit()));

            if (a.getVleresimi() == null) ps.setNull(5, Types.DOUBLE);
            else ps.setDouble(5, a.getVleresimi());

            ps.setString(6, a.getGjendja());
            ps.setString(7, a.getKomentetRekrutuesit());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create aplikim failed", e);
        }
    }

    @Override
    public Optional<Aplikim> findById(int idAplikimi) {
        String sql = "SELECT * FROM aplikim WHERE id_aplikimi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAplikimi);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find aplikim by id failed", e);
        }
    }

    @Override
    public List<Aplikim> findByKandidatId(int idKandidat) {
        String sql = "SELECT * FROM aplikim WHERE id_kandidat = ? ORDER BY id_aplikimi";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKandidat);
            try (ResultSet rs = ps.executeQuery()) {
                List<Aplikim> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find aplikim by kandidat id failed", e);
        }
    }

    @Override
    public List<Aplikim> findByPozicionId(int idPozicion) {
        String sql = "SELECT * FROM aplikim WHERE id_pozicion = ? ORDER BY id_aplikimi";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPozicion);
            try (ResultSet rs = ps.executeQuery()) {
                List<Aplikim> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find aplikim by pozicion id failed", e);
        }
    }

    private Aplikim map(ResultSet rs) throws SQLException {
        Aplikim a = new Aplikim();
        a.setIdAplikimi(rs.getInt("id_aplikimi"));
        a.setIdKandidat(rs.getInt("id_kandidat"));
        a.setIdPozicion(rs.getInt("id_pozicion"));
        a.setIdCv(rs.getInt("id_cv"));

        Date d = rs.getDate("data_aplikimit");
        if (d != null) a.setDataAplikimit(d.toLocalDate());

        double v = rs.getDouble("vleresimi");
        a.setVleresimi(rs.wasNull() ? null : v);

        a.setGjendja(rs.getString("gjendja"));
        a.setKomentetRekrutuesit(rs.getString("komentet_rekrutuesit"));
        return a;
    }
    @Override
    public boolean updateStatusAndComment(int idAplikimi, String gjendja, String komentetRekrutuesit) {
        String sql = "UPDATE aplikim SET gjendja = ?, komentet_rekrutuesit = ? WHERE id_aplikimi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gjendja);
            ps.setString(2, komentetRekrutuesit);
            ps.setInt(3, idAplikimi);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update aplikim status/comment failed", e);
        }
    }

    @Override
    public boolean updateScore(int idAplikimi, double score) {
        String sql = "UPDATE aplikim SET vleresimi = ? WHERE id_aplikimi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, score);
            ps.setInt(2, idAplikimi);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update aplikim score failed", e);
        }
    }

}