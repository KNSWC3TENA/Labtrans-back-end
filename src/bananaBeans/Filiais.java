package bananaBeans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "Filiais", eager = true)
@RequestScoped
public class Filiais {
	//Sele��es das listas de Filiais e os locais dos mesmos, respectivamente - Juntamente de seus Getters e Setters
	private String selecaoFilial ="";
	private String selecaoLocal ="";
	public String getSelecaoLocal() {
		return selecaoLocal;
	}
	public void setSelecaoLocal(String selecaoLocal) {
		this.selecaoLocal = selecaoLocal;
	}
	public String getSelecaoFilial() {
		return selecaoFilial;
	}
	public void setSelecaoFilial(String selecaoFilial) {
		this.selecaoFilial = selecaoFilial;
	}

	//4 Locais/Filiais para serem armazenados em Strings
	private List<String> filial;
	
	//Cada local possui sua quantidade individual para armazenamento de salas, tamb�m em Strings
	private List<String> locaisBanana;
	private List<String> locaisLaranja;
	private List<String> locaisMaca;
	private List<String> locaisMamao;
	
	//---Construtor da classe---
	Filiais() {
		filial.add(""); //<--- Primeiro valor vazio, para ser mostrado como caixa branca antes de ser selecionado
		filial.add("Banana Ltda.");
		filial.add("Laranja Incorporated.");
		filial.add("Ma�� PLLC.");
		filial.add("Mam�o S.A.");
	
		//No momento apenas s�o inseridas salas gen�ricas para cada filial.
		//Nomes das salas s�o inseridos como 'Sala <n�mero gen�rico>'
		for (int i=0; i<13; i++) {
			locaisBanana.add("Sala "+(i*3-1));
		}
		
		for (int i=0; i<5; i++) {
			locaisLaranja.add("Sala "+(i*2-1));
		}
		
		for (int i=0; i<6; i++) {
		locaisMaca.add("Sala "+(i*2-1));
		}
	
		for (int i=0; i<3; i++) {
			locaisMamao.add("Sala "+(i*1-1));
		}
		
	}
	//---Fim do construtor da classe.---

	//Getters da filial e dos locais de cada filial
	public List<String> getFilial() {
		return filial;
	}
	public List<String> getLocaisBanana() {
		return locaisBanana;
	}
	public List<String> getLocaisLaranja() {
		return locaisLaranja;
	}
	public List<String> getLocaisMaca() {
		return locaisMaca;
	}
	public List<String> getLocaisMamao() {
		return locaisMamao;
	}
	
	public List<String> selecionarFilial(){
		//Recebe a sele��o da lista, retorna a lista de locais com o nome da sele��o
		List<String> lista;
		
		switch(selecaoFilial) {
		case "Banana Ltda":
			lista = getLocaisBanana();
			return lista;
		case "Laranja Incorporated." :
			lista = getLocaisLaranja();
			return lista;
		case "Ma�� PLLC." :
			lista = getLocaisMaca();
			return lista;
		case "Mam�o S.A." :
			lista = getLocaisMamao();
			return lista;
		default:
			return null; //<-- Caso valor for vazio/diferente dos pr�-selecionados, a lista voltada ser� nula e n�o ser� gerada a pr�xima lista.
			
		}
	}
}
