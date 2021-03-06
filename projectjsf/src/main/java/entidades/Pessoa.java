package entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Pessoa implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome;
	
	/*Validações usando Bean validator (Server side)*/
	@NotEmpty(message = "Sobrenome deve ser informado")
	@NotNull(message = "Sobrenome deve ser informado")
	private String sobrenome;
	
	private Integer idade;
	
	private Integer[] linguagens;
	
	private String sexo;

	private String programador;
	
	private String[] frameworks;
	
	private Boolean ativo;
	
	private String login;
	
	private String senha;
	
	private String cep;
	
	private String logradouro;
	
	private String complemento;
	
	private String bairro;
	
	private String localidade;
	
	private String uf;
	
	private String ibge;
	
	private String perfilUser;
	
	@Column(columnDefinition = "text")/*define o tipo da coluna na tabela do banco*/
	private String fotoIconBase64;
	
	private String extensao; /*extensao da imagem*/
	
	@Lob/*Gravar arquivos no banco de dados*/
	/*@Basic(fetch = FetchType.LAZY) : por ser um objeto pesado isso define que só vai ser carregada quando chamar
	 * o atributo e nao quando instanciamos o objeto*/
	@Basic(fetch = FetchType.LAZY)
	private byte[] fotoIconBase64Original;
	
	@ManyToOne
	private Cidades cidades;
	
	/*Objeto temporário para armazenar o estado selecionado na tela jsf*/
	@Transient/*Não fica persistente ou não cria essa coluna no banco*/
	private Estados estado;
	
	@Temporal(TemporalType.DATE)
	private Date dataNascimento = new Date();
	
	public Pessoa() {
		
	}

	/*INICIO - Setters e Getters -------*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String[] getFrameworks() {
		return frameworks;
	}
	
	public void setFrameworks(String[] frameworks) {
		this.frameworks = frameworks;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setPerfilUser(String perfilUser) {
		this.perfilUser = perfilUser;
	}
	
	public String getPerfilUser() {
		return perfilUser;
	}
	
	public String getProgramador() {
		return programador;
	}
	
	public void setProgramador(String programador) {
		this.programador = programador;
	}
	
	public Integer[] getLinguagens() {
		return linguagens;
	}
	
	public void setLinguagens(Integer[] linguagens) {
		this.linguagens = linguagens;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getCep() {
		return cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public Estados getEstado() {
		return estado;
	}
	
	public void setEstado(Estados estado) {
		this.estado = estado;
	}
	
	public String getFotoIconBase64() {
		return fotoIconBase64;
	}
	
	public void setFotoIconBase64(String fotoIconBase64) {
		this.fotoIconBase64 = fotoIconBase64;
	}
	
	public String getExtensao() {
		return extensao;
	}
	
	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
	
	public byte[] getFotoIconBase64Original() {
		return fotoIconBase64Original;
	}
	
	public void setFotoIconBase64Original(byte[] fotoIconBase64Original) {
		this.fotoIconBase64Original = fotoIconBase64Original;
	}
	
	public void setCidades(Cidades cidades) {
		this.cidades = cidades;
	}
	
	public Cidades getCidades() {
		return cidades;
	}
	
	/*FIM - Setters e Getters ---------*/
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
