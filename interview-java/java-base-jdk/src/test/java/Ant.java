public class Ant extends insect implements IAttack {
    @Override
    public void move() {

    }

    @Override
    public void attack() {

    }

    public Ant(String name, Double size, String color) {
        super(name, size, color);
    }

    public static void main(String[] args) {

        insect ant = new Ant("pp",1.0,"red");
        System.out.println("动物是"+ant.name+"重量是"+ant.size+"颜色是"+ant.color);
    }
}
