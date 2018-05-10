/*
*
*ImageCloud v1.01
*autor: W. Lopes
*
*/
package imageCloud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ImageCloud {
	
	public static String delimiter = ",";	

	public static void main(String[] args) throws IOException{
		
		if(args.length > 0 ){
			delimiter = args[0];
		}
		new SelectionWindow();		
		//VERSÃO AUTOMÁTICA
		/*BufferedReader br = new BufferedReader(new FileReader(new File("configs.txt")));
		String fileName = br.readLine();
		String imageDir = br.readLine();
		int column1 = Integer.parseInt(br.readLine());
		int column2 = Integer.parseInt(br.readLine());
		boolean ascend = br.readLine().compareTo("ascend")==0;
		int n = Integer.parseInt(br.readLine());
		int i = Integer.parseInt(br.readLine());
		int w = Integer.parseInt(br.readLine());
		boolean removeDuplicates = br.readLine().compareTo("remove")==0;
		String outputFileName = br.readLine();
		br.close();
		
		br = new BufferedReader(new FileReader(new File(fileName)));
		String[] line = br.readLine().split(";");
		
		column1 = (column1<0) ? column1+line.length : column1;
		column2 = (column2<0) ? column2+line.length : column2;
		
		imageCloud(br, imageDir, column1,column2,ascend,n,i,w,removeDuplicates,outputFileName);*/
	}
		
	public static void imageCloud(BufferedReader br, String dir, int column1, int column2, final boolean ascend, int n, int i, int w, boolean r,String outputFileName) throws NumberFormatException, IOException{
	
	ArrayList<ListValue> lista = new ArrayList<>();
	
	File[] listFiles = new File(dir).listFiles();
	ArrayList<String> namesList = new ArrayList<>();
	for(File f : listFiles){
		namesList.add(f.getName());
	}
	
	/*Lendo o arquivo*/
	while(br.ready()){
		String[] line = br.readLine().split(delimiter);			
		String fileName = line[column1%line.length].replace("\"","");
		Double value = Double.parseDouble(line[column2%line.length].replace("\"",""));
		if(namesList.contains(fileName)){
			lista.add(new ListValue(fileName,value));
		}else{
			System.out.println("Arquivo " + fileName + " não encontrado.");
		}
	}
	
	/*Ordena o arquivo
	 * ascend = true : ordena de forma crescente
	 * ascend = false: oredena de forma decrescente*/
	if(ascend){
		Collections.sort(lista, new Comparator<ListValue>(){
			@Override
			public int compare(ListValue o1, ListValue o2) {
				return o1.value < o2.value ? -1 : (o1.value > o2.value ? 1 : 0);
			}			
		});
	}else{
		Collections.sort(lista, new Comparator<ListValue>(){
			@Override
			public int compare(ListValue o1, ListValue o2) {
				return o1.value > o2.value ? -1 : (o1.value < o2.value ? 1 : 0);
			}			
		});
	}
		
	/*Remove duplicatas se r for true*/
	if(r){
		for(int j = 0; j<lista.size()-1; j++){
			for(int k= j+1; k <lista.size();){
				if(lista.get(j).name.compareTo(lista.get(k).name)==0){
					lista.remove(k);
				}else{
					k++;
				}
			}
		}		
	}
			
	/*Calculando o tamanho da saída*/
	int screenWidth = w;
	System.out.println(screenWidth);
	
	/*Criando a janela*/
	final JFrame jf = new JFrame("Image Cloud v1.1");
	jf.setLayout(null);
	final JPanel panel = new JPanel(null);
	jf.add(panel);
	
	/*Preparando o espaço de memória para as imagens*/
	JLabel[] labels = new JLabel[lista.size()];
	ImageIcon[] images = new ImageIcon[lista.size()];
	
	/*Inicializando as variáveis de posição*/
	int y = 0;
	int counter = 0;
	
	/*Renderização das imagens*/
	int size = 0;
	
	for(ListValue lv : lista){		
		/*Calculando a posição da imagem*/
		int x = counter * screenWidth / n;
		size = screenWidth/n;
		
		/*Carrega a imagem*/
		images[i] = new ImageIcon(dir + lv.name);
		images[i].setImage(images[i].getImage().getScaledInstance(size,size,100));
		labels[i] = new JLabel(images[i]);
		panel.add(labels[i]);
				
		/*Plota a imagem*/		
		labels[i].setBounds(x,y,size,size);
				
		/*Verifica se é necessário passar para a próxima linha*/
		counter++;
		if(counter == n){
			n = n+i;
			counter = 0;
			/*Incrementa a posição da linha*/
			y = y + size;
			System.out.println(lv.name + " " + x + " " + y + " " + size);
		}
		
	}
	/*Atualiza o tamanho da linha no fim da plotagem, se necessário*/
	if(counter!=0){
		y = y+ size;
	}
	
	/*Menu para salvar a imagem gerada*/
	JMenuBar mb = new JMenuBar();
	jf.setJMenuBar(mb);
	JMenu menu = new JMenu("Arquivo");
	mb.add(menu);
	JMenuItem item = new JMenuItem("Salvar");
	menu.add(item);	
	
	/*Mostra a janela*/		
	panel.setBounds(0,0,screenWidth,y);	
	jf.setSize(screenWidth,y);
	jf.setVisible(true);
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	if(outputFileName != null){
		saveFile(jf,panel,outputFileName);
		jf.dispose();
		System.out.println(outputFileName + " salvo com sucesso");
		System.exit(0);
	}
	
	/*Efeito do item do menu*/
	item.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {			
			saveFile(jf,panel,null);			
		}
	});
	
	
	/*Fecha o stream de leitura*/
	br.close();
	}
	
	public static void saveFile(JFrame jf, JPanel panel, String outputFilename){
		BufferedImage image = new BufferedImage(panel.getWidth(),panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		panel.paint(image.getGraphics());
		
		if(outputFilename == null){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int temp = chooser.showSaveDialog(jf);
			if(temp == JFileChooser.APPROVE_OPTION){
				File output = chooser.getSelectedFile();
				writeImage(output, image);			
			}
		}else{
			writeImage(new File(outputFilename),image);
		}
	}
	
	public static void writeImage(File output, BufferedImage image){
		try {
			ImageIO.write((RenderedImage)image, "PNG", output);
			System.out.println(output.getAbsolutePath() + " salvo com sucesso.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
