package br.ifce.crato;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import br.ifce.crato.Agenda.Agendas;

public class RepositorioAgenda {
	private static final String CATEGORIA = "livro";

	// Nome do banco
	private static final String NOME_BANCO = "livro_android";
	// Nome da tabela
	public static final String NOME_TABELA = "agenda";

	protected SQLiteDatabase db;

	public RepositorioAgenda(Context ctx) {
		// Abre o banco de dados j� existente
		db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	}

	protected RepositorioAgenda() {
		// Apenas para criar uma subclasse...
	}

	// Salva o agenda, insere um novo ou atualiza
	public long salvar(Agenda agenda) {
		long id = agenda.id;

		if (id != 0) {
			atualizar(agenda);
		} else {
			// Insere novo
			id = inserir(agenda);
		}

		return id;
	}

	// Insere um novo agenda
	public long inserir(Agenda agenda) {
		ContentValues values = new ContentValues();
		values.put(Agendas.HORAINICIAL, agenda.horaInicial);
		values.put(Agendas.HORAFINAL, agenda.horaFinal);
		values.put(Agendas.ESTADO, agenda.estado);
		values.put(Agendas.SEG, agenda.seg);
		values.put(Agendas.TER, agenda.ter);
		values.put(Agendas.QUA, agenda.qua);
		values.put(Agendas.QUI, agenda.qui);
		values.put(Agendas.SEX, agenda.sex);
		values.put(Agendas.SAB, agenda.sab);
		values.put(Agendas.DOM, agenda.dom);
		values.put(Agendas.S1, agenda.s1);
		values.put(Agendas.S2, agenda.s2);
		values.put(Agendas.S3, agenda.s3);
		values.put(Agendas.S4, agenda.s4);
		
		long id = inserir(values);
		return id;
	}

	// Insere um novo agenda
	public long inserir(ContentValues valores) {
		long id = db.insert(NOME_TABELA, "", valores);
		return id;
	}

	// Atualiza o agenda no banco. O id do agenda � utilizado.
	public int atualizar(Agenda agenda) {
		ContentValues values = new ContentValues();
		values.put(Agendas.HORAINICIAL, agenda.horaInicial);
		values.put(Agendas.HORAFINAL, agenda.horaFinal);
		values.put(Agendas.ESTADO, agenda.estado);
		values.put(Agendas.SEG, agenda.seg);
		values.put(Agendas.TER, agenda.ter);
		values.put(Agendas.QUA, agenda.qua);
		values.put(Agendas.QUI, agenda.qui);
		values.put(Agendas.SEX, agenda.sex);
		values.put(Agendas.SAB, agenda.sab);
		values.put(Agendas.DOM, agenda.dom);
		values.put(Agendas.S1, agenda.s1);
		values.put(Agendas.S2, agenda.s2);
		values.put(Agendas.S3, agenda.s3);
		values.put(Agendas.S4, agenda.s4);

		String _id = String.valueOf(agenda.id);

		String where = Agendas._ID + "=?";
		String[] whereArgs = new String[] { _id };

		int count = atualizar(values, where, whereArgs);

		return count;
	}

