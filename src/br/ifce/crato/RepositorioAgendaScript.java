package br.ifce.crato;

import android.content.Context;

public class RepositorioAgendaScript extends RepositorioAgenda {

	// Script para fazer drop na tabela
	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS agenda";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_DATABASE_CREATE = new String[] {
			"create table agenda ( _id integer primary key autoincrement, horaInicial text not null,horaFinal text not null,estado integer not null ,seg integer not null,ter integer not null,qua integer not null,qui integer not null,sex integer not null,sab integer not null,dom integer not null,s1 integer not null,s2 integer not null,s3 integer not null,s4 integer not null);",
			"insert into agenda(horaInicial,horaFinal,estado,seg,ter,qua,qui,sex,sab,dom,s1,s2,s3,s4) values('22:00','23:00',1,1,1,1,1,1,1,1,1,1,1,1);",
			"insert into agenda(horaInicial,horaFinal,estado,seg,ter,qua,qui,sex,sab,dom,s1,s2,s3,s4) values('23:01','00:00',1,1,1,1,1,1,1,1,1,1,1,1);",
			"insert into agenda(horaInicial,horaFinal,estado,seg,ter,qua,qui,sex,sab,dom,s1,s2,s3,s4) values('00:01','01:00',1,1,1,1,1,1,1,1,1,1,1,1);" };

	// Nome do banco
	private static final String NOME_BANCO = "livro_android";

	// Controle de vers�o
	private static final int VERSAO_BANCO = 1;

	// Nome da tabela
	public static final String TABELA_AGENDA = "agenda";

	// Classe utilit�ria para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	// Cria o banco de dados com um script SQL
	public RepositorioAgendaScript(Context ctx) {
		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, RepositorioAgendaScript.NOME_BANCO, RepositorioAgendaScript.VERSAO_BANCO,
				RepositorioAgendaScript.SCRIPT_DATABASE_CREATE, RepositorioAgendaScript.SCRIPT_DATABASE_DELETE);

		// abre o banco no modo escrita para poder alterar tamb�m
		db = dbHelper.getWritableDatabase();
	}

	// Fecha o banco
	@Override
	public void fechar() {
		super.fechar();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
