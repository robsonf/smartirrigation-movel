package br.ifce.crato;

import br.ifce.crato.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class BuscarAgenda extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.form_buscar_agenda);

		ImageButton btBuscar = (ImageButton) findViewById(R.id.btBuscar);
		btBuscar.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Cancela para n�o ficar nada pendente na tela
		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View view) {

		EditText horaInicial = (EditText) findViewById(R.id.campoHoraInicial);
		EditText horaFinal = (EditText) findViewById(R.id.campoHoraFinal);
		EditText estado = (EditText) findViewById(R.id.campoEstado);

		String sHoraInicial = horaInicial.getText().toString();

		Agenda agenda = buscarAgenda(sHoraInicial);

		if (agenda != null) {
			// Atualiza os campos com o resultado
			horaFinal.setText(agenda.horaFinal);
			estado.setText(String.valueOf(agenda.estado));
		} else {
			// Limpa os campos
			horaFinal.setText("");
			estado.setText("");

			Toast.makeText(BuscarAgenda.this, "Nenhuma agenda encontrada", Toast.LENGTH_SHORT).show();
		}
	}

	protected Agenda buscarAgenda(String nomeCarro) {
		Agenda agenda = CadastroAgendas.repositorio.buscarAgendaPorHoraInicial(nomeCarro);
		return agenda;
	}
}
