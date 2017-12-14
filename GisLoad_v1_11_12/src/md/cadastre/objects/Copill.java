package md.cadastre.objects;

public class Copill {
	
	private String nume;
	private String prenume;
	
	public Copill() {
		super();
	}

	public Copill(String nume, String prenume) {
		super();
		this.nume = nume;
		this.prenume = prenume;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	@Override
	public String toString() {
		return "Copill [nume=" + nume + ", prenume=" + prenume + "]";
	}
	
}
