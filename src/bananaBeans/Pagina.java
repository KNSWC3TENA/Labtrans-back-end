package bananaBeans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import org.primefaces.model.menu.*;


@ManagedBean(name = "Pagina", eager = true)
@NoneScoped
public class Pagina {
	/*Cria��o do growl (caixa de mensagens) para as p�ginas*/
	private MenuModel model;
	 
    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
    }
 
    public MenuModel getModel() {
        return model;
    }
	
    public static void save() {
        addMessage("Reuni�o Registrada.");
    }
    public static void saveFail() {
    	addMessage("J� existe uma reuni�o na data selecionada.");
    }
    
    
    public static void update() {
        addMessage("Registro Atualizado.");
    }
    public static void updateFail() {
    	addMessage("Falha na atualiza��o do registro.");
    }
    
    
    public static void delete() {
        addMessage("Registro Exclu�do.");
    }
 
    public static void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	
   public Pagina() {
   }
   
}