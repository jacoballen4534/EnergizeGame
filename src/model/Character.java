package model;

public class Character {
    private String imgDirectory;
    private String name;

    public Character(){
        this.imgDirectory = "";
        this.name = "";
    }

    public Character(String dir, String name){
        this.imgDirectory = dir;
        this.name = name;
    }

    public String getImgDirectory() {
        return imgDirectory;
    }

    public String getName() {
        return name;
    }
}
