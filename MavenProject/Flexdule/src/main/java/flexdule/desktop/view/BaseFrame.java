package flexdule.desktop.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BaseFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BaseFrame frame = new BaseFrame();
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
	public BaseFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

//		pack();
//		setMinimumSize(getSize());
		setSize(new Dimension(1200, 900));
		setLocationRelativeTo(null);

		ImageIcon myAppImage = new ImageIcon(this.getClass().getResource("/img/fxd-icon.png"));
		if (myAppImage != null)
			setIconImage(myAppImage.getImage());

		setVisible(true);
		setTitle("Flexdule");
	}

	public void showWarning(String message) {
		JOptionPane.showMessageDialog(this, message, "", JOptionPane.WARNING_MESSAGE);
	}

	public void exceptionDialog(Exception e) {
		String msg = "";
		if(e!=null && e.getMessage()!=null) {
			msg = e.getMessage();
		}
		JOptionPane.showMessageDialog(this, "No se ha podido realizar la operación: "+msg, "Ha ocurrido un error", JOptionPane.WARNING_MESSAGE);
	}

	public void showInfo(String message) {
		JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean showConfirm(String msg) {
		int option = JOptionPane.showConfirmDialog(this, msg, "", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			return true;
		} else {
			return false;
		}

	}

}
