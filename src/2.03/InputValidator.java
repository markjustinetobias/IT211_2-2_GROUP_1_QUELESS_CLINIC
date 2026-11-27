public class InputValidator {
    public boolean validateInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public boolean validateMenuChoice(String input, int min, int max) {
        if (!validateInput(input)) return false;
        try {
            int n = Integer.parseInt(input.trim());
            return n >= min && n <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void showErrorMessage(String message) {
        System.out.println("Invalid input: " + message);
    }
}
