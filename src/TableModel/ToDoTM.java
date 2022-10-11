package TableModel;

public record ToDoTM(String id, String description) {

    @Override
    public String toString() {
        return description;
    }
}
