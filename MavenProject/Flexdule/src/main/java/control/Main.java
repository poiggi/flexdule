package control;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connection.Server;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String... args) {
		log.info("==========[ BEGIN main ]==========");

//		ControlContext controller = new ControlContext();
//		controller.start();

		Server s = new Server(4400);
		try {
			s.startListen();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
