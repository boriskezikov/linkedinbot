package com.tr.linkedinbot.allpay.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.linkedinbot.allpay.model.CheckPaymentStatusReq;
import com.tr.linkedinbot.allpay.model.PaymentRequest;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@UtilityClass
public class PaymentUtil {

    public static String getApiSignature(PaymentRequest paymentRequest, String apiKey, ObjectMapper objectMapper) {
        var signatureData = PaymentRequestMapper.toSignatureData(paymentRequest);
        Map<String, Object> params = objectMapper.convertValue(signatureData, Map.class);
        params.values().removeIf(Objects::isNull);
        // Sort the parameters by key
        Map<String, Object> sortedParams = new TreeMap<>(params);
        List<String> chunks = new ArrayList<>();

        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List) {
                List<?> list = (List<?>) value;
                for (Object item : list) {
                    if (item instanceof Map) {
                        Map<String, Object> itemMap = (Map<String, Object>) item;
                        Map<String, Object> sortedItemMap = new TreeMap<>(itemMap);
                        for (Map.Entry<String, Object> itemEntry : sortedItemMap.entrySet()) {
                            Object val = itemEntry.getValue();
                            if (val != null && !val.toString().trim().isEmpty()) {
                                chunks.add(val.toString());
                            }
                        }
                    }
                }
            } else {
                if (value != null && !value.toString().trim().isEmpty() && !"sign".equals(key)) {
                    chunks.add(value.toString());
                }
            }
        }

        // Join the chunks with ":" and append the apiKey
        String signatureBase = String.join(":", chunks) + ":" + apiKey;

        // Hash the final string using SHA-256
        return sha256(signatureBase);
    }

    private static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getApiSignature(CheckPaymentStatusReq paymentRequest, String apiKey, ObjectMapper objectMapper) {
        Map<String, Object> params = objectMapper.convertValue(paymentRequest, Map.class);
        params.values().removeIf(Objects::isNull);
        // Sort the parameters by key
        Map<String, Object> sortedParams = new TreeMap<>(params);
        List<String> chunks = new ArrayList<>();

        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List) {
                List<?> list = (List<?>) value;
                for (Object item : list) {
                    if (item instanceof Map) {
                        Map<String, Object> itemMap = (Map<String, Object>) item;
                        Map<String, Object> sortedItemMap = new TreeMap<>(itemMap);
                        for (Map.Entry<String, Object> itemEntry : sortedItemMap.entrySet()) {
                            Object val = itemEntry.getValue();
                            if (val != null && !val.toString().trim().isEmpty()) {
                                chunks.add(val.toString());
                            }
                        }
                    }
                }
            } else {
                if (value != null && !value.toString().trim().isEmpty() && !"sign".equals(key)) {
                    chunks.add(value.toString());
                }
            }
        }

        // Join the chunks with ":" and append the apiKey
        String signatureBase = String.join(":", chunks) + ":" + apiKey;

        // Hash the final string using SHA-256
        return sha256(signatureBase);
    }

}
