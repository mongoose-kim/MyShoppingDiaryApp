package ddwu.mobile.finalproject.ma02_20160214;

import java.io.Serializable;

public class BlogDto implements Serializable {

    private int _id;
    private String title;
    private String link;
    private String description;
    private String bloggername;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTitle(String title) { this.title = title; }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBloggername(String bloggername) {
        this.bloggername = bloggername;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getBloggername() {
        return bloggername;
    }

    @Override
    public String toString() {
        return "BlogDto{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", bloggername='" + bloggername + '\'' +
                '}';
    }
}
