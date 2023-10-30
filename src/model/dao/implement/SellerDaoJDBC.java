package model.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement prStatement = null;
		ResultSet resultSet = null;
		
		try {
			prStatement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+"WHERE seller.Id = ?");
			
			prStatement.setInt(1, id);
			resultSet = prStatement.executeQuery(); //Executa a query 'SELECT' acima.
			
			if(resultSet.next()) {
				// Instanciando o objeto Department
				Department dep = instantiateDepartment(resultSet);
				
				// Instanciando o objeto Seller
				Seller obj = instantiateSeller(resultSet, dep);
				
				return obj;
			}
			return null;
		}
		catch (SQLException error) {
			throw new DbException(error.getMessage());
		}
		finally {
			DB.closeStatement(prStatement);
			DB.closeResultSet(resultSet);
		}
	}
	
	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(resultSet.getInt("Id"));
		seller.setName(resultSet.getString("Name"));
		seller.setEmail(resultSet.getString("Email"));
		seller.setBirthDate(resultSet.getDate("BirthDate"));
		seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
		
		// Associando os objetos seller e department
		seller.setDepartement(department);
		return seller;
	}

	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("DepartmentId"));
		department.setName(resultSet.getString("DepName"));
		return department;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
