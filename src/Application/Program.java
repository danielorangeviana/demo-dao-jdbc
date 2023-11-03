package Application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST ONE: Seller findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== TEST TWO: Seller findByDepartment ===");
		Department department = new Department(3, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST THREE: Seller findByDepartment ===");
		List<Seller> listFindAll = sellerDao.findAll();
		for (Seller obj : listFindAll)
		System.out.println(obj);
		
		System.out.println("\n=== TEST FOUR: Seller insert ===");
		Seller newSeller = new Seller(null, "Deivson Correa", "deivson@gmail.com", new Date(), 7000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted new Id = " + newSeller.getId());
		
		System.out.println("\n=== TEST FIVE: Seller insert ===");
		seller = sellerDao.findById(8);
		seller.setName("Pedro Silva");
		seller.setEmail("pedro@gmail.com");
		sellerDao.update(seller);
		System.out.println("Update completed!");
		
	}

}
