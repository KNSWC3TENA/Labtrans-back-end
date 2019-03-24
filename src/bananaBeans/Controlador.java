package bananaBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "Controlador", eager = true)
@RequestScoped
public class Controlador {
	private String mensagem = "";
	private String nome;

	Controlador(String nomeIN){ //Não se pode instanciar a classe caso o nome for vazio ou nulo
		if (nomeIN!=null || nomeIN!="") {
			this.nome = nomeIN;
		}
	}
	
	public void NovaReserva() {
		return;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
