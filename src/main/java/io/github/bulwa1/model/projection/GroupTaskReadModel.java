package io.github.bulwa1.model.projection;

import io.github.bulwa1.model.Task;

public class GroupTaskReadModel {
    private String description;
    private boolean done;

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

     public void setDescription(final String description) {
        this.description = description;
    }

     public boolean isDone() {
        return done;
    }

     public void setDone(final boolean done) {
        this.done = done;
    }
}
