package Model;

/**
 * Created by Anh Trung on 6/24/2015.
 */
public class Friend {
    private long id;
    private String name;
    private String image;

    public Friend(String name,String image,long id){
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Friend(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
