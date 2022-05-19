package ga.justreddy.wiki.rtags.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class DatabaseManager {


    private Connection con;

    public void connectMySQL(String database, String username, String password, String host, int port) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS rtags_playerTags (uuid VARCHAR(255), identifier VARCHAR(255))");
            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS rtags_tags (identifier VARCHAR(255), name VARCHAR(255), description VARCHAR(255))");
            //con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS rtags_playerstags (uuid VARCHAR(255), tags VARCHAR(1000000000))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void connectH2(JavaPlugin plugin, String storagePath) {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/" + storagePath);
            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS rtags_playerTags (uuid VARCHAR(255), identifier VARCHAR(255))");
            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS rtags_tags (identifier VARCHAR(255), name VARCHAR(255), description VARCHAR(255))");
           // con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS rtags_playerstags (uuid VARCHAR(255), tags VARCHAR(1000000000))");
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private MongoDatabase database;
    private boolean connected = false;
    public void connectMongoDB(String URI, String db){
        MongoClientURI connectURI = new MongoClientURI(URI);
        MongoClient mongoClient = new MongoClient(connectURI);
        database = mongoClient.getDatabase(db);
        connected = true;
    }

    public MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }

    public void closeConnection() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean isConnected() {
        return con != null;
    }

    public boolean isMongoConnected() {
        return connected;
    }


    public void update(String qry) {
        try {
            getConnection().createStatement().executeUpdate(qry);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public ResultSet getResult(String qry) {
        try {
            return getConnection().createStatement().executeQuery(qry);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public PreparedStatement prepareStatement(String qry) {

        try{
            return getConnection().prepareStatement(qry);
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return null;
    }


    public Connection getConnection() {
        return con;
    }
}
