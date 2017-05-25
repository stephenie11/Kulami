package main.objects;

import java.io.Serializable;

public class PlayerNames implements Serializable{
	
	String Name;

	public String getName() {
		return Name;
	}

	public PlayerNames(String name) {
		super();
		Name = name;
	}
	

}