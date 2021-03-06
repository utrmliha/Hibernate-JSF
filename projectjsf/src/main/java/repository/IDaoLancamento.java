package repository;

import java.util.List;

import entidades.Lancamento;

public interface IDaoLancamento {

	List<Lancamento> consultar(Long codUser);
}
