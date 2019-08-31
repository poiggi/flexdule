package flexdule.desktop.control;

import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexdule.desktop.connection.Server;
import flexdule.desktop.connection.Server.ServerListener;
import flexdule.desktop.view.MainView;
import flexdule.desktop.view.MainView.MainViewListener;

public class MainControl implements MainViewListener, ServerListener {
	private static Logger log = LoggerFactory.getLogger(MainView.class);

	protected ControlContext controlContext;
	protected MainView mainView;

	Server server;

	public MainControl(ControlContext controlContext) {
		this.controlContext = controlContext;
		mainView = new MainView();
		mainView.addListener(this);
		
		connect();
	}

	public void getControl() {
		ControlContext.replaceFramePanel(controlContext.getFrame(), mainView, "Flexdule Desktop");
	}

	@Override
	public void atAction() {
		log.info("DOING atAction()");
		mainView.setLabelMessage(":)", 1000);
	}

	@Override
	public void atReconnect() {
		log.info("DOING atReconnect()");
		connect();
	}

	public void connect() {
		log.info("BEGIN connect()");
		
		mainView.setLabelMessage("Conectando a la red...", 60000);

		if (findIp() && startServer()) {
			mainView.setLabelMessage("Conexión a la red exitosa", 3000);
		} else {
			mainView.setLabelMessage("Conexión a la red fallida", 3000);
		}

	}

	public boolean findIp() {
		boolean ok = false;
		String ip = null;
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			ip = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			log.error("ERROR in findIp(): " + e);
			e.printStackTrace();
		}
		if (ip != null) {
			mainView.setLabelIp(ip);
			ok = true;
		}else {
			mainView.setLabelIp("Sin acceso a la red");
		}
		
		return ok;
	}

	public boolean startServer() {
		boolean ok = false;
		Thread t = null;
		boolean inUse = false;

		try {

			if (server == null) {
				server = new Server(4400);
				server.addListener(this);
			}
			if (server.getServerSocket() != null && !server.getServerSocket().isClosed()) {
				server.closeServer();
			}
			t = server.startListen();
		} catch (BindException e) {
			log.error("ERROR in startServer(): " + e);
			inUse = true;
			e.printStackTrace();
		} catch (Exception e) {
			log.error("ERROR in startServer(): " + e);
			e.printStackTrace();
		}

		if (inUse) {
			mainView.setLabelServerState("El canal de escucha está ocupado. Quizás la aplicación ya esté abierta.");
		} else if (t != null && t.isAlive()) {
			ok = true;
			mainView.setLabelServerState("Escuchando peticiones");
		} else {
			mainView.setLabelServerState("No es posible atender peticiones");
		}
		return ok;
	}

	@Override
	public void onHelloService(Boolean hello) {
		log.info("DOING onHelloService(). hello= " + hello);

		if (hello == true) {
			mainView.setLabelMessage("Conexión con dispositivo móvil exitosa!", 5000);
		}

	}

	@Override
	public void onSendSchedulesService(String fileName) {
		log.info("DOING onHelloService(). fileNmae= " + fileName);
		mainView.setLabelMessage("Se ha exportado con éxito un horario: " + fileName,
				60000);
	}

}
