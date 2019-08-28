package desktop.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	private static Logger log = LoggerFactory.getLogger(Server.class);

	public static final int KEY_SHORT = 0;
	public static final int KEY_MEDIUM = 1;
	public static final int KEY_LONG = 2;



	protected int port;
	protected boolean stop;

	public Server(int port) {
		this.port = port;
	}

	ServerSocket serverSocket;

	public void startListen() throws Exception {
		log.info("BEGIN startListen(). port=" + port);

		try {
			serverSocket = new ServerSocket(port);
			log.info("Services started");

			new Thread(new Runnable() {
				@Override
				public void run() {

					while (!stop) {
						try {
							Socket socket = null;
							socket = serverSocket.accept();
							// socketServidor.setSoLinger(true, 1);
							new ServerThread(socket).start();
						} catch (Exception e) {
							log.error("Exception in [Thread]startListen(): " + e);
						}
					}
					closeServer();
				}
			}).start();

		} catch (Exception e) {
			log.error("Error in startListen(): " + e);
			closeServer();
			throw e;
		}
		log.info("END startListen()");
	}

	public void thisIsLastService() {
		log.info("DOING thisIsLastService()");
		stop = true;
	}

	public void closeServer() {
		log.info("DOING closeServer()");
		try {
			serverSocket.close();
			log.info("DONE closeServer()");
		} catch (IOException e) {
			log.error("error closing serverSocket: " + e);
			e.printStackTrace();
		}
	}
}
