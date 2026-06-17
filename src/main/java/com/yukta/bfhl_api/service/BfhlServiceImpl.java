package com.yukta.bfhl_api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.yukta.bfhl_api.dto.RequestDto;
import com.yukta.bfhl_api.dto.ResponseDto;

@Service
public class BfhlServiceImpl implements BfhlService {

    @Override
    public ResponseDto process(RequestDto request, String requestId) {

        long startTime = System.currentTimeMillis();

        ResponseDto response = new ResponseDto();

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialChars = new ArrayList<>();
        List<Double> numericValues = new ArrayList<>();

        Map<String, Integer> alphabetFrequency = new HashMap<>();

        Set<String> uniqueValues = new LinkedHashSet<>();

        boolean duplicateFound = false;
        int vowelCount = 0;

        String longestAlphabetic = "";
        String shortestAlphabetic = null;

        for (String item : request.getData()) {

            if (item == null || item.trim().isEmpty()) {
                continue;
            }

            if (!uniqueValues.add(item)) {
                duplicateFound = true;
            }
        }

        for (String value : uniqueValues) {

            if (value.matches("-?\\d+(\\.\\d+)?")) {

                double num = Double.parseDouble(value);

                numericValues.add(num);

                if (num % 2 == 0)
                    evenNumbers.add(value);
                else
                    oddNumbers.add(value);
            }

            else if (value.matches("[A-Za-z]+")) {

                String upper = value.toUpperCase();

                alphabets.add(upper);

                if (upper.length() > longestAlphabetic.length()) {
                    longestAlphabetic = upper;
                }

                if (shortestAlphabetic == null ||
                        upper.length() < shortestAlphabetic.length()) {
                    shortestAlphabetic = upper;
                }

                for (char c : upper.toCharArray()) {

                    String letter = String.valueOf(c);

                    alphabetFrequency.put(
                            letter,
                            alphabetFrequency.getOrDefault(letter, 0) + 1);

                    if ("AEIOU".contains(letter))
                        vowelCount++;
                }
            }

            else if (value.matches("[A-Za-z0-9]+")) {

                Matcher letterMatcher =
                        Pattern.compile("[A-Za-z]")
                                .matcher(value);

                while (letterMatcher.find()) {

                    String letter =
                            letterMatcher.group()
                                    .toUpperCase();

                    alphabets.add(letter);

                    alphabetFrequency.put(
                            letter,
                            alphabetFrequency.getOrDefault(letter, 0) + 1);

                    if ("AEIOU".contains(letter))
                        vowelCount++;
                }

                Matcher numberMatcher =
                        Pattern.compile("\\d+")
                                .matcher(value);

                while (numberMatcher.find()) {

                    double num =
                            Double.parseDouble(
                                    numberMatcher.group());

                    numericValues.add(num);
                }
            }

            else {

                specialChars.add(value);
            }
        }

        Collections.sort(numericValues);

        List<String> sortedNumbers = new ArrayList<>();

        for (Double d : numericValues) {
            sortedNumbers.add(String.valueOf(d));
        }

        double sum = numericValues.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        response.setIs_success(true);
        response.setRequest_id(requestId);

        response.setOdd_numbers(oddNumbers);
        response.setEven_numbers(evenNumbers);
        response.setAlphabets(alphabets);
        response.setSpecial_characters(specialChars);

        response.setAlphabet_count(alphabets.size());
        response.setNumber_count(numericValues.size());
        response.setSpecial_character_count(specialChars.size());

        response.setContains_duplicates(duplicateFound);
        response.setUnique_element_count(uniqueValues.size());

        response.setVowel_count(vowelCount);
        response.setAlphabet_frequency(alphabetFrequency);

        response.setSorted_numbers(sortedNumbers);

        response.setLongest_alphabetic_value(longestAlphabetic);
        response.setShortest_alphabetic_value(shortestAlphabetic);

        response.setSum(String.valueOf(sum));

        if (!numericValues.isEmpty()) {

            response.setLargest_number(
                    String.valueOf(Collections.max(numericValues)));

            response.setSmallest_number(
                    String.valueOf(Collections.min(numericValues)));
        }

        response.setProcessing_time_ms(
                System.currentTimeMillis() - startTime);

        return response;
    }
}