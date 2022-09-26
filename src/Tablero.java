import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.Component;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class Tablero extends JFrame{

	static int num_filas;
	static int num_columnas;
	static int trenes_a_trozos;
	private JFrame frmTrenecitos;
	static ArrayList<Tren> trenes = new ArrayList<Tren>();
	static ArrayList<Colision> colisiones = new ArrayList<Colision>();
	JButton botones[][] = new JButton[30][30];
	boolean auxiliar = false;
	int tipo_de_tren;
	int num_trenes;
	StringBuffer longitud_tren = new StringBuffer();
	StringBuffer posx_tren = new StringBuffer();
	StringBuffer posy_tren = new StringBuffer();
	File archivo;
	FileInputStream input;
	FileOutputStream output;
	JFileChooser chooser = new JFileChooser();
	public static void main(String[] args) {
		JTextField filas = new JTextField(2);
		JPanel panel_filas = new JPanel();
		panel_filas.add(new JLabel("Filas:"));
		panel_filas.add(filas);
		num_filas = JOptionPane.showConfirmDialog(null, panel_filas, 
                  "Introduce el numero de filas", JOptionPane.OK_CANCEL_OPTION);
		num_filas = Integer.valueOf(filas.getText());
		JTextField columnas = new JTextField(2);
		JPanel panel_columnas = new JPanel();
		panel_columnas.add(new JLabel("Columnas:"));
		panel_columnas.add(columnas);
		num_columnas = JOptionPane.showConfirmDialog(null, panel_columnas, 
                  "Introduce el numero de columnas", JOptionPane.OK_CANCEL_OPTION);
		num_columnas = Integer.valueOf(columnas.getText());
			if((num_filas < 10 || num_filas > 30) || (num_columnas < 10 || num_columnas > 30)) {
				System.out.println("Error: numero de filas o columnas incorrecto(debe ser entre 10 y 30)");
				System.exit(0);
			}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tablero window = new Tablero();
					window.frmTrenecitos.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Constructor del tablero
	 */
	public Tablero() {
		initialize();
	}
	/**
	 * Inicializacion del tablero
	 */
	private void initialize() {
		
		frmTrenecitos = new JFrame();
		frmTrenecitos.setTitle("Trenecitos");
		/**
		 * USAR SETPOS Y SETSIZE: EL TAMAÑO DEPENDERA DEL NUMERO DE FILAS Y COLUMNAS
		 */
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int x = pantalla.width;
		int y = pantalla.height;
		frmTrenecitos.setLocation(x/2, y/2);
		frmTrenecitos.setSize(num_columnas*51, num_filas*41);
		frmTrenecitos.setLocationRelativeTo(null);
		frmTrenecitos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel_arriba = new JPanel();
		frmTrenecitos.getContentPane().add(panel_arriba, BorderLayout.NORTH);
		panel_arriba.setLayout(new BoxLayout(panel_arriba,BoxLayout.X_AXIS));
		
		JPanel panel_izq = new JPanel();
		frmTrenecitos.getContentPane().add(panel_izq, BorderLayout.WEST);
		panel_izq.setLayout(new BoxLayout(panel_izq,BoxLayout.Y_AXIS));
		JPanel panel_medio = new JPanel();
		panel_medio.addKeyListener(new KeyListener() {

			//EN ESTE PAR DE METODOS NO HAREMOS NADA DE MOMENTO
			public void keyTyped(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				int arriba = KeyEvent.VK_UP;
				int abajo = KeyEvent.VK_DOWN;
				int derecha = KeyEvent.VK_RIGHT;
				int izquierda = KeyEvent.VK_LEFT;
				if(e.getKeyCode() == arriba) {
					for(int i=0;i<trenes.size();i++) {
						if(trenes.get(i).getDireccion() == 'A') {
							trenes.get(i).avanzar('A');
							moverTren(trenes.get(i),botones);
						}
					}
					
				}else if(e.getKeyCode() == abajo) {
					for(int i=0;i<trenes.size();i++) {
						if(trenes.get(i).getDireccion() == 'B') {
							trenes.get(i).avanzar('B');
							moverTren(trenes.get(i),botones);
						}
					}
					
				}else if(e.getKeyCode() == derecha) {
					for(int i=0;i<trenes.size();i++) {
						if(trenes.get(i).getDireccion() == 'D') {
							trenes.get(i).avanzar('D');
							moverTren(trenes.get(i),botones);
						}
					}
					
				}else if(e.getKeyCode() == izquierda) {
					for(int i=0;i<trenes.size();i++) {
						if(trenes.get(i).getDireccion() == 'I') {
							trenes.get(i).avanzar('I');
							moverTren(trenes.get(i),botones);
						}
					}
				}
			}
			
		});
		frmTrenecitos.getContentPane().add(panel_medio, BorderLayout.CENTER);
		panel_medio.setLayout(new GridLayout(num_filas+1,num_columnas+1,8,8));
		
		/**
		 * CREACION DE LOS MENUS DESPLEGABLES
		 */
		
		JMenuBar menuBar = new JMenuBar();
		frmTrenecitos.setJMenuBar(menuBar);
		
		JMenu Archivo = new JMenu("Archivo");
		JMenu Editar = new JMenu("Editar");
		JMenu Realizar_simulacion = new JMenu("Realizar Simulacion");
		JMenu Ayuda = new JMenu("Ayuda");
		
		menuBar.add(Archivo);
		menuBar.add(Editar);
		menuBar.add(Realizar_simulacion);
		menuBar.add(Ayuda);
		
		JMenuItem mover_tren = new JMenuItem("Mover trenes");
		mover_tren.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean tren_encontrado = false;
				JTextField x = new JTextField(5);
		         JTextField y = new JTextField(5);
		       
		         JPanel myPanel = new JPanel();
		         myPanel.add(new JLabel("x:"));
		         myPanel.add(x);
		         myPanel.add(Box.createHorizontalStrut(15)); 
		         myPanel.add(new JLabel("y:"));
		         myPanel.add(y);

		         @SuppressWarnings("unused")
				int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                  "Coordenadas de un vagon del tren", JOptionPane.OK_CANCEL_OPTION);
		         for(int i=0;i<trenes.size();i++) {
		        	 for(int j=0;j<trenes.get(i).getLongitud();j++) {
		        		 if(trenes.get(i).vagones.get(j).getPos_x() == Integer.valueOf(x.getText()) && trenes.get(i).vagones.get(j).getPos_y() == Integer.valueOf(y.getText())) {
		        			 moverTren(trenes.get(i),botones);
		        			 break;
		        		 }
		        	 }
		        	 if(tren_encontrado == true) {
		        		 break;
		        	 }
		         }
			}
		});
		JMenuItem trenes_divididos = new JMenuItem("Trenes divididos");
		trenes_divididos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int cont = 0;
				if(trenes.size() < trenes_a_trozos) {
					trenes_a_trozos = trenes_a_trozos-(trenes_a_trozos-trenes.size());
				}
				cont = trenes_a_trozos;
				JOptionPane.showMessageDialog(null, "Numero de trenes divididos: "+ cont, "Trenes divididos",JOptionPane.INFORMATION_MESSAGE);
			}
			
		});
		
		
		
		JMenuItem nuevo = new JMenuItem("Nuevo");
		nuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_medio.removeAll();
				trenes_a_trozos = 0;
				String filas = JOptionPane.showInputDialog(null,"Introduce el numero de filas","Numero de filas",JOptionPane.INFORMATION_MESSAGE);
				num_filas = Integer.valueOf(filas);
				String columnas = JOptionPane.showInputDialog(null,"Introduce el numero de filas","Numero de filas",JOptionPane.INFORMATION_MESSAGE);
				num_columnas = Integer.valueOf(columnas);
				if(num_filas < 10 || num_filas > 30 || num_columnas < 10 || num_columnas > 30) {
					JOptionPane.showMessageDialog(null,"Numero de filas o columnas incorrecto","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				panel_medio.setLayout(new GridLayout(num_filas+1,num_columnas+1,8,8));
				vaciarArrayBotones(botones);
				JLabel vacio = new JLabel(" ");
				panel_medio.add(vacio);
				for(int k=0;k<num_columnas;k++) {
					JLabel coord_columnas = new JLabel(String.valueOf(num_columnas-k-1));
					panel_medio.add(coord_columnas,SwingConstants.CENTER);
				}
				for(int k=0;k<num_filas;k++) {
					JLabel coord_filas = new JLabel(String.valueOf(k));
					panel_medio.add(coord_filas, SwingConstants.CENTER);
					for(int j=0;j<num_columnas;j++) {
							JButton btnNewButton = new JButton(".");
							btnNewButton.setToolTipText(num_columnas-j-1 + "," + k);
							botones[k][num_columnas-j-1] = btnNewButton;
							panel_medio.add(btnNewButton, SwingConstants.CENTER);
					}
				}
				 String resultado = JOptionPane.showInputDialog(null,"Introduce el numero de trenes","Numero de trenes",JOptionPane.INFORMATION_MESSAGE);
				 JTextField direccion = new JTextField(5);
		         JTextField longitud = new JTextField(5);
		         JTextField posx = new JTextField(5);
		         JTextField posy = new JTextField(5);
		         
		         setBotonesDefault(botones);
		         trenes.clear();
		         colisiones.clear();
		         
		         JPanel panel_datos_trenes = new JPanel();
		         panel_datos_trenes.add(new JLabel("Direccion:"));
		         panel_datos_trenes.add(direccion);
		         panel_datos_trenes.add(Box.createHorizontalStrut(15));
		         panel_datos_trenes.add(new JLabel("Longitud:"));
		         panel_datos_trenes.add(longitud);
		         panel_datos_trenes.add(Box.createHorizontalStrut(15));
		         panel_datos_trenes.add(new JLabel("Posicion X:"));
		         panel_datos_trenes.add(posx);
		         panel_datos_trenes.add(Box.createHorizontalStrut(15));
		         panel_datos_trenes.add(new JLabel("Posicion Y:"));
		         panel_datos_trenes.add(posy);

		         for(int i=0;i<Integer.valueOf(resultado);i++) {
		        	 @SuppressWarnings("unused")
					int result = JOptionPane.showConfirmDialog(null, panel_datos_trenes, 
			                  "Introduce los datos del tren " + i, JOptionPane.OK_CANCEL_OPTION);
		        	 Tren tren = new Tren();
		        	 tren.setDireccion(direccion.getText().charAt(0));
		        	 tren.setLongitud(Integer.valueOf(longitud.getText()));
		        	 
		        	 tren.setX_inicial(Integer.valueOf(posx.getText()));
		        	 tren.setY_inicial(Integer.valueOf(posy.getText()));
		        	 tren.setTipo(tren.getDireccion());
		        	 tren.setAllVagones();
		        	 tren.setNum_tren(i);
		        	 tren.setPosFinal();
		        	 switch(tren.getDireccion()) {
	        		 case 'A':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
	        					 
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        							
	        								 trenes.get(k).colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 case 'B':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
	        					 
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        							
	        								 trenes.get(k).colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 case 'I':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo())); 
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        						
	        								 trenes.get(k).colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 case 'D':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        								
	        								 trenes.get(k).colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        								 
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 }
		        		 trenes.add(tren);
		         }
			}
		});
		JMenuItem borrar_tren = new JMenuItem("Borrar Tren");
		borrar_tren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean tren_borrado = false;
				JTextField x = new JTextField(5);
		         JTextField y = new JTextField(5);
		       
		         JPanel myPanel = new JPanel();
		         myPanel.add(new JLabel("x:"));
		         myPanel.add(x);
		         myPanel.add(Box.createHorizontalStrut(15)); 
		         myPanel.add(new JLabel("y:"));
		         myPanel.add(y);

		         @SuppressWarnings("unused")
				int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                  "Coordenadas de un vagon del tren", JOptionPane.OK_CANCEL_OPTION);
				for(int i=0;i<trenes.size();i++) {
					if(tren_borrado == false) {
						for(int j=0;j<trenes.get(i).vagones.size();j++) {
							if(trenes.get(i).vagones.get(j).getPos_x() == Integer.valueOf(x.getText()) && trenes.get(i).vagones.get(j).getPos_y() == Integer.valueOf(y.getText())) {
								for(int k=0;j<trenes.get(i).getLongitud();k++) {
									 if(botones[trenes.get(i).vagones.get(k).getPos_y()][trenes.get(i).vagones.get(k).getPos_x()].getText() == "X") {
										 
										 
									 }else {
										 botones[trenes.get(i).vagones.get(k).getPos_y()][trenes.get(i).vagones.get(k).getPos_x()].setText(".");
									 }
			        			}
								trenes.remove(trenes.get(i));
								tren_borrado = true;
								break;
							}
						}

						
					}
				}
					
			}
		});
		JMenuItem deshacer = new JMenuItem("Deshacer");
		deshacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Funcion no implementada :(","Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		JMenuItem rehacer = new JMenuItem("Rehacer");
		rehacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Funcion no implementada :(","Error",JOptionPane.ERROR_MESSAGE);
			}
		});
		JMenuItem simulacion_completa = new JMenuItem("Realizar simulacion");
		simulacion_completa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "¿Quieres continuar?","Simulacion completa",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.OK_OPTION) {
					simulacion(botones);
				}
				
			}
		});
		JMenuItem acerca_de = new JMenuItem("Acerca de...");
		acerca_de.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Programa realizado por David García Díez, de DNI 71478250T.\n Espero que todo funcione como deberia.","Acerca de...",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JMenuItem info = new JMenuItem("Informacion adicional...");
		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Funciones de deshacer y rehacer no implementadas.","Informacion adicional...",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JMenuItem salir = new JMenuItem("Salir");
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(frmTrenecitos,"¿Estás seguro de que quieres cerrar la aplicación?","Confirmación de cierre",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
			}
		});
		JMenuItem importar_problema = new JMenuItem("Importar tablero");
		importar_problema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean trozo = false;
				panel_medio.removeAll();
				if(chooser.showDialog(null,"Abrir") == chooser.APPROVE_OPTION) {
					int i = 0;
					archivo = chooser.getSelectedFile();
					if(archivo.canRead() && archivo.getName().endsWith("txt")) {
						String doc = abrirTxt(archivo);
						String arraydoc[] = doc.split(" ");
						num_filas = Integer.valueOf(arraydoc[i]);
						i++;
						num_columnas = Integer.valueOf(arraydoc[i]);
						i++;
						vaciarArrayBotones(botones);
						panel_medio.setLayout(new GridLayout(num_filas+1,num_columnas+1,8,8));
						JLabel vacio = new JLabel(" ");
						panel_medio.add(vacio);
						for(int k=0;k<num_columnas;k++) {
							JLabel coord_columnas = new JLabel(String.valueOf(num_columnas-k-1));
							panel_medio.add(coord_columnas,SwingConstants.CENTER);
						}
						for(int k=0;k<num_filas;k++) {
							JLabel coord_filas = new JLabel(String.valueOf(k));
							panel_medio.add(coord_filas, SwingConstants.CENTER);
							for(int j=0;j<num_columnas;j++) {
									JButton btnNewButton = new JButton(".");
									btnNewButton.setToolTipText(num_columnas-j-1 + "," + k);
									botones[k][num_columnas-j-1] = btnNewButton;
									panel_medio.add(btnNewButton, SwingConstants.CENTER);
							}
						}
						
						int numtrenes = Integer.valueOf(arraydoc[i]);
						Tren tren_nuevo = new Tren();
						tren_nuevo.setDireccion(arraydoc[i=i+1].charAt(0));
						tren_nuevo.setLongitud(Integer.valueOf(arraydoc[i=i+1]));
						
						tren_nuevo.setX_inicial(Integer.valueOf(arraydoc[i=i+1]));
						tren_nuevo.setY_inicial(Integer.valueOf(arraydoc[i=i+1]));
						tren_nuevo.setTipo(tren_nuevo.getDireccion());
						tren_nuevo.setAllVagones();
						tren_nuevo.setNum_tren(trenes.size());
						tren_nuevo.setPosFinal();
							switch(tren_nuevo.getDireccion()) {
			        		 case 'A':
			        			 for(int j=0;j<tren_nuevo.getLongitud();j++) {
			        				 if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() == ".") {
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText(String.valueOf(tren_nuevo.getTipo()));
			        					 
			        				 }else if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "." && botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "X"){
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText("X");
			        					 
			        					 tren_nuevo.colisiones.add(new Colision(tren_nuevo.getX_inicial(),tren_nuevo.vagones.get(j).getPos_y()));
			        					 colisiones.add(new Colision(tren_nuevo.getX_inicial(),tren_nuevo.vagones.get(j).getPos_y()));
			        					 for(int k=0;k<trenes.size();k++) {
			        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
			        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren_nuevo.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren_nuevo.vagones.get(j).getPos_y()){
			        								 
			        								 trenes.get(k).colisiones.add(new Colision(tren_nuevo.getX_inicial(),tren_nuevo.vagones.get(j).getPos_y()));
			        								 
			        							 }
			        						 }
			        					 }
			        				 }
			        			 }
			        			 break;
			        		 case 'B':
			        			 for(int j=0;j<tren_nuevo.getLongitud();j++) {
			        				 if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() == ".") {
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText(String.valueOf(tren_nuevo.getTipo()));
			        					 
			        				 }else if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "." && botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "X"){
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText("X");
			        					
			        					 tren_nuevo.colisiones.add(new Colision(tren_nuevo.getX_inicial(),tren_nuevo.vagones.get(j).getPos_y()));
			        					 colisiones.add(new Colision(tren_nuevo.getX_inicial(),tren_nuevo.vagones.get(j).getPos_y()));
			        					 for(int k=0;k<trenes.size();k++) {
			        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
			        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren_nuevo.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren_nuevo.vagones.get(j).getPos_y()){
			        								
			        								 trenes.get(k).colisiones.add(new Colision(tren_nuevo.getX_inicial(),tren_nuevo.vagones.get(j).getPos_y()));
			        							
			        							 }
			        						 }
			        					 }
			        				 }
			        			 }
			        			 break;
			        		 case 'I':
			        			 for(int j=0;j<tren_nuevo.getLongitud();j++) {
			        				 if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() == ".") {
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText(String.valueOf(tren_nuevo.getTipo())); 
			        				 }else if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "." && botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "X"){
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText("X");
			        				
			        					 tren_nuevo.colisiones.add(new Colision(tren_nuevo.vagones.get(j).getPos_x(),tren_nuevo.getY_inicial()));
			        					 colisiones.add(new Colision(tren_nuevo.vagones.get(j).getPos_x(),tren_nuevo.getY_inicial()));
			        					 for(int k=0;k<trenes.size();k++) {
			        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
			        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren_nuevo.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren_nuevo.vagones.get(j).getPos_y()){
			        								
			        								 trenes.get(k).colisiones.add(new Colision(tren_nuevo.vagones.get(j).getPos_x(),tren_nuevo.getY_inicial()));
			        							 }
			        						 }
			        					 }
			        				 }
			        			 }
			        			 break;
			        		 case 'D':
			        			 for(int j=0;j<tren_nuevo.getLongitud();j++) {
			        				 if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() == ".") {
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText(String.valueOf(tren_nuevo.getTipo()));
			        				 }else if(botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "." && botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].getText() != "X"){
			        					 botones[tren_nuevo.vagones.get(j).getPos_y()][tren_nuevo.vagones.get(j).getPos_x()].setText("X");
			        				
			        					 tren_nuevo.colisiones.add(new Colision(tren_nuevo.vagones.get(j).getPos_x(),tren_nuevo.getY_inicial()));
			        					 colisiones.add(new Colision(tren_nuevo.vagones.get(j).getPos_x(),tren_nuevo.getY_inicial()));
			        					 for(int k=0;k<trenes.size();k++) {
			        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
			        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren_nuevo.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren_nuevo.vagones.get(j).getPos_y()){
			        								 
			        								 trenes.get(k).colisiones.add(new Colision(tren_nuevo.vagones.get(j).getPos_x(),tren_nuevo.getY_inicial()));
			        							 }
			        						 }
			        					 }
			        				 }
			        			 break;
							}
				        		 
						}
							trenes.add(tren_nuevo);
						for(int m=1;m<=numtrenes;m++) {
							trozo = false;
							Tren tren = new Tren();
							tren.setDireccion(arraydoc[i=i+1].charAt(0));
							tren.setLongitud(Integer.valueOf(arraydoc[i=i+1]));
							
							tren.setX_inicial(Integer.valueOf(arraydoc[i=i+1]));
							tren.setY_inicial(Integer.valueOf(arraydoc[i=i+1]));
							
							switch(tren.getDireccion()) {
							case 'A':
								
								if(tren.getDireccion() == trenes.get(m-1).getDireccion() && tren.getX_inicial() == trenes.get(m-1).getX_inicial() && tren.getY_inicial() == (trenes.get(m-1).getY_inicial() - trenes.get(m-1).getLongitud() - 1)) {
									//si se cumple la condicion modificar el tren anterior y restar la variable m
									trozo = true;
									trenes_a_trozos++;
									colisiones.add(new Colision(trenes.get(m-1).getX_inicial(),trenes.get(m-1).getY_inicial() - trenes.get(m-1).getLongitud()));
									trenes.get(m-1).setLongitud(trenes.get(m-1).getLongitud() + tren.getLongitud());
									trenes.get(m-1).setPosFinal();
									trenes.get(m-1).vagones.removeAll(trenes.get(m-1).vagones);
									for(int n=0;n<trenes.get(m-1).getLongitud();n++) {
										trenes.get(m-1).vagones.add(new Vagon(trenes.get(m-1).getX_inicial(),trenes.get(m-1).getY_inicial()-n,trenes.get(m-1).getTipo()));
									}
									for(int j=0;j<trenes.get(m-1).vagones.size();j++) {
										if(botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].getText() == ".") {
											botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].setText(String.valueOf(trenes.get(m-1).getTipo()));
										}
									}
									if(String.valueOf(arraydoc[i+1]).equals("A") || String.valueOf(arraydoc[i+1]).equals("B") || String.valueOf(arraydoc[i+1]).equals("I") || String.valueOf(arraydoc[i+1]).equals("D")) {
										m = m-1;
									}
									
								}
								
								break;
							case 'B':
								
								if(tren.getDireccion() == trenes.get(m-1).getDireccion() && tren.getX_inicial() == trenes.get(m-1).getX_inicial() && tren.getY_inicial() == (trenes.get(m-1).getY_inicial() + trenes.get(m-1).getLongitud() + 1)) {
									//si se cumple la condicion modificar el tren anterior y restar la variable m
									trozo = true;
									trenes_a_trozos++;
									colisiones.add(new Colision(trenes.get(m-1).getX_inicial(),trenes.get(m-1).getY_inicial() + trenes.get(m-1).getLongitud()));
									trenes.get(m-1).setLongitud(trenes.get(m-1).getLongitud() + tren.getLongitud());
									trenes.get(m-1).setPosFinal();
									trenes.get(m-1).vagones.removeAll(trenes.get(m-1).vagones);
									for(int n=0;n<trenes.get(m-1).getLongitud();n++) {
										trenes.get(m-1).vagones.add(new Vagon(trenes.get(m-1).getX_inicial(),trenes.get(m-1).getY_inicial()+n,trenes.get(m-1).getTipo()));
									}
									for(int j=0;j<trenes.get(m-1).vagones.size();j++) {
										if(botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].getText() == ".") {
											botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].setText(String.valueOf(trenes.get(m-1).getTipo()));
										}
									}
									if(String.valueOf(arraydoc[i+1]).equals("A") || String.valueOf(arraydoc[i+1]).equals("B") || String.valueOf(arraydoc[i+1]).equals("I") || String.valueOf(arraydoc[i+1]).equals("D")) {
										m = m-1;
									}
								}
								
								break;
							case 'I':
								
								if(tren.getDireccion() == trenes.get(m-1).getDireccion() && tren.getX_inicial() == trenes.get(m-1).getX_inicial() + trenes.get(m-1).getLongitud() + 1 && tren.getY_inicial()  == trenes.get(m-1).getY_inicial()) {
									//si se cumple la condicion modificar el tren anterior y restar la variable m
									trozo = true;
									trenes_a_trozos++;
									colisiones.add(new Colision(trenes.get(m-1).getX_inicial() + trenes.get(m-1).getLongitud(),trenes.get(m-1).getY_inicial()));
									trenes.get(m-1).setLongitud(trenes.get(m-1).getLongitud() + tren.getLongitud());
									trenes.get(m-1).setPosFinal();
									trenes.get(m-1).vagones.removeAll(trenes.get(m-1).vagones);
									for(int n=0;n<trenes.get(m-1).getLongitud();n++) {
										trenes.get(m-1).vagones.add(new Vagon(trenes.get(m-1).getX_inicial()+n,trenes.get(m-1).getY_inicial(),trenes.get(m-1).getTipo()));
									}
									for(int j=0;j<trenes.get(m-1).vagones.size();j++) {
										if(botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].getText() == ".") {
											botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].setText(String.valueOf(trenes.get(m-1).getTipo()));
										}
									}
									if(String.valueOf(arraydoc[i+1]).equals("A") || String.valueOf(arraydoc[i+1]).equals("B") || String.valueOf(arraydoc[i+1]).equals("I") || String.valueOf(arraydoc[i+1]).equals("D")) {
										m = m-1;
									}
								}
								
								break;
							case 'D':
								
								if(tren.getDireccion() == trenes.get(m-1).getDireccion() && tren.getX_inicial() == trenes.get(m-1).getX_inicial() - trenes.get(m-1).getLongitud() - 1 && tren.getY_inicial()  == trenes.get(m-1).getY_inicial()) {
									//si se cumple la condicion modificar el tren anterior y restar la variable m
									trozo = true;
									trenes_a_trozos++;
									colisiones.add(new Colision(trenes.get(m-1).getX_inicial() - trenes.get(m-1).getLongitud(),trenes.get(m-1).getY_inicial()));
									trenes.get(m-1).setLongitud(trenes.get(m-1).getLongitud() + tren.getLongitud());
									trenes.get(m-1).setPosFinal();
									trenes.get(m-1).vagones.removeAll(trenes.get(m-1).vagones);
									for(int n=0;n<trenes.get(m-1).getLongitud();n++) {
										trenes.get(m-1).vagones.add(new Vagon(trenes.get(m-1).getX_inicial()-n,trenes.get(m-1).getY_inicial(),trenes.get(m-1).getTipo()));
									}
									for(int j=0;j<trenes.get(m-1).vagones.size();j++) {
										if(botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].getText() == ".") {
											botones[trenes.get(m-1).vagones.get(j).getPos_y()][trenes.get(m-1).vagones.get(j).getPos_x()].setText(String.valueOf(trenes.get(m-1).getTipo()));
										}
									}
									if(String.valueOf(arraydoc[i+1]).equals("A") || String.valueOf(arraydoc[i+1]).equals("B") || String.valueOf(arraydoc[i+1]).equals("I") || String.valueOf(arraydoc[i+1]).equals("D")) {
										m = m-1;
									}
								}
								
								break;
							}
							if(trozo == false) {
								tren.setTipo(tren.getDireccion());
								tren.setAllVagones();
								tren.setNum_tren(trenes.size());
								tren.setPosFinal();
									switch(tren.getDireccion()) {
					        		 case 'A':
					        			 for(int j=0;j<tren.getLongitud();j++) {
					        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
					        					 
					        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
					        					 
					        					 tren.colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
					        					 colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
					        					 for(int k=0;k<trenes.size();k++) {
					        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
					        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
					        								 
					        								 trenes.get(k).colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
					        								 
					        							 }
					        						 }
					        					 }
					        				 }
					        			 }
					        			 break;
					        		 case 'B':
					        			 for(int j=0;j<tren.getLongitud();j++) {
					        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
					        					 
					        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
					        					
					        					 tren.colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
					        					 colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
					        					 for(int k=0;k<trenes.size();k++) {
					        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
					        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
					        								
					        								 trenes.get(k).colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
					        							
					        							 }
					        						 }
					        					 }
					        				 }
					        			 }
					        			 break;
					        		 case 'I':
					        			 for(int j=0;j<tren.getLongitud();j++) {
					        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo())); 
					        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
					        				
					        					 tren.colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
					        					 colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
					        					 for(int k=0;k<trenes.size();k++) {
					        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
					        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
					        								
					        								 trenes.get(k).colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
					        							 }
					        						 }
					        					 }
					        				 }
					        			 }
					        			 break;
					        		 case 'D':
					        			 for(int j=0;j<tren.getLongitud();j++) {
					        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
					        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
					        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
					        				
					        					 tren.colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
					        					 colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
					        					 for(int k=0;k<trenes.size();k++) {
					        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
					        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
					        								 
					        								 trenes.get(k).colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
					        							 }
					        						 }
					        					 }
					        				 }
					        			 break;
									}
								}

					        		 trenes.add(tren);
							}
							
				         }
						int tam = Integer.valueOf(arraydoc[i=i+1]);
						for(int l=0;l<tam;l++) {
							colisiones.add(new Colision(Integer.valueOf(arraydoc[i=i+1]),Integer.valueOf(arraydoc[i=i-1])));
							
						}
						for(int m=0;m<colisiones.size();m++) {
							botones[colisiones.get(m).getPos_y()][colisiones.get(m).getPos_x()].setText("X");
						}
					 }
						
					}else {
						JOptionPane.showMessageDialog(null,"El archivo no se puede importar","Error",JOptionPane.ERROR_MESSAGE);
					}
				
				}
		});
		JMenuItem Guardar_tren = new JMenuItem("Guardar Tablero");
		Guardar_tren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chooser.showDialog(null,"Guardar") == JFileChooser.APPROVE_OPTION) {
					archivo = chooser.getSelectedFile();
					if(archivo.getName().endsWith("txt")) {
					StringBuffer doc = new StringBuffer();
					doc.append(num_filas + " ");
					doc.append(num_columnas);
					doc.append(" \n" + trenes.size() + " ");
					for(int i=0;i<trenes.size();i++) {
						doc.append("\n");
						
						if(trenes.get(i).colisiones.size() != 0) {
							doc.append(trenes.get(i).getDireccion() + " ");
							switch(trenes.get(i).getDireccion()) {
							case 'A':
								doc.append(trenes.get(i).getY_inicial() - trenes.get(i).colisiones.get(0).getPos_y() + " ");
								break;
							case 'B':
								doc.append(trenes.get(i).colisiones.get(0).getPos_y() - trenes.get(i).getY_inicial() + " ");
								break;
							case 'I':
								doc.append(trenes.get(i).colisiones.get(0).getPos_x() - trenes.get(i).getX_inicial() + " ");
								break;
							case 'D':
								doc.append(trenes.get(i).getX_inicial() - trenes.get(i).colisiones.get(0).getPos_x() + " ");
								break;
							}
							doc.append(trenes.get(i).getX_inicial() + " ");
							doc.append(trenes.get(i).getY_inicial() + " ");
							for(int j=0;j<trenes.get(i).colisiones.size();j++) {
								switch(trenes.get(i).getDireccion()) {
								case 'A':
									doc.append(trenes.get(i).getDireccion() + " ");
									if(j+1 < trenes.get(i).colisiones.size()) {
										doc.append(trenes.get(i).colisiones.get(j).getPos_y() - trenes.get(i).colisiones.get(j+1).getPos_y() + " ");
										 doc.append(trenes.get(i).getX_inicial() + " ");
										 doc.append(trenes.get(i).colisiones.get(j).getPos_y() - 1 + " ");
										
									}else {
										 doc.append(trenes.get(i).colisiones.get(j).getPos_y() - trenes.get(i).getY_final()+  " ");
										 doc.append(trenes.get(i).getX_inicial() + " ");
										 doc.append(trenes.get(i).colisiones.get(j).getPos_y() - 1  + " ");
									}
									break;
								case 'B':
									doc.append(trenes.get(i).getDireccion() + " ");
									if(j+1 < trenes.get(i).colisiones.size()) {
										doc.append(trenes.get(i).colisiones.get(j+1).getPos_y() - trenes.get(i).colisiones.get(j).getPos_y() +  " ");
										 doc.append(trenes.get(i).getX_inicial() + " ");
										 doc.append(trenes.get(i).colisiones.get(j).getPos_y() + 1 + " ");
										
									}else {
										doc.append(trenes.get(i).getY_final() - trenes.get(i).colisiones.get(j).getPos_y() +  " ");
										 doc.append(trenes.get(i).getX_inicial() + " ");
										 doc.append(trenes.get(i).colisiones.get(j).getPos_y() + 1 + " ");
									}
									break;
								case 'I':
									doc.append(trenes.get(i).getDireccion() + " ");
									if(j+1 < trenes.get(i).colisiones.size()) {
										doc.append(trenes.get(i).colisiones.get(j+1).getPos_x() - trenes.get(i).colisiones.get(j).getPos_x() + " ");
										doc.append(trenes.get(i).colisiones.get(j).getPos_x() + 1 + " ");
										doc.append(trenes.get(i).getY_inicial() + " ");
										
									}else {
										doc.append(trenes.get(i).getX_final() - trenes.get(i).colisiones.get(j).getPos_x() + " ");
										doc.append(trenes.get(i).colisiones.get(j).getPos_x() + 1 + " ");
										doc.append(trenes.get(i).getY_inicial() + " ");
									}
									break;
								case 'D':
									doc.append(trenes.get(i).getDireccion() + " ");
									if(j+1 < trenes.get(i).colisiones.size()) {
										doc.append(trenes.get(i).colisiones.get(j).getPos_x() - trenes.get(i).colisiones.get(j+1).getPos_x() + " ");
										doc.append(trenes.get(i).colisiones.get(j).getPos_x() - 1 + " ");
										doc.append(trenes.get(i).getY_inicial() + " ");
										
									}else {
										doc.append(trenes.get(i).colisiones.get(j).getPos_x() - trenes.get(i).getX_final() + " ");
										doc.append(trenes.get(i).colisiones.get(j).getPos_x() - 1 + " ");
										doc.append(trenes.get(i).getY_inicial() + " ");
									}
									break;
								}
							}
							
						}else {
							doc.append(trenes.get(i).getDireccion() + " ");
							doc.append(trenes.get(i).getLongitud() + " ");
							doc.append(trenes.get(i).getX_inicial() + " ");
							doc.append(trenes.get(i).getY_inicial() + " ");
							
						}
					}
					doc.append("\n" + colisiones.size() + " ");
					for(int i=0;i<colisiones.size();i++) {
						doc.append("\n");
						doc.append(colisiones.get(i).getPos_y() + " ");
						doc.append(colisiones.get(i).getPos_x() + " ");
					}
						saveTxt(archivo,doc.toString());
						JOptionPane.showMessageDialog(null, "Archivo guardado","Informacion",JOptionPane.INFORMATION_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(null,"El archivo no se puede guardar","Error",JOptionPane.ERROR_MESSAGE);
					}
						
				}
			}
			
		});
		
		JMenuItem add_tren = new JMenuItem("Añadir tren");
		add_tren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 JTextField direccion = new JTextField(5);
		         JTextField longitud = new JTextField(5);
		         JTextField posx = new JTextField(5);
		         JTextField posy = new JTextField(5);
		         
		         JPanel panel_datos_trenes = new JPanel();
		         panel_datos_trenes.add(new JLabel("Direccion:"));
		         panel_datos_trenes.add(direccion);
		         panel_datos_trenes.add(Box.createHorizontalStrut(15));
		         panel_datos_trenes.add(new JLabel("Longitud:"));
		         panel_datos_trenes.add(longitud);
		         panel_datos_trenes.add(Box.createHorizontalStrut(15));
		         panel_datos_trenes.add(new JLabel("Posicion X:"));
		         panel_datos_trenes.add(posx);
		         panel_datos_trenes.add(Box.createHorizontalStrut(15));
		         panel_datos_trenes.add(new JLabel("Posicion Y:"));
		         panel_datos_trenes.add(posy);
		         @SuppressWarnings("unused")
					int result = JOptionPane.showConfirmDialog(null, panel_datos_trenes, 
			                  "Introduce los datos del tren ", JOptionPane.OK_CANCEL_OPTION);
		        	 Tren tren = new Tren();
		        	 tren.setDireccion(direccion.getText().charAt(0));
		        	 tren.setLongitud(Integer.valueOf(longitud.getText()));
		        	
		        	 tren.setX_inicial(Integer.valueOf(posx.getText()));
		        	 tren.setY_inicial(Integer.valueOf(posy.getText()));
		        	 tren.setTipo(tren.getDireccion());
		        	 tren.setAllVagones();
		        	 tren.setNum_tren(trenes.size());
		        	 tren.setPosFinal();
		        	 switch(tren.getDireccion()) {
	        		 case 'A':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
	        					 
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        								 trenes.get(k).colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 case 'B':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
	        					 
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        								
	        								 trenes.get(k).colisiones.add(new Colision(tren.getX_inicial(),tren.vagones.get(j).getPos_y()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 case 'I':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo())); 
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        								
	        								 trenes.get(k).colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 case 'D':
	        			 for(int j=0;j<tren.getLongitud();j++) {
	        				 if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() == ".") {
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText(String.valueOf(tren.getTipo()));
	        				 }else if(botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "." && botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].getText() != "X"){
	        					 botones[tren.vagones.get(j).getPos_y()][tren.vagones.get(j).getPos_x()].setText("X");
	        					 trenes_a_trozos++;
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).vagones.size();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()) {
	        								 if(trenes.get(k).colisiones.isEmpty()) {
	        									 trenes_a_trozos++;
	        								 }
	        							 }
	        							 
	        						 }
	        					 }
	        					 tren.colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        					 for(int k=0;k<trenes.size();k++) {
	        						 for(int l=0;l<trenes.get(k).getLongitud();l++) {
	        							 if(trenes.get(k).vagones.get(l).getPos_x() == tren.vagones.get(j).getPos_x() && trenes.get(k).vagones.get(l).getPos_y() == tren.vagones.get(j).getPos_y()){
	        								
	        								 trenes.get(k).colisiones.add(new Colision(tren.vagones.get(j).getPos_x(),tren.getY_inicial()));
	        							 }
	        						 }
	        					 }
	        				 }
	        			 }
	        			 break;
	        		 }
		        	trenes.add(tren);
				Collections.sort(trenes, new Comparator<Tren>(){
					
					@SuppressWarnings("deprecation")
					public int compare(Tren t1, Tren t2) {
						return new Integer(t1.getTipo()).compareTo(new Integer(t2.getTipo()));
					}
				});
			}
			
		});
		
		Archivo.add(nuevo);
		Archivo.add(importar_problema);
		Archivo.add(Guardar_tren);
		Archivo.add(salir);
		Editar.add(add_tren);
		Editar.add(borrar_tren);
		Editar.add(mover_tren);
		Editar.add(deshacer);
		Editar.add(rehacer);
		Realizar_simulacion.add(simulacion_completa);
		Ayuda.add(acerca_de);
		Ayuda.add(info);
		Ayuda.add(trenes_divididos);
		
		/**
		 * indicamos las coordenadas mediante JLabel con el numero de fila/columna correspondiente
		 */
		JLabel vacio = new JLabel("");
		panel_medio.add(vacio);
		for(int i=0;i<num_columnas;i++) {
			JLabel coord_columnas = new JLabel(String.valueOf(num_columnas-i-1));
			panel_medio.add(coord_columnas,SwingConstants.CENTER);
		}
		for(int i=0;i<num_filas;i++) {
			JLabel coord_filas = new JLabel(String.valueOf(i));
			panel_medio.add(coord_filas, SwingConstants.CENTER);
			for(int j=0;j<num_columnas;j++) {
					JButton btnNewButton = new JButton(".");
					btnNewButton.setToolTipText(num_columnas-j-1 + "," + i);
					botones[i][num_columnas-j-1] = btnNewButton;
					panel_medio.add(btnNewButton, SwingConstants.CENTER);
			}
		}
	}
	
	public boolean existeTrenID(int id,ArrayList<Tren> trenes) {
		boolean existe = false;
		for(int i=0;i<trenes.size();i++) {
			if(trenes.get(i).getNum_tren() == id) {
				existe = true;
				break;
			}
		}
		return existe;
	}
	/**
	 * Metodo para mover tren en el tablero
	 * @param tren Tren a mover(YA ACTUALIZADO EN EL EVENTO DE TECLADO)
	 */
	public static void moverTren(Tren tren, JButton botones[][]) {
		if(tren.vagones.size() == 0) {
			trenes.remove(tren);
			return;
		}else {
			switch(tren.getDireccion()) {
			case 'A':
					if(tren.getY_inicial() + 1 == num_filas) {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
							tren.vagones.remove(0);
							botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
							for(int i=0;i<tren.vagones.size();i++) {
								tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() + 1);
							}
						}
						
					}else if(botones[tren.getY_inicial() + 1][tren.getX_inicial()].getText() == "X") {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						tren.vagones.remove(0);
						botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() + 1);
						}
						}
					}else if(botones[tren.getY_inicial() + 1][tren.getX_inicial()].getText() != "." && botones[tren.getY_inicial() + 1][tren.getX_inicial()].getText() != String.valueOf(tren.getTipo())) {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						botones[tren.getY_inicial() + 1][tren.getX_inicial()].setText("X");
						colisiones.add(new Colision(tren.getX_inicial(),tren.getY_inicial() + 1));
						tren.vagones.remove(0);
						botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() + 1);
						}
						}
					}else {
							tren.setY_inicial(tren.getY_inicial() + 1);
							botones[tren.vagones.get(0).getPos_y() + 1][tren.vagones.get(0).getPos_x()].setText(String.valueOf(tren.getTipo()));
							botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
							for(int i=0;i<tren.vagones.size();i++) {
								tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() + 1);
							}
						
					}
				break;
			case 'B':
					if(tren.getY_inicial() == 0) {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						tren.vagones.remove(0);
						botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() - 1);
						}
						}
						
					}else if(botones[tren.getY_inicial() - 1][tren.getX_inicial()].getText() == "X") {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						tren.vagones.remove(0);
						botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() - 1);
						}
						}
						
					}else if(botones[tren.getY_inicial() - 1][tren.getX_inicial()].getText() != "." && botones[tren.getY_inicial() - 1][tren.getX_inicial()].getText() != String.valueOf(tren.getTipo())) {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						botones[tren.getY_inicial() - 1][tren.getX_inicial()].setText("X");
						colisiones.add(new Colision(tren.getX_inicial(),tren.getY_inicial() - 1));
						
						tren.vagones.remove(0);
						botones[tren.vagones.get(tren.vagones.size()-1).getPos_y()][tren.getX_inicial()].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() - 1);
						}
						}
					}else {
						tren.setY_inicial(tren.getY_inicial() - 1);
						botones[tren.getY_inicial() - 1][tren.getX_inicial()].setText(String.valueOf(tren.getTipo()));
						botones[tren.vagones.size() - 1][tren.getX_inicial()].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_y(tren.vagones.get(i).getPos_y() - 1);
						}
						
						
					}
				break;
			case 'I':
					if(tren.getX_inicial() == 0) {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						tren.vagones.remove(0);
						botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() - 1);
						}
						}
					}else if(botones[tren.getY_inicial()][tren.getX_inicial() - 1].getText() == "X") {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						tren.vagones.remove(0);
						botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() - 1);
						}
						}
					}else if(botones[tren.getY_inicial()][tren.getX_inicial() - 1].getText() != "." && botones[tren.getY_inicial()][tren.getX_inicial() - 1].getText() != String.valueOf(tren.getTipo())) {
						if(tren.vagones.size() == 1) {
							trenes.remove(tren);
							botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
							return;
						}else {
						botones[tren.getY_inicial()][tren.getX_inicial() - 1].setText("X");
						colisiones.add(new Colision(tren.getX_inicial() - 1,tren.getY_inicial()));
						tren.vagones.remove(0);
						botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() - 1);
						}
						}
					}else {
						
						botones[tren.getY_inicial()][tren.vagones.size()-1].setText(".");
						botones[tren.getY_inicial()][tren.getX_inicial() - 1].setText(String.valueOf(tren.getTipo()));
						tren.setX_inicial(tren.getX_inicial() - 1);
						for(int i=0;i<tren.vagones.size();i++) {
							tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() - 1);
						}	
						
					}
				break;
			case 'D':
				if(tren.getX_inicial() + 1 == num_columnas) {
					if(tren.vagones.size() == 1) {
						trenes.remove(tren);
						botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
						return;
					}else {
					tren.vagones.remove(0);
					botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
					for(int i=0;i<tren.vagones.size();i++) {
						tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() + 1);
					}
					}
					
				}else if(botones[tren.getY_inicial()][tren.getX_inicial() + 1].getText() == "X") {
					if(tren.vagones.size() == 1) {
						trenes.remove(tren);
						botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
						return;
					}else {
					tren.vagones.remove(0);
					botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
					for(int i=0;i<tren.vagones.size();i++) {
						tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() + 1);
					}
					}
				}else if(botones[tren.getY_inicial()][tren.getX_inicial() + 1].getText() != "." && botones[tren.getY_inicial()][tren.getX_inicial() + 1].getText() != String.valueOf(tren.getTipo())) {
					if(tren.vagones.size() == 1) {
						trenes.remove(tren);
						botones[tren.getY_inicial()][tren.getX_inicial()].setText(".");
						return;
					}else {
					botones[tren.getY_inicial()][tren.getX_inicial() + 1].setText("X");
					colisiones.add(new Colision(tren.getX_inicial() + 1,tren.getY_inicial()));
					tren.vagones.remove(0);
					botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
					for(int i=0;i<tren.vagones.size();i++) {
						tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() + 1);
					}
					}
				}else {
					botones[tren.getY_inicial()][tren.vagones.size() - 1].setText(".");
					botones[tren.getY_inicial()][tren.getX_inicial() + 1].setText(String.valueOf(tren.getTipo()));
					tren.setX_inicial(tren.getX_inicial() + 1);
					for(int i=0;i<tren.vagones.size();i++) {
						tren.vagones.get(i).setPos_x(tren.vagones.get(i).getPos_x() + 1);
					}	
					
				}
				break;
			}
			
		}
		
	}
	public static void simulacion(JButton botones[][]){
		simulacionCompleta(trenes,colisiones,botones);
		setBotonesDefault(botones);
		setBotonesColisiones(botones);
	}
	/**
	 * METODO QUE SIRVE PARA IMPRIMIR LAS COLISIONES(PRIMERO PONEMOS EL TABLERO POR DEFECTO Y DESPUES PONEMOS LAS COLISIONES)
	 */
	public static void setBotonesDefault(JButton botones[][]) {
		for(int i=0;i<num_filas;i++){
			for(int j=0;j<num_columnas;j++) {
				botones[i][j].setText(".");
			}
			
		}
	}
	/**
	 * METODO QUE SIRVE PARA IMPRIMIR LAS COLISIONES
	 */
	public static void setBotonesColisiones(JButton botones[][]) {
		for(int i=0;i<colisiones.size();i++) {
			botones[colisiones.get(i).getPos_y()][colisiones.get(i).getPos_x()].setText("X");
		}
	}
	
	public int convertirCoordenadaY(int coord_y) {
		for(int i=1;i<=num_filas;i++) {
			if(num_filas - i == coord_y) {
				coord_y = coord_y + (i*10);
				break;
			}
		}
		return coord_y;
	}
	
	public static void simulacionCompleta(ArrayList<Tren> trenes,ArrayList<Colision> colisiones,  JButton botones[][]){
		while(hayTrenes(trenes)) {
			for(int i=0;i<trenes.size();i++) {
				
				moverTren(trenes.get(i),botones);
			}
		}
			
	}
	/**
	 * Metodo que comprueba si quedan trenes en el tablero
	 * @param trenes Array con los trenes que hay en el tablero
	 * @return True si quedan trenes, false si no quedan
	 */
	public static boolean hayTrenes(ArrayList<Tren> trenes) {
		if(trenes.isEmpty()) {
			return false;
			
		}else {
			return true;
		}
	}
	/**
	 * Metodo que comprueba si un tren tiene delante una colision
	 * @param tren Tren nuevo creado en el main 
	 * @param trenes Array de los trenes ya creados
	 * @param colisiones array con las colisiones
	 * @return Falso si no hay colision, true si hay colision
	 */
	public static boolean hayColision(Tren tren,ArrayList<Tren> trenes,ArrayList<Colision> colisiones) {
		boolean condicion = false;
		for(int i=0;i<colisiones.size();i++) {
			switch(tren.getDireccion()) {
			 case 'A':
				 if(tren.getX_inicial() == colisiones.get(i).getPos_x() && tren.getY_inicial() + 1 == colisiones.get(i).getPos_y()) {
					 condicion = true;
					 break;
				 }
				 break;
			 case 'B':
				 if(tren.getX_inicial() == colisiones.get(i).getPos_x() && tren.getY_inicial() - 1 == colisiones.get(i).getPos_y()) {
					 condicion = true;
					 break;
				 }
				 break;
			 case 'I':
				 if(tren.getX_inicial() - 1 == colisiones.get(i).getPos_x() && tren.getY_inicial() == colisiones.get(i).getPos_y()) {
					 condicion = true;
					 break;
				 }
				 break;
			 case 'D':
				 if(tren.getX_inicial() + 1 == colisiones.get(i).getPos_x() && tren.getY_inicial() == colisiones.get(i).getPos_y()) {
					 condicion = true;
					 break;
				 }
				 break;
			 }
		}
		
		 return condicion;
	}
	
	public String abrirTxt(File archivo) {
		String texto = "";
		try {
			input = new FileInputStream(archivo);
			int aux;
			while((aux=input.read()) != -1) {
				char caracter = (char)aux;
				if(caracter != '\n') {
					texto += caracter;
				}
			}
		}catch(Exception ex) {
			System.out.println("Error al abrir");
		}
		return texto;
	}
	
	public void saveTxt(File archivo, String txt) {
		try {
			output = new FileOutputStream(archivo);
			byte[] texto = txt.getBytes();
			output.write(texto);
		}catch(Exception e) {
			System.out.println("Error al guardar archivo");
		}
	}
	
	public void vaciarArrayBotones(JButton botones[][]) {
		for(int i=0;i<30;i++) {
			for(int j=0;j<30;j++) {
				if(botones[i][j] != null) {
					botones[i][j] = null;
				}
			}
		}
	}
}