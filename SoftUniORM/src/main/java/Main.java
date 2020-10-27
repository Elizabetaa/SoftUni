import entities.User;
import orm.Connector;
import orm.EntityManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        Connector.createConnection(
                "root","1234","soft_uni");

        Connection connection = Connector.getConnection();

        User user = new User("root","",
                44, Date.valueOf("2019-05-06"));

        EntityManager<User> userEntityManager = new EntityManager<>(connection);

        userEntityManager.persist(user);
        user.setPassword("1234");
        userEntityManager.persist(user);
    }
}
