package bananaBeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.spinner.*;

@ManagedBean(name = "Controlador", eager = true)
@ApplicationScoped
public class Controlador {
	
	//Integer para armazenar último valor selecionado no spinner, separado do que vai ser colocado no BD
	private int spinvalue = 0;
	
	@ManagedProperty(value="#{cafeQPessoas}")
	private int cafeQPessoas = 0;
	
	public int getCafeQPessoas() {
		return cafeQPessoas;
	}

	public void setCafeQPessoas(int cafeQPessoas2) {
		if (isBoxCafe() == false) {
			this.cafeQPessoas = 0;
		} else {
		this.cafeQPessoas = cafeQPessoas2;
		}
		//System.out.println("Q. de pessoas para o café: "+this.cafeQPessoas);
	}
	
	@ManagedProperty(value="#{boxCafe}")
	private boolean boxCafe = false;
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

	private String mensagem = "";
	private String nome;

	public Controlador(){
		System.out.println("Controlador Chamado");
	}
	
	public void NovaReserva() {
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
	
	//Ambos valores são atribuídos, porém cafeQPessoas volta à 0 quando o botão de café é de-selecionado, o spinvalue serve para voltar o valor
	// caso a caixa seja selecionada novamente.
	public void cafeQPessoasChange(AjaxBehaviorEvent event) {
		Spinner spinner = (Spinner)event.getSource();
		int value = Integer.parseInt((String)spinner.getValue());
		setCafeQPessoas(value);
		setSpinvalue(value);
	}
	
	//Recebe o evento do botão de "Café?" e separa o botão de selecionado ou não
	public void cafeBoxChange(AjaxBehaviorEvent event) {
		if (((SelectBooleanCheckbox)event.getSource()).isSelected()) {
			setBoxCafe(true);
		} else {
			setBoxCafe(false);
		};
			
	}
}
