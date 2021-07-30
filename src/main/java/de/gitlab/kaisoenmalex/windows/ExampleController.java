package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import javafx.scene.control.Button;
import org.jetbrains.annotations.Nullable;

public class ExampleController implements IController<ExampleController>{

    @Override
    public ExampleController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return null;
    }

}
