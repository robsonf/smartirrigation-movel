package br.ifce.crato;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmeMinuto extends BroadcastReceiver {
	public static CadastroAgendas telaAgendas = null;
	
	private static Integer port;
    /** The BluetoothAdapter is the gateway to all bluetooth functions **/
    protected BluetoothAdapter bluetoothAdapter = null;

    /** We will write our message to the socket **/
    protected BluetoothSocket socket = null;

    /** The Bluetooth is an external device, which will receive our message **/
    BluetoothDevice blueToothDevice = null;

    private static final String CATEGORIA = "livro";
	public static EstadoSistema estadoSistema;
	public static RepositorioAgenda repositorio;
	public Context contexto;
	@Override
	public void onReceive(Context context, Intent intent) {
		contexto = context;
		Log.i(CATEGORIA, "Alarme disparado!");
		
		Calendar agora = Calendar.getInstance();
		if(repositorio!=null){
		List<Agenda> agendas = repositorio.listarAgendas();
		EstadoSistema.setor = new boolean[4];
		EstadoSistema.msg = "abcd";			
		
		for (Agenda a : agendas) {
		  if(a!=null){
			  if(a.estado==1 && verificaDiaDaSemana(a)){
				if(agora.getTimeInMillis() >= calculaPeriodo(a.getHoraInicial()) && agora.getTimeInMillis() <= calculaPeriodo(a.getHoraFinal())){
					if(a.s1 == 1){
						EstadoSistema.setor[0] = true;
						EstadoSistema.msg = EstadoSistema.msg.replaceFirst("a", "A");
					}
					if(a.s2 == 1){
						EstadoSistema.msg = EstadoSistema.msg.replaceFirst("b", "B");
						EstadoSistema.setor[1] = true;
					}
					if(a.s3 == 1){
						EstadoSistema.msg = EstadoSistema.msg.replaceFirst("c", "C");
						EstadoSistema.setor[2] = true;
					}
					if(a.s4 == 1){
						EstadoSistema.msg = EstadoSistema.msg.replaceFirst("d", "D");
						EstadoSistema.setor[3] = true;
					}
				}
			  }
			}
		  }
		}
		

//		Toast.makeText(context, "Passou agendas", Toast.LENGTH_LONG).show();
		

		//Caso pelo menos um setor ligado o estado da bomba vai para ligada (valor "1" ligada, "0" desligada)
		if(EstadoSistema.msg.equals("abcd"))
			EstadoSistema.msg += "0";
		else
			EstadoSistema.msg += "1";

		String s2 = "";
		for (boolean s1 : EstadoSistema.setor) {
			s2 += (s1?"1 ":"0 ");
		}
		Toast.makeText(contexto, "Setores = "  + EstadoSistema.msg , Toast.LENGTH_LONG).show();

		//envia mensagem de estado do sistema para o dispositivo Bluetooth
		acionaBluetooth();
		
//		//atualiza banco local e telas com os registros do servidor
//		if(telaAgendas!=null)
//			telaAgendas.atualizarLista();
	}
	
	private static long calculaPeriodo(String hora) {
		Calendar ci = Calendar.getInstance();
		String [] shi = hora.split("\\:");
		int ihi = Integer.valueOf(shi[0]);
		int imi = Integer.valueOf(shi[1]);
		ci.set(Calendar.HOUR_OF_DAY, ihi);
		ci.set(Calendar.MINUTE, imi);
		return ci.getTimeInMillis();
	}
	
	private boolean verificaDiaDaSemana(Agenda a){
		boolean retorno = false;
		Calendar agora = Calendar.getInstance();
		if(a.dom == 1 && agora.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			retorno = true;
		}
		if(a.seg == 1 && agora.get(Calendar.DAY_OF_WEEK)  == Calendar.MONDAY){
			retorno = true;
		}
		if(a.ter == 1 && agora.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
			retorno = true;
		}
		if(a.qua == 1 && agora.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
			retorno = true;
		}
		if(a.qui == 1 && agora.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
			retorno = true;
		}
		if(a.sex == 1 && agora.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
			retorno = true;
		}
		if(a.sab == 1 && agora.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			retorno = true;
		}
		return retorno;
	}

/*
 *  METODO USADO DURANTE OS TESTES UNITARIOS
 */
/*	public static Agenda agendaTeste(String hInicial, String hFinal, int estado) {
		Agenda a = new Agenda();
		a.setId(1);
		a.setHoraInicial(hInicial);
		a.setHoraFinal(hFinal);
		a.setEstado(1);
		a.setSeg(1);
		a.setTer(1);
		a.setQua(1);
		a.setQui(1);
		a.setSex(1);
		a.setSab(1);
		a.setDom(1);
		a.setS1(estado);
		a.setS2(1);
		a.setS3(estado);
		a.setS4(1);
		return a;
	}*/

	
	public void acionaBluetooth(){
	    
//			Toast.makeText(contexto, "Debug 0", Toast.LENGTH_LONG).show();
	        // Grab the BlueToothAdapter. The first line of most bluetooth programs.
	        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	        if (bluetoothAdapter == null) {
//	        	Toast.makeText(contexto, "Sem adaptador bluetooth ", Toast.LENGTH_LONG).show();
	            Log.e(this.toString(), "Bluetooth Not Available.");
	            return;
	        }else{
		        blueToothDevice = bluetoothAdapter.getRemoteDevice("00:12:03:02:75:06");
		        port = Integer.valueOf(1);
		        // The documents tell us to cancel the discovery process.
//		    	Toast.makeText(contexto, "Debug 1", Toast.LENGTH_LONG).show();
		        atualizarHardware();

		//        for (Integer port = 1; port <= 3; port++) {
		//            simpleComm(Integer.valueOf(port));
		//        }
	        }

	    }


	    public void atualizarHardware() {
	        bluetoothAdapter.cancelDiscovery();
	    	
//	    	Toast.makeText(contexto, "Passou Cancel ", Toast.LENGTH_LONG).show();

	        Log.d(this.toString(), "Port = " + port);
	        try {
	            Method m = blueToothDevice.getClass().getMethod(
	                    "createRfcommSocket", new Class[] { int.class });
	            socket = (BluetoothSocket) m.invoke(blueToothDevice, port);

	            // debug check to ensure socket was set.
	            assert (socket != null) : "Socket is Null";

	            // attempt to connect to device
	            socket.connect();
	            try {
	                Log.d(this.toString(),
	                        "************ CONNECTION SUCCEES! *************");
	                OutputStream outputStream = socket.getOutputStream();
	                
//	            	Toast.makeText(contexto, "Debug 2 ", Toast.LENGTH_LONG).show();
	                if(EstadoSistema.msg!=null){
	                    outputStream.write(EstadoSistema.msg.getBytes());
	                }
//	            	Toast.makeText(contexto, "Debug 3 ", Toast.LENGTH_LONG).show();
	            } finally {
	                // close the socket and we are done.
	                socket.close();
	            }
	            // IOExcecption is thrown if connect fails.
	        } catch (IOException ex) {
//	        	Toast.makeText(contexto, "Debug 5 ", Toast.LENGTH_LONG).show();
	            Log.e(this.toString(), "IOException " + ex.getMessage());
	            // NoSuchMethodException IllegalAccessException
	            // InvocationTargetException
	            // are reflection exceptions.
	        } catch (NoSuchMethodException ex) {
//	        	Toast.makeText(contexto, "Debug 6 " + ex.getMessage(), Toast.LENGTH_LONG).show();
	            Log.e(this.toString(), "NoSuchMethodException " + ex.getMessage());
	        } catch (IllegalAccessException ex) {
//	        	Toast.makeText(contexto, "Debug 7 "+ ex.getMessage(), Toast.LENGTH_LONG).show();
	            Log.e(this.toString(), "IllegalAccessException " + ex.getMessage());
	        } catch (InvocationTargetException ex) {
//	        	Toast.makeText(contexto, "Debug 8 " + ex.getMessage(), Toast.LENGTH_LONG).show();
	            Log.e(this.toString(),
	                    "InvocationTargetException " + ex.getMessage());
	        }
	    }

}
