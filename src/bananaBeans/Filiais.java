package bananaBeans;

import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean (name="Filiais", eager=true)
@ApplicationScoped
public class Filiais {
	//Seleções das listas de Filiais e os locais dos mesmos, respectivamente
	@ManagedProperty(value="#{selecaoFilial}")
	private String selecaoFilial = "Seleção Filial";
	
	@ManagedProperty(value="#{selecaoLocal}")
	private String selecaoLocal;

	private List<String> lista = new Vector<String>();

	//4 Locais/Filiais para serem armazenados em Strings
	private List<String> filial = new Vector<String>();
	
	//Cada local possui sua quantidade individual para armazenamento de salas, também em Strings
	private List<String> locaisBanana = new Vector<String>();
	private List<String> locaisLaranja = new Vector<String>();
	private List<String> locaisMaca = new Vector<String>();
	private List<String> locaisMamao = new Vector<String>();
	
	private List<String> listaVazia = new Vector<String>();
	
	@PostConstruct
	void setFiliais(){
	filial.add(" "); //<--- Primeiro valor "vazio", para ser mostrado como caixa branca antes de ser selecionado
	filial.add("Banana Ltda.");
	filial.add("Laranja Incorporated.");
	filial.add("Maçã PLLC.");
	filial.add("Mamão S.A.");
	//No momento apenas são inseridas salas genéricas para cada filial.
		//Nomes das salas são inseridos como 'Sala <número genérico>'
		for (int i=0; i<13; i++) {
			locaisBanana.add("Sala "+(i*3+2));
		}
		
		for (int i=0; i<5; i++) {
			locaisLaranja.add("Sala "+(i*5+3));
		}
		
		for (int i=0; i<6; i++) {
			locaisMaca.add("Sala "+((i+1)*8-1));
		}

		for (int i=0; i<3; i++) {
			locaisMamao.add("Sala "+(i*1));
		}
	}
	
	
	//---Construtor da classe---
	public Filiais() {
		System.out.println("Filiais chamada");
	}
	//---Fim do construtor da classe.---

	//Getters e Setters
	public List<String> getFilial() {
		return filial;
	}
	public List<String> getLista() {
		return lista;
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
	public String getSelecaoLocal() {
		return selecaoLocal;
	}
	public void setSelecaoLocal(String selecaoLocal2) {
		this.selecaoLocal = selecaoLocal2;
	}
	public String getSelecaoFilial() {
		return selecaoFilial;
	}
	public void setSelecaoFilial(String selecaoFilial2) {
		System.out.println("Seleção Filial - "+selecaoFilial2);
		this.selecaoFilial = selecaoFilial2;
		selecionarFilial();
	}
	public List<String> getListaVazia() {
		return listaVazia;
	}
	
	
	public void selecionarFilial(){
		listaVazia.add(" ");
		listaVazia.add(" ");
		//Recebe a seleção da lista, retorna a lista de locais com o nome da seleção
		if(selecaoFilial!=null) {
			switch(selecaoFilial) {
			case "Banana Ltda.":
				lista = getLocaisBanana();
				break;
			case "Laranja Incorporated." :
				lista = getLocaisLaranja();
				break;
			case "Maçã PLLC." :
				lista = getLocaisMaca();
				break;
			case "Mamão S.A." :
				lista = getLocaisMamao();
				break;
			case "" :
				lista = listaVazia; //Caso vazio, lista se torna vazia.
				break;
			case " " :
				lista = listaVazia;
				break;
			default:
				lista = listaVazia; //Caso nome não esteja cadastrado, lista se torna vazia.
				break;
			}
		} else {
			lista = listaVazia; //Caso nulo, lista retorna vazia.
		}
		return;
	}
}