	// Atualiza o agenda com os valores abaixo
	// A cl�usula where � utilizada para identificar o agenda a ser atualizado
	public int atualizar(ContentValues valores, String where, String[] whereArgs) {
		int count = db.update(NOME_TABELA, valores, where, whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}

	// Deleta o agenda com o id fornecido
	public int deletar(long id) {
		String where = Agendas._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int count = deletar(where, whereArgs);

		return count;
	}

	// Deleta o agenda com os argumentos fornecidos
	public int deletar(String where, String[] whereArgs) {
		int count = db.delete(NOME_TABELA, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}

	// Busca o agenda pelo id
	public Agenda buscarAgenda(long id) {
		// select * from agenda where _id=?
		Cursor c = db.query(true, NOME_TABELA, Agenda.colunas, Agendas._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			Agenda agenda = new Agenda();

			// L� os dados
			agenda.id = c.getLong(0);
			agenda.horaInicial = c.getString(1);
			agenda.horaFinal = c.getString(2);
			agenda.estado = c.getInt(3);
			agenda.seg = c.getInt(4);
			agenda.ter = c.getInt(5);
			agenda.qua = c.getInt(6);
			agenda.qui = c.getInt(7);
			agenda.sex = c.getInt(8);
			agenda.sab = c.getInt(9);
			agenda.dom = c.getInt(10);
			agenda.s1 = c.getInt(11);
			agenda.s2 = c.getInt(12);
			agenda.s3  = c.getInt(13);
			agenda.s4 = c.getInt(14);

			return agenda;
		}

		return null;
	}

	// Retorna um cursor com todos os agendas
	public Cursor getCursor() {
		try {
			// select * from agendas
			return db.query(NOME_TABELA, Agenda.colunas, null, null, null, null, null, null);
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os agendas: " + e.toString());
			return null;
		}
	}

	// Retorna uma lista com todos os agendas
	public List<Agenda> listarAgendas() {
		Cursor c = getCursor();

		List<Agenda> agendas = new ArrayList<Agenda>();

		if (c.moveToFirst()) {

			// Recupera os �ndices das colunas
			int idxId = c.getColumnIndex(Agendas._ID);
			int idxHoraInicial = c.getColumnIndex(Agendas.HORAINICIAL);
			int idxHoraFinal = c.getColumnIndex(Agendas.HORAFINAL);
			int idxEstado = c.getColumnIndex(Agendas.ESTADO);
			int idxSeg = c.getColumnIndex(Agendas.SEG);
			int idxTer = c.getColumnIndex(Agendas.TER);
			int idxQua = c.getColumnIndex(Agendas.QUA);
			int idxQui = c.getColumnIndex(Agendas.QUI);
			int idxSex = c.getColumnIndex(Agendas.SEX);
			int idxSab = c.getColumnIndex(Agendas.SAB);
			int idxDom = c.getColumnIndex(Agendas.DOM);
			int idxS1 = c.getColumnIndex(Agendas.S1);
			int idxS2 = c.getColumnIndex(Agendas.S2);
			int idxS3 = c.getColumnIndex(Agendas.S3);
			int idxS4 = c.getColumnIndex(Agendas.S4);

			// Loop at� o final
			do {
				Agenda agenda = new Agenda();
				agendas.add(agenda);

				// recupera os atributos de agenda
				agenda.id = c.getLong(idxId);
				agenda.horaInicial = c.getString(idxHoraInicial);
				agenda.horaFinal = c.getString(idxHoraFinal);
				agenda.estado = c.getInt(idxEstado);
				agenda.seg = c.getInt(idxSeg);
				agenda.ter = c.getInt(idxTer);
				agenda.qua = c.getInt(idxQua);
				agenda.qui = c.getInt(idxQui);
				agenda.sex = c.getInt(idxSex);
				agenda.sab = c.getInt(idxSab);
				agenda.dom = c.getInt(idxDom);
				agenda.s1 = c.getInt(idxS1);
				agenda.s2 = c.getInt(idxS2);
				agenda.s3 = c.getInt(idxS3);
				agenda.s4 = c.getInt(idxS4);

			} while (c.moveToNext());
		}

		return agendas;
	}

	// Busca o agenda pelo nome "select * from agenda where nome=?"
	public Agenda buscarAgendaPorHoraInicial(String nome) {
		Agenda agenda = null;

		try {
			// Idem a: SELECT _id,nome,placa,ano from CARRO where nome = ?
			Cursor c = db.query(NOME_TABELA, Agenda.colunas, Agendas.HORAINICIAL + "='" + nome + "'", null, null, null, null);

			// Se encontrou...
			if (c.moveToNext()) {

				agenda = new Agenda();

				// utiliza os m�todos getLong(), getString(), getInt(), etc para recuperar os valores
				agenda.id = c.getLong(0);
				agenda.horaInicial = c.getString(1);
				agenda.horaFinal = c.getString(2);
				agenda.estado = c.getInt(3);
				agenda.seg = c.getInt(4);
				agenda.ter = c.getInt(5);
				agenda.qua = c.getInt(6);
				agenda.qui = c.getInt(7);
				agenda.sex = c.getInt(8);
				agenda.sab = c.getInt(9);
				agenda.dom = c.getInt(10);
				agenda.s1 = c.getInt(11);
				agenda.s2 = c.getInt(12);
				agenda.s3 = c.getInt(13);
				agenda.s4 = c.getInt(14);
			}
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar o agenda pelo nome: " + e.toString());
			return null;
		}

		return agenda;
	}

	// Busca um agenda utilizando as configura��es definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider de agenda
	public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
		return c;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
