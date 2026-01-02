package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.IntervisteDao;
import com.example.cvscreening.model.Interviste;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntervisteDaoJdbc implements IntervisteDao {

    private final Connection conn;

    public IntervisteDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Interviste i) {
        String sql = """
      INSERT INTO interviste
      (id_aplikimi, data_intervistes, ora, vendi, vleresimi_rekrutuesit, pershtypja, rezultati)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdAplikimi());
            ps.setDate(2, i.getDataIntervistes() == null ? null : Date.valueOf(i.getDataIntervistes()));
            ps.setString(3, i.getOra());
            ps.setString(4, i.getVendi());

            if (i.getVleresimiRekrutuesit() == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, i.getVleresimiRekrutuesit());

            ps.setString(6, i.getPershtypja());
            ps.setString(7, i.getRezultati());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create interviste failed", e);
        }
    }

    @Override
    public Optional<Interviste> findById(int idInterviste) {
        String sql = "SELECT * FROM interviste WHERE id_interviste = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idInterviste);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find interviste by id failed", e);
        }
    }

    @Override
    public List<Interviste> findByAplikimId(int idAplikimi) {
        String sql = "SELECT * FROM interviste WHERE id_aplikimi = ? ORDER BY id_interviste";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAplikimi);
            try (ResultSet rs = ps.executeQuery()) {
                List<Interviste> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find interviste by aplikim id failed", e);
        }
    }

    private Interviste map(ResultSet rs) throws SQLException {
        Interviste i = new Interviste();
        i.setIdInterviste(rs.getInt("id_interviste"));
        i.setIdAplikimi(rs.getInt("id_aplikimi"));

        Date d = rs.getDate("data_intervistes");
        if (d != null) i.setDataIntervistes(d.toLocalDate());

        i.setOra(rs.getString("ora"));
        i.setVendi(rs.getString("vendi"));

        int v = rs.getInt("vleresimi_rekrutuesit");
        i.setVleresimiRekrutuesit(rs.wasNull() ? null : v);

        i.setPershtypja(rs.getString("pershtypja"));
        i.setRezultati(rs.getString("rezultati"));
        return i;
    }
    @Override
    public boolean updateRezultat(int idInterviste, String rezultat, Integer vleresimRekrutuesit, String pershtypja) {
        String sql = "UPDATE interviste SET rezultati = ?, vleresimi_rekrutuesit = ?, pershtypja = ? WHERE id_interviste = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rezultat);

            if (vleresimRekrutuesit == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, vleresimRekrutuesit);

            ps.setString(3, pershtypja);
            ps.setInt(4, idInterviste);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Update interviste rezultat failed", e);
        }
    }

}
