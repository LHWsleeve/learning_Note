public class insect {
    String name;
    Double size;
    String color;

    public insect() {
    }

    public insect(String name, Double size, String color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "insect{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", color='" + color + '\'' +
                '}';
    }
}
