package me.moontimer.dreampvp.dremapi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLPlayerManager {

    private static MySQL mySQL = DreamAPI.getInstance().getMySQL();
    public static void updateTime(UUID uuid) {
        if (isRegistered(uuid)) {
            mySQL.execute("UPDATE PlayerTimes SET PlayTime = '" + (getTime(uuid) + 1) + "' WHERE UUID = '" + uuid.toString() + "'");
        } else {
            mySQL.execute("INSERT INTO PlayerTimes (UUID, PlayTime) VALUES ('" + uuid.toString() + "', '1')");
        }
    }

    public static int getTime(UUID uuid) {
            try {
                ResultSet rs = mySQL.executeQuery("SELECT * FROM PlayerTimes WHERE UUID = '" + uuid.toString() + "'");
                if (rs.next())
                    return rs.getInt("PlayTime");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return 0;
    }

    private static boolean isRegistered(UUID uuid) {
            try {
                ResultSet rs = mySQL.executeQuery("SELECT * FROM PlayerTimes WHERE UUID = '" + uuid.toString() + "'");
                if (rs.next())
                    return (rs.getInt("PlayTime") != 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }
}
