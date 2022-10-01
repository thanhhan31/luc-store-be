
package com.lucistore.lucistorebe.utility;

import java.util.Random;

public final class RandomString {
	private static final String SALT_OTP_CHARS = "1234567890";
	private static final String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final Random rand = new Random();
	
	public static String get(int length) {
        StringBuilder salt = new StringBuilder();
        while (salt.length() < length) {
            int index = (int) (rand.nextFloat() * SALT_CHARS.length());
            salt.append(SALT_CHARS.charAt(index));
        }
        return salt.toString();
    }
	
	public static String generateOtp(int length) {
        StringBuilder salt = new StringBuilder();
        while (salt.length() < length) {
            int index = (int) (rand.nextFloat() * SALT_OTP_CHARS.length());
            salt.append(SALT_OTP_CHARS.charAt(index));
        }
        return salt.toString();
    }
	
	private RandomString() {}
}
