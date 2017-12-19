package md.cadastre.settingsparam;

import java.sql.SQLException;

public class DbUtilities {
	
	public static String printSQLException(SQLException ex) {
        String result = "";
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(
                        ((SQLException) e).
                        getSQLState()) == false) {

                    result = "SQLState: "
                            + ((SQLException) e).getSQLState();

                    result = result + "|Error Code: "
                            + ((SQLException) e).getErrorCode();

                    result = result + "|Message: " + e.getMessage();

                    Throwable t = ex.getCause();
                    while (t != null) {
                        result = result + "|Cause: " + t;
                        t = t.getCause();
                    }
                }
            }
        }
        return result;
    }

    public static boolean ignoreSQLException(String sqlState) {

        if (sqlState == null) {
            System.out.println("The SQL state is not defined!");
            return false;
        }

        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32")) {
            return true;
        }

        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55")) {
            return true;
        }

        return false;
    }
}
