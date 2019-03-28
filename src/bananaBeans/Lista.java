package bananaBeans;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
	private Date datahoraini;
	private String datahorainiString;
	private int duracao;
	private String desc;
	private int cafe;
	private int nRowsTotal;
	private int nRowsSelecionadas;
	private List<Lista> listaCompleta = new ArrayList<Lista>();
	private List<Lista> rowsSelecionadas = new ArrayList<Lista>();
	private List<String> listaLocais = new Vector<String>();
	private List<String> listaVazia = new Vector<String>();
	
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
	Date edicaoDatahoraini;
	int edicaoDuracao;
	String edicaoDesc;
	//<-- Dados para a edição de entradas
	
	
	
	public Lista() {
	}
	
	public Lista(int id,
	String responsavel,
	String filial,
	String local,
	Date datahoraini,
	String datahorainiString,
	int duracao,
	String desc,
	int cafe) {
		this.id = id;
		this.responsavel = responsavel;
		this.filial = filial;
		this.local = local;
		this.datahoraini = datahoraini;
		this.datahorainiString = datahorainiString;
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //Variável para formatar a String de data do banco, valor padrão para SQL
	Date dataHoraFormatada; //Variável para receber o valor do banco (String) em Date
		try {
			while (rs.next()) {
			dataHoraFormatada = sdf.parse(rs.getString("datahoraini")); //Transferência de String do banco para Date da classe
			listaCompleta.add(new Lista (rs.getInt("id"), rs.getString("responsavel"), rs.getString("filial"), rs.getString("local"), dataHoraFormatada,
						rs.getString("datahoraini"), rs.getInt("duracao"), rs.getString("desc"), rs.getInt("cafe")));
			nRowsTotal++;
		}
	} catch (SQLException | ParseException e) {
		e.printStackTrace();
		}
	}
	

	//GETTERS E SETTERS --->
	
	public List<Lista> getRowsSelecionadas() {
		return rowsSelecionadas;
	}

	public String getDatahorainiString() {
		return datahorainiString;
	}

	public void setDatahorainiString(String datahorainiString) {
		this.datahorainiString = datahorainiString;
	}

	public List<String> getListaLocais() {
		return listaLocais;
	}

	public void setListaLocais(List<String> listaLocais) {
		this.listaLocais = listaLocais;
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
	public Date getDatahoraini() {
		return datahoraini;
	}
	public void setDatahoraini(Date datahoraini) {
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
		this.boxCafe = boxCafe2;
		if(this.boxCafe == false) {
			setCafeQPessoas(0);
		} else {
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
		System.out.println("SET EDICAO LOCAL");
		this.edicaoLocal = edicaoLocal;
	}

	public Date getEdicaoDatahoraini() {
		return edicaoDatahoraini;
	}

	public void setEdicaoDatahoraini(Date edicaoDatahoraini) {
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

	public void setnRowsTotal(int nRowsTotal) {
		this.nRowsTotal = nRowsTotal;
	}

	public void setListaCompleta(List<Lista> listaCompleta) {
		this.listaCompleta = listaCompleta;
	}
	
	//<--- FIM DE GETTERS E SETTERS
	public void selecionarFilialAjax (AjaxBehaviorEvent e) {
		selecionarFilial(this.edicaoFilial);
	}
	
	//CLONES DE FUNÇÕES DA CLASSE CONTROLADOR --->
	private void selecionarFilial(String selecaoFilial){
		//Recebe a seleção da lista, retorna a lista de locais com o nome da seleção
		listaVazia.add(" ");
		listaVazia.add(" ");
		if(selecaoFilial!=null) {
			switch(selecaoFilial) {
			case "Banana Ltda.":
				this.listaLocais = Controlador.getLocaisBanana();
				break;
			case "Laranja Incorporated." :
				this.listaLocais = Controlador.getLocaisLaranja();
				break;
			case "Maçã PLLC." :
				this.listaLocais = Controlador.getLocaisMaca();
				break;
			case "Mamão S.A." :
				this.listaLocais = Controlador.getLocaisMamao();
				break;
			case "" :
				this.listaLocais = listaVazia; //Caso vazio, lista se torna vazia.
				break;
			case " " :
				this.listaLocais = listaVazia;
				break;
			default:
				this.listaLocais = listaVazia; //Caso nome não esteja cadastrado, lista se torna vazia.
				break;
			}
		} else {
			listaLocais = listaVazia; //Caso nulo, lista retorna vazia.
		}
		return;
	}	
	
	public void cafeBoxChange(AjaxBehaviorEvent event) {//Recebe o evento do botão de "Café?" e separa o botão de selecionado ou não
		if (((SelectBooleanCheckbox)event.getSource()).isSelected()) {
			setBoxCafe(true);
		} else {
			setBoxCafe(false);
		}
	}
	//<-- CLONES DE FUNÇÕES DA CLASSE CONTROLADOR
	
	
	public String cancelarSelecoes(){
		rowsSelecionadas  = new ArrayList<Lista>();
		return "listar.jsf";
	}
	
	public String excluirEntradas(){ //Função chamada pelo botão "Sim" do diálogo de exclusão da página de listagem
		for(int i=0; i<rowsSelecionadas.size();i++) {
			Controlador.dbReservaDelete(rowsSelecionadas.get(i).id);
		}
		Pagina.delete(); //Mensagem pré-selecionada para aparecer na tela "Registro Excluído"
		return "listar.jsf";
	}
	
	public String envioParaEditar() {
		edicaoId = rowsSelecionadas.get(0).id;
		edicaoResponsavel = rowsSelecionadas.get(0).responsavel;
		edicaoFilial = rowsSelecionadas.get(0).filial;
		edicaoLocal = rowsSelecionadas.get(0).local;
		selecionarFilial(edicaoFilial);
		edicaoDatahoraini = rowsSelecionadas.get(0).datahoraini;
		edicaoDuracao = rowsSelecionadas.get(0).duracao;
		edicaoDesc = rowsSelecionadas.get(0).desc;
		cafeQPessoas = rowsSelecionadas.get(0).cafe;
		if (cafeQPessoas > 0) {
			boxCafe = true;
		} else if (cafeQPessoas <= 0) {
			boxCafe = false;
		}	
		return "";
	}
	

	public String EditarReserva() {
		String dataFormatada = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(edicaoDatahoraini);//Formatação da Date da classe para String pro banco.
		if (Controlador.dbReservaUpdate(edicaoId, edicaoResponsavel, edicaoFilial, edicaoLocal, dataFormatada, edicaoDuracao, edicaoDesc, cafeQPessoas)) {
			Pagina.update();
		} else {
			Pagina.updateFail();
		}
		return "listar.jsf";
	}
}
