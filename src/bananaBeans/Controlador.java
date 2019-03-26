package bananaBeans;

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
import java.util.Properties;

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
				sql =("SELECT * FROM reservas WHERE datahoraini "//Selecionar entre a data e a data + dura��o da reuni�o selecionada.
						+ "BETWEEN datetime('"+datahoraini+"') AND datetime('"+datahoraini+"', '+'"+duracao+"' hours);\n");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next() == true) { // Se a sele��o acima obteve resultados (j� existe uma reserva no mesmo local)
					return false;
				} else { //A sele��o n�o obteve resultados, prosseguir com a inser��o-
				stmt.close(); //Fecha statement para que um novo seja criado sem conflitos
				sql = ("INSERT INTO reservas (datahoraini, duracao, responsavel, filial, local, desc, cafe) "
							+ "VALUES ('"+datahoraini+"' '"+duracao+"' '"+responsavel+"' '"+filial+"' '"+local+"' '"+desc+"' '"+cafe+"');\n");
				stmt = conn.createStatement();
				stmt.executeQuery(sql);
				return true;
				}
		} catch (SQLException g) {
			System.out.println("Erro na conex�o para INSERT: " + g.getMessage() + "  C�digo: " + g.getErrorCode());
			return false;
		}
	}
	
	protected boolean dbReservaDelete(int id) {
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
	
	protected ResultSet dbReservaSelect() {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
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
	
	protected boolean dbReservaUpdate(int id, String datahoraini, int duracao, String responsavel, String filial, String local, String desc, int cafe) {
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
	private String datahoraini;
	private int duracao;
	private String responsavel;
	private String filial;
	private String local;
	private String desc;
	private int cafe;
	private String mensagem = "";
	
	public Controlador(){ //Construtor
		System.out.println("Controlador Chamado");
	}
	
	//ENVIO DE DADOS PARA O PROCESSAMENTO NO BANCO
	public String NovaReserva() {
		System.out.println("SQL A SER EXECUTADO --\n INSERT INTO reservas (datahoraini, duracao, responsavel, filial, local, desc, cafe)"
				+ "VALUES ('"+datahoraini+"' '"+duracao+"' '"+responsavel+"' '"+filial+"' '"+local+"' '"+desc+"' '"+cafe+"');\n");
		/*if (dbReservaInsert(datahoraini, duracao, responsavel, filial, local, desc, cafe) == true) {
			setMensagem("Sucesso.");
			return ("Opera��o bem-sucedida!");
		} else  {
			setMensagem("Erro.");
			return ("Falha na reserva.");
		}*/
		return ("ok");
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

	public String getDatahoraini() {
		return datahoraini;
	}

	public void setDatahoraini(String datahoraini) {
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
	// <-- FIM DOS GETTERS E SETTERS
	
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
