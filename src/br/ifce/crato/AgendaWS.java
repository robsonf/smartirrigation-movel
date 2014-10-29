package br.ifce.crato;
import java.util.List;

public class AgendaWS {
	private int id;
	private String horaInicial;
	private String horaFinal;
	private boolean estado;
	private boolean seg;
	private boolean ter;
	private boolean qua;
	private boolean qui;
	private boolean sex;
	private boolean sab;
	private boolean dom;
	private List<SetorWS> setores;
	// getters e setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHoraInicial() {
		return horaInicial;
	}
	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}
	public String getHoraFinal() {
		return horaFinal;
	}
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public boolean isSeg() {
		return seg;
	}
	public void setSeg(boolean seg) {
		this.seg = seg;
	}
	public boolean isTer() {
		return ter;
	}
	public void setTer(boolean ter) {
		this.ter = ter;
	}
	public boolean isQua() {
		return qua;
	}
	public void setQua(boolean qua) {
		this.qua = qua;
	}
	public boolean isQui() {
		return qui;
	}
	public void setQui(boolean qui) {
		this.qui = qui;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public boolean isSab() {
		return sab;
	}
	public void setSab(boolean sab) {
		this.sab = sab;
	}
	public boolean isDom() {
		return dom;
	}
	public void setDom(boolean dom) {
		this.dom = dom;
	}
	public List<SetorWS> getSetores() {
		return setores;
	}
	public void setSetores(List<SetorWS> setores) {
		this.setores = setores;
	}

	
}
