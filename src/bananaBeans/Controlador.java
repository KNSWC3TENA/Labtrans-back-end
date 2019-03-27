package bananaBeans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.spinner.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

@ManagedBean(name = "Controlador", eager = true)
@ApplicationScoped
public class Controlador {
	//======================FUN��ES PARA O BANCO DE DADOS==================
	private static String userName = "root";
	private static String password = "root";
	
	
	public static Connection getConnection() { //M�todo chamado para instanciar uma conex�o com o banco
		try {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				System.out.println("Erro no JDBC");
			}

			Connection conn = null;
			Properties connectionProps = new Properties();
			connectionProps.put("user", userName);
			connectionProps.put("password", password);
			conn = DriverManager.getConnection("jdbc:sqlite:bananadb.db", connectionProps);
			return conn; //

		} catch (SQLException e) {
			System.out.println("Erro na conex�o prim�ria: " + e.getMessage() + "  C�digo: " + e.getErrorCode());
			return null;
		}
	}
	
	public static void checkTables() { //M�todo para checar se as tabelas existem no banco (se n�o existem, cri�-las).
		System.out.println("checkTables Iniciada.");
		Connection conn = getConnection();
		Statement stmt;
		String sql = "";
		
		try { //criar tabela reservas se n�o existir...
				sql=("CREATE TABLE IF NOT EXISTS 'reservas' ('id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
						+ "'datahoraini' TEXT NOT NULL," //Datas no SQLITE s�o armazenadas como tipo texto e formatadas pelo DateTime functions.
						+ "'duracao' INTEGER NOT NULL," //Dura��o da reserva ser� utilizada nas fun��es de DateTime.
						+ "'responsavel' TEXT NOT NULL,"
						+ "'filial' TEXT NOT NULL,"
						+ "'local' TEXT NOT NULL,"
						+ "'desc' TEXT,"
						+ "'cafe' INTEGER);\n");
				stmt = conn.createStatement();
				if (stmt.execute(sql) == true) {
					try {if (conn != null) conn.close();} catch (Exception e) {}; // Fecha conex�o
					try {if (stmt != null)stmt.close();} catch (Exception e2) {}; // Fecha statement
					System.out.println("Sucesso na cria��o da tabela");
					return;
				} else {
					try {if (conn != null) conn.close();} catch (Exception e) {}; // Fecha conex�o
					try {if (stmt != null)stmt.close();} catch (Exception e2) {}; // Fecha statement
					return;
				}
		} catch (SQLException e) {
			System.out.println("Erro na conex�o: " + e.getMessage() + "  C�digo: " + e.getErrorCode());
		}
	}
	
	protected boolean dbReservaInsert(String datahoraini, int duracao, String responsavel, String filial, String local, String desc, int cafe){
		Statement stmt = null;
		Connection conn = getConnection();
		ResultSet rs = null;
		String sql = "";
		
		if (datahoraini == null || responsavel == null || filial == null || local == null) {//Checa se alguma String recebida � nula
			System.out.println("Inser��o nula, cancelando opera��o.");
			return false;
		}
		// fun��o dateTime do SQLite utiliza de "YYYY-MM-DD HH:MM:SS" - formata��o utilizada em inser��es no BD.
		try {
				sql =("SELECT * FROM reservas WHERE datetime(datahoraini) "//Selecionar entre a data e a data + dura��o da reuni�o selecionada.
						+ "BETWEEN datetime('"+datahoraini+"') AND datetime('"+datahoraini+"', '+"+duracao+" hours');\n");
				System.out.println(sql);
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				
				if (rs.next()) { // Se a sele��o acima obteve resultados (j� existe uma reserva no mesmo local)
					System.out.println("J� havia reserva cadastrada na hora selecionada, ignorando.");
					rs.close();
					stmt.close();
					conn.close();
					return false;
				} else {
				//A sele��o n�o obteve resultados, prosseguir com a inser��o-
				System.out.println("Sele��o n�o obteve resultados, prosseguir com a inser��o");
				stmt.close(); //Fecha statement para que um novo seja criado sem conflitos
				sql = ("INSERT INTO reservas (datahoraini, duracao, responsavel, filial, local, desc, cafe) "
							+ "VALUES ('"+datahoraini+"', '"+duracao+"', '"+responsavel+"', '"+filial+"', '"+local+"', '"+desc+"', '"+cafe+"');\n");
				stmt = conn.createStatement();
				stmt.execute(sql);
				System.out.println("Sucesso na inser��o de reserva.");
				return true;
				}
		} catch (SQLException g) {
				System.out.println("Erro na conex�o para INSERT: " + g.getMessage() + "  C�digo: " + g.getErrorCode());
				return false;
		}
	}
	
	protected static boolean dbReservaDelete(int id) {
		try {
			Connection conn = getConnection();
			ResultSet rs = null;

			String sql = ("SELECT responsavel FROM reservas WHERE id='"+id+"'");
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs.next() == true) { // Se estiver ocupado
				rs.close();
				stmt.close();
				System.out.println("Exclu�ndo registro...");
				sql = ("DELETE FROM reservas WHERE id ='"+id+"';\n");
				stmt.execute(sql);
				System.out.println("Registro Exclu�do.");
				stmt.close();
				conn.close();
				return true;
			} else { //Caso n�o esteja ocupado ou o id recebido seja inv�lido
				System.out.println("Registro vazio, ignorando...");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro na conex�o para DELETE: " + e.getMessage() + "  C�digo: " + e.getErrorCode());
			return false;
		}
	}
	
	protected static ResultSet dbReservaSelect() {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
				System.out.println("Reserva Select invocado");
				// fun��o dateTime utiliza de "YYYY-MM-DD HH:MM:SS" - formata��o utilizada em inser��es no BD.
				String sql = ("SELECT * FROM reservas ORDER BY dateTime(datahoraini) DESC;");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				return rs;
		} catch (SQLException g) {
			System.out.println("Erro na conex�o para SELECT: " + g.getMessage() + "  C�digo: " + g.getErrorCode());
			return null;
		}
	}
	
	protected static boolean dbReservaUpdate(int id, String responsavel, String filial, String local, String datahoraini, int duracao, String desc, int cafe) {
		try {
			Connection conn = getConnection();
			ResultSet rs = null;

			String sql = ("SELECT responsavel FROM reservas WHERE id='"+id+"'");
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs.next() == true) { // Se estiver ocupado
				rs.close();
				stmt.close();
				System.out.println("Atualizando registro...");
				sql = ("UPDATE reservas SET  datahoraini='"+datahoraini+"', duracao='"+duracao+"', responsavel='"+responsavel+"', "
						+ "filial='"+filial+"', local='"+local+"', desc='"+desc+"', cafe='"+cafe+"' WHERE id ='"+id+"';\n");
				stmt.execute(sql);
				System.out.println("Registro Atualizado.");
				try {if (conn != null) conn.close();} catch (Exception e) {}; // Fecha conex�o
				try {if (stmt != null)stmt.close();} catch (Exception e2) {}; // Fecha statement
				return true;
			} else { //Caso n�o esteja ocupado ou o id recebido seja inv�lido
				System.out.println("Registro vazio, ignorando...");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro na conex�o para DELETE: " + e.getMessage() + "  C�digo: " + e.getErrorCode());
			return false;
		}
	}
	
	
	//======================FUN��ES PARA A P�GINA DE CADASTROS=================
	
	@ManagedProperty(value="#{cafeQPessoas}")
	private int cafeQPessoas = 0;
	@ManagedProperty(value="#{boxCafe}")
	private boolean boxCafe = false;
	private int spinvalue = 0; //Integer para armazenar �ltimo valor selecionado no spinner, separado do que vai ser colocado no BD
	private int id;
	private Date datahoraini;
	private int duracao;
	private String responsavel;
	private String desc;
	private String mensagem = "";
	
	public Controlador(){ //Construtor
		System.out.println("Controlador Chamado");
	}
	
	//ENVIO DE DADOS PARA O PROCESSAMENTO NO BANCO
	public String NovaReserva() {
		System.out.println(datahoraini);
		String dataFormatada = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datahoraini);//FORMATA��O DA DATA RECEBIDA PARA A INSER��O NA TABELA
		/*System.out.println("SQL A SER EXECUTADO --\n INSERT INTO reservas (datahoraini, duracao, responsavel, filial, local, desc, cafe) "
				+ "VALUES ('"+dataFormatada+"' '"+duracao+"' '"+responsavel+"' '"+selecaoFilial+"' '"+selecaoLocal+"' '"+desc+"' '"+cafeQPessoas+"');\n");*/
		if (dbReservaInsert(dataFormatada, duracao, responsavel, selecaoFilial, selecaoLocal, desc, cafeQPessoas) == true) {
			setMensagem("Sucesso.");
			return ("listar.jsf");
		} else  {
			setMensagem("Erro.");
			return ("Falha na reserva.");
		}
	}

	//GETTERS E SETTERS --->
	public String getMensagem() {
		return mensagem;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDatahoraini() {
		return datahoraini;
	}

	public void setDatahoraini(Date datahoraini) {
		System.out.println("datahoraini modificada");
		this.datahoraini = datahoraini;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
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
		selecaoLocal = selecaoLocal2;
	}
	public String getSelecaoFilial() {
		return selecaoFilial;
	}
	// <-- FIM DOS GETTERS E SETTERS
	
	//Sele��es das listas de Filiais e os locais dos mesmos, respectivamente
		@ManagedProperty(value="#{selecaoFilial}")
		protected String selecaoFilial = "Sele��o Filial";
		
		@ManagedProperty(value="#{selecaoLocal}")
		protected String selecaoLocal;

		private List<String> lista = new Vector<String>();

		//4 Locais/Filiais para serem armazenados em Strings
		private List<String> filial = new Vector<String>();
		
		//Cada local possui sua quantidade individual para armazenamento de salas, tamb�m em Strings
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
		filial.add("Ma�� PLLC.");
		filial.add("Mam�o S.A.");
		//No momento apenas s�o inseridas salas gen�ricas para cada filial.
			//Nomes das salas s�o inseridos como 'Sala <n�mero gen�rico>'
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
		public void setSelecaoFilial(String selecaoFilial2) {
			System.out.println("Sele��o Filial - "+selecaoFilial2);
			this.selecaoFilial = selecaoFilial2;
			selecionarFilial(null);
		}
		
		public List<String> getListaVazia() {
			return listaVazia;
		}
		
		public void selecionarFilial(AjaxBehaviorEvent e){
			listaVazia.add(" ");
			listaVazia.add(" ");
			//Recebe a sele��o da lista, retorna a lista de locais com o nome da sele��o
			if(selecaoFilial!=null) {
				switch(selecaoFilial) {
				case "Banana Ltda.":
					lista = getLocaisBanana();
					break;
				case "Laranja Incorporated." :
					lista = getLocaisLaranja();
					break;
				case "Ma�� PLLC." :
					lista = getLocaisMaca();
					break;
				case "Mam�o S.A." :
					lista = getLocaisMamao();
					break;
				case "" :
					lista = listaVazia; //Caso vazio, lista se torna vazia.
					break;
				case " " :
					lista = listaVazia;
					break;
				default:
					lista = listaVazia; //Caso nome n�o esteja cadastrado, lista se torna vazia.
					break;
				}
			} else {
				lista = listaVazia; //Caso nulo, lista retorna vazia.
			}
			return;
		}	
	
	public void cafeQPessoasChange(int value) {
		/*Spinner spinner = (Spinner)event.getSource();
		int value = Integer.parseInt((String)spinner.getValue());*/
		//Ambos valores s�o atribu�dos, por�m cafeQPessoas volta � 0 quando o bot�o de caf� � de-selecionado, o spinvalue serve para voltar o valor
		// caso a caixa seja selecionada novamente.
		setCafeQPessoas(value);
		setSpinvalue(value);
	}

	public void cafeBoxChange(AjaxBehaviorEvent event) {//Recebe o evento do bot�o de "Caf�?" e separa o bot�o de selecionado ou n�o
		if (((SelectBooleanCheckbox)event.getSource()).isSelected()) {
			setBoxCafe(true);
		} else {
			setBoxCafe(false);
		};
			
	}
}
