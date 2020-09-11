package inowen.testing;

import net.minecraft.client.gui.widget.button.Button;

import java.util.Stack;

/**
 * Testing different ways to implement functional interfaces.
 */
public class FunctionalInterfaceTest {

    public interface IFuncInterface{
        void theFunction(); // No parameters
    }

    /**
     * Using an extra class just to implement the interface
     */
    public static class Implementer implements IFuncInterface {
        @Override public void theFunction() { }
    }


    /**
     * Anonymous class for the implementation
     */
    public void aMethod0() {
        IFuncInterface aFunction = new IFuncInterface() {
            @Override public void theFunction() {}
        };
    }



    /**
     * Using lambdas to implement the functional interface
     */
    public void aMethod1() {
        IFuncInterface aFunction = () -> System.out.println("Doing something!");
        aFunction.theFunction();
    }

    /**
     * Test if lambdas are useful when working with objects as parameters (if there is a way to access their methods)
     */
    Button aButton = new Button(0, 0, 10, 10, "hi", (Button button) -> {
       String msg = button.getMessage(); // There is some kind of understanding of what the constructor needs... IDE knows that button is a Button
    });
}
