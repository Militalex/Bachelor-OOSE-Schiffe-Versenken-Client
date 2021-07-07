package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;

public class ExampleController implements IController<ExampleController>{

    @Override
    public ExampleController getInstance() {
        return this;
    }

}
