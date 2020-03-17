package jdk;

/**
 * 枚举类测试
 */
public class Enum {
    private PizzaStatus status;

    public PizzaStatus getStatus() {
        return status;
    }

    public void setStatus(PizzaStatus status) {
        this.status = status;
    }

    public enum PizzaStatus {
        ORDERED,
        READY,
        DELIVERED;
    }
    public enum PizzaStatus2 {
        ORDERED,
        READY,
        DELIVERED;
    }

    public boolean isDeliverable() {
        if (getStatus() == PizzaStatus.READY) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Enum anEnum = new Enum();
        PizzaStatus status = anEnum.getStatus();

    }
}
