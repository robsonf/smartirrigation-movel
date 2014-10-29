package br.ifce.crato;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Agenda {

	public static String[] colunas = new String[] { Agendas._ID, Agendas.HORAINICIAL, Agendas.HORAFINAL, Agendas.ESTADO , Agendas.SEG , Agendas.TER , Agendas.QUA , Agendas.QUI , Agendas.SEX , Agendas.SAB , Agendas.DOM , Agendas.S1 , Agendas.S2 , Agendas.S3 , Agendas.S4 };

	/**
	 * Pacote do Content Provider. Precisa ser �nico.
	 */
	public static final String AUTHORITY = "br.livro.android.provider.agenda";

	public long id;
	public String horaInicial;
	public String horaFinal;
	public int estado;
	public int seg;
	public int ter;
	public int qua;
	public int qui;
	public int sex;
	public int sab;
	public int dom;
	public int s1;
	public int s2;
	public int s3;
	public int s4;

	public Agenda() {
	}

	public Agenda(String horaInicial, String horaFinal, int estado) {
		super();
		this.horaInicial = horaInicial;
		this.horaFinal = horaFinal;
		this.estado = estado;
	}

	public Agenda(long id, String horaInicial, String horaFinal, int estado) {
		super();
		this.id = id;
		this.horaInicial = horaInicial;
		this.horaFinal = horaFinal;
		this.estado = estado;
	}

	public Agenda(long id, String horaInicial, String horaFinal, int estado, int seg, int ter, int qua, int qui, int sex, int sab, int dom, int s1, int s2, int s3, int s4) {
		super();
		this.id = id;
		this.horaInicial = horaInicial;
		this.horaFinal = horaFinal;
		this.estado = estado;
		this.seg = seg;
		this.ter = estado;
		this.qua = estado;
		this.qui = estado;
		this.sex = estado;
		this.sab = estado;
		this.dom = estado;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
	}
	
	public Agenda( String horaInicial, String horaFinal, int estado, int seg, int ter, int qua, int qui, int sex, int sab, int dom, int s1, int s2, int s3, int s4) {
		super();
		this.horaInicial = horaInicial;
		this.horaFinal = horaFinal;
		this.estado = estado;
		this.seg = seg;
		this.ter = estado;
		this.qua = estado;
		this.qui = estado;
		this.sex = estado;
		this.sab = estado;
		this.dom = estado;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
	}
	/**
	 * Classe interna para representar as colunas e ser utilizada por um Content
	 * Provider
	 * 
	 * Filha de BaseColumns que j� define (_id e _count), para seguir o padr�o
	 * Android
	 */
	public static final class Agendas implements BaseColumns {
	
		// N�o pode instanciar esta Classe
		private Agendas() {
		}
	
		// content://br.livro.android.provider.agenda/agendas
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/agendas");
	
		// Mime Type para todos os agendas
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.agendas";
	
		// Mime Type para um �nico agenda
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.agendas";
	
		// Ordena��o default para inserir no order by
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
	
		public static final String HORAINICIAL = "horaInicial";
		public static final String HORAFINAL = "horaFinal";
		public static final String ESTADO = "estado";
		public static final String SEG = "seg";
		public static final String TER = "ter";
		public static final String QUA = "qua";
		public static final String QUI = "qui";
		public static final String SEX = "sex";
		public static final String SAB = "sab";
		public static final String DOM = "dom";
		public static final String S1 = "s1";
		public static final String S2 = "s2";
		public static final String S3 = "s3";
		public static final String S4 = "s4";

	
		// M�todo que constr�i uma Uri para um Carro espec�fico, com o seu id
		// A Uri � no formato "content://br.livro.android.provider.agenda/agendas/id"
		public static Uri getUriId(long id) {
			// Adiciona o id na URI default do /agendas
			Uri uriAgenda = ContentUris.withAppendedId(Agendas.CONTENT_URI, id);
			return uriAgenda;
		}
	}

	@Override
	public String toString() {
		return "Hora Inicial: " + horaInicial + ", Hora Final: " + horaFinal + ", Estado: " + estado;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getSeg() {
		return seg;
	}

	public void setSeg(int seg) {
		this.seg = seg;
	}

	public int getTer() {
		return ter;
	}

	public void setTer(int ter) {
		this.ter = ter;
	}

	public int getQua() {
		return qua;
	}

	public void setQua(int qua) {
		this.qua = qua;
	}

	public int getQui() {
		return qui;
	}

	public void setQui(int qui) {
		this.qui = qui;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSab() {
		return sab;
	}

	public void setSab(int sab) {
		this.sab = sab;
	}

	public int getDom() {
		return dom;
	}

	public void setDom(int dom) {
		this.dom = dom;
	}

	public int getS1() {
		return s1;
	}

	public void setS1(int s1) {
		this.s1 = s1;
	}

	public int getS2() {
		return s2;
	}

	public void setS2(int s2) {
		this.s2 = s2;
	}

	public int getS3() {
		return s3;
	}

	public void setS3(int s3) {
		this.s3 = s3;
	}

	public int getS4() {
		return s4;
	}

	public void setS4(int s4) {
		this.s4 = s4;
	}
	
	
}
