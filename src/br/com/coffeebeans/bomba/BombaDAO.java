package br.com.coffeebeans.bomba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.coffeebeans.exception.BombaNaoEncontradaException;
import br.com.coffeebeans.util.Conexao;

public class BombaDAO implements IBombaDAO {
	private Connection connection = null;
	private String sistema = "mysql";

	public BombaDAO() throws Exception {
		this.connection = Conexao.conectar(sistema);
	}

	public void cadastrar(Bomba bomba) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int id = 0;
		try {
			String sql = "INSERT INTO BOMBA (DESCRICAO,STATUS,POTENCIA,VAZAO,ACIONAMENTO,ID_REPOSITORIO_ENC,ID_REPOSITORIO_SEC)"
					+ "VALUES(?,?,?,?,?,?,?)";
			stmt = this.connection.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, bomba.getDescricao());
			stmt.setString(2, bomba.getStatus());
			stmt.setDouble(3, bomba.getPotencia());
			stmt.setDouble(4, bomba.getVazao());
			stmt.setString(5, bomba.getAcionamento());
			stmt.setInt(6, bomba.getIdRepositorioEnche());
			stmt.setInt(7, bomba.getIdRepositorioSeca());

			stmt.execute();

			if (sistema.equals("mysql")) {
				rs = stmt.getGeneratedKeys();
			}

			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
			rs.close();
		}
	}

	public ArrayList<Bomba> listar() throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Bomba bomba = null;
		ArrayList<Bomba> bombas = new ArrayList<Bomba>();
		try {
			String sql = "SELECT * FROM BOMBA";
			stmt = this.connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				bomba = new Bomba(rs.getString("DESCRICAO"),
						rs.getString("STATUS"), rs.getDouble("POTENCIA"),
						rs.getDouble("VAZAO"), rs.getString("ACIONAMENTO"),
						rs.getInt("ID_REPOSITORIO_ENC"));
				bomba.setCodigo(rs.getInt("ID"));
				bomba.setIdRepositorioSeca(rs.getInt("ID_REPOSITORIO_SEC"));
				bombas.add(bomba);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
			rs.close();
		}
		return bombas;
	}

	public Bomba procurar(String descricao) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Bomba bomba = null;
		try {
			String sql = "SELECT * FROM BOMBA WHERE DESCRICAO=?";
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, descricao);
			rs = stmt.executeQuery();

			while (rs.next()) {
				bomba = new Bomba(rs.getString("DESCRICAO"),
						rs.getString("STATUS"), rs.getDouble("POTENCIA"),
						rs.getDouble("VAZAO"), rs.getString("ACIONAMENTO"),
						rs.getInt("ID_REPOSITORIO_ENC"));
				bomba.setCodigo(rs.getInt("ID"));
				bomba.setIdRepositorioSeca(rs.getInt("ID_REPOSITORIO_SEC"));

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
			rs.close();
		}
		return bomba;
	}

	public Bomba procurar(int id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Bomba bomba = null;
		try {
			String sql = "SELECT * FROM BOMBA WHERE ID=?";
			stmt = this.connection.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				bomba = new Bomba(rs.getString("DESCRICAO"),
						rs.getString("STATUS"), rs.getDouble("POTENCIA"),
						rs.getDouble("VAZAO"), rs.getString("ACIONAMENTO"),
						rs.getInt("ID_REPOSITORIO_ENC"));
				bomba.setCodigo(rs.getInt("ID"));
				bomba.setIdRepositorioSeca(rs.getInt("ID_REPOSITORIO_SEC"));

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
			rs.close();
		}
		return bomba;
	}

	public void atualizar(Bomba bomba) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE BOMBA SET DESCRICAO=?,STATUS=?,POTENCIA=?,VAZAO=?,ACIONAMENTO=?,ID_REPOSITORIO_ENC=?,ID_REPOSITORIO_SEC=? WHERE ID=?";
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, bomba.getDescricao());
			stmt.setString(2, bomba.getStatus());
			stmt.setDouble(3, bomba.getPotencia());
			stmt.setDouble(4, bomba.getVazao());
			stmt.setString(5, bomba.getAcionamento());
			stmt.setInt(6, bomba.getIdRepositorioEnche());
			stmt.setInt(7, bomba.getIdRepositorioSeca());
			stmt.setInt(8, bomba.getCodigo());

			Integer resultado = stmt.executeUpdate();
			if (resultado == 0) {
				throw new BombaNaoEncontradaException();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
		}
	}

	public void excluir(int id) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = "DELETE FROM BOMBA WHERE ID=?";
			stmt = this.connection.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
		}
	}
}