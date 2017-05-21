package objects;

import java.io.Serializable;

public class WinnerFrame implements Serializable{
	
	String s,scor;

	
	public WinnerFrame(String s,String scor) {
		this.s = s;
		this.scor = scor;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getScor() {
		return scor;
	}

	public void setScor(String scor) {
		this.scor = scor;
	}
	
	

}
