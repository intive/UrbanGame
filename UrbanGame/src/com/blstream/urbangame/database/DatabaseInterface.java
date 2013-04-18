package com.blstream.urbangame.database;

import java.util.List;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public interface DatabaseInterface {

	// GAMES METHOD END
	/**
	 * @param gameShortInfo
	 *            - all you need for game list view, only short info obtained
	 *            from server query
	 * @return return true if insertion successful
	 */
	public boolean insertGameShortInfo(UrbanGameShortInfo gameShotInfo);

	/**
	 * @param gameInfo
	 *            - all you need for game details view, full info obtained from
	 *            server query. Should have the same id that details have.
	 *            Server should do that.
	 * @return return true if insertion successful
	 */
	public boolean insertGameInfo(UrbanGame gameInfo);

	/**
	 * @return list of all short information about game needed for game list
	 *         view
	 */
	public List<UrbanGameShortInfo> getAllGamesShortInfo();

	/**
	 * @param beforeFirst
	 *            - other words it's offset. Set to 0 if you need first part of
	 *            data else pass the number after which it should return next
	 *            games short info. Summary: It's kind of pagination
	 * @param howMany
	 *            - number of games short info to be returned, if there is less
	 *            in database it will return that many as it can
	 * @return list of all short information about game needed for game list
	 *         view
	 */
	public List<UrbanGameShortInfo> getAllGamesShortInfoOrderedByStartTime(
			int beforeFirst, int howMany);

	/**
	 * @param gameID
	 *            id of game short info to retrieve
	 * @return if match - game short info, else null
	 */
	public UrbanGameShortInfo getGameShortInfo(Long gameID);

	/**
	 * @param gameID
	 *            id of game info to retrieve
	 * @return if match - game info, else null
	 */
	public UrbanGame getGameInfo(Long gameID);

	/**
	 * @param game
	 *            - new game. ID of new game is important. Must be the same as
	 *            ID of game to update. Fields that have to remain unchanged,
	 *            should be null. You can use that as well to update short info.
	 * @return true if successful
	 */
	public boolean updateGame(UrbanGame game);

	/**
	 * @param game
	 *            - new game. ID of new game is important. Must be the same as
	 *            ID of game to update. Fields that have to remain unchanged,
	 *            should be null.
	 * @return true if successful
	 */
	public boolean updateGameShortInfo(UrbanGameShortInfo game);

	/**
	 * @param gameID
	 *            - game ID that indicates the game to delete.
	 * @return true if successful
	 */
	public boolean deleteGameInfoAndShortInfo(Long gameID);

	// GAMES METHOD END

	// USER METHOD
	/**
	 * @param player
	 *            - player to be inserted in database
	 * @return true if successful
	 */
	public boolean insertUser(Player player);

	/**
	 * @param email
	 *            - email id for user
	 * @return user if found else null
	 */
	public Player getPlayer(String email);

	/**
	 * @param player
	 *            - new player info. Email of player is important. Must be the
	 *            same as Email of player to update. Fields that have to remain
	 *            unchanged, should be null.
	 * @return true if successful
	 * 
	 */
	public boolean updatePlayer(Player player);

	/**
	 * @param email
	 *            - email of player to delete
	 * @return true if successful
	 */
	public boolean deletePlayer(String email);

	// USER METHOD END

	// USER GAMES SPECIFIC METHOD
	/**
	 * @param playerGameSpecific
	 *            - player's info about game to be inserted
	 * @return true if successful
	 */
	public boolean insertUserGameSpecific(PlayerGameSpecific playerGameSpecific);

	/**
	 * @param email
	 *            - user identification
	 * @param gameID
	 *            - game identification
	 * @return user data in game if successful, else null
	 */
	public PlayerGameSpecific getUserGameSpecific(String email, Long gameID);

	/**
	 * @param - new player data in game. Email of player and game id are
	 *        important. Must be the same as Email of player and id of game to
	 *        update. Fields that have to remain unchanged, should be null.
	 * @return true if successful
	 */
	public boolean updateUserGameSpecific(PlayerGameSpecific playerGameSpecific);

	/**
	 * @param email
	 *            - user identification
	 * @param gameID
	 *            - game identification
	 * @return true if successful
	 */
	public boolean deleteUserGameSpecific(String email, Long gameID);

	// USER GAMES SPECIFIC METHOD END

	// WIPE OUT USER DATA
	/**
	 * @param email
	 *            - email of user that's data should be tootaly removed from
	 *            database
	 * @return true if successful
	 */
	public boolean wipeOutUserData(String email);
	// WIPE OUT USER DATA END
}