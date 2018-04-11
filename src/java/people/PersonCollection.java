package people;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class PersonCollection {
    
    private ArrayList<Person> persArr = new ArrayList<Person>();
    
    
    public void PersonCollection(){
        
    }
    
    public ArrayList getPersonCollection(){
        
        return persArr;
    }
    
    public void addPerson(Person p){
        
        this.persArr.add(p);
    }
    
    public void removePerson(int i){
        
        this.persArr.remove(i);
    }
    
    public int getSize() {
        
        int x = persArr.size();
        return x;
    }
    
    public Person getPerson(int i) {
        
        return this.persArr.get(i);
    }
    
    public void editPerson(Person p, int i){
        
        this.persArr.set(i, p);
    }
    
    public void clear(Connection con){
        
        this.persArr.clear();
    }
    
    public void createTable(Connection con)throws SQLException{
       
        Statement statement = con.createStatement();
        
        statement.executeUpdate("CREATE TABLE people"
                + "("
                + "ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
                + "name varchar(40),"
                + "height varchar(40),"
                + "weight varchar(40),"
                + "hairColor varchar(40),"
                + "eyeColor varchar(40)"
                + ")"
        );
    }
    
    public void drop(Connection con)throws SQLException{
        
        Statement statement = con.createStatement();
        statement.executeUpdate("DROP TABLE PEOPLE");
    } 
    
    public void fillTable(Connection con)throws SQLException{
        
        Statement statement = con.createStatement();
        ResultSet results = statement.executeQuery("select * from people");
        while(results.next()){
            String name = results.getString("name");
            String height = results.getString("height");
            String weight = results.getString("weight");
            String hairColor = results.getString("hairColor");
            String eyeColor = results.getString("eyeColor");
            int id = results.getInt("id");
            persArr.add(new Person(name, height, weight, hairColor, eyeColor, id));
        }
    }
    
    public void insert(Connection con, HttpServletRequest request)throws SQLException{
        
            String name = request.getParameter("name");
            String height = request.getParameter("height");
            String weight = request.getParameter("weight");
            String hairColor = request.getParameter("hairColor");
            String eyeColor = request.getParameter("eyeColor");
            
            Statement statement = con.createStatement();
            
            statement.executeUpdate("insert into people"
                + "(name, height, weight, hairColor, eyeColor) values("
                + qSurround(name) + "," 
                + qSurround(height) + ","
                + qSurround(weight) + ","
                + qSurround(hairColor) + ","
                + qSurround(eyeColor)
                + ")"
            );
    }
    // TODO Placeholder for insert syntax debugging
    public void insertTest(Connection con)throws SQLException{
        
        Statement statement = con.createStatement();
        
        statement.executeUpdate("insert into people"
            + "(name, height, weight, hairColor, eyeColor) values"
            + "('michael jackson', '5 feet', '85 lbs', 'black', 'blue')"
        );
        statement.executeUpdate("insert into people"
            + "(name, height, weight, hairColor, eyeColor) values"
            + "('fred frog', '5 feet', '85 lbs', 'black', 'blue')"
        );
        statement.executeUpdate("insert into people"
            + "(name, height, weight, hairColor, eyeColor) values"
            + "('bob ross', '5 feet', '85 lbs', 'black', 'blue')"
        );
    }
    
    public void update(Connection con, HttpServletRequest request)throws SQLException{
        
        String name = request.getParameter("name");
        String height = request.getParameter("height");
        String weight = request.getParameter("weight");
        String hairColor = request.getParameter("hairColor");
        String eyeColor = request.getParameter("eyeColor");
        String index = (String) request.getParameter("id");
        
        Statement statement = con.createStatement();
        
        System.out.println("sql= " + "update people set "
                + "name = " + qSurround(name) + ", "
                + "height = " + qSurround(height) + ", "
                + "weight = " + qSurround(weight) + ", "
                + "hairColor = " + qSurround(hairColor) + ", "
                + "eyeColor = " + qSurround(eyeColor) + " "
                + "where ID= " + index
        );
        statement.executeUpdate("update people set "
                + "name = " + qSurround(name) + ", "
                + "height = " + qSurround(height) + ", "
                + "weight = " + qSurround(weight) + ", "
                + "hairColor = " + qSurround(hairColor) + ", "
                + "eyeColor = " + qSurround(eyeColor) + " "
                + "where ID= " + index
        );
    }
    
    public void delete(Connection con, HttpServletRequest request)throws SQLException{
        
        String id = request.getParameter("id");
        Statement statement = con.createStatement();
        statement.executeUpdate("delete from people where ID=" + id);
    }
    
    private String qSurround(String s){
        
        if (s == "")
            return "";
        String quoted = "'" + s + "'";
        return quoted;
    }
}
