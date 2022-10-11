package mjw;

import java.sql.SQLException;

import javax.inject.*;
import javax.sql.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/_hc")
public class HealthCheckResource {
    private static final int DB_CONN_TIMEOUT_SEC = 10;
    private static final String createTableSQL = "CREATE TABLE IF NOT EXISTS entry (uuid int not null primary key auto_increment, timeStamp  date, createStamp  datetime, value decimal(10,2), balance decimal(10,2), category varchar(255), description varchar(255));";

    @Inject
    DataSource ds;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String healthCheck() {
        try (var conn = ds.getConnection()){
            conn.isValid(DB_CONN_TIMEOUT_SEC);
        }catch(SQLException ex){
            throw new WebApplicationException("Could not connect to database", 500);
        }
        return "Application is healthy";
    }
    @Path("createTable")
    @GET
    public String createTable() {
        try (var conn = ds.getConnection();
             var stmt = conn.createStatement()){
            stmt.execute(createTableSQL);
            return "Table created!";
        }catch(SQLException e){
            return "Error creating table: " + e.getMessage();
        }
    }
}