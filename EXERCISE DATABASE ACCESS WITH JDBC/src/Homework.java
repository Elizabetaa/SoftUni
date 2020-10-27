import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Homework {

    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";
    private static final String MINIONS_TABLE_NAME = "minions_db";
    private Connection connection;

    public void setConnection(String user, String password) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        connection = DriverManager
                .getConnection(CONNECTION_STRING + MINIONS_TABLE_NAME, properties);
    }

    public void getVillainsNamesEx2() throws SQLException {
        String query = "SELECT v.name, COUNT(mv.minion_id) AS count " +
                "FROM villains  AS v " +
                "JOIN minions_villains AS mv " +
                "ON mv.villain_id = v.id " +
                "GROUP BY name " +
                "HAVING count > 15 " +
                "ORDER BY count DESC";

        PreparedStatement statement = connection
                .prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            System.out.printf(" %s %d %n",
                    resultSet.getString("name"),
                    resultSet.getLong("count"));
        }
    }

    public void getMinionsNamesEx3() throws SQLException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());

        String villainName = getEntityNameById(id, "villains");

        if (villainName == null) {
            System.out.println("No villain with ID 10 exists in the database.");
            return;
        }
        System.out.println("Villain: " + villainName);

        String minions = String.format("SELECT  m.name, m.age FROM villains AS v " +
                "JOIN minions_villains mv " +
                "on v.id = mv.villain_id " +
                "JOIN minions AS m " +
                "ON m.id = mv.minion_id " +
                "WHERE v.id = %d", id);

        PreparedStatement statement = connection
                .prepareStatement(minions);
        ResultSet result = statement.executeQuery();

        int count = 1;
        while (result.next()) {
            System.out.printf("%d. %s %d%n", count,
                    result.getString("name"),
                    result.getInt("age"));
            count++;
        }
    }

    private String getEntityNameById(int id, String tableName) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE id = ?", tableName);

        PreparedStatement statement = connection
                .prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        return result.next() ? result.getString("name") : null;
    }


    public void addMinionEx4() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] minionInformation = reader.readLine().split("\\s+");
        String minionName = minionInformation[1];
        int minionAge = Integer.parseInt(minionInformation[2]);
        String minionTown = minionInformation[3];
        String villainName = (reader.readLine().split("\\s+"))[1];

        int town_id = getEntityByName(minionTown, "towns");
        if (town_id < 0) {
            setIntoTowns(minionTown);
            System.out.printf("Town %s was added to the database.%n", minionTown);
        }
        int villain_id = getEntityByName(villainName, "villains");
        if (villain_id < 0) {
            insertVillain(villainName);
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }
        //TO DOO
        String query = "INSERT INTO minions(name, age, town_id) VALUES(?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, minionName);
        statement.setInt(2, minionAge);
        statement.setInt(3, getEntityByName(minionTown, "towns"));

        statement.executeUpdate();

        query = "INSERT INTO minions_villains VALUES(?,?)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, getEntityByName(minionName, "minions"));
        statement.setInt(2, getEntityByName(villainName, "villains"));
        statement.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villainName);


    }

    private void insertVillain(String villainName) throws SQLException {
        String query = "INSERT INTO villains(name, evilness_factor) VALUES (?,?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, villainName);
        statement.setString(2, "evil");
        statement.executeUpdate();
    }

    private void setIntoTowns(String townName) throws SQLException {
        String query = "INSERT INTO towns (name) VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, townName);
        statement.executeUpdate();
    }

    private Integer getEntityByName(String entityName, String tableName) throws SQLException {
        String query = String.format("SELECT id FROM %s WHERE name = ?", tableName);

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entityName);

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next() ? resultSet.getInt(1) : -1;

    }

    public void changeTownNameEx5() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String country = reader.readLine();

        String query = ("SELECT COUNT(name) AS count FROM towns " +
                "WHERE country = ? " +
                "GROUP BY country ");
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, country);
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            System.out.println("No town names were affected.");
        } else {
            System.out.printf("%d town names were affected.%n",
                    resultSet.getLong("count"));
            setToUpperCase(country);
            System.out.println(getTowns(country));
        }
    }
    private void setToUpperCase(String country) throws SQLException {
        String query = "UPDATE towns "  +
                "SET name = UPPER(name) " +
                "WHERE country = ?; ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, country);

        statement.executeUpdate();

    }
    private List<String> getTowns(String country) throws SQLException {
        String query = "SELECT name FROM towns " +
                "WHERE country = ? " +
                "GROUP BY name;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, country);
        ResultSet resultSet = statement.executeQuery();
        List<String> countries = new ArrayList<>();
        while(resultSet.next()){
            countries.add(resultSet.getString("name"));
        }
        return countries;
    }


    public void removeVillainEx6() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());

        String query = "SELECT COUNT(minion_id) AS count FROM minions_villains " +
                "WHERE villain_id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        if (resultSet.getInt("count") == 0){
            System.out.println("No such villain was found");
        }else{

            int count = resultSet.getInt("count");
            query = "DELETE v FROM minions_villains AS v " +
                    "WHERE villain_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            System.out.printf("%s was deleted%n", getEntityNameById(id, "villains"));
            query = "DELETE v FROM villains AS v " +
                    "WHERE v.id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            System.out.printf("%d minions released%n",count);
        }
    }

    public void printAllMinionNamesEx7() throws SQLException {
        String query = "SELECT name FROM minions";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        List<String> minionNames = new ArrayList<>();

        while (resultSet.next()){
            minionNames.add(resultSet.getString("name"));
        }
        int counter = 1;

        for (int i = 0; i <minionNames.size() - 1 ; i++) {
            System.out.println(minionNames.get(i));
            System.out.println(minionNames.get(minionNames.size() - counter++));
        }
    }


    public void increaseMinionsAgeEx8() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<Integer> ids  = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        for (int i = 0; i < ids.size() - 1; i++) {
            String query = "UPDATE minions " +
                    "SET age = age + 1, name = LOWER(name) " +
                    "WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, ids.get(i));
            statement.executeUpdate();
        }

        String query = "SELECT name, age FROM minions";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            System.out.printf("%s %d%n",
                    resultSet.getString("name"),
                    resultSet.getInt("age")
                    );
        }
    }


    public void increaseAgeStoredProcedureEx9() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int minion_id = Integer.parseInt(reader.readLine());


        String query = "CALL usp_get_older (?)";
        CallableStatement statement = connection.prepareCall(query);
        statement.setInt(1, minion_id);
        statement.execute();
    }
}
