package converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import entidades.Estados;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable{

	private static final long serialVersionUID = 5191639376275487539L;

	/*Para salvar no banco é nescessário os convertes pois ao clicar em
	 * salvar ele tenta injetar os valores do form em um objeto e o Estados e Cidades
	 * são chamadas do ajax, sendo outro processo, precisamos buscar os estados e Cidades
	 * através dos converters e ai sim injetar no objeto para salvar no banco*/
	@Override/*Retorna objeto inteiro*/
	public Object getAsObject(FacesContext context, UIComponent component, String codEstado) {
		/*Retorna o entityManager que está no escopo de execução*/
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		
		Estados estados = (Estados) entityManager.find(Estados.class, Long.parseLong(codEstado));
		return estados;
	}

	@Override/*Retorna apenas o código em String*/
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		
		if(estado == null) {/*Se estado for null, retornamos null para continuar o processo*/
			return null;
		}
		if(estado instanceof Estados) {/*Se o estado é uma instancia do objeto, ele converte a instancia e pega o id desse objeto*/
			return ((Estados) estado).getId().toString();
		}else {
			return estado.toString();/*Se ao invés de objeto ele ja vir uma string, ele só retorna a string*/
		}
			
	}
	
	

}
