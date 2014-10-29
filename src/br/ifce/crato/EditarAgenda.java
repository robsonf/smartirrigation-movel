package br.ifce.crato;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import br.ifce.crato.Agenda.Agendas;

public class EditarAgenda extends Activity {
	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;

	// Campos texto
	private TimePicker campoHoraInicial;
	private TimePicker campoHoraFinal;
	private ToggleButton campoEstado;
	
	private CheckBox campoSeg;
	private CheckBox campoTer;
	private CheckBox campoQua;
	private CheckBox campoQui;
	private CheckBox campoSex;
	private CheckBox campoSab;
	private CheckBox campoDom;
	private CheckBox campoS1;
	private CheckBox campoS2;
	private CheckBox campoS3;
	private CheckBox campoS4;

	private Long id;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.form_editar_agenda);
		campoHoraInicial = (TimePicker)findViewById(R.id.campoHoraInicialNovo);
		campoHoraInicial.setIs24HourView(true);
		
		campoHoraFinal = (TimePicker)findViewById(R.id.campoHoraFinalNovo);
		campoHoraFinal.setIs24HourView(true);

		campoEstado = (ToggleButton) findViewById(R.id.campoEstadoNovo);
		
		campoSeg = (CheckBox) findViewById(R.id.campoSeg);
		campoTer = (CheckBox) findViewById(R.id.campoTer);
		campoQua = (CheckBox) findViewById(R.id.campoQua);
		campoQui = (CheckBox) findViewById(R.id.campoQui);
		campoSex = (CheckBox) findViewById(R.id.campoSex);
		campoSab = (CheckBox) findViewById(R.id.campoSab);
		campoDom = (CheckBox) findViewById(R.id.campoDom);
		campoS1 = (CheckBox) findViewById(R.id.campoS1);
		campoS2 = (CheckBox) findViewById(R.id.campoS2);
		campoS3 = (CheckBox) findViewById(R.id.campoS3);
		campoS4 = (CheckBox) findViewById(R.id.campoS4);

		id = null;

		Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id = extras.getLong(Agendas._ID);

			if (id != null) {
				// � uma edi��o, busca o agenda...
				Agenda c = buscarAgenda(id);
				campoHoraInicial.setCurrentHour(Integer.valueOf(retornaHora(c.horaInicial)));
				campoHoraInicial.setCurrentMinute(Integer.valueOf(retornaMinuto(c.horaInicial)));
				campoHoraFinal.setCurrentHour(Integer.valueOf(retornaHora(c.horaFinal)));
				campoHoraFinal.setCurrentMinute(Integer.valueOf(retornaMinuto(c.horaFinal)));
				
				campoEstado.setChecked(c.estado==1);
				campoSeg.setChecked(c.seg==1);
				campoTer.setChecked(c.ter==1);
				campoQua.setChecked(c.qua==1);
				campoQui.setChecked(c.qui==1);
				campoSex.setChecked(c.sex==1);
				campoSab.setChecked(c.sab==1);
				campoDom.setChecked(c.dom==1);
				campoS1.setChecked(c.s1==1);
				campoS2.setChecked(c.s2==1);
				campoS3.setChecked(c.s3==1);
				campoS4.setChecked(c.s4==1);
			}
		}

		ImageButton btCancelar = (ImageButton) findViewById(R.id.btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				// Fecha a tela
				finish();
			}
		});

		// Listener para salvar o agenda
		ImageButton btSalvar = (ImageButton) findViewById(R.id.btSalvar);
		btSalvar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				salvar();
				atualizarWS();
			}
		});

		ImageButton btExcluir = (ImageButton) findViewById(R.id.btExcluir);

		if (id == null) {
			// Se id est� nulo, n�o pode excluir
			btExcluir.setVisibility(View.INVISIBLE);
		} else {
			// Listener para excluir o agenda
			btExcluir.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					excluir();
					atualizarWS();
				}
			});
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Cancela para n�o ficar nada na tela pendente
		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();
	}

	public void salvar() {

		int estado = 0, seg = 0, ter = 0, qua = 0, qui = 0, sex = 0, sab = 0, dom = 0, s1 = 0, s2 = 0, s3 = 0, s4 = 0;
		try {
			estado = campoEstado.isChecked()?1:0;
//			estado = Integer.parseInt(campoEstado.getText().toString());
			seg = campoSeg.isChecked()?1:0;
			ter = campoTer.isChecked()?1:0;
			qua = campoQua.isChecked()?1:0;
			qui = campoQui.isChecked()?1:0;
			sex = campoSex.isChecked()?1:0;
			sab = campoSab.isChecked()?1:0;
			dom = campoDom.isChecked()?1:0;
			s1 = campoS1.isChecked()?1:0;
			s2 = campoS2.isChecked()?1:0;
			s3 = campoS3.isChecked()?1:0;
			s4 = campoS4.isChecked()?1:0;
		} catch (NumberFormatException e) {
			// ok neste exemplo, tratar isto em aplica��es reais
		}

		Agenda agenda = new Agenda();
		agenda.id = 0;
		if (id != null) {
			// � uma atualiza��o
			agenda.id = id;
		}
		int hInicial = campoHoraInicial.getCurrentHour();
		String shInicial = (hInicial<10?"0":"")+ String.valueOf(hInicial);
		int mInicial = campoHoraInicial.getCurrentMinute();
		String smInicial = (mInicial<10?"0":"")+ String.valueOf(mInicial);
		int hFinal = campoHoraFinal.getCurrentHour();
		String shFinal = (hFinal<10?"0":"")+ String.valueOf(hFinal);
		int mFinal = campoHoraFinal.getCurrentMinute();
		String smFinal = (mFinal<10?"0":"")+ String.valueOf(mFinal);
		
//		Toast.makeText(this.getBaseContext(), " " + hora + ":" + min , Toast.LENGTH_LONG).show();
		  
		agenda.horaInicial =  shInicial + ":" + smInicial; 
		agenda.horaFinal = shFinal + ":" + smFinal;
		agenda.estado = estado;
		agenda.seg = seg;
		agenda.ter = ter;
		agenda.qua = qua;
		agenda.qui = qui;
		agenda.sex = sex;
		agenda.sab = sab;
		agenda.dom = dom;
		agenda.s1 = s1;
		agenda.s2 = s2;
		agenda.s3 = s3;
		agenda.s4 = s4;

		// Salvar
		salvarAgenda(agenda);

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	public void excluir() {
		if (id != null) {
			excluirAgenda(id);
		}

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	// Buscar o agenda pelo id
	protected Agenda buscarAgenda(long id) {
		return CadastroAgendas.repositorio.buscarAgenda(id);
	}

	// Salvar o agenda
	protected void salvarAgenda(Agenda agenda) {
		CadastroAgendas.repositorio.salvar(agenda);
	}

	private void atualizarWS() {
		ManipulaWebService mw = new ManipulaWebService();
		if(mw.apagarTodasAgendaWSs()){
			List<Agenda> as = CadastroAgendas.repositorio.listarAgendas();
			if(as!=null){
				for (Agenda a : as) {
					if(a != null){
						AgendaWS aw = new AgendaWS();
						aw.setId((int)a.getId());
						aw.setHoraInicial(a.getHoraInicial());
						aw.setHoraFinal(a.getHoraFinal());
						aw.setEstado(a.getEstado()==1?true:false);
						aw.setSeg(a.getSeg()==1?true:false);
						aw.setTer(a.getTer()==1?true:false);
						aw.setQua(a.getQua()==1?true:false);
						aw.setQui(a.getQui()==1?true:false);
						aw.setSex(a.getSex()==1?true:false);
						aw.setSab(a.getSab()==1?true:false);
						aw.setDom(a.getDom()==1?true:false);
						
						List<SetorWS> setores = new ArrayList<SetorWS>();
						if(a.getS1() == 1)
							setores.add(new SetorWS("setor1", 1));
						if(a.getS2() == 1)
							setores.add(new SetorWS("setor2", 2));
						if(a.getS3() == 1)
							setores.add(new SetorWS("setor3", 3));
						if(a.getS4() == 1)
							setores.add(new SetorWS("setor4", 4));
						aw.setSetores(setores);
						mw.adicionarAgendaWS(aw);
					}
				}
			}
		}
	}

	// Excluir o agenda
	protected void excluirAgenda(long id) {
		CadastroAgendas.repositorio.deletar(id);
	}

	private static int retornaHora(String hora) {
		String [] shi = hora.split("\\:");
		int ihi = Integer.valueOf(shi[0]);
		return ihi;
	}
	
	private static int retornaMinuto(String hora) {
		String [] shi = hora.split("\\:");
		int imi = Integer.valueOf(shi[1]);
		return imi;
	}
}
