package projectjsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dao.DaoGeneric;
import entidades.Lancamento;
import entidades.Pessoa;
import repository.IDaoLancamento;

@ViewScoped
@Named(value="lancamentoBean")
public class LancamentoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Lancamento lancamento = new Lancamento();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	
	@Inject
	private DaoGeneric<Lancamento> daoGeneric;
	
	@Inject
	private IDaoLancamento daoLancamento;
	
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext esContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) esContext.getSessionMap().get("usuarioLogado");
		lancamento.setUsuario(pessoa);
		lancamento = daoGeneric.merge(lancamento);
		
		carregarLancamentos();
		
		return "";
	}
	
	@PostConstruct
	public void carregarLancamentos() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext esContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) esContext.getSessionMap().get("usuarioLogado");
		lancamentos = daoLancamento.consultar(pessoa.getId());
	}

	public String novo() {
		lancamento = new Lancamento();
		return "";
	}
	
	public String remover() {
		daoGeneric.deletePorId(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}
	
	public Lancamento getLancamento() {
		return lancamento;
	}
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	

}
