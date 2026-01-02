package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.CvDao;
import com.example.cvscreening.model.Cv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CvDaoJdbc implements CvDao {

    private final Connection conn;

    public CvDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Cv cv) {
        String sql = """
      INSERT INTO cv
      (id_kandidat, emri_skedarit, formati_skedarit, madhesia_kb, data_ngarkimit, version, perputhshmeria_me_pozicionin)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cv.getIdKandidat());
            ps.setString(2, cv.getEmriSkedarit());
            ps.setString(3, cv.getFormatiSkedarit());

            if (cv.getMadhesiaKb() == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, cv.getMadhesiaKb());

            ps.setDate(5, cv.getDataNgarkimit() == null ? null : Date.valueOf(cv.getDataNgarkimit()));
            ps.setString(6, cv.getVersion());

            if (cv.getPerputhshmeriaMePozicionin() == null) ps.setNull(7, Types.DOUBLE);
            else ps.setDouble(7, cv.getPerputhshmeriaMePozicionin());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create cv failed", e);
        }
    }

    @Override
    public Optional<Cv> findById(int idCv) {
        String sql = "SELECT * FROM cv WHERE id_cv = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCv);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find cv by id failed", e);
        }
    }

    @Override
    public List<Cv> findByKandidatId(int idKandidat) {
        String sql = "SELECT * FROM cv WHERE id_kandidat = ? ORDER BY id_cv";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKandidat);
            try (ResultSet rs = ps.executeQuery()) {
                List<Cv> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find cv by kandidat id failed", e);
        }
    }

    private Cv map(ResultSet rs) throws SQLException {
        Cv cv = new Cv();
        cv.setIdCv(rs.getInt("id_cv"));
        cv.setIdKandidat(rs.getInt("id_kandidat"));
        cv.setEmriSkedarit(rs.getString("emri_skedarit"));
        cv.setFormatiSkedarit(rs.getString("formati_skedarit"));

        int mkb = rs.getInt("madhesia_kb");
        cv.setMadhesiaKb(rs.wasNull() ? null : mkb);

        Date d = rs.getDate("data_ngarkimit");
        if (d != null) cv.setDataNgarkimit(d.toLocalDate());

        cv.setVersion(rs.getString("version"));

        double p = rs.getDouble("perputhshmeria_me_pozicionin");
        cv.setPerputhshmeriaMePozicionin(rs.wasNull() ? null : p);

        return cv;
    }

    @Override
    public boolean updateMatchScore(int idCv, double score) {
        String sql = "UPDATE cv SET perputhshmeria_me_pozicionin = ? WHERE id_cv = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, score);
            ps.setInt(2, idCv);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update cv match score failed", e);
        }
    }

}
