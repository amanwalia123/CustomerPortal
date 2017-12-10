package Application;

public class BookInfo {

    String title;           //Title of given book
    int year;               //year of given book
    String language;        //language for given book
    int weight;             //weight of given book
    String category;        //category of book


    public BookInfo(String t, int y, String l, int w, String c) {
        title = t;
        year = y;
        language = l;
        weight = w;
        category = c;

    }

    public BookInfo(BookInfo book) {
        title = book.getTitle();
        year = book.getYear();
        language = book.getLanguage();
        weight = book.getWeight();
        category = book.getCategory();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString(){
        return this.getTitle()+"  "+this.getCategory()+"  "+this.getLanguage()+"  "+this.getWeight()+"  "+this.getLanguage();
    }
}
