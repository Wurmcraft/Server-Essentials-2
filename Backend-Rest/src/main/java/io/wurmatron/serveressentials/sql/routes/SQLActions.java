package io.wurmatron.serveressentials.sql.routes;

import io.wurmatron.serveressentials.models.Action;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static io.wurmatron.serveressentials.ServerEssentialsRest.GSON;
import static io.wurmatron.serveressentials.ServerEssentialsRest.LOG;

public class SQLActions extends SQLDirect {

    public static String ACTIONS_TABLE = "actions";

    /**
     * Creates a new action to be saved in the db
     *
     * @param action instance of the action to be created
     * @return the created instance that was added to the db
     */
    @Nullable
    public static Action create(Action action) {
        try {
            insert(ACTIONS_TABLE, ACTIONS_COLUMNS, action, false);
            return action;
        } catch (Exception e) {
            LOG.debug("Failed to add action from '" + action.host + "' (" + e.getMessage() + ")");
            LOG.debug("Action: " + GSON.toJson(action));
        }
        return null;
    }

    /**
     * Update a given action, with the provided values
     *
     * @param action action to pull the updated values from
     * @return updated instance of the action
     */
    @Nullable
    public static Action update(Action action, String[] columnsToUpdate) {
        try {
            update(ACTIONS_TABLE, columnsToUpdate, new String[]{"relatedID", "host", "action", "timestamp"}, new String[]{action.relatedID, action.host, action.action, action.timestamp + ""}, action);
            List<Action> actions = get(action.relatedID, action.action, action.relatedID);
            if (actions != null)
                for (Action a : actions)
                    if (a.timestamp.equals(action.timestamp))
                        return a;
        } catch (Exception e) {
            LOG.debug("Failed to add update action from '" + action.host + "' (" + e.getMessage() + ")");
            LOG.debug("Action: " + GSON.toJson(action));
        }
        return null;
    }

    /**
     * Gets a array / list of the actions related to the provided user
     *
     * @param relatedID id of the provided user or channel
     * @return a list of the action related to the provided id
     */
    public static List<Action> get(String relatedID) {
        try {
            return queryArray("SELECT * from " + ACTIONS_TABLE + "WHERE relatedID='" + relatedID + "'", new Action());
        } catch (Exception e) {
            LOG.debug("Failed to add get action from '" + relatedID + "' (" + e.getMessage() + ")");
        }
        return null;
    }

    /**
     * Gets a array / list of actions related to the provided user with filtered actions
     *
     * @param relatedID id of the provided user or channel
     * @param action    name of the action that occurred
     * @return a list of all the actions related to the provided user, based on its action
     */
    public static List<Action> get(String relatedID, String action) {
        try {
            return queryArray("SELECT * from `" + ACTIONS_TABLE + "` WHERE relatedID='" + relatedID + "' AND action='" + action + "'", new Action());
        } catch (Exception e) {
            LOG.debug("Failed to add get action from '" + relatedID + "' (" + e.getMessage() + ")");
        }
        return new ArrayList<>();
    }

    /**
     * Gets a array / list of actions related to the requested criteria
     *
     * @param host      name of the controller, discord bot or server
     * @param action    name of the action that has occurred
     * @param relatedID id of the user involved in the action, discordID or uuid
     * @return a list of all the actions related to the provided details
     */
    public static List<Action> get(String host, String action, String relatedID) {
        try {
            return queryArray("SELECT * from " + ACTIONS_TABLE + " WHERE relatedID='" + relatedID + "' AND action='" + action + "'", new Action());
        } catch (Exception e) {
            LOG.debug("Failed to add get action from '" + relatedID + "' (" + e.getMessage() + ")");
        }
        return new ArrayList<>();
    }

    /**
     * Deletes an instance of a action from the db
     *
     * @param host      name of the controller, discord bot or server
     * @param action    name of the action that occurred
     * @param relatedID id of the user involved in the action, discordID or uuid
     * @param timestamp unix timestamp of when the action was created / happened
     * @return instance of the deleted action
     */
    public static Action delete(String host, String action, String relatedID, long timestamp) {
        try {
            List<Action> actions = queryArray("SELECT * from " + ACTIONS_TABLE + "WHERE host='" + host + "' AND action='" + action + "' AND relatedID='" + relatedID + "'", new Action());
            PreparedStatement statement = connection.createPrepared("DELETE FROM " + ACTIONS_TABLE + " WHERE host='" + host + "' AND action='" + action + "' AND relatedID='" + relatedID + "'");
            statement.execute();
            for (Action a : actions)
                if (a.timestamp == timestamp)
                    return a;
        } catch (Exception e) {
            LOG.debug("Failed to delete action for '" + host + "' (" + e.getMessage() + ")");
            LOG.debug("Action: " + GSON.toJson(action));
        }
        return null;
    }
}
