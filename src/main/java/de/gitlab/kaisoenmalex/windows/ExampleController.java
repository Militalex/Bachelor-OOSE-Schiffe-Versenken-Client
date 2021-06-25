package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.IController;

public class ExampleController implements IController<ExampleController> {

    @Override
    public ExampleController getInstance() {
        return this;
    }
}
