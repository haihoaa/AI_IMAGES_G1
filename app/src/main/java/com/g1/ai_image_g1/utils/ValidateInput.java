package com.g1.ai_image_g1.utils;

public class ValidateInput {

    public static boolean validateInput(String input) {
        if (input.trim().isEmpty()) {
            return false;
        }
        String regex = ".*[a-zA-Z].*";
        return input.matches(regex);
    }
    public static String modifyPrompt(String input) {
        String[] keywords = {"hair", "eyes"};
        StringBuilder modified = new StringBuilder();
        String[] parts = input.split(", ");
        for (String part : parts) {
            boolean modifiedPart = false;
            for (String keyword : keywords) {
                if (part.contains(keyword)) {
                    modified.append("(").append(part).append(":1.5)");
                    modifiedPart = true;
                    break;
                }
            }
            if (!modifiedPart) {
                modified.append(part);
            }
            modified.append(", ");
        }

        return modified.substring(0, modified.length() - 2);
    }
}
