package projectjsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import dao.DaoGeneric;
import entidades.Cidades;
import entidades.Estados;
import entidades.Pessoa;
import jpautil.JPAUtil;
import repository.IDaoPessoa;

@ViewScoped
@Named(value="pessoaBean")
public class PessoaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	
	@Inject
	/*Dentro do <> é passado a entidade para o daoGeneric indentificar o tipo a qual será trabalhado*/
	private DaoGeneric<Pessoa> daoGeneric;
	
	@Inject
	private IDaoPessoa iDaoPessoa;
	/*Essa classe java pega o arquivo que o usuario selecionou
	na tela e cria temporariamente do lado do servidor para poder processar*/
	private Part arquivofoto;
	
	@Inject
	private JPAUtil jpaUtil;
	
	public String salvar() throws IOException{
		/*INICIO - Processar Imagem*/
		byte[] imagemByte = getByte(arquivofoto.getInputStream());
		pessoa.setFotoIconBase64Original(imagemByte); /*Salva imagem original*/
		
		/*Transformar em bufferimage (tipo manipulável)*/
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		
		/*Pega tipo da imagem*/
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int largura = 200;
		int altura = 200;
		
		/*Cria miniatura*/
		BufferedImage resizedImage = new BufferedImage(altura, largura, type);
		Graphics2D g = resizedImage.createGraphics();
		
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		
		/*Escrever novamente a imagem em tamanho miniatura*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivofoto.getContentType().split("\\/")[1];
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImagem = "data:" + arquivofoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		
		
		/*FIM - Processar Imagem*/

		
		
		//daoGeneric.salvar(pessoa);
		//pessoa = new Pessoa();
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com Sucesso!");
		return "";
	}
	
	public void registrarLog() {
		System.out.println("log00001.txt gerado com sucesso");
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		try {
			URL url = new URL("https://viacep.com.br/ws/"+pessoa.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setIbge(gsonAux.getIbge());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar cep");
		}
		
		
	}
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
	
		context.addMessage(null, message);
		
	}

	public String limpar() {
		pessoa = new Pessoa();/*Limpa os campos da tela*/
		mostrarMsg("Campos limpos!");
		return "";
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();/*Limpa os campos da tela apos apagar do banco*/
		carregarPessoas();
		mostrarMsg("Removido com Sucesso!");
		return "";
	}
	
	public String deslogar() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext esContext = context.getExternalContext();
		esContext.getSessionMap().remove("usuarioLogado");
			
		HttpServletRequest httpServletRequest = (HttpServletRequest)
				context.getCurrentInstance().getExternalContext().getRequest();

		httpServletRequest.getSession().invalidate();
		
		return "login.xhtml";
	}
	
	public String logar() {
		
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if(pessoaUser != null) {/*Achou usuario*/
			/*Adiciona usuario na sessão usuarioLogado(Passa parametro)*/
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext esContext = context.getExternalContext();
			esContext.getSessionMap().put("usuarioLogado", pessoaUser);
			
			return "index.xhtml";
		}
		
		return "login.xhtml";
	}
	
	
	public boolean permiteAcesso(String acesso) {
		/*Recupera usuario da sessão usuarioLogado(Pega o valor do parametro)*/
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext esContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) esContext.getSessionMap().get("usuarioLogado");
		
		/*Se o acesso passado como parâmetro for igual ao resultado do getPerfilUser ele retorna true
		 se for diferente, ele retorna false*/
		return pessoa.getPerfilUser().equals(acesso);
	}
	
	/*Colocando essa anotação assim que o bean for instanciado, ele carrega o método como construtor*/
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}
	
	public void setArquivofoto(Part arquivofoto) {
		this.arquivofoto = arquivofoto;
	}
	
	public Part getArquivofoto() {
		return arquivofoto;
	}
	
	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
	
	public List<SelectItem> getCidades() {
		return cidades;
	}
	
	public void carregaCidades(AjaxBehaviorEvent event) {
		
		/*Pega o objeto selecionado do comboBox*/
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();
				
			if(estado != null) {
				pessoa.setEstado(estado);
				List<Cidades> cidades = jpaUtil.getEntityManager().
						createQuery("from Cidades where estados.id ="+
								estado.getId()).getResultList();
				
				List<SelectItem> sCidades = new ArrayList<SelectItem>();
				
				for (Cidades cidade : cidades) {
					sCidades.add(new SelectItem(cidade, cidade.getNome()));
				}
				
				setCidades(sCidades);
			}
		
	}
	
	/*Método que converte InputStream em Byte[]*/
	private byte[] getByte(InputStream is) throws IOException{
		
		int len;
		int size = 1024;
		byte[] buf = null;
		if(is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while((len = is.read(buf, 0, size)) != -1){
				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		
		return buf;
	}
	
	public void download() throws IOException{
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);
		
		HttpServletResponse response = (HttpServletResponse) 
				FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();

	}
}
