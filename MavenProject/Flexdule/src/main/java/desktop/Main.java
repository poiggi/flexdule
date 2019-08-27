package desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import desktop.control.ControlContext;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String... args) {
		log.info("==========[ BEGIN main ]==========");

		ControlContext controller = new ControlContext();
		controller.start();

	}
}
