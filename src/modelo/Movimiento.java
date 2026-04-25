package modelo;

public class Movimiento {

	private int idMovimiento;
	private String nombre;
	private Tipo tipo;
	private int potencia;
	private Estado estado;
	private int numTurnos;
	private String mejora;
	private String descMovimiento;
	private int claseMov;
	private int numPP;
	private int numPPMax;
	private int precision;
	
	
	public Movimiento(int idMovimiento, String nombre, Tipo tipo, int potencia, Estado estado, int numTurnos,
			String mejora, String descMovimiento, int claseMov, int numPP, int numPPMax, int precision) {
		super();
		this.idMovimiento = idMovimiento;
		this.nombre = nombre;
		this.tipo = tipo;
		this.potencia = potencia;
		this.estado = estado;
		this.numTurnos = numTurnos;
		this.mejora = mejora;
		this.descMovimiento = descMovimiento;
		this.claseMov = claseMov;
		this.numPP = numPP;
		this.numPPMax = numPPMax;
		
		this.precision = precision;
	}
	
	public Movimiento(int idMovimiento) {
		super();
		this.idMovimiento = idMovimiento;
	}
	
	public Movimiento() {
		super();

	}

	public int getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(int idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getNumTurnos() {
		return numTurnos;
	}

	public void setNumTurnos(int numTurnos) {
		this.numTurnos = numTurnos;
	}

	public String getMejora() {
		return mejora;
	}

	public void setMejora(String mejora) {
		this.mejora = mejora;
	}

	public String getDescMovimiento() {
		return descMovimiento;
	}

	public void setDescMovimiento(String descMovimiento) {
		this.descMovimiento = descMovimiento;
	}

	public int getClaseMov() {
		return claseMov;
	}

	public void setClaseMov(int claseMov) {
		this.claseMov = claseMov;
	}

	public int getNumPP() {
		return numPP;
	}

	public void setNumPP(int numPP) {
		this.numPP = numPP;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getNumPPMax() {
		return numPPMax;
	}

	public void setNumPPMax(int numPPMax) {
		this.numPPMax = numPPMax;
	}
	
	
	@Override
	public String toString() {
	    return this.nombre + " (Pot: " + potencia + ")";
	}

	
	
	
	
	
}
