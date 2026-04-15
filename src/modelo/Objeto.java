package modelo;

public class Objeto {
	private String nombre;
	private double aumento;
	private double reduccion;
	
	
	
	
	public Objeto(String nombre, double aumento, double reduccion) {
		super();
		this.nombre = nombre;
		this.aumento = aumento;
		this.reduccion = reduccion;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getAumento() {
		return aumento;
	}
	public void setAumento(double aumento) {
		this.aumento = aumento;
	}
	public double getReduccion() {
		return reduccion;
	}
	public void setReduccion(double reduccion) {
		this.reduccion = reduccion;
	}
	
	
}
