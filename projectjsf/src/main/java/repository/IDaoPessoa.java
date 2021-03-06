package repository;

import java.util.List;

import javax.faces.model.SelectItem;

import entidades.Pessoa;

public interface IDaoPessoa {
	
	Pessoa consultarUsuario(String login, String senha);

	/*Para o combobox o formato de lista deve ser SelectItem*/
	List<SelectItem> listaEstados();
}
