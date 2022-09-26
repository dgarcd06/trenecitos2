
public class Vagon {

	private int pos_x;
	private int pos_y;
	private int tipo;
	
	//constructor 1
	public Vagon() {
		
	}
	//constructor 2
	public Vagon(int x,int y,int tipo) {
		this.pos_x = x;
		this.pos_y = y;
		this.tipo = tipo;
	}
	public int getPos_x() {
		return pos_x;
	}
	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}
	public int getPos_y() {
		return pos_y;
	}
	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	
}
