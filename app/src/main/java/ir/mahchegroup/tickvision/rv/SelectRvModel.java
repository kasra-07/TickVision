package ir.mahchegroup.tickvision.rv;

public class SelectRvModel {
    private String title, isTick;

    public SelectRvModel(String title, String isTick) {
        this.isTick = isTick;
        this.title = title;
    }

    public String getIsTick() {
        return isTick;
    }

    public void setIsTick(String isTick) {
        this.isTick = isTick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
