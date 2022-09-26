import java.util.ArrayList;

public class Tren implements Comparable<Tren>{		
		private char direccion;
		private int longitud;
		private int num_tren;
		private int tipo;
		private int x_inicial;
		private int y_inicial;
		private int x_final;
		private int y_final;
		private Vagon vagon = new Vagon();
		public ArrayList<Vagon> vagones = new ArrayList<Vagon>();
		//colision
		public ArrayList<Colision> colisiones = new ArrayList<Colision>();
		//constructor 1
		public Tren() {
			
		}
		//constructor 2
		public Tren(char direccion,int longitud,int x_inicial,int y_inicial) {
			switch(direccion) {
			case 'A':
				for(int i=y_inicial;i>(y_inicial-longitud);i--) {
					vagones.add(new Vagon(x_inicial,i,1));
				}
				break;
			case 'B':
				for(int i=y_inicial;i<(y_inicial+longitud);i++) {
					vagones.add(new Vagon(x_inicial,i,0));
				}
				break;
			case 'I':
				for(int i=x_inicial;i<(x_inicial+longitud);i++) {
					vagones.add(new Vagon(i,y_inicial,2));
				}
				break;
			case 'D':
				for(int i=x_inicial;i<(x_inicial-longitud);i--) {
					vagones.add(new Vagon(i,y_inicial,3));
				}
				break;
			}
		}

		//metodo para comparar trenes segun su tipo
		public int compareTo(Tren tren) {
			if(this.getTipo() > tren.getTipo()) {
				return -1;
			}else if(this.getTipo() < tren.getTipo()) {
				return 1;
			}else {
				return 0;
			}
		}
		/**
		 * Metodo para que el tren avance una casilla en la direccion indicada en el input
		 * @param direccion Direccion hacia la que avanza el tren
		 */
		public void avanzar(char direccion) {
			switch(direccion) {
			case 'A':
				if(vagones.get(0).getPos_y() + 1 == 29) {
					vagones.remove(0);
					this.setLongitud(this.getLongitud() - 1);
				}else {
					for(int i=0;i<vagones.size();i++) {
						vagones.get(i).setPos_y(vagones.get(i).getPos_y()+1);
					}
				}
				break;
			case 'B':
				if(vagones.get(0).getPos_y() - 1 == 0) {
					vagones.remove(0);
					this.setLongitud(this.getLongitud() - 1);
				}else {
					for(int i=0;i<vagones.size();i++) {
						vagones.get(i).setPos_y(vagones.get(i).getPos_y()-1);
					}
				}
				break;
			case 'I':
				if(vagones.get(0).getPos_x() - 1 == 0) {
					vagones.remove(0);
					this.setLongitud(this.getLongitud() - 1);
				}else {
					for(int i=0;i<vagones.size();i++) {
						vagones.get(i).setPos_x(vagones.get(i).getPos_y()-1);
					}
				}
				break;
			case 'D':
				if(vagones.get(0).getPos_x() + 1 == 29) {
					vagones.remove(0);
					this.setLongitud(this.getLongitud() - 1);
				}else {
					for(int i=0;i<vagones.size();i++) {
						vagones.get(i).setPos_x(vagones.get(i).getPos_y()+1);
					}
				}
				break;
			}
		}
		/**
		 * Metodo que establece el comportamiento del tren en caso de que tenga una colision delante
		 */
		public void avanceColision() {
			vagones.remove(0);
			this.setLongitud(this.getLongitud() - 1);
		}
		
		//getters y setters
		public char getDireccion() {
			return direccion;
		}
		public void setDireccion(char direccion) {
			this.direccion = direccion;
		}
		public int getLongitud() {
			return longitud;
		}
		public void setLongitud(int longitud) {
			this.longitud = longitud;
		}
		public int getNum_tren() {
			return num_tren;
		}
		public void setNum_tren(int num_tren) {
			this.num_tren = num_tren;
		}
		public int getTipo() {
			return tipo;
		}
		public void setTipo(char dir) {
			switch(dir) {
			case 'A':
				this.tipo = 1;
				break;
			case 'B':
				this.tipo = 0;
				break;
			case 'I':
				this.tipo = 2;
				break;
			case 'D':
				this.tipo = 3;
				break;
			}
		}
		public Vagon getVagon(int posicion) {
			return vagones.get(posicion);
		}
		public void setVagon(Vagon vagon) {
			this.vagon = vagon;
			vagones.add(vagon);
		}
		public int getX_inicial() {
			return x_inicial;
		}
		public void setX_inicial(int x_inicial) {
			this.x_inicial = x_inicial;
		}
		public int getY_inicial() {
			return y_inicial;
		}
		public void setY_inicial(int y_inicial) {
			this.y_inicial = y_inicial;
		}
		
		public void setPosFinal() {
			switch(this.direccion) {
			case 'A':
				this.x_final = this.x_inicial;
				this.y_final = this.y_inicial - longitud;
				break;
			case 'B':
				this.x_final = this.x_inicial;
				this.y_final = this.y_inicial + longitud;
				break;
			case 'I':
				this.x_final = this.x_inicial + longitud;
				this.y_final = this.y_inicial;
				break;
			case 'D':
				this.x_final = this.x_inicial - longitud;
				this.y_final = this.y_inicial;
				break;
			}
		}
		
		
		
		public int getX_final() {
			return x_final;
		}
		public void setX_final(int x_final) {
			this.x_final = x_final;
		}
		public int getY_final() {
			return y_final;
		}
		public void setY_final(int y_final) {
			this.y_final = y_final;
		}
		public void setAllVagones() {
			switch(direccion) {
			case 'A':
				for(int i=y_inicial;i>(y_inicial-longitud);i--) {
					vagones.add(new Vagon(x_inicial,i,1));
				}
				break;
			case 'B':
				for(int i=y_inicial;i<(y_inicial+longitud);i++) {
					vagones.add(new Vagon(x_inicial,i,0));
				}
				break;
			case 'I':
				for(int i=x_inicial;i<(x_inicial+longitud);i++) {
					vagones.add(new Vagon(i,y_inicial,2));
				}
				break;
			case 'D':
				for(int i=x_inicial;i>(x_inicial-longitud);i--) {
					vagones.add(new Vagon(i,y_inicial,3));
				}
				break;
			}
		}
		public void avanceColision(char direccion) {
				this.longitud--;
				this.vagones.remove(this.vagones.size() - 1);
		}
}