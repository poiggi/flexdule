package util;

import org.slf4j.Logger;

import util.CoreLog;

public class DesktopLog implements CoreLog {
	private Logger log;

	public DesktopLog(Logger log) {
		this.log = log;
	}

	@Override
	public void d(String msg) {
		log.debug(msg);
	}

	@Override
	public void i(String msg) {
		log.info(msg);
	}

	@Override
	public void w(String msg) {
		log.warn(msg);
	}

	@Override
	public void e(String msg) {
		log.error(msg);
	}

}
