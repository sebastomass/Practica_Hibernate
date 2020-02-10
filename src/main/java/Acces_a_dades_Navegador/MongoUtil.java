package Acces_a_dades_Navegador;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoUtil {
    public static void main(String[] args) {
        String uri = "mongodb+srv://thomse:Blabplace1995@cluster0-pyp8i.mongodb.net/test?authSource=admin&replicaSet=Cluster0-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true";
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("accesadades");
        MongoCollection collection = mongoDatabase.getCollection("accesadades");

        Document document = new Document("name", "Thomse");

        document.append("Sex", "male");
        document.append("Age", "22");
        document.append("Country", "Spain");
        collection.insertOne(document);


    }
}
