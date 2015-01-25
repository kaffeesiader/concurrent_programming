package cp.ex1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class Gui {
	
	private SortController controller;
	private boolean sorting;
	
	private JFrame frame;
	private JList<String> lstData;
	private JButton btnAction;
	private JLabel lblStatus;
	private JProgressBar pgbProgress;
	private ButtonGroup btnGroup;
	
	private Gui() {
		sorting = false;
		initialize();
	}
	
	public void setController(SortController controller) {
		this.controller = controller;
	}
	
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showGUI();
			}
		});
	}
	
	public void displayArray(String[] array) {
		lstData.setListData(array);
	}
	
	public void setBusy(boolean busy) {
		if(busy) {
			sorting = true;
			btnAction.setText("Cancel");
		} else {
			sorting = false;
			btnAction.setText("Start");
		}
	}
	
	public void setStatus(String status) {
		lblStatus.setText(status);
	}
	
	public void reportProgress(int progress) {
		pgbProgress.setValue(progress);
	}

	protected void handleSelectionChange(ActionEvent e) {
		btnAction.setEnabled(true);
	}

	protected void handleButton(ActionEvent e) {
		if(controller == null)
			return;
		
		if(!sorting) {
			int n = Integer.parseInt(btnGroup.getSelection().getActionCommand());
			controller.startTask(n);
		} else {
			controller.cancelTask();
		}
		
	}

	private void showGUI() {
		frame.setVisible(true);
	}
	
	private void initialize() {
		frame = new JFrame("Sorting GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(500, 150);
		frame.setSize(400, 400);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout(2, 2));
		containerPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		
		frame.add(containerPanel);
		
		lstData = new JList<String>();
		lstData.setEnabled(false);
		
		btnAction = new JButton("Start");
		btnAction.setEnabled(false);
		btnAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleButton(e);
			}
		});
		
		lblStatus = new JLabel("Idle");
		pgbProgress = new JProgressBar();
		
		ActionListener rbListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSelectionChange(e);
			}
		};
		
		JRadioButton rb1 = new JRadioButton("1000");
		rb1.setActionCommand("1000");
		rb1.addActionListener(rbListener);
		JRadioButton rb2 = new JRadioButton("100000");
		rb2.setActionCommand("100000");
		rb2.addActionListener(rbListener);
		JRadioButton rb3 = new JRadioButton("1000000");
		rb3.setActionCommand("1000000");
		rb3.addActionListener(rbListener);
		JRadioButton rb4 = new JRadioButton("10000000");
		rb4.setActionCommand("10000000");
		rb4.addActionListener(rbListener);
		
		btnGroup = new ButtonGroup();
		btnGroup.add(rb1);
		btnGroup.add(rb2);
		btnGroup.add(rb3);
		btnGroup.add(rb4);
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(rb1);
		topPanel.add(rb2);
		topPanel.add(rb3);
		topPanel.add(rb4);
		topPanel.add(btnAction);
		
		containerPanel.add(topPanel, BorderLayout.NORTH);
		
		JScrollPane displayArea = new JScrollPane(lstData);
		displayArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		containerPanel.add(displayArea, BorderLayout.CENTER);
		
		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 22));
		statusPanel.setLayout(new BorderLayout());
		containerPanel.add(statusPanel, BorderLayout.SOUTH);
		
		
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lblStatus, BorderLayout.WEST);
		statusPanel.add(pgbProgress, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		Gui gui = new Gui();
		SortController controller = new SortController(gui);
		
		gui.setController(controller);
		gui.show();
	}

}
