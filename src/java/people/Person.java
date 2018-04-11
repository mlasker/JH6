package people;

public class Person {
    
    private String name;
    private String hairColor;
    private String eyeColor;
    private String height;
    private String weight;
    private int id;
    
    public Person (String name, String height, String weight, String hairColor, String eyeColor) {
        
        this.name = name;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.height = height;
        this.weight = weight;
    }
    // Overloaded method to work with database, adds ID to constructor
    public Person (String name, String height, String weight, String hairColor, String eyeColor, int id) {
        
        this.name = name;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.height = height;
        this.weight = weight;
        this.id = id;
    }
    
    public Person () {
        
    }
    
    public void setName (String name) {
    
        this.name = name;
    }   
    
    public void setHairColor (String haircolor) {
    
        this.hairColor = haircolor;
    }
    
    public void setEyeColor (String eyecolor) {
    
        this.eyeColor = eyecolor;
    }
    
    public void setHeight (String height) {
    
        this.height = height;
    }
    
    public void setWeight (String weight) {
    
        this.weight = weight;
    }
    
    public void setId (int id){
        this.id=id;
    }
    
    public String getName () {
        
        return this.name;
    }
    
    public String getHairColor () {
        
        return this.hairColor;
    }
    
    public String getEyeColor () {
        
        return this.eyeColor;
    }
    
    public String getHeight () {
        
        return this.height;
    }
    
    public String getWeight () {
        
        return this.weight;
    }
    
    public int getId(){
        
        return this.id;
    }
}
