package com.kovospace.paster.base.services;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class MD5ServiceImpl implements MD5service {

    @Override
    public String getHash(String input) {
        return getHash(input.getBytes());
    }

    @Override
    public String getHash(long input) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(input);
        return getHash(buffer.array());
    }

    private String getHash(byte[] input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input);
            return Arrays.toString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            //TODO custom exception
            throw new RuntimeException(e);
        }
    }
}
