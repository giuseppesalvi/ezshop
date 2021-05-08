package it.polito.ezshop.model;

public class Position {

    private String aisleID, rackID, levelID;

    public Position(String aisleID, String rackID, String levelID) {
        this.aisleID = aisleID;
        this.rackID = rackID;
        this.levelID = levelID;
    }

    public Position(String position) {
        String[] pos = position.split("-");
        this.aisleID = pos[0];
        this.rackID = pos[1];
        this.levelID = pos[2];
    }

    public String getPosition() {
        return aisleID+"-"+rackID+"-"+levelID;
    }

    public void setPosition(String position){
        String[] pos = position.split("-");
        this.aisleID = pos[0];
        this.rackID = pos[1];
        this.levelID = pos[2];
    }

}
