public class InputValidator {
    public boolean validateInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public void showErrorMessage(String message) {
        System.out.println("Invalid input: " + message);
    }
}
