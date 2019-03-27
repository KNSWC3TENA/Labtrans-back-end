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
	//======================FUNÇÕES PARA O BANCO DE DADOS==================
	private static String userName = "root";
	private static String password = "root";
	
	
	public static Connection getConnection() { //Método chamado para instanciar uma conexão com o banco
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
			System.out.println("Erro na conexão primária: " + e.getMessage() + "  Código: " + e.getErrorCode());
			return null;
		}
	}
	
	public static void checkTables() { //Método para checar se as tabelas existem no banco (se não existem, criá-las).
		System.out.println("checkTables Iniciada.");
		Connection conn = getConnection();
		Statement stmt;
		String sql = "";
		
		try { //criar tabela reservas se não existir...
				sql=("CREATE TABLE IF NOT EXISTS 'reservas' ('id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
						+ "'datahoraini' TEXT NOT NULL," //Datas no SQLITE são armazenadas como tipo texto e formatadas pelo DateTime functions.
						+ "'duracao' INTEGER NOT NULL," //Duração da reserva será utilizada nas funções de DateTime.
						+ "'responsavel' TEXT NOT NULL,"
						+ "'filial' TEXT NOT NULL,"
						+ "'local' TEXT NOT NULL,"
						+ "'desc' TEXT,"
						+ "'cafe' INTEGER);\n");
				stmt = conn.createStatement();
				if (stmt.execute(sql) == true) {
					try {if (conn != null) conn.close();} catch (Exception e) {}; // Fecha conexão
					try {if (stmt != null)stmt.close();} catch (Exception e2) {}; // Fecha statement
					System.out.println("Sucesso na criação da tabela");
					return;
				} else {
					try {if (conn != null) conn.close();} catch (Exception e) {}; // Fecha conexão
					try {if (stmt != null)stmt.close();} catch (Exception e2) {}; // Fecha statement
					return;
				}
		} catch (SQLException e) {
			System.out.println("Erro na conexão: " + e.getMessage() + "  Código: " + e.getErrorCode());
		}
	}
	
	protected boolean dbReservaInsert(String datahoraini, int duracao, String responsavel, String filial, String local, String desc, int cafe){
		Statement stmt = null;
		Connection conn = getConnection();
		ResultSet rs = null;
		String sql = "";
		
		if (datahoraini == null || responsavel == null || filial == null || local == null) {//Checa se alguma String recebida é nula
			System.out.println("Inserção nula, cancelando operação.");
			return false;
		}
		// função dateTime do SQLite utiliza de "YYYY-MM-DD HH:MM:SS" - formatação utilizada em inserções no BD.
		try {
				sql =("SELECT * FROM reservas WHERE datetime(datahoraini) "//Selecionar entre a data e a data + duração da reunião selecionada.
						+ "BETWEEN datetime('"+datahoraini+"') AND datetime('"+datahoraini+"', '+"+duracao+" hours');\n");
				System.out.println(sql);
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				
				if (rs.next()) { // Se a seleção acima obteve resultados (já existe uma reserva no mesmo local)
					System.out.println("Já havia reserva cadastrada na hora selecionada, ignorando.");
					rs.close();
					stmt.close();
					conn.close();
					return false;
				} else {
				//A seleção não obteve resultados, prosseguir com a inserção-
				System.out.println("Seleção não obteve resultados, prosseguir com a inserção");
				stmt.close(); //Fecha statement para que um novo seja criado sem conflitos
				sql = ("INSERT INTO reservas (datahoraini, duracao, responsavel, filial, local, desc, cafe) "
							+ "VALUES ('"+datahoraini+"', '"+duracao+"', '"+responsavel+"', '"+filial+"', '"+local+"', '"+desc+"', '"+cafe+"');\n");
				stmt = conn.createStatement();
				stmt.execute(sql);
				System.out.println("Sucesso na inserção de reserva.");
				return true;
				}
		} catch (SQLException g) {
				System.out.println("Erro na conexão para INSERT: " + g.getMessage() + "  Código: " + g.getErrorCode());
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
				System.out.println("Excluíndo registro...");
				sql = ("DELETE FROM reservas WHERE id ='"+id+"';\n");
				stmt.execute(sql);
				System.out.println("Registro Excluído.");
				stmt.close();
				conn.close();
				return true;
			} else { //Caso não esteja ocupado ou o id recebido seja inválido
				System.out.println("Registro vazio, ignorando...");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro na conexão para DELETE: " + e.getMessage() + "  Código: " + e.getErrorCode());
			return false;
		}
	}
	
	protected static ResultSet dbReservaSelect() {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
				System.out.println("Reserva Select invocado");
				// função dateTime utiliza de "YYYY-MM-DD HH:MM:SS" - formatação utilizada em inserções no BD.
				String sql = ("SELECT * FROM reservas ORDER BY dateTime(datahoraini) DESC;");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				return rs;
		} catch (SQLException g) {
			System.out.println("Erro na conexão para SELECT: " + g.getMessage() + "  Código: " + g.getErrorCode());
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
				try {if (conn != null) conn.close();} catch (Exception e) {}; // Fecha conexão
				try {if (stmt != null)stmt.close();} catch (Exception e2) {}; // Fecha statement
				return true;
			} else { //Caso não esteja ocupado ou o id recebido seja inválido
				System.out.println("Registro vazio, ignorando...");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro na conexão para DELETE: " + e.getMessage() + "  Código: " + e.getErrorCode());
			return false;
		}
	}
	
	
	//======================FUNÇÕES PARA A PÁGINA DE CADASTROS=================
	
	@ManagedProperty(value="#{cafeQPessoas}")
	private int cafeQPessoas = 0;
	@ManagedProperty(value="#{boxCafe}")
	private boolean boxCafe = false;
	private int spinvalue = 0; //Integer para armazenar último valor selecionado no spinner, separado do que vai ser colocado no BD
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
		String dataFormatada = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datahoraini);//FORMATAÇÃO DA DATA RECEBIDA PARA A INSERÇÃO NA TABELA
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
	
	//Seleções das listas de Filiais e os locais dos mesmos, respectivamente
		@ManagedProperty(value="#{selecaoFilial}")
		protected String selecaoFilial = "Seleção Filial";
		
		@ManagedProperty(value="#{selecaoLocal}")
		protected String selecaoLocal;

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
		public void setSelecaoFilial(String selecaoFilial2) {
			System.out.println("Seleção Filial - "+selecaoFilial2);
			this.selecaoFilial = selecaoFilial2;
			selecionarFilial(null);
		}
		
		public List<String> getListaVazia() {
			return listaVazia;
		}
		
		public void selecionarFilial(AjaxBehaviorEvent e){
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
	
	public void cafeQPessoasChange(int value) {
		/*Spinner spinner = (Spinner)event.getSource();
		int value = Integer.parseInt((String)spinner.getValue());*/
		//Ambos valores são atribuídos, porém cafeQPessoas volta à 0 quando o botão de café é de-selecionado, o spinvalue serve para voltar o valor
		// caso a caixa seja selecionada novamente.
		setCafeQPessoas(value);
		setSpinvalue(value);
	}

	public void cafeBoxChange(AjaxBehaviorEvent event) {//Recebe o evento do botão de "Café?" e separa o botão de selecionado ou não
		if (((SelectBooleanCheckbox)event.getSource()).isSelected()) {
			setBoxCafe(true);
		} else {
			setBoxCafe(false);
		};
			
	}
}
