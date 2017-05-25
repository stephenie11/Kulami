package main.objects;

import java.io.Serializable;

public class ButtonPosition implements Serializable{
	
	int index;
	Boolean firstMove=false,secondMove=false;

	public ButtonPosition(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setFirstMove(Boolean firstMove) {
		this.firstMove = firstMove;
	}

	public void setSecondMove(Boolean secondMove) {
		this.secondMove = secondMove;
	}

	public Boolean getFirstMove() {
		return firstMove;
	}

	public Boolean getSecondMove() {
		return secondMove;
	}
	
	

}
