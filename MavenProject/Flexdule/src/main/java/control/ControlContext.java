package control;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import view.BaseFrame;

public class ControlContext {

	protected BaseFrame frame;
	protected MainControl mainControl;

	public ControlContext() {
		this.frame = new BaseFrame();
		mainControl = new MainControl(this);

		UIManager.put("OptionPane.background", Color.white);
		UIManager.put("Panel.background", Color.white);

	}

	public void start() {
		getMainControl().getControl();
	}

	public static void replaceFramePanel(JFrame frame, JPanel view) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(view);
		frame.revalidate();
		frame.repaint();
	}

	public static void replaceFramePanel(JFrame frame, JPanel view, String frameTitle) {
		frame.setTitle(frameTitle);
		replaceFramePanel(frame, view);
	}

	// GETTERS AND SETTERS

	public BaseFrame getFrame() {
		return frame;
	}

	public void setFrame(BaseFrame frame) {
		this.frame = frame;
	}

	public MainControl getMainControl() {
		return mainControl;
	}

	public void setMainControl(MainControl mainControl) {
		this.mainControl = mainControl;
	}

}
