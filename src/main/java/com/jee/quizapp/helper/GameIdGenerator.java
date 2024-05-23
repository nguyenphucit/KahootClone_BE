package com.jee.quizapp.helper;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class GameIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        int length = 10;

        // Ký tự cho phép
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // Sinh chuỗi ngẫu nhiên
        StringBuilder randomId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedCharacters.length());
            randomId.append(allowedCharacters.charAt(index));
        }

        return randomId.toString();
    }
}
