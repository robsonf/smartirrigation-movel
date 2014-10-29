package br.ifce.crato;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import br.ifce.crato.Agenda.Agendas;
import br.ifce.crato.R;

public class CadastroAgendas extends ListActivity {
	protected static final int INSERIR_EDITAR = 1;
	protected static final int BUSCAR = 2;
	public static final String CATEGORIA = "SMARTIRRIGATION";
	public static RepositorioAgenda repositorio;

	public static List<Agenda> agendas = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//verifica se já existe registros no banco local
		repositorio = new RepositorioAgendaScript(this);

		// retorna uma lista de agendas para exibir na tela
		agendas = repositorio.listarAgendas();

		atualizarLista();
		
		// Adaptador de lista customizado para cada linha de um agenda
		setListAdapter(new AgendaListAdapter(this, agendas));

		//lanca alarme para repetir chamada a cada 60 segundos
		agendar();
		
	}

	
	//Caso existam registros no servidor o banco local é atualizado
	public void atualizarLista() {

		ManipulaWebService mw = new ManipulaWebService();
		List<AgendaWS> as = mw.recuperarAgendaWSs();
		
		if(as!=null){
		  if(agendas!=null){
		    for (Agenda a : agendas) {
			 	if(a!=null){
						repositorio.deletar(a.id);
				}
			}	
		
		    for (AgendaWS agenda : as) {
			  if(agenda != null){
				ContentValues values = new ContentValues();
				values.put(Agendas.HORAINICIAL, agenda.getHoraInicial());
				values.put(Agendas.HORAFINAL, agenda.getHoraFinal());
				values.put(Agendas.ESTADO, agenda.isEstado()?"1":"0");
				values.put(Agendas.SEG, agenda.isSeg()?"1":"0");
				values.put(Agendas.TER, agenda.isTer()?"1":"0");
				values.put(Agendas.QUA, agenda.isQua()?"1":"0");
				values.put(Agendas.QUI, agenda.isQui()?"1":"0");
				values.put(Agendas.SEX, agenda.isSex()?"1":"0");
				values.put(Agendas.SAB, agenda.isSab()?"1":"0");
				values.put(Agendas.DOM, agenda.isDom()?"1":"0");

				values.put(Agendas.S1, "0");
				values.put(Agendas.S2, "0");
				values.put(Agendas.S3, "0");
				values.put(Agendas.S4, "0");

				List<SetorWS> setores = agenda.getSetores();
				if(setores !=null)
				for (SetorWS s : setores) {
					if(s!=null){
						switch (s.getId()) {
						case 1:
							values.remove(Agendas.S1);
							values.put(Agendas.S1, "1");
							break;
						case 2:
							values.remove(Agendas.S2);
							values.put(Agendas.S2, "1");
							break;
						case 3:
							values.remove(Agendas.S3);
							values.put(Agendas.S3, "1");
							break;
						case 4:
							values.remove(Agendas.S4);
							values.put(Agendas.S4, "1");
							break;
						}
					}
				}
				
				repositorio.inserir(values);
			}
		   }
		   agendas = repositorio.listarAgendas();
		  }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERIR_EDITAR, 0, "Inserir Novo").setIcon(R.drawable.novo);
		menu.add(0, BUSCAR, 0, "Buscar").setIcon(R.drawable.pesquisar);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// Clicou no menu
		switch (item.getItemId()) {
		case INSERIR_EDITAR:
			// Abre a tela com o formul�rio para adicionar
			startActivityForResult(new Intent(this, EditarAgenda.class), INSERIR_EDITAR);
			break;
		case BUSCAR:
			// Abre a tela para buscar o agenda pelo nome
			startActivity(new Intent(this, BuscarAgenda.class));
			break;
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int posicao, long id) {
		super.onListItemClick(l, v, posicao, id);
		editarAgenda(posicao);
	}

	// Recupera o id do agenda, e abre a tela de edi��o
	protected void editarAgenda(int posicao) {
		// Usu�rio clicou em algum agenda da lista
		// Recupera o agenda selecionado
		Agenda agenda = agendas.get(posicao);
		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, EditarAgenda.class);
		// Passa o id do agenda como par�metro
		it.putExtra(Agendas._ID, agenda.id);
		// Abre a tela de edi��o
		startActivityForResult(it, INSERIR_EDITAR);
	}

	@Override
	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarAgenda retornar, seja se foi para adicionar vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			atualizarLista();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(CATEGORIA, "onDestroy() - alarme cancelado.");
		Intent it = new Intent("EXECUTAR_ALARME");
		PendingIntent p = PendingIntent.getBroadcast(this, 0, it, 0);

		//Cancela o alarme
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(p);

		// Fecha o banco
		repositorio.fechar();
	}

	
	// Agenda o alarme para executar a cada x segundos
	private void agendar() {
		AlarmeMinuto.repositorio = repositorio; 
		AlarmeMinuto.telaAgendas = this;
		
		// Intent para disparar o BroadcastReceiver 'ReceberAlarme'
		Intent it = new Intent("EXECUTAR_ALARME");
		PendingIntent p = PendingIntent.getBroadcast(CadastroAgendas.this, 0, it, 0);

		// Agenda o alarme para ser acionado a cada x segundos
		int x = 10;
		AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		Calendar agora = Calendar.getInstance(); 
		alarme.setRepeating(AlarmManager.RTC_WAKEUP, agora.getTimeInMillis(), x*1000, p);
	}

}