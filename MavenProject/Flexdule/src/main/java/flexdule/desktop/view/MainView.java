package flexdule.desktop.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexdule.desktop.view.MainView.MainViewListener;
import flexdule.desktop.view.sub.ListenableView;
import net.miginfocom.swing.MigLayout;

public class MainView extends ListenableView<MainViewListener> {
	private static Logger log = LoggerFactory.getLogger(MainView.class);

	private JPanel contentPane;
	protected JLabel flexduleTitle;
	protected JLabel labelIp;
	protected JLabel labelServerState;
	protected JLabel labelMessage;
	protected JLabel lblBtnReconnect;

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
		setBounds(100, 100, 715, 636);
		contentPane = new JPanel();
		setLayout(new MigLayout("", "[100px,grow][180px,grow,center][100px,grow]",
				"[50px,grow][grow][50px,grow][40px][40px][40px][50px:n,grow][75px,grow]"));

		setBackground(Color.WHITE);
		
		flexduleTitle = new JLabel("");
		flexduleTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(flexduleTitle, "cell 1 1");
		flexduleTitle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (MainViewListener mainViewListener : listeners) {
					mainViewListener.atAction();
				}
			}
		});

		ImageIcon im = new ImageIcon(
				this.getClass().getResource("/img/flexdule-exporter-title-500x200.png"));
		flexduleTitle.setIcon(im);

		labelIp = new JLabel("IP: ");
		labelIp.setForeground(Color.DARK_GRAY);
		labelIp.setFont(new Font("Arial", Font.PLAIN, 20));
		add(labelIp, "cell 1 3");

		labelServerState = new JLabel();
		labelServerState.setText("Sin acceso a la red");
		labelServerState.setForeground(Color.DARK_GRAY);
		labelServerState.setFont(new Font("Arial", Font.PLAIN, 18));
		add(labelServerState, "cell 1 4");

		lblBtnReconnect = new JLabel();
		lblBtnReconnect.setHorizontalTextPosition(SwingConstants.CENTER);
		lblBtnReconnect.setHorizontalAlignment(SwingConstants.CENTER);
		lblBtnReconnect.setPreferredSize(new Dimension(200, 30));
		lblBtnReconnect.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		lblBtnReconnect.setText("Reconectar");
		lblBtnReconnect.setForeground(Color.DARK_GRAY);
		lblBtnReconnect.setFont(new Font("Arial", Font.PLAIN, 18));
		add(lblBtnReconnect, "cell 1 5");
		lblBtnReconnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (MainViewListener mainViewListener : listeners) {
					mainViewListener.atReconnect();
				}
			}
		});

		labelMessage = new JLabel();
		labelMessage.setForeground(Color.DARK_GRAY);
		labelMessage.setFont(new Font("Arial", Font.PLAIN, 18));
		add(labelMessage, "cell 1 6");
		
	}

	public interface MainViewListener {

		public void atAction();

		public void atReconnect();
	}

	public void setLabelIp(String ip) {
		labelIp.setText("IP: " + ip);
	}

	public void setLabelServerState(String msg) {
		labelServerState.setText("Estado: " + msg);
	}

	public void setLabelMessage(String msg, int ms) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					labelMessage.setText(msg);
					Thread.sleep(ms);
					labelMessage.setText("");
				} catch (InterruptedException e) {
					log.error("ERROR in [setLabelMessage]Thread: " + e);
					e.printStackTrace();
				}
			}
		}).start();
	}
}
