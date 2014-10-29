package br.ifce.crato;

public class SetorWS {
	private String descricao;
	private int id;
	public SetorWS() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SetorWS(String descricao, int id) {
		super();
		this.descricao = descricao;
		this.id = id;
	}
	// getters e setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
}
