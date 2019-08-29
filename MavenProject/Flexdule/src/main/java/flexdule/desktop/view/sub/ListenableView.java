package flexdule.desktop.view.sub;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class ListenableView<T> extends JPanel {

	protected List<T> listeners = new ArrayList<T>();

	public void addListener(T listener) {
		listeners.add(listener);
	}

	public List<T> getListeners() {
		return listeners;
	}

	public void setListeners(List<T> listeners) {
		this.listeners = listeners;
	}

	public static void orangeOver(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				button.setBackground(new Color(255, 181, 91));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(Color.WHITE);
			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button.setBackground(Color.WHITE);
			}
		});
	}

}
