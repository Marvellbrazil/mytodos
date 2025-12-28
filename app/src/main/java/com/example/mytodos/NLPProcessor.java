package com.example.mytodos;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLPProcessor {
    // Hashmap for time keywords user input
    private static final Map<String, String> TIME_KEYWORDS = new HashMap<String, String>() {{
        put("jam", "jam");
        put("pukul", "jam");
        put("pada", "jam");
        put("siang", "jam");
        put("sore", "jam");
        put("malam", "jam");
        put("pagi", "jam");
        put("tanggal", "tanggal");
        put("tgl", "tanggal");
        put("besok", "besok");
        put("lusa", "lusa");
        put("hariini", "hariini");
        put("sekarang", "sekarang");
    }};

    // Hashmap for time values
    private static final Map<String, Integer> TIME_VALUES = new HashMap<String, Integer>() {{
        put("satu", 1);
        put("dua", 2);
        put("tiga", 3);
        put("empat", 4);
        put("lima", 5);
        put("enam", 6);
        put("tujuh", 7);
        put("delapan", 8);
        put("sembilan", 9);
        put("sepuluh", 10);
        put("sebelas", 11);
        put("duabelas", 12);
    }};

    // Class for Parsed TODO
    public static class ParsedTodo {
        public String title;
        public String description;
        public Date deadline;

        public ParsedTodo(String title, String description, Date deadline) {
            this.title = title;
            this.description = description;
            this.deadline = deadline;
        }
    }

    // Levenshtein Distance similarity approach
    public static int levenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(
                            dp[i - 1][j] + 1, // deletion
                            dp[i][j - 1] + 1), // insertion
                            dp[i - 1][j - 1] + cost // substitution
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    // Typo helper with fuzzy match
    public static String fuzzyMatch(String input, Map<String, String> dictionary) {
        input = input.toLowerCase();

        // Exact match
        if (dictionary.containsKey(input)) {
            return dictionary.get(input);
        }

        // Fuzzy match with Levenshtein Distance
        String bestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            int distance = levenshtein(input, entry.getKey());
            if (distance < minDistance && distance <= 2) {
                minDistance = distance;
                bestMatch = entry.getValue();
            }
        }

        return bestMatch;
    }

    // Parse input natural lang
    public static ParsedTodo parsedTodoInput(String input) {
        String title = extractTitle(input);
        String description = generateDescription(input);
        Date deadline = extractDeadline(input);

        return new ParsedTodo(title, description, deadline);
    }

    private static String extractTitle(String input) {
        // Delete time strings
        String[] words = input.split("\\s+");
        StringBuilder title = new StringBuilder();

        for (String word : words) {
            String normalizedWord = word.toLowerCase();
            boolean isTimeKeyword = false;

            // Check if the word is a time keywords
            for (String keyword : TIME_KEYWORDS.keySet()) {
                if (levenshtein(normalizedWord, keyword) <= 2) {
                    isTimeKeyword = true;
                    break;
                }
            }

            // Check if the word is time values
            if (normalizedWord.matches("\\d+") || TIME_VALUES.containsKey(normalizedWord) || normalizedWord.matches("jam\\d+")) {
                isTimeKeyword = true;
            }

            if (!isTimeKeyword) {
                title.append(word).append(" ");
            }
        }

        // Capitalize each word
        String result = capitalize(title.toString().trim());
        return result.isEmpty() ? "Task Baru" : result;
    }

    private static String generateDescription(String title) {
        // Description of the title that soon will be generated
        String[] templates = {
                "Task: " + title,
                "Kegiatan: " + title,
                "Pekerjaan: " + title,
                "Reminder untuk: " + title
        };

        return templates[(int) (System.currentTimeMillis() % templates.length)];
    }

    private static Date extractDeadline(String input) {
        Calendar calendar = Calendar.getInstance();
        input = input.toLowerCase();

        // Pattern for the time
        Pattern timePattern = Pattern.compile("(jam|pukul|pada|pas|saat|untuk)\\s*(\\d{1,2})");
        Matcher matcher = timePattern.matcher(input);

        if (matcher.find()) {
            try {
                int hour = Integer.parseInt(Objects.requireNonNull(matcher.group(2)));
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                // If the time is past, +1 day
                if (calendar.getTime().before(new Date())) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                return calendar.getTime();
            } catch (NumberFormatException e) {
                // Try with fuzzy match
                String[] words = input.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    if (words[i].equals("jam") || levenshtein(words[i], "jam") <= 2) {
                        if (i +  1 < words.length) {
                            String nextWord = words[i + 1];
                            Integer hour = TIME_VALUES.get(nextWord);
                            if (hour != null) {
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, 0);
                                calendar.set(Calendar.SECOND, 0);

                                if (calendar.getTime().before(new Date())) {
                                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                                }

                                return calendar.getTime();
                            }
                        }
                    }
                }
            }
        }

        // Simple pattern only for numbers
        Pattern simplePattern = Pattern.compile("\\b(\\d{1,2})\\b");
        matcher = simplePattern.matcher(input);

        if (matcher.find()) {
            try {
                int hour = Integer.parseInt(Objects.requireNonNull(matcher.group(1)));
                if (hour >= 0 && hour <= 24) {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);

                    if (calendar.getTime().before(new Date())) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                    }

                    return calendar.getTime();
                }
            } catch (NumberFormatException e) {
                //noinspection ThrowablePrintedToSystemOut
                System.err.println(e);
            }
        }

        // Default value: 2 hours from NOW
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return calendar.getTime();
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }
}
