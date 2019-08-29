package flexdule.desktop.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import flexdule.desktop.view.MainView.MainViewListener;
import flexdule.desktop.view.sub.ListenableView;
import net.miginfocom.swing.MigLayout;

public class MainView extends ListenableView<MainViewListener> {
	private static final long serialVersionUID = -7168410550803883969L;

	protected JLabel lblHolaaa;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setBounds(100, 100, 363, 313);
		lblHolaaa = new JLabel("Hola Mundo");
		lblHolaaa.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblHolaaa.setHorizontalAlignment(SwingConstants.CENTER);
		lblHolaaa.setOpaque(true);
		lblHolaaa.setBackground(Color.WHITE);
		lblHolaaa.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				btnExample();
			}
		});
		setLayout(new MigLayout("", "[100px,grow][180px][100px,grow]", "[100px,grow][108px][100px,grow]"));
		add(lblHolaaa, "cell 1 1,grow");

		setBackground(Color.WHITE);
	}

	protected void btnExample() {
		for (MainViewListener listener : listeners) {
			listener.atBtnExample();
		}
	}

	public interface MainViewListener {

		public void atBtnExample();
	}

}
