package bananaBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "pagina", eager = true)
@RequestScoped
public class Pagina {
   private String inicio;
   
   public Pagina() {
      System.out.println("Página inicial chamada.");
   }
   
   public String getInicio() {
	   inicio ="Seja bem vindo ao sistema de Banana Ltda";
	   return inicio;
   }
   
   public String setCadastroPagina(ActionEvent e) {
	   String cadastro="";
	   System.out.println("Botão de cadastro iniciado");
	   cadastro="Wow-------------------------------------------------";
	   return cadastro;
   }
}