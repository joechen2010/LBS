package database;

import java.util.List;

import model.Note;
import model.Position;
import model.User;
import model.UserInfo;

/**
 * This is the the main interface for the database.
 * Here are defined all the methods the web service needs.
 */
public interface ffLocationDBIface {
	
	
	/**
	 * Adds a new user to the database.
	 * @param u User
	 * @return whether the process succeed.
	 */
	public boolean newUser(User u);

	/**
	 * Change a user that already exists in the database.
	 * @param u
	 * @return whether the process succeed.
	 */
	public boolean saveUser(User u);

	/**
	 * This method creates a friendship request.
	 * @param u1 The id of the user that makes the request.
	 * @param u2 The id of the user that receives the request.
	 * @return whether the process succeed.
	 */
	public boolean newRequest(int u1, int u2);
	
	/**
	 * This method removes a friendship request.
	 * @param u1 The id of the user that makes the request.
	 * @param u2 The id of the user that receives the request.
	 * @return whether the process succeed.
	 */
	public boolean delRequest(int u1, int u2);

	/**
	 * Return the list of users that had request for friendship to an specific user.
	 * @param id The id of the user that owns the requests.
	 * @return The list of request that the user owns.
	 */
	public List<UserInfo> getRequests(int id);
	
	/**
	 * Loads a user from the database using the id.
	 * @param id of the user.
	 * @return the User object
	 */
	public User loadUser(int id);
	
	/**
	 * Loads a user from the database using the nick.
	 * @param nick of the user.
	 * @return the User object
	 */
	public User loadUser(String nick);
	
	/**
	 * Remove a user from the database
	 * @param id The users id
	 * @return whether it succeed
	 */
	public boolean removeNote(int id);
	
	/**
	 * Get users friend list
	 * @param id of the user
	 * @return List of users
	 */
	public List<UserInfo> getFriends(int id);
	
	
	/**
	 * This function return if the users are friends
	 * @param id1 One user's id
	 * @param id2 The other user's id
	 * @return whether the two users are friends
	 */
	public boolean areFriends(int id1, int id2);

	/**
	 * Stores that two users are friends.
	 * @param us1 First users id.
	 * @param us2 Second users id.
	 * @return whether the process succeed.
	 * 
	 */
	public boolean addFriends(int us1, int us2);
	
	/**
	 * Returns the last n position of a user.
	 * @param id The user's id.
	 * @param c The number of positions.
	 * @return The list of positions.
	 */
	public List<Position> getPositions(int id, int c);
	
	/**
	 * Registers a user's new position.
	 * @param userID The user's id.
	 * @param lat latitude.
	 * @param lon longitude.
	 * @return whether it succeed.
	 */
	public boolean newPosition(int userID, float lat, float lon);
	
	/**
	 * Stores a new note with or without photo.
	 * @param not The note object.
	 * @return whether it succeed.
	 */
	public boolean setNote(Note not);
	
	/**
	 * Gets a user's n last notes notes.
	 * @param id The user's id.
	 * @param c The number of notes.
	 * @return The list of notes.
	 */
	public List<Note> getNotes(int id, int c);

	/**
	 * Search for users using some fields.
	 * @param nick String the nick should contain.
	 * @param name String the name should contain.
	 * @param surname String the surname should contain.
	 * @param country String the country should contain.
	 * @return the list of matches.
	 */
	public List<UserInfo> searchFriend(String nick, String name,
			String surname, String country);

	/**
	 * Checks if a nick is already been used.
	 * @param nick The string of the nick.
	 * @return whether the nick already exists.
	 */
	boolean exists(String nick);

	/**
	 * This function returns all the users of the system by page.
	 * @param count Number of Users per page.
	 * @param page The page number.
	 * @return the list of users.
	 */
	public List<UserInfo> getUsers(int count, int page);

	/**
	 * Given a note's id returns the note with photo, if it has.
	 * @param id The id of the note.
	 * @return The note object.
	 */
	public Note getNote(int id);

	/**
	 * Get the user's friends by page.
	 * @param id The user's id.
	 * @param count The number of friends per page.
	 * @param page The number of page.
	 * @return The list of friends on the page.
	 */
	public List<UserInfo> getFriends(int id, int count, int page);

	/**
	 * Deletes a user by id.
	 * @param id The user's id.
	 * @return whether it succeed.
	 */
	public boolean delUser(int id);
	
}
