package bananaBeans;

import javax.faces.bean.*;

@ManagedBean(name = "Pagina", eager = true)
@ApplicationScoped
public class Pagina {
   private String inicio;
   
   public Pagina() {
      System.out.println("Página inicial chamada.");
   }
   
   public String getInicio() {
	   inicio ="Seja bem vindo ao sistema de Banana Ltda";
	   return inicio;
   }
   
   public String NovaReserva() {
	   
	   String resultado ="";
	   resultado ="Sucesso.";
	   return resultado;
   }
}