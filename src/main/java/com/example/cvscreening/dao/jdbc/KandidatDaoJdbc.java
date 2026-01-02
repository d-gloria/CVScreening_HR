package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.KandidatDao;
import com.example.cvscreening.model.Kandidat;

import java.sql.*;
import java.util.Optional;

public class KandidatDaoJdbc implements KandidatDao {

    private final Connection conn;

    public KandidatDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Kandidat k) {
        String sql = """
      INSERT INTO kandidat
      (id_perdorues, data_lindjes, gjinia, vendbanimi, numri_celular,
       niveli_arsimor, fusha_studimit, vite_eksperience, gjuhet_e_huaja, aftesite_profesionale)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, k.getIdPerdorues());
            ps.setDate(2, k.getDataLindjes() == null ? null : Date.valueOf(k.getDataLindjes()));
            ps.setString(3, k.getGjinia());
            ps.setString(4, k.getVendbanimi());
            ps.setString(5, k.getNumriCelular());
            ps.setString(6, k.getNiveliArsimor());
            ps.setString(7, k.getFushaStudimit());
            if (k.getViteEksperience() == null) ps.setNull(8, Types.INTEGER); else ps.setInt(8, k.getViteEksperience());
            ps.setString(9, k.getGjuhetEHuaja());
            ps.setString(10, k.getAftesiteProfesionale());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create kandidat failed", e);
        }
    }

    @Override
    public Optional<Kandidat> findById(int idKandidat) {
        String sql = "SELECT * FROM kandidat WHERE id_kandidat = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKandidat);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find kandidat by id failed", e);
        }
    }

    @Override
    public Optional<Kandidat> findByPerdoruesId(int idPerdorues) {
        String sql = "SELECT * FROM kandidat WHERE id_perdorues = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPerdorues);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find kandidat by perdorues id failed", e);
        }
    }

    private Kandidat map(ResultSet rs) throws SQLException {
        Kandidat k = new Kandidat();
        k.setIdKandidat(rs.getInt("id_kandidat"));
        k.setIdPerdorues(rs.getInt("id_perdorues"));

        Date d = rs.getDate("data_lindjes");
        if (d != null) k.setDataLindjes(d.toLocalDate());

        k.setGjinia(rs.getString("gjinia"));
        k.setVendbanimi(rs.getString("vendbanimi"));
        k.setNumriCelular(rs.getString("numri_celular"));
        k.setNiveliArsimor(rs.getString("niveli_arsimor"));
        k.setFushaStudimit(rs.getString("fusha_studimit"));

        int v = rs.getInt("vite_eksperience");
        if (rs.wasNull()) k.setViteEksperience(null); else k.setViteEksperience(v);

        k.setGjuhetEHuaja(rs.getString("gjuhet_e_huaja"));
        k.setAftesiteProfesionale(rs.getString("aftesite_profesionale"));
        return k;
    }
}