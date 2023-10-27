package Application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department object = new Department(1, "book");
		
		Seller seller = new Seller(21, "Daniel", "daniel@gmail.com", new Date(), 3000.0, object);
		
		System.out.println(seller);
		
	}

}
