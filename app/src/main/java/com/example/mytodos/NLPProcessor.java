package com.example.mytodos;

import java.util.HashMap;
import java.util.Map;

public class NLPProcessor {
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

    public static class ParsedTodo {
        public String title;
        public String description;
        public String deadline;

        public ParsedTodo(String title, String description, String deadline) {
            this.title = title;
            this.description = description;
        }
    }
}
