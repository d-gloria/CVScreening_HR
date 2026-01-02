package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.PozicionPuneDao;
import com.example.cvscreening.model.PozicionPune;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PozicionPuneDaoJdbc implements PozicionPuneDao {

    private final Connection conn;

    public PozicionPuneDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(PozicionPune p) {
        String sql = """
      INSERT INTO pozicion_pune
      (titulli_pozicionit, pershkrimi_detyra, departamenti, data_hapjes, data_aplikimit,
       kriteret, gjendja, krijuar_nga)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getTitulliPozicionit());
            ps.setString(2, p.getPershkrimiDetyra());
            ps.setString(3, p.getDepartamenti());
            ps.setDate(4, p.getDataHapjes() == null ? null : Date.valueOf(p.getDataHapjes()));
            ps.setDate(5, p.getDataAplikimit() == null ? null : Date.valueOf(p.getDataAplikimit()));
            ps.setString(6, p.getKriteret());
            ps.setString(7, p.getGjendja());
            ps.setInt(8, p.getKrijuarNga());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create pozicion_pune failed", e);
        }
    }

    @Override
    public Optional<PozicionPune> findById(int idPozicion) {
        String sql = "SELECT * FROM pozicion_pune WHERE id_pozicion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPozicion);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find pozicion_pune by id failed", e);
        }
    }

    @Override
    public List<PozicionPune> findAll() {
        String sql = "SELECT * FROM pozicion_pune ORDER BY id_pozicion";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<PozicionPune> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("FindAll pozicion_pune failed", e);
        }
    }

    private PozicionPune map(ResultSet rs) throws SQLException {
        PozicionPune p = new PozicionPune();
        p.setIdPozicion(rs.getInt("id_pozicion"));
        p.setTitulliPozicionit(rs.getString("titulli_pozicionit"));
        p.setPershkrimiDetyra(rs.getString("pershkrimi_detyra"));
        p.setDepartamenti(rs.getString("departamenti"));

        Date dh = rs.getDate("data_hapjes");
        if (dh != null) p.setDataHapjes(dh.toLocalDate());

        Date da = rs.getDate("data_aplikimit");
        if (da != null) p.setDataAplikimit(da.toLocalDate());

        p.setKriteret(rs.getString("kriteret"));
        p.setGjendja(rs.getString("gjendja"));
        p.setKrijuarNga(rs.getInt("krijuar_nga"));
        return p;
    }
}
