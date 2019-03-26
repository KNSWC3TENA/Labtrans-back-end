package bananaBeans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="Lista", eager=true)
@ViewScoped
public class Lista {
	private int id;
	private String responsavel;
	private String filial;
	private String local;
	private String datahoraini;
	private int duracao;
	private String desc;
	private int cafe;
	private int nRowsTotal;
	private int nRowsSelecionadas;
	private List<Lista> listaCompleta = new ArrayList<Lista>();
	private List<Lista> rowsSelecionadas = new ArrayList<Lista>();

	public Lista() {
	}
	
	public Lista(int id,
	String responsavel,
	String filial,
	String local,
	String datahoraini,
	int duracao,
	String desc,
	int cafe) {
		this.id = id;
		this.responsavel = responsavel;
		this.filial = filial;
		this.local = local;
		this.datahoraini = datahoraini;
		this.duracao = duracao;
		this.desc = desc;
		this.cafe = cafe;
	}
	
	//=============FUNÇÕES PARA A PÁGINA DE LISTAGEM=================
	@PostConstruct
	public void receberLista() {
		System.out.println("Receber Lista iniciado");
	nRowsTotal = 0;
	ResultSet rs = Controlador.dbReservaSelect();
		try {
			while (rs.next()) {
			listaCompleta.add(new Lista (rs.getInt("id"), rs.getString("responsavel"), rs.getString("filial"), rs.getString("local"), rs.getString("datahoraini"),
						rs.getInt("duracao"), rs.getString("desc"), rs.getInt("cafe")));
			nRowsTotal++;
		}
	} catch (SQLException e) {
		
		e.printStackTrace();
		}
	}
	

	//GETTERS E SETTERS --->
	public List<Lista> getRowsSelecionadas() {
		return rowsSelecionadas;
	}

	public int getnRowsTotal() {
		return nRowsTotal;
	}

	public void setRowsSelecionadas(List<Lista> rowsSelecionadas) {
		this.rowsSelecionadas = rowsSelecionadas;
	}

	public int getnRowsSelecionadas() {
		return nRowsSelecionadas;
	}
	public List<Lista> getListaCompleta() {
		return listaCompleta;
	}


	public void setnRowsSelecionadas(int nRowsSelecionadas) {
		this.nRowsSelecionadas = nRowsSelecionadas;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getDatahoraini() {
		return datahoraini;
	}
	public void setDatahoraini(String datahoraini) {
		this.datahoraini = datahoraini;
	}
	public int getDuracao() {
		return duracao;
	}
	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getCafe() {
		return cafe;
	}
	public void setCafe(int cafe) {
		this.cafe = cafe;
	}
	//<--- FIM DE GETTERS E SETTERS
}
