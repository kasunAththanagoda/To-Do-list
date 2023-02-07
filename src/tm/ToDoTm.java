package tm;

public class ToDoTm {

    String id;
    String item;
    String category;
    String userid;

    public ToDoTm() {
    }

    public ToDoTm(String id, String item, String category, String userid) {
        this.id = id;
        this.item = item;
        this.category = category;
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return item;
    }
}
