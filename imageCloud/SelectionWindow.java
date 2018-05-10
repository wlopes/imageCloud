package imageCloud;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class SelectionWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel label;
	JTextField dirName;
	JComboBox<String> imageNameColumn, paramColumn;
	ButtonGroup orderType;
	JRadioButton ascend, descend;
	JTextField firstLine, increment, width;
	JButton ok, selectDirectory;
	JCheckBox remove;
	Toolkit tk = Toolkit.getDefaultToolkit();
	String path; 
	
	public SelectionWindow() throws IOException{
		super("ImageCloud - configuration window");
		final JFrame ref = this;
		setLayout(null);
		setSize(300,450);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int x = jf.showOpenDialog(this);
		if(x!=JFileChooser.APPROVE_OPTION){
			System.exit(0);
		}
		
		File f = jf.getSelectedFile();		
		path = jf.getSelectedFile().getPath();
		final BufferedReader br = new BufferedReader(new FileReader(f));
		System.out.println(ImageCloud.delimiter);
		String[] columns = br.readLine().split(ImageCloud.delimiter);
				
		label = new JLabel("Diretório das Imagens");
		add(label);
		label.setBounds(10,10,280,20);
		
		dirName = new JTextField(jf.getSelectedFile().getPath() + "images/");
		add(dirName);
		dirName.setBounds(10,35,240,20);
		dirName.setEnabled(false);
		
		label = new JLabel("Coluna com o nome das Imagens");
		add(label);
		label.setBounds(10,60,280,20);
		
		imageNameColumn = new JComboBox<String>(columns);
		add(imageNameColumn);
		imageNameColumn.setBounds(10,85,280,20);
		
		label = new JLabel("Coluna do parâmetro de ordenação");
		add(label);
		label.setBounds(10,110,280,20);
		
		paramColumn = new JComboBox<String>(columns);
		add(paramColumn);
		paramColumn.setBounds(10,135,280,20);
		
		label = new JLabel("Ordem");
		add(label);
		label.setBounds(10,160,280,20);
		
		ascend = new JRadioButton("Crescente");
		add(ascend);
		ascend.setBounds(10,185,135,20);
		ascend.setSelected(true);
		
		descend = new JRadioButton("Decrescente");
		add(descend);
		descend.setBounds(155,185,135,20);
		
		orderType = new ButtonGroup();
		orderType.add(ascend);
		orderType.add(descend);
		
		label = new JLabel("Número de imagens na primeira linha");
		add(label);
		label.setBounds(10,210,280,20);
		
		firstLine = new JTextField("4");
		add(firstLine);
		firstLine.setBounds(10,235,280,20);
		
		label = new JLabel("Incremento do número de imagens por linha");
		add(label);
		label.setBounds(10,260,280,20);
		
		increment = new JTextField("2");
		add(increment);
		increment.setBounds(10,285,280,20);
		
		label = new JLabel("Largura da Imagem");
		add(label);
		label.setBounds(10,310,280,20);		
		
		width = new JTextField(String.valueOf(tk.getScreenSize().width));
		add(width);
		width.setBounds(10,335,280,20);
		
		remove = new JCheckBox("Remover duplicatas?");
		add(remove);
		remove.setBounds(10,360,280,20);
		remove.setSelected(true);
		
		/*Botões*/
		selectDirectory = new JButton("...");
		add(selectDirectory);
		selectDirectory.setBounds(245,35,45,20);
		selectDirectory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jf = new JFileChooser(path);
				jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int x = jf.showOpenDialog(ref);
				if(x == JFileChooser.APPROVE_OPTION){
					File f = jf.getSelectedFile();
					String s = f.getAbsolutePath();
					dirName.setText(s+"/");
				}
			}			
		});
		
		ok = new JButton("OK");
		add(ok);
		ok.setBounds(100,385,100,20);
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ref.dispose();
				boolean b = ascend.isSelected();
				int n = Integer.parseInt(firstLine.getText());
				int i = Integer.parseInt(increment.getText());
				int w = Integer.parseInt(width.getText());
				boolean r = remove.isSelected();
				try {
					ImageCloud.imageCloud(br,dirName.getText(),imageNameColumn.getSelectedIndex(),paramColumn.getSelectedIndex(),b,n,i,w,r,null);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}			
		});
		
		
		setVisible(true);
		
	}
	
	
}
