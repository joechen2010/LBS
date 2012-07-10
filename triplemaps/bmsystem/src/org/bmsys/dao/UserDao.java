package org.bmsys.dao;

import java.util.List;

import org.bmsys.form.UserCommand;

public interface UserDao
{
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return {@link UserCommand}
	 */
	public UserCommand checkCredentials(String userName, String password);

	/**
	 * 
	 * @param command
	 * @return int
	 */
	public int addUser(UserCommand command);
	
	/**
	 * 
	 * @param command
	 * @return int
	 */
	public int updateUser(UserCommand command);
	/**
	 * 
	 * @return List<UserCommand>
	 */
	public List<UserCommand> listUsers();
	
	/**
	 * 
	 * @param userID
	 * @return UserCommand
	 */
	public UserCommand findUserByID(String userID);
	
	/**
	 * 
	 * @param userID
	 * @return int
	 */
	public int deleteUserByID(String userID);
}
