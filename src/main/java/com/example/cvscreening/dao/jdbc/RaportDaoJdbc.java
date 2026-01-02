package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.RaportDao;
import com.example.cvscreening.model.Raport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RaportDaoJdbc implements RaportDao {

    private final Connection conn;

    public RaportDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Raport r) {
        String sql = """
      INSERT INTO raport
      (id_rekrutues, data_gjenerimit, lloji_raportit, pershkrimi, formati_raportit)
      VALUES (?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdRekrutues());
            ps.setDate(2, r.getDataGjenerimit() == null ? null : Date.valueOf(r.getDataGjenerimit()));
            ps.setString(3, r.getLlojiRaportit());
            ps.setString(4, r.getPershkrimi());
            ps.setString(5, r.getFormatiRaportit());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create raport failed", e);
        }
    }

    @Override
    public Optional<Raport> findById(int idRaport) {
        String sql = "SELECT * FROM raport WHERE id_raport = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRaport);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find raport by id failed", e);
        }
    }

    @Override
    public List<Raport> findByRekrutuesId(int idRekrutues) {
        String sql = "SELECT * FROM raport WHERE id_rekrutues = ? ORDER BY id_raport";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRekrutues);
            try (ResultSet rs = ps.executeQuery()) {
                List<Raport> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find raport by rekrutues id failed", e);
        }
    }

    private Raport map(ResultSet rs) throws SQLException {
        Raport r = new Raport();
        r.setIdRaport(rs.getInt("id_raport"));
        r.setIdRekrutues(rs.getInt("id_rekrutues"));

        Date d = rs.getDate("data_gjenerimit");
        if (d != null) r.setDataGjenerimit(d.toLocalDate());

        r.setLlojiRaportit(rs.getString("lloji_raportit"));
        r.setPershkrimi(rs.getString("pershkrimi"));
        r.setFormatiRaportit(rs.getString("formati_raportit"));
        return r;
    }
}
