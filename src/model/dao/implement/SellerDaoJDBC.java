package model.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	// Concexão com o banco de dados
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller object) {
		PreparedStatement st = null;
		
		try {
			st = connection.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, object.getName());
			st.setString(2, object.getName());
			st.setDate(3, new java.sql.Date(object.getBirthDate().getTime()));
			st.setDouble(4, object.getBaseSalary());
			st.setInt(5, object.getDepartement().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					object.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error, no rows affected!");
			}
					
		}
			catch (SQLException error) {
				throw new DbException(error.getMessage());
			}
				finally {
					DB.closeStatement(st);
				}
	}

	@Override
	public void update(Seller object) {
		PreparedStatement st = null;
		
		try {
			st = connection.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, object.getName());
			st.setString(2, object.getEmail());
			st.setDate(3, new java.sql.Date(object.getBirthDate().getTime()));
			st.setDouble(4, object.getBaseSalary());
			st.setInt(5, object.getDepartement().getId());
			st.setInt(6, object.getId());
			
			st.executeUpdate();
			
		}
			catch(SQLException error) {
				throw new DbException(error.getMessage());
			}
				finally {
					DB.closeStatement(st);
				}
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
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement prStatement = null;
		ResultSet resultSet = null;
		
		try {
			prStatement = connection.prepareStatement(
				"SELECT seller.*,department.Name as DepName " 
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE DepartmentId = ? "
				+ "ORDER BY Name");
			
			prStatement.setInt(1, department.getId());
			resultSet = prStatement.executeQuery();
			
		List<Seller> list = new ArrayList<>(); // Lista para armazenar os resultados
		Map<Integer, Department> map = new HashMap<>();
		
		while(resultSet.next()) {
			
			// Se o departamento existir ele é reaproveitado
			Department dep = map.get(resultSet.getInt("DepartmentId"));
			
			//Se ele for nulo, é instanciado 
			if(dep == null) {
				dep = instantiateDepartment(resultSet);
				map.put(resultSet.getInt("DepartmentId"), dep);
			}
			
			// Assim, é instanciado todos os vendedores sem a repetição dos departamentos
			Seller obj = instantiateSeller(resultSet, dep);
			list.add(obj); // Eincluídos na lista Seller
		}
		
		return list;
		}
		
			catch(SQLException error) {
				throw new DbException(error.getMessage());
			}
			
				finally {
					DB.closeStatement(prStatement);
					DB.closeResultSet(resultSet);
				}		
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement prSt = null;
		ResultSet rsSet = null;
		
		try {
			prSt = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rsSet = prSt.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rsSet.next()) {
				
				Department dep = map.get(rsSet.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rsSet);
					map.put(rsSet.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rsSet, dep);
				list.add(obj);
			}
			
		return list;	
		}
			catch(SQLException error) {
				throw new DbException(error.getMessage());
			}
				finally {
					DB.closeStatement(prSt);
					DB.closeResultSet(rsSet);
				}
	}
	
	
	//METHOD	
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

}
