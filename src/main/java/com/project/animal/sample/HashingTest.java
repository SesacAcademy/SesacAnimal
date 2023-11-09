package com.project.animal.sample;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashingTest {

    public static void main(String[] args) {
        String rawPassword = "1234"; // 사용자로부터 입력 받은 패스워드

        // PasswordEncoder를 생성 (Bcrypt 사용)
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 랜덤 솔트와 함께 패스워드 해싱
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // 해싱된 패스워드 출력
        System.out.println("해싱된 패스워드: " + hashedPassword);

        // $2a$10$HB5Sr8K.OF6Xrt02NlpfzewvRLk9osNEPv4TziM8naJZiPMpEVgK2
        // $2a$10$1yKjKdgzOPekrkCCNO9T/e1TnDh.csYUMAssNuQfMP27uNynRtGei
        // $2a$10$egrDGIF3Ne5Gv9cVHzODhOe2aZQtBPCJbJhGpQ8BvZfAn/5NFT2s2

        // 패스워드 검증 예제
        boolean passwordMatches = passwordEncoder.matches(rawPassword, "$2a$10$HB5Sr8K.OF6Xrt02NlpfzewvRLk9osNEPv4TziM8naJZiPMpEVgK2");
        System.out.println("패스워드 검증 결과: " + passwordMatches);
    }
}
    /*public static void main(String[] args) {
        String input = "Hello, World!"; // 해싱할 문자열

        // 안전한 랜덤 솔트 생성
        byte[] salt = generateSalt();

        String sha256Hash = Hashing.sha256()
                .hashString(input + "wNuqQwSxyrNHl4dXL8deCg==", StandardCharsets.UTF_8)
                .toString();

        System.out.println("SHA-256 해시: " + sha256Hash);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }*/