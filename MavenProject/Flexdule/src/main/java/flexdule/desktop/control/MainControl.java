package flexdule.desktop.control;

import flexdule.desktop.view.MainView;
import flexdule.desktop.view.MainView.MainViewListener;

public class MainControl implements MainViewListener {

	protected ControlContext controlContext;
	protected MainView mainView;

	public MainControl(ControlContext controlContext) {
		this.controlContext = controlContext;
		mainView = new MainView();
		mainView.addListener(this);

	}

	public void getControl() {
		ControlContext.replaceFramePanel(controlContext.getFrame(), mainView, "Flexdule Desktop");
	}

	@Override
	public void atBtnExample() {
		System.out.println("plinc!");
	}


}
