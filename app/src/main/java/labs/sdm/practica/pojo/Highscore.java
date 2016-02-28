package labs.sdm.practica.pojo;

public class HighScore implements Comparable<HighScore>{

    String name;
    String scoring;
    String latitude;
    String longitude;

    public HighScore(){

    }

    public HighScore(String _name, String _score, String _latitude, String _longitude){
        name = _name;
        scoring = _score;
        latitude = _latitude;
        longitude = _longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScoring() {
        return scoring;
    }

    public void setScoring(String scoring) {
        this.scoring = scoring;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(HighScore another) {
        if (Integer.parseInt(getScoring()) > Integer.parseInt(another.getScoring())) {
            return -1;
        }
        if (Integer.parseInt(getScoring()) < Integer.parseInt(another.getScoring())) {
            return 1;
        }
        return 0;
    }
}
