package flexdule.desktop.model.daos;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import flexdule.core.dtos.Activity;
import flexdule.core.dtos.ActivityVars;
import flexdule.core.dtos.Limits;
import flexdule.core.dtos.Schedule;
import flexdule.core.dtos.ScheduleWithActivities;
import flexdule.desktop.control.Main;

public class SchedulesWithActivitiesXmlDao {
	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static final String FILE_PATH = /* "\\Flexdule_Exports\\ */"flexdule-export-";

	public static String export(List<ScheduleWithActivities> swas) {
		log.info("BEGGIN export()");
		String exportedFile = null;

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			Element eRoot = doc.createElement("SchedulesWithActivities");
			doc.appendChild(eRoot);

			for (ScheduleWithActivities swa : swas) {
				Element eSchedule = doc.createElement("Schedule");
				eRoot.appendChild(eSchedule);
				
				Schedule schedule = swa.getSchedule();
				appendTextElement(doc, eSchedule, "IdSchedule", schedule.getIdSchedule());
				appendTextElement(doc, eSchedule, "Name", schedule.getName());
				appendTextElement(doc, eSchedule, "Color", schedule.getColor());

				List<Activity> acs = swa.getActivties();
				for (Activity activity : acs) {

					Element eActivity = doc.createElement("Activity");
					eSchedule.appendChild(eActivity);
					appendTextElement(doc, eActivity, "IdActivity", activity.getIdActivity());
					appendTextElement(doc, eActivity, "IdSchedule", activity.getIdSchedule());
					appendTextElement(doc, eActivity, "Name", activity.getName());
					appendTextElement(doc, eActivity, "Color", activity.getColor());
					appendTextElement(doc, eActivity, "PositionInSchedule", activity.getPositionInSchedule());
					ActivityVars v = activity.getConfigVars();
					appendTextElement(doc, eActivity, "Sn", v.getSn());
					appendTextElement(doc, eActivity, "Sx", v.getSx());
					appendTextElement(doc, eActivity, "Dn", v.getDn());
					appendTextElement(doc, eActivity, "Dx", v.getDx());
					appendTextElement(doc, eActivity, "Fn", v.getFn());
					appendTextElement(doc, eActivity, "Fx", v.getFx());
					Limits l = activity.getLimits();
					appendTextElement(doc, eActivity, "LimitSn", l.getSn());
					appendTextElement(doc, eActivity, "LimitSx", l.getSx());
					appendTextElement(doc, eActivity, "LimitFn", l.getFn());
					appendTextElement(doc, eActivity, "LimitFx", l.getFx());
				}
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(doc);
			exportedFile = FILE_PATH + new Date().getTime() + ".xml";

//			FileWriter writer = new FileWriter(new File(exportedFile));
//			StreamResult result = new StreamResult(writer);

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StreamResult result = new StreamResult(new File(exportedFile));
			transformer.transform(source, result);

		} catch (Exception e) {
			log.error("ERROR in export():" + e);
			e.printStackTrace();
		}
		log.info("END export(). exportedFile= " + exportedFile);
		return exportedFile;
	}

	private static void appendTextElement(Document doc, Element parent, String childName, Object childValue) {
		Element child = doc.createElement(childName);
		if (childValue != null)
			child.appendChild(doc.createTextNode(childValue.toString()));
		parent.appendChild(child);
	}
}
