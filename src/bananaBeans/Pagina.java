package bananaBeans;

import javax.faces.bean.*;

@ManagedBean(name = "Pagina", eager = true)
@NoneScoped
public class Pagina {
	private String mensagem="";
	private String inicio;
   
   public Pagina() {
   }
   
   public String getInicio() {
	   return inicio;
   }
   
   public String NovaReserva() {
	   
	   String resultado ="";
	   resultado ="Sucesso.";
	   return resultado;
   }
   
   public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}