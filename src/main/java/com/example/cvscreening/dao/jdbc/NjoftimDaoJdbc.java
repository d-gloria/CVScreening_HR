package com.example.cvscreening.dao.jdbc;

import com.example.cvscreening.dao.NjoftimDao;
import com.example.cvscreening.model.Njoftim;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NjoftimDaoJdbc implements NjoftimDao {

    private final Connection conn;

    public NjoftimDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int create(Njoftim n) {
        String sql = """
      INSERT INTO njoftim
      (id_perdorues, menyra_dergimit, permbajtja, data_dergimit, statusi_leximit)
      VALUES (?, ?, ?, ?, ?)
      """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, n.getIdPerdorues());
            ps.setString(2, n.getMenyraDergimit());
            ps.setString(3, n.getPermbajtja());
            ps.setDate(4, n.getDataDergimit() == null ? null : Date.valueOf(n.getDataDergimit()));
            ps.setString(5, n.getStatusiLeximit());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("No generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Create njoftim failed", e);
        }
    }

    @Override
    public Optional<Njoftim> findById(int idNjoftim) {
        String sql = "SELECT * FROM njoftim WHERE id_njoftim = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNjoftim);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find njoftim by id failed", e);
        }
    }

    @Override
    public List<Njoftim> findByPerdoruesId(int idPerdorues) {
        String sql = "SELECT * FROM njoftim WHERE id_perdorues = ? ORDER BY id_njoftim";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPerdorues);
            try (ResultSet rs = ps.executeQuery()) {
                List<Njoftim> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find njoftim by perdorues id failed", e);
        }
    }

    private Njoftim map(ResultSet rs) throws SQLException {
        Njoftim n = new Njoftim();
        n.setIdNjoftim(rs.getInt("id_njoftim"));
        n.setIdPerdorues(rs.getInt("id_perdorues"));
        n.setMenyraDergimit(rs.getString("menyra_dergimit"));
        n.setPermbajtja(rs.getString("permbajtja"));

        Date d = rs.getDate("data_dergimit");
        if (d != null) n.setDataDergimit(d.toLocalDate());

        n.setStatusiLeximit(rs.getString("statusi_leximit"));
        return n;
    }
    @Override
    public boolean markAsRead(int idNjoftim) {
        String sql = "UPDATE njoftim SET statusi_leximit = ? WHERE id_njoftim = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "LEXUAR");
            ps.setInt(2, idNjoftim);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Mark njoftim as read failed", e);
        }
    }

}
