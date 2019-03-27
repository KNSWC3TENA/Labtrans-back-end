package bananaBeans;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;

@SuppressWarnings("serial")
@ManagedBean (name="Lista", eager=true)
@ViewScoped
public class Lista implements Serializable{
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
	
	//Dados para a edição de entradas -->
	@ManagedProperty(value="#{cafeQPessoas}") //Mesmos dados que o controlador, porém armazenados para edição e com um valor já inicializado
	private int cafeQPessoas;
	@ManagedProperty(value="#{boxCafe}") // Idem
	private boolean boxCafe;
	private int spinvalue = 0; //Integer para armazenar último valor selecionado no spinner, separado do que vai ser colocado no BD
	int edicaoId;
	String edicaoResponsavel;
	String edicaoFilial;
	String edicaoLocal;
	String edicaoDatahoraini;
	int edicaoDuracao;
	String edicaoDesc;
	int edicaoCafe;
	//<-- Dados para a edição de entradas
	
	
	
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
		setnRowsSelecionadas(rowsSelecionadas.size());
		envioParaEditar();
	}

	public int getnRowsSelecionadas() {
		return nRowsSelecionadas;
	}
	public List<Lista> getListaCompleta() {
		return listaCompleta;
	}


	public void setnRowsSelecionadas(int nRowsSelecionadas) {
		this.nRowsSelecionadas = nRowsSelecionadas;
		System.out.println("SetRowsSelecionadas N-"+this.nRowsSelecionadas);
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
	public int getCafeQPessoas() {
		return cafeQPessoas;
	}
	public boolean isBoxCafe() {
		return boxCafe;
	}
	public void setBoxCafe(boolean boxCafe2) {
		System.out.println("SETBOXCAFE");
		this.boxCafe = boxCafe2;
		if(this.boxCafe == false) {
			System.out.println("SETBOXCAFE - FALSE");
			setCafeQPessoas(0);
		} else {
			System.out.println("SETBOXCAFE - TRUE");
			setCafeQPessoas(getSpinvalue());
		}
	}
	public int getSpinvalue() {
		return spinvalue;
	}
	public void setSpinvalue(int spinvalue) {
		this.spinvalue = spinvalue;
	}
	public void setCafeQPessoas(int cafeQPessoas2) {
		if (isBoxCafe() == false) {
			this.cafeQPessoas = 0;
		} else {
		this.cafeQPessoas = cafeQPessoas2;
		}
	}
	public int getEdicaoId() {
		return edicaoId;
	}

	public void setEdicaoId(int edicaoId) {
		this.edicaoId = edicaoId;
	}

	public String getEdicaoResponsavel() {
		return edicaoResponsavel;
	}

	public void setEdicaoResponsavel(String edicaoResponsavel) {
		this.edicaoResponsavel = edicaoResponsavel;
	}

	public String getEdicaoFilial() {
		return edicaoFilial;
	}

	public void setEdicaoFilial(String edicaoFilial) {
		this.edicaoFilial = edicaoFilial;
	}

	public String getEdicaoLocal() {
		return edicaoLocal;
	}

	public void setEdicaoLocal(String edicaoLocal) {
		this.edicaoLocal = edicaoLocal;
	}

	public String getEdicaoDatahoraini() {
		return edicaoDatahoraini;
	}

	public void setEdicaoDatahoraini(String edicaoDatahoraini) {
		this.edicaoDatahoraini = edicaoDatahoraini;
	}

	public int getEdicaoDuracao() {
		return edicaoDuracao;
	}

	public void setEdicaoDuracao(int edicaoDuracao) {
		this.edicaoDuracao = edicaoDuracao;
	}

	public String getEdicaoDesc() {
		return edicaoDesc;
	}

	public void setEdicaoDesc(String edicaoDesc) {
		this.edicaoDesc = edicaoDesc;
	}

	public int getEdicaoCafe() {
		return edicaoCafe;
	}

	public void setEdicaoCafe(int edicaoCafe) {
		this.edicaoCafe = edicaoCafe;
	}

	public void setnRowsTotal(int nRowsTotal) {
		this.nRowsTotal = nRowsTotal;
	}

	public void setListaCompleta(List<Lista> listaCompleta) {
		this.listaCompleta = listaCompleta;
	}
	
	//<--- FIM DE GETTERS E SETTERS

	public void cafeBoxChange(AjaxBehaviorEvent event) {//Recebe o evento do botão de "Café?" e separa o botão de selecionado ou não
		if (((SelectBooleanCheckbox)event.getSource()).isSelected()) {
			setBoxCafe(true);
		} else {
			setBoxCafe(false);
		}
	}
	
	public String cancelarSelecoes(){
		rowsSelecionadas = null;
		return "listar.jsf";
	}
	
	public String excluirEntradas(){
		for(int i=0; i<rowsSelecionadas.size();i++) {
			Controlador.dbReservaDelete(rowsSelecionadas.get(i).id);
		}
		return "listar.jsf";
	}
	
	public String envioParaEditar() {
		edicaoId = rowsSelecionadas.get(0).id;
		edicaoResponsavel = rowsSelecionadas.get(0).responsavel;
		edicaoFilial = rowsSelecionadas.get(0).filial;
		edicaoLocal = rowsSelecionadas.get(0).local;
		edicaoDatahoraini = rowsSelecionadas.get(0).datahoraini;
		edicaoDuracao = rowsSelecionadas.get(0).duracao;
		edicaoDesc = rowsSelecionadas.get(0).desc;
		edicaoCafe = rowsSelecionadas.get(0).cafe;
		cafeQPessoas = rowsSelecionadas.get(0).cafe;
		System.out.println("Lista para edição - "+ edicaoResponsavel +" "+edicaoDatahoraini + " "+edicaoLocal);
		if (cafeQPessoas > 0) {
			boxCafe = true;
		} else if (cafeQPessoas <= 0) {
			boxCafe = false;
		}	
		return "";
	}
	

	public String EditarReserva() {
		Controlador.dbReservaUpdate(edicaoId, edicaoResponsavel, edicaoFilial, edicaoLocal, edicaoDatahoraini, edicaoDuracao, edicaoDesc, edicaoCafe);
		return "listar.jsf";
	}
}
