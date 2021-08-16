import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.TreeMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;


import com.sun.tools.javac.Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IPtoPCLookup {

	private JFrame frame;
	private JTextField ipInput;
	private JLabel deviceTxt;
	private JLabel userTxt;
	private JLabel timeTxt;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IPtoPCLookup window = new IPtoPCLookup();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IPtoPCLookup() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("mg.png"));
		Image image = icon.getImage();
		frame.setIconImage(image);
		
		ipInput = new JTextField();
		ipInput.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		ipInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
			
				if(key == 10) {
					String guiInput = ipInput.getText();
					/////////////////starting map fill and search//////////////////////////////
					String fileName = "R:\\IT\\NetLogs\\Daily\\2021\\Users.csv";
					TreeMap<String, String> map = new TreeMap<String, String>();
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new FileReader(fileName));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					String line = "";
					try {
						line = reader.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					while(line != null) {
						
						String[] array = line.split(",");
						String username = array[0].trim();
						String ip = array[3].trim();
						String device = array[5].trim();
						String[] tempTime = array[2].split(" ");
						String time = tempTime[1] + " " + tempTime[2];
						String[] dateArr = array[1].split("/");
						String month = dateArr[1];
						String day = dateArr[2];
						String year = dateArr[0].trim().substring(2,4);
						String date = month + "/" + day + "/" + year;
						String dateTime = date + " " + time;
						String info = username + ", " + device + ", " + dateTime;
						map.put(ip, info);
						//System.out.println(map.size() + ", " + ip);
						try {
							line = reader.readLine();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					try {
						reader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
						
						String result = map.get(guiInput);
						
					
					//////////////////////////////////////////////////////////////////////
					if(result == null) {
						deviceTxt.setText("Invalid IP");
						userTxt.setText("");
						timeTxt.setText("");
					}else {
						String[] tempArr = result.split(",");
						String user = tempArr[0];
						String device = tempArr[1];
						String timeText = tempArr[2];
						deviceTxt.setText(device);
						userTxt.setText(user);
						timeTxt.setText(timeText);
					}
				}
			}
		});
		ipInput.setBounds(113, 69, 205, 27);
		frame.getContentPane().add(ipInput);
		ipInput.setColumns(10);
		
		deviceTxt = new JLabel("");
		deviceTxt.setHorizontalAlignment(SwingConstants.LEFT);
		deviceTxt.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		deviceTxt.setBounds(146, 149, 193, 27);
		frame.getContentPane().add(deviceTxt);
		
		userTxt = new JLabel("");
		userTxt.setHorizontalAlignment(SwingConstants.LEFT);
		userTxt.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		userTxt.setBounds(151, 177, 193, 27);
		frame.getContentPane().add(userTxt);
		
		timeTxt = new JLabel("");
		timeTxt.setHorizontalAlignment(SwingConstants.LEFT);
		timeTxt.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		timeTxt.setBounds(146, 204, 193, 27);
		frame.getContentPane().add(timeTxt);
		
		JLabel lblNewLabel = new JLabel("Enter IP Address:");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNewLabel.setBounds(135, 37, 158, 21);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String guiInput = ipInput.getText();
				/////////////////starting map fill and search//////////////////////////////
				String fileName = "R:\\IT\\NetLogs\\Daily\\2021\\Users.csv";
				TreeMap<String, String> map = new TreeMap<String, String>();
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(fileName));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				String line = "";
				try {
					line = reader.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while(line != null) {
					
					String[] array = line.split(",");
					String username = array[0].trim();
					String ip = array[3].trim();
					String device = array[5].trim();
					String[] tempTime = array[2].split(" ");
					String time = tempTime[1] + " " + tempTime[2];
					String[] dateArr = array[1].split("/");
					String month = dateArr[1];
					String day = dateArr[2];
					String year = dateArr[0].trim().substring(2,4);
					String date = month + "/" + day + "/" + year;
					String dateTime = date + " " + time;
					String info = username + ", " + device + ", " + dateTime;
					map.put(ip, info);
					//System.out.println(map.size() + ", " + ip);
					try {
						line = reader.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
					
					String result = map.get(guiInput);
					
				
				//////////////////////////////////////////////////////////////////////
					if(result == null) {
						deviceTxt.setText("Invalid IP");
						userTxt.setText("");
						timeTxt.setText("");
					}else {
						String[] tempArr = result.split(",");
						String user = tempArr[0];
						String device = tempArr[1];
						String timeText = tempArr[2];
						deviceTxt.setText(device);
						userTxt.setText(user);
						timeTxt.setText(timeText);
					}
				
			}
		});
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			}
		});
		btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		btnSearch.setBounds(113, 107, 96, 33);
		frame.getContentPane().add(btnSearch);
		
		JLabel lblDevice = new JLabel("Device: ");
		lblDevice.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblDevice.setBounds(78, 151, 67, 21);
		frame.getContentPane().add(lblDevice);
		
		JLabel lblUser = new JLabel("User: ");
		lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblUser.setBounds(96, 179, 48, 21);
		frame.getContentPane().add(lblUser);
		
		JLabel lblLastLogin = new JLabel("Last Login: ");
		lblLastLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblLastLogin.setBounds(48, 204, 107, 27);
		frame.getContentPane().add(lblLastLogin);
		
		
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ipInput.setText("");
				deviceTxt.setText("");
				userTxt.setText("");
				timeTxt.setText("");
			}
		});
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnClear.setFont(new Font("Segoe UI", Font.BOLD, 15));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			}
		});
		btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		btnClear.setBounds(222, 107, 96, 33);
		frame.getContentPane().add(btnClear);
	}
}
