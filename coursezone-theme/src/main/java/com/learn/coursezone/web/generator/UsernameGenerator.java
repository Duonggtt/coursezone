package com.learn.coursezone.web.generator;

import java.security.SecureRandom;

public class UsernameGenerator {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomUsername() {
        StringBuilder username = new StringBuilder();

        // Tạo 4 ký tự chữ cái
        for (int i = 0; i < 4; i++) {
            username.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }

        // Tạo 8 số ngẫu nhiên
        for (int i = 0; i < 8; i++) {
            username.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }

        return username.toString();
    }

    public static void main(String[] args) {
        // Kiểm tra phương thức tạo tên người dùng ngẫu nhiên
        System.out.println("Generated Username: " + generateRandomUsername());
    }
}
