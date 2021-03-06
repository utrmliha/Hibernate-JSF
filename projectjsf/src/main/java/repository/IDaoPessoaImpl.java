package repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import entidades.Estados;
import entidades.Pessoa;

@Named
public class IDaoPessoaImpl implements IDaoPessoa, Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		
		Pessoa pessoaUser = null;
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		pessoaUser = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login ='"+login+"' and p.senha='"+senha+"'").getSingleResult();
		
		transaction.commit();
		
		return pessoaUser;
	}

	@Override
	public List<SelectItem> listaEstados() {
		
		/*Fez a lista*/
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		
		/*Realizamos o select no banco retornando uma Lista*/
		List<Estados> estados = entityManager.createQuery("from Estados").getResultList();
		
		/*Transforma a lista de Estados para um formato compreensível ao combobox(SelectItem)*/
		for (Estados estado : estados) {
			/*Passo o objeto inteiro mas ao chamar o método, lista apenas os nomes*/
			//selectItems.add(new SelectItem(estado, estado.getNome()));
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}
		
		return selectItems;
	}

}
