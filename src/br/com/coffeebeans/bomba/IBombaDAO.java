package br.com.coffeebeans.bomba;

import java.sql.SQLException;
import java.util.ArrayList;

import br.com.coffeebeans.exception.BombaJaExistenteException;
import br.com.coffeebeans.exception.BombaNaoEncontradaException;
import br.com.coffeebeans.exception.ListaVaziaException;

public interface IBombaDAO {
	public void cadastrar(Bomba bomba) throws SQLException,
			BombaJaExistenteException;

	public ArrayList<Bomba> listar() throws SQLException, ListaVaziaException;

	public Bomba procurar(int id) throws SQLException,
			BombaNaoEncontradaException;

	public void atualizar(Bomba bomba) throws BombaNaoEncontradaException,
			SQLException;

	public void excluir(int id) throws SQLException,
			BombaNaoEncontradaException;

	public Bomba procurar(String descricao) throws SQLException,
			BombaNaoEncontradaException;

}