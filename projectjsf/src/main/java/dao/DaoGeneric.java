package dao;


import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jpautil.JPAUtil;

@Named/*Toda classe que for usar essa injeção de dependencia deve usar essa anotação*/
public class DaoGeneric<E> implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;
	
	@Inject
	private JPAUtil jpaUtil;
	
	@Inject
	public DaoGeneric(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.persist(entidade);
		
		transaction.commit();
	}
	
	/*Para retornar o objeto salvo no banco pra tela, ultiliza-se o merge pois,
	 * alem de salvar/atualizar ele retorna o objeto salvo no banco*/
	public E merge(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		E retorno = entityManager.merge(entidade);
		
		transaction.commit();
		
		return retorno;
	}

	public void delete(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.remove(entidade);
		
		transaction.commit();
	}
	
	public void deletePorId(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Object id = jpaUtil.getPrimaryKey(entidade);
		
		
		entityManager.createQuery("delete from "+ entidade.getClass().getSimpleName() +" where id ="+id).executeUpdate();
		
		transaction.commit();
	}
	
	public List<E> getListEntity(Class<E> entidade){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();
		
		transaction.commit();
		return retorno;
	}
	
	public E consultar(Class<E> entidade, String codigo){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigo));
		entityTransaction.commit();
		return objeto;
		
	}
	
}
