package br.com.coffeebeans.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.coffeebeans.exception.ListaUsuarioVaziaException;
import br.com.coffeebeans.exception.UsuarioJaExistenteException;
import br.com.coffeebeans.exception.UsuarioNaoEncontradoException;
import br.com.coffeebeans.util.Conexao;

/*-----------------------------------------------------------------------
 *				 	M�TODOS A SEREM IMPLEMENTADOS------------------------
 */
public class UsuarioDAO implements IUsuarioDAO {
	private Connection connection = null;
	private String sistema = "mysql";

	public UsuarioDAO() throws Exception {
		this.connection = Conexao.conectar(sistema);
	}

	@Override
	public void cadastrar(Usuario usuario) throws SQLException,
			UsuarioJaExistenteException {
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO USUARIO VALUES (?, ?, ?, ?, ?, ?)";
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getLogin());
			stmt.setString(3, usuario.getSenha());
			stmt.setString(4, usuario.getEmail());
			stmt.setString(5, usuario.getAtivo());
			stmt.setString(6, usuario.getPerfil());
			stmt.execute();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			stmt.close();
		}
	}

	@Override
	public ArrayList<Usuario> listar() throws SQLException,
			ListaUsuarioVaziaException {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM USUARIO";
			stmt = this.connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario(rs.getString("NOME"),
						rs.getString("LOGIN"), rs.getString("SENHA"),
						rs.getString("EMAIL"), rs.getString("ATIVO"),
						rs.getString("PERFIL"));
				usuario.setId(rs.getInt("ID"));
				usuarios.add(usuario);
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		} finally {
			stmt.close();
		}
		return usuarios;
	}

	@Override
	public Usuario procurar(int id) throws SQLException {
		Usuario usuario = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM USUARIO WHERE ID = ?";
			stmt = this.connection.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			
			usuario = new Usuario (rs.getString("NOME"), rs.getString("LOGIN"), rs.getString("SENHA"),
					rs.getString("EMAIL"), rs.getString("ATIVO"), rs.getString("PERFIL"));
			usuario.setId(rs.getInt("ID"));
		} catch (SQLException e) {
			
		} finally {
			stmt.close();
			rs.close();
		}
		return usuario;
	}

	@Override
	public void atualizar(Usuario usuario)
			throws UsuarioNaoEncontradoException, SQLException {

	}

	@Override
	public void excluir(int id) throws SQLException,
			UsuarioNaoEncontradoException {
		PreparedStatement stmt = null;
		try {
			String sql = "DELETE FROM USUARIO WHERE ID = ?";
			stmt = this.connection.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
		} catch (SQLException e) {
			
		} finally {
			stmt.close();
		}

	}

}