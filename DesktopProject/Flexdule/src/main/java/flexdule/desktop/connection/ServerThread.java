package flexdule.desktop.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexdule.core.dtos.ScheduleWithActivities;
import flexdule.desktop.model.daos.SchedulesWithActivitiesXmlDao;

public class ServerThread extends Thread {
	private static Logger log = LoggerFactory.getLogger(Server.class);

	public static final int HELLO = 0;
	public static final int SEND_SCHEDULES = 1;

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ServerThread(Socket socket) throws Exception {
		log.info("DOING ServerThread()");
		this.socket = socket;

		try {
			// Se crean los ObjectStreams para enviar y recibir datos por el socket:
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			log.error("Error in ServerThread(): " + e);
			throw e;
		}
	}

	@Override
	public void run() {
		log.info("BEGIN [ServerThread]run()");
		try {

			int header = ois.readInt();
			log.info("header= " + header);

			switch (header) {

			case HELLO:
				helloService();
				break;

			case SEND_SCHEDULES:
				sendSchedulesService();
				break;

			default:
				log.error("Unknown petition for header= " + header);
				throw new IllegalArgumentException("Unknown petition");
			}

		} catch (Exception e) {
			log.error("ERROR in [ServerThread]run(): " + e);
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("error closing server socket: " + e);
				e.printStackTrace();
			}
		}

		log.info("END [ServerThread]run()");
	}// fin run

	private void sendSchedulesService() {
		log.info("BEGIN sendSchedulesService()");
		try {

			List<ScheduleWithActivities> schedules = (List<ScheduleWithActivities>) ois.readObject();
			log.info("Schedules will be exported to XML: " + schedules);
			SchedulesWithActivitiesXmlDao.export(schedules);

		} catch (Exception e) {
			log.error("ERROR in sendSchedulesService(): " + e);
		}
		log.info("END sendSchedulesService");
	}

	private void helloService() {
		log.info("BEGIN helloService()");
		try {
			Boolean helloRequest = (Boolean) ois.readObject();
			log.info("helloRequest= " + helloRequest);
			oos.writeObject(helloRequest);
			oos.flush();
		} catch (Exception e) {
			log.error("ERROR in helloService(): " + e);
		}
		log.info("END helloService");
	}



}
