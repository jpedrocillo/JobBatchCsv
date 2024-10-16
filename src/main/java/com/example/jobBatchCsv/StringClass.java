package com.example.jobBatchCsv;

public class StringClass {

    public String getStringValue(String input) throws IllegalArgumentException{

        if(input==null){
            throw new IllegalArgumentException("Input cannot be null");
        }else if(input.isEmpty()){
            return "Empty String";
        } else if (input.trim().isEmpty()) {
            return "Whitespace";
        }else {
            return input.toUpperCase();
        }

    }
}
