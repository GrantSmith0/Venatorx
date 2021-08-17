package Classes;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EmployeeLookup extends JComboBox{

	private JFrame frame;
	private String[] arr;
	public TreeMap<String, String> map;
	private JLabel lblName;
	private JLabel lblTitle;
	private JLabel lblCell;
	private JLabel lblPhone;
	private JLabel lblEmail;
	//private Set<String> set;
	private JLabel lblEmail_2;
	private JLabel lblPhone_1;
	private JLabel lblCell_1;
	private JLabel lblLocation;
	private JLabel lblLocation_1;
	private JLabel lblExt;
	private JLabel lblExtension;
	private JFrame error;
	
	private ArrayList<String> list;
	public EmployeeLookup el;
	private FilterComboBoxModel model;
	private MouseAdapter ma;
	public static EmployeeLookup window;
	/**
	 * Launch the application.
	 */
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new EmployeeLookup();
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
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public EmployeeLookup() {
		fill();
		initialize();
		//AutoCompleteDecorator.decorate(el);
	}

	/**
	 * Initialize the contents of the frame.
	 */
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void initialize() {
		Collections.sort(list);
		String[] items =  list.toArray(new String[list.size()]);
		
		el = new EmployeeLookup(items);
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 313);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("phoneicon.png"));
		Image image = icon.getImage();
		frame.setIconImage(image);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		el.setBounds(106, 51, 216, 28);
		el.setEnabled(true);
		el.setEditable(getIgnoreRepaint());
		el.setSelectedIndex(-1);
		el.setFocusable(true);
		el.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchPressed();
			}
		});
		frame.getContentPane().add(el);
	
		JLabel lblNewLabel = new JLabel("Enter Name:");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblNewLabel.setBounds(167, 24, 132, 28);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Clicked on Search
				searchPressed();
	
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
		btnSearch.setBounds(106, 90, 99, 36);
		frame.getContentPane().add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblEmail.setForeground(null);
				el.setSelectedIndex(-1);
				lblName.setText("");
				lblTitle.setText("");
				lblPhone.setText("");
				lblExt.setText("");
				lblExtension.setText("");
				lblCell.setText("");
				lblEmail.setText("");
				el.model.restoreItems();
				lblEmail_2.setText("");
				lblCell_1.setText("");
				lblPhone_1.setText("");
				lblLocation_1.setText("");
				lblLocation.setText("");
				lblEmail.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				lblEmail.removeMouseListener(ma);
				el.grabFocus();
				
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
		btnClear.setBounds(223, 90, 99, 36);
		frame.getContentPane().add(btnClear);
		
		lblName = new JLabel("");
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBackground(Color.RED);
		//lblName.setOpaque(true);
		lblName.setBounds(43, 128, 349, 23);
		frame.getContentPane().add(lblName);
		
		lblTitle = new JLabel("");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		//lblTitle.setOpaque(true);
		lblTitle.setBackground(Color.RED);
		lblTitle.setBounds(10, 149, 424, 23);
		frame.getContentPane().add(lblTitle);
		
		lblPhone = new JLabel("");
		lblPhone.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		//lblPhone.setOpaque(true);
		lblPhone.setBackground(Color.RED);
		lblPhone.setBounds(154, 214, 106, 25);
		frame.getContentPane().add(lblPhone);
		
		lblCell = new JLabel("");
		lblCell.setHorizontalAlignment(SwingConstants.LEFT);
		lblCell.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		//lblCell.setOpaque(true);
		lblCell.setBackground(Color.RED);
		lblCell.setBounds(154, 236, 270, 25);
		frame.getContentPane().add(lblCell);
		
		lblEmail = new JLabel("");
		lblEmail.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
		lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblEmail.setBackground(Color.RED);
		//lblEmail.setOpaque(true);
		lblEmail.setBounds(154, 192, 270, 25);
		frame.getContentPane().add(lblEmail);
		
		lblEmail_2 = new JLabel("");
		lblEmail_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblEmail_2.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblEmail_2.setBackground(Color.RED);
		//lblEmail_2.setOpaque(true);
		lblEmail_2.setBounds(106, 192, 48, 25);
		frame.getContentPane().add(lblEmail_2);
		
		lblPhone_1 = new JLabel("");
		lblPhone_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhone_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblPhone_1.setBackground(Color.RED);
		//lblPhone_1.setOpaque(true);
		lblPhone_1.setBounds(102, 214, 48, 25);
		frame.getContentPane().add(lblPhone_1);
		
		lblCell_1 = new JLabel("");
		lblCell_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblCell_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblCell_1.setBackground(Color.RED);
		//lblCell_1.setOpaque(true);
		lblCell_1.setBounds(119, 236, 35, 25);
		frame.getContentPane().add(lblCell_1);
		
		lblLocation = new JLabel("");
		//lblLocation.setOpaque(true);
		lblLocation.setHorizontalAlignment(SwingConstants.LEFT);
		lblLocation.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblLocation.setBackground(Color.RED);
		lblLocation.setBounds(154, 170, 270, 25);
		frame.getContentPane().add(lblLocation);
		
		lblLocation_1 = new JLabel("");
		//lblLocation_1.setOpaque(true);
		lblLocation_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblLocation_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblLocation_1.setBackground(Color.RED);
		lblLocation_1.setBounds(84, 170, 70, 25);
		frame.getContentPane().add(lblLocation_1);
		
		lblExt = new JLabel("");
		lblExt.setHorizontalAlignment(SwingConstants.LEFT);
		lblExt.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblExt.setBackground(Color.RED);
		lblExt.setBounds(260, 214, 40, 25);
		frame.getContentPane().add(lblExt);
		
		lblExtension = new JLabel("");
		lblExtension.setHorizontalAlignment(SwingConstants.LEFT);
		lblExtension.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblExtension.setBackground(Color.RED);
		lblExtension.setBounds(302, 214, 106, 25);
		frame.getContentPane().add(lblExtension);
		ImageIcon vrx = new ImageIcon(getClass().getClassLoader().getResource("Venatorx Logo.jpg"));
		
		
		
		
	}
	///////////////////////////////////////Initializes tree map before user types anything//////////////////////////////////////////////
	private void fill() {
		map = new TreeMap<String, String>();
		list = new ArrayList<String>();
		try {

			FileInputStream ExcelFileToRead = new FileInputStream("V:\\Public\\Venatorx Employee and Conference Room Phone List.xlsx");

			XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row; 
			XSSFCell cell;
			
			arr = new String[9];
			Iterator<Row> rows = sheet.rowIterator();
			String temp = "";
		
			while (temp.equals(""))
			{
				
				row=(XSSFRow) rows.next();
				Iterator<Cell> cells = row.cellIterator();
				for (int i = 0; i < 9; i++)
				{
					
					cell=(XSSFCell) cells.next();
					
					if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
					{
						String check = cell.getStringCellValue();
						if(check.equals("First Name")) {
							temp = "First";
						}else {
							arr[i] = cell.getStringCellValue();
						}
						
						
						//System.out.println(arr[i]);
						//System.out.print(cell.getStringCellValue()+", ");
					}
					else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
					{
						Cell tempCell = cell;
						DataFormatter formatter = new DataFormatter();
						String strValue = formatter.formatCellValue(tempCell);
						arr[i] = strValue;
						//System.out.println(arr[i]);
						//System.out.print(cell.getNumericCellValue()+", ");
					}
					else if((i == 0) && (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK)) {
						temp = "Final line";
						break;
					}
					else
					{
						String unknown = "Unknown";
						arr[i] = unknown;
					}
				}
				
				
				if(temp.equals("Final line")) {
					temp = "";
					break;
				}
				else if(temp.equals("First")) {
					temp = "";
				}
				else {
					
					String name = arr[0].trim() + " " + arr[1].trim();
					String accreditions = arr[2].trim();
					String title = arr[3].trim();
					String extension = arr[4].trim();
					String directNumber = arr[5].trim();
					String cellNumber = arr[6].trim();
					String email = arr[7].trim();
					String location = arr[8].trim();
					
					String info = accreditions + ":" + title + ":" + extension + ":" + directNumber + ":" + cellNumber + ":" + email + ":" + location;
					//System.out.println(name + info);
					map.put(name, info);
					//set.add(name);
					list.add(name);
					
					arr = new String[9];
					//System.out.println();
				}
			}
		    
		  } catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void searchPressed() {
		
		String guiInput = (String) el.getSelectedItem();
		hidePopup();
		
		if(el.getSelectedIndex() == -1 || map.get(guiInput) == null) {
			lblName.setText("Unknown");
		}else {
			//Event handling. Once user presses search. Split info and add to proper fields
			guiInput = (String) el.getItemAt(el.getSelectedIndex());
			String temp = map.get(guiInput);
			if(temp == null) {
				//System.out.println("temp = null");
			}else {
				String[] tempArr = temp.split(":");
				lblEmail.setForeground(Color.blue.darker());
				lblEmail.setCursor(new Cursor(Cursor.HAND_CURSOR));
				ma = new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Desktop desktop;
						if (Desktop.isDesktopSupported() 
							    && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
							  URI mailto;
								try {
									mailto = new URI("mailto:" + tempArr[5].trim());
									desktop.mail(mailto);
								} catch (URISyntaxException z) {
									error = new JFrame();
									JOptionPane.showMessageDialog(error, "Improper Email Format");
									z.printStackTrace();
								} catch (IOException e1) {
									error = new JFrame();
									JOptionPane.showMessageDialog(error, "Could Not Open Mail App");
									e1.printStackTrace();
								}
								 
							 
							} else {
							  error = new JFrame();
							  JOptionPane.showMessageDialog(error, "Desktop does not support email link");
							}
					}
				};
				lblEmail.addMouseListener(ma);
				
				if(tempArr[0].equals("Unknown")) {
					lblName.setText(guiInput);
				}else {
					lblName.setText(guiInput + " - " + tempArr[0]);
				}
				lblEmail_2.setText("Email:");
				lblCell_1.setText("Cell:");
				lblPhone_1.setText("Direct:");
				lblTitle.setText(tempArr[1]);
				lblPhone.setText(tempArr[3]);
				lblExt.setText("- Ext. ");
				lblExtension.setText(tempArr[2]);
				lblCell.setText(tempArr[4]);
				lblEmail.setText("<html><a href=' '>"+tempArr[5]+"</a></html>");
				lblLocation_1.setText("Location:");
				lblLocation.setText(tempArr[6]);
			}
		}
	}
	
	public EmployeeLookup(String[] itemList) {
	      model = new FilterComboBoxModel(itemList);
	      setModel(model);

	      model.addAllElements(itemList);

	      // Remove standard key listeners that come with the JComboBox
	      KeyListener[] lis = getKeyListeners();
	      
	      for (int i = 0; i < lis.length; i++) {
	         removeKeyListener(lis[i]);  
	      }
	      addKeyListener(model.getKeyListener());
	      // Add custom KeyListener class
	    
	      // Add custom ActionListener class
	      addActionListener(model.getActionListener());
	      

	   }
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class FilterComboBoxModel extends DefaultComboBoxModel {

	      private String[] masterItemList;
	      private String masterSelectedItem;
	      private StringBuilder filter = new StringBuilder(8);
	      private int fidx = 0;
	      private CustomKeyListener keylis = null;
	      private CustomActionListener actionlis = null;

	      private ActionListener[] actionLisList;
	      private ItemListener[] itemLisList;

	      public FilterComboBoxModel(String[] itemList) {
	         initComboBoxModel(itemList);
	      }

	      private void initComboBoxModel( String[] itemList ) {
	         setMasterItemList(itemList);
	      }

	      public CustomKeyListener getKeyListener() {
	    	  if (keylis == null) {
	            keylis = new CustomKeyListener();
	           
	         }
	         return keylis;
	      }

	      public CustomActionListener getActionListener() {
	         if (actionlis == null) {
	            actionlis = new CustomActionListener();
	            
	         }
	         return actionlis;
	      }

	      public void setMasterItemList(String[] items) {
	         masterItemList = items;
	         masterSelectedItem = items[0];
	         restoreItems();
	      }

	      public void addAllElements(Object[] items) {
	         for (int i = 0; i < items.length; i++) {
	            addItem(items[i]);
	         }
	      }

	      public Object[] getAllElements() {
	         Object[] list = new Object[getItemCount()];
	         for (int i = 0; i < list.length; i++) {
	            list[i] = this.getElementAt(i);
	         }
	         return list;
	      }
	      

	      public void filterItems(String pat) {
	    	 
	         ArrayList<String> newList = new ArrayList<String>();

	         String[] list = masterItemList;

	         int patlen = pat.length();
	         for (int i = 0; i < list.length; i++) {
	            String item = list[i];
	            if (item.length() < patlen) {
	               continue;
	            }
	            String tok = item.substring(0, patlen);

	            if (tok.equalsIgnoreCase(pat)) {
	               newList.add(item);
	            }
	         }

	         // Add the new list to the combobox - notice we disable listeners
	         suspendAllListeners();
	         removeAllElements();
	         if (newList.isEmpty()) {
	            addItem("<Empty>");
	            hidePopup();
	         }else {
	            addAllElements(newList.toArray());
	         }
	         if (fidx == 0)
	            setSelectedItem(masterSelectedItem);

	         restoreAllListeners();

	      }

	      private void suspendAllListeners() {
	         actionLisList = getActionListeners();
	         for (ActionListener a : actionLisList) {
	            removeActionListener(a);
	         }
	         itemLisList = getItemListeners();
	         for (ItemListener i : itemLisList) {
	            removeItemListener(i);
	         }
	      }

	      private void restoreAllListeners() {
	         for (ActionListener a : actionLisList) {
	            addActionListener(a);
	         }
	         for (ItemListener i : itemLisList) {
	            addItemListener(i);
	         }
	      }

	      public void restoreItems() {
	         suspendAllListeners();
	         removeAllElements();
	         addAllElements(masterItemList);
	         resetFilter();
	         setSelectedItem(masterSelectedItem);
	         restoreAllListeners();
	      }

	      private void resetFilter() {
	         filter.setLength(0);
	         fidx = 0;
	      }

	      public class CustomKeyListener extends KeyAdapter  {
	    	 
	         @Override
	         public void keyPressed(KeyEvent e) {
	        	showPopup();
	            int keyCode = e.getKeyCode();
	            if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
	               char letter = (char) keyCode;
	               filter.insert(fidx, letter);
	               fidx++;
	               filter.setLength(fidx);
	               filterItems(filter.toString());
	            } else if (keyCode == KeyEvent.VK_LEFT) {
	               fidx = (fidx == 0 ? fidx : fidx - 1);
	               filterItems(filter.substring(0, fidx));
	            } else if (keyCode == KeyEvent.VK_RIGHT) {
	               fidx = (fidx == filter.length() ? fidx : fidx + 1);
	               filterItems(filter.substring(0, fidx));
	            } else if (keyCode == KeyEvent.VK_BACK_SPACE
	                    || keyCode == KeyEvent.VK_DELETE
	                    || keyCode == KeyEvent.VK_ESCAPE) {
	            	hidePopup();
	               restoreItems();
	               
	            }
	          

//	            System.out.println("fidx = " + fidx + " -- Filter: " + filter);
	            setToolTipText("Current filter: " + filter.substring(0, fidx));
	         }
	        
	      }

	      public class CustomActionListener implements ActionListener {
	         public void actionPerformed(ActionEvent e) {
	            masterSelectedItem = (String) getSelectedItem();
	            restoreItems();
	            
	         }
	        
	      }
	}
}
