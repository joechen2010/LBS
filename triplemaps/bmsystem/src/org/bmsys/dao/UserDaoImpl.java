package org.bmsys.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.bmsys.form.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao
{
	private SimpleJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource)
	{
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	public UserCommand checkCredentials(String userName, String password)
	{
		String sql = "select * from bm_users where username = ? and password = ?";

		RowMapper<UserCommand> mapper = new RowMapper<UserCommand>()
		{
			@Override
			public UserCommand mapRow(ResultSet rs, int arg1)
					throws SQLException
			{
				UserCommand user = new UserCommand();
				user.setUserId(rs.getInt("userid"));
				user.setUserName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("firstName"));
				user.setMiddleName(rs.getString("middleName"));
				user.setLastName(rs.getString("lastName"));
				user.setAddress1(rs.getString("address1"));
				user.setAddress2(rs.getString("address2"));
				user.setContactNo(rs.getString("contactNo"));
				user.setReccuringAmount(rs.getString("recAmount"));
				return user;
			}
		};

		List<UserCommand> list = jdbcTemplate.query(sql, mapper, new Object[]
		{ userName, password });
		return list.size() == 0 ? null : list.get(0);
	}

	public int addUser(UserCommand command)
	{
		String sql = "insert into bm_users (firstName,middleName,lastName,address1,address2,contactNo,recAmount) values"
				+ "(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, new Object[]
		{ command.getFirstName(), command.getMiddleName(),
				command.getLastName(), command.getAddress1(),
				command.getAddress2(), command.getContactNo(),
				command.getReccuringAmount() });
	}

	public List<UserCommand> listUsers()
	{
		String sql = "select * from bm_users";
		RowMapper<UserCommand> mapper = new RowMapper<UserCommand>()
		{
			@Override
			public UserCommand mapRow(ResultSet rs, int arg1)
					throws SQLException
			{
				UserCommand user = new UserCommand();
				user.setUserId(rs.getInt("userid"));
				user.setUserName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("firstName"));
				user.setMiddleName(rs.getString("middleName"));
				user.setLastName(rs.getString("lastName"));
				user.setAddress1(rs.getString("address1"));
				user.setAddress2(rs.getString("address2"));
				user.setContactNo(rs.getString("contactNo"));
				user.setReccuringAmount(rs.getString("recAmount"));
				return user;
			}
		};
		return jdbcTemplate.query(sql, mapper);
	}

	public UserCommand findUserByID(String userID)
	{
		String sql = "select * from bm_users where userid = ?";

		RowMapper<UserCommand> mapper = new RowMapper<UserCommand>()
		{
			@Override
			public UserCommand mapRow(ResultSet rs, int arg1)
					throws SQLException
			{
				UserCommand user = new UserCommand();
				user.setUserId(rs.getInt("userid"));
				user.setUserName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("firstName"));
				user.setMiddleName(rs.getString("middleName"));
				user.setLastName(rs.getString("lastName"));
				user.setAddress1(rs.getString("address1"));
				user.setAddress2(rs.getString("address2"));
				user.setContactNo(rs.getString("contactNo"));
				user.setReccuringAmount(rs.getString("recAmount"));
				return user;
			}
		};

		List<UserCommand> list = jdbcTemplate.query(sql, mapper, new Object[]
		{ userID });
		return list.size() == 0 ? null : list.get(0);
	}

	public int deleteUserByID(String userID)
	{
		String sql = "delete from bm_users where userid = ?";
		return jdbcTemplate.update(sql, new Object[]
		{ userID });
	}

	@Override
	public int updateUser(UserCommand command)
	{
		String sql = "update bm_users set firstName = ?,middleName = ?,lastName = ?,address1 = ?,address2 = ?,contactNo =? ,recAmount = ?"
				+ "where userid = ?";
		return jdbcTemplate.update(sql, new Object[]
		{ command.getFirstName(), command.getMiddleName(),
				command.getLastName(), command.getAddress1(),
				command.getAddress2(), command.getContactNo(),
				command.getReccuringAmount(), command.getUserId() });
	}
}
