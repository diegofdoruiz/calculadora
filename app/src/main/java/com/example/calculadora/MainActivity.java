package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //global vars
    private TextView screen_result, screen_input;
    private Boolean parenthesis_error = true;
    private String signs = "+-x÷^√";
    private Double memory;
    private Double result;
    private String input;
    private Boolean equal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen_result = findViewById(R.id.screen_result);
        screen_input = findViewById(R.id.screen_input);

        this.input = "";
        this.result = 0.0;
        this.memory = 0.0;
        this.equal = false;
        //Struct final_struct = this.processString("1000");

        //(3+4)1+(2+3)+3+1(9+23)x4-6+2-4
        //1÷(1÷3)÷3x34÷(3+4x3)÷3
        //1x(3+5(7+8))9x6

        /*Struct final_struct = this.processParentheses("+");

        Struct tttt = new Struct(null, null, null, null, null);
        String algo = tttt.structToString(final_struct);

        String TAG = "Recursion: ";
        Log.i(TAG, algo);

        Log.i("res", Double.toString(tttt.calculate(final_struct)));*/

    }

    public void onClick(View view) {
        Button button = (Button) view;
        String str_input = button.getText().toString();
        String old_result = screen_result.getText().toString();
        String old_input = screen_input.getText().toString();
        String last_in_old_input = Character.toString(old_input.charAt(old_input.length() - 1));

        //save memory
        if (str_input.equals("MS")){
            try {
                this.memory = Double.parseDouble(old_result);
                if (this.memory.isInfinite()){
                    this.memory = 0.0;
                }
            } catch (NumberFormatException nfe){
                this.memory = 0.0;
            }
            Log.i("Memory", this.memory+"");
            return;
        }

        //recovery memory
        if (str_input.equals("MR")){
            String str_memory = Double.toString(this.memory);
            String str_show;

            String [] array_str_memory = str_memory.split("\\.");
            if (array_str_memory.length > 1){
                if (Double.parseDouble(array_str_memory[1]) > 0.0){
                    str_show = str_memory;
                }else{
                    str_show = array_str_memory[0];
                }
            }else {
                str_show = array_str_memory[0];
            }
            if (old_input.equals(old_result)){
                screen_result.setText(str_show);
                screen_input.setText(str_show);
            }else{
                old_input += "("+str_show+")";
                screen_input.setText(old_input);
                this.solveInput(old_input);
            }
            return;
        }

        //clear all
        if (str_input.equals("C")){
            this.input = "";
            this.result = 0.0;
            this.memory = 0.0;
            this.equal = false;
            screen_result.setText("0");
            screen_input.setText("0");
            old_input = "0";
            old_result = "0";
            str_input = "0";

        }

        //if equal
        if (str_input.equals("=")){
            this.equal = true;
            this.solveInput(old_input);
            return;
        }

        //if has equal
        if ((old_result.equals(old_input) || !old_result.equals("Error")) && this.equal && !signs.contains(str_input) && !str_input.equals("(") && !str_input.equals(")") && !str_input.equals("⌫")){
            this.result=0.0;
            this.equal = false;
            this.input = "";
            screen_result.setText("0");
            old_input = "0";
        }else if (this.equal && old_result.equals("Error")){
            screen_result.setText("0");
            this.equal = false;
        }else{
            this.equal = false;
        }

        //Set correct input sings
        if (last_in_old_input.equals("(") && signs.contains(str_input)){
            return;
        }

        if (signs.contains(str_input) && signs.contains(last_in_old_input)){
            old_input = old_input.substring(0,old_input.length()-1);
        }

        if (str_input.equals("⌫") && old_input.length()>0){
            if (old_input.length() == 1){
                old_input = "0";
            }else{
                old_input = old_input.substring(0,old_input.length()-1);
            }
        }

        String all_input;

        if (old_input.equals("0")){
            if ((signs.contains(str_input) || str_input.equals(".")) && !str_input.equals("√")) {
                all_input = "0"+str_input;
            }else if(str_input.equals("√")) {
                all_input = "1"+str_input;
            }else if(!str_input.equals("⌫")) {
                all_input = str_input;
            }else{
                all_input = "0";
            }
        }else{
            if (!str_input.equals("⌫")){
                all_input = old_input + str_input;
            }else if(signs.contains(str_input)){
                all_input = "0"+str_input;
            }else{
                all_input = old_input;
            }
        }
        screen_input.setText(all_input);
        this.input = all_input;
        this.solveInput(all_input);
    }

    private void solveInput(String all_input){
        //Verify error in parenthesis
        Boolean verify_input = this.verifyParentheses(all_input);
        if (verify_input){

            parenthesis_error = false;
            Struct final_struct = this.processParentheses(all_input);

            String algo = final_struct.structToString(final_struct);
            Log.i("Result", algo);

            try {
                this.result = final_struct.calculate(final_struct);
                String str_result = Double.toString(this.result);
                String str_show;

                String [] array_str_result = str_result.split("\\.");
                if (array_str_result.length > 1){
                    if (Double.parseDouble(array_str_result[1]) > 0.0){
                        str_show = str_result;
                    }else{
                        str_show = array_str_result[0];
                    }
                }else {
                    str_show = array_str_result[0];
                }

                screen_result.setText(str_show);

                if (this.equal){
                    this.input = str_show;
                    screen_input.setText(str_show);
                    //this.equal = false;
                }else{
                    this.input = all_input;
                }


            } catch (NumberFormatException nfe){
                screen_result.setText("Error");
            }

        }else{
            this.result = 0.0;
            if (this.equal){
                //this.equal = false;
                screen_result.setText("Error");
            }
        }
    }

    private Boolean verifyParentheses(String str_input){

        Boolean result = false;
        Integer count_open = 0;
        Integer count_close = 0;

        //split string by character
        String[] array_str_input = str_input.split("");

        //remove array edges because this
        array_str_input = Arrays.copyOfRange(array_str_input, 1, array_str_input.length);

        //Log.i("all", Arrays.toString(array_str_input));

        for (Integer i=0; i<array_str_input.length; i++){
            if(array_str_input[i].equals("(")){
                count_open++;
            }else if (array_str_input[i].equals(")")){
                count_close++;
            }

            //finish one parentheses(correct)
            if (count_open == 0 && count_close == 0) { //partially
                result = true;
            }else if(count_open > count_close) { //partially
                result = false;
            }else if (count_open > 0 && count_open == count_close){//(correct)
                if (i+1 < array_str_input.length){
                    String rest_of_string = str_input.substring(i+1);
                    result = this.verifyParentheses(rest_of_string);
                }else{
                    result = true;
                }
                break;
            }else if(count_open < count_close){ //(incorrect)
                result = false;
                break;
            }
        }
        return result;
    }

    private Struct processParentheses(String str_input){
        Struct result = new Struct(null, null, null, null, null);

        //If this str_input -> √(
        if (str_input.length()>=2){
            if (Character.toString(str_input.charAt(0)).equals("√") && Character.toString(str_input.charAt(1)).equals("(")){
                List<String>  splits = this.valid_sqrt(str_input);

                result.setSign(this.getSign("√"));
                result.setNumber1(2.0);
                while (splits.remove("")) {}
                if (splits.size()>0){
                    result.setExp2(this.processParentheses(splits.get(0)));
                }else{
                    result.setNumber2(0.0);
                }

                return result;
            }
        }
        if (str_input.contains("(") && str_input.contains(")")) {
            String sign="";
            List<String> splits = null;
            if (!this.valid_plus(str_input).isEmpty()){
                splits = this.valid_plus(str_input);
                sign = this.getSign("+");
            }else if (!this.valid_minus(str_input).isEmpty()){
                splits = this.valid_minus(str_input);
                sign = this.getSign("-");
            }else if (!this.valid_division(str_input).isEmpty()){
                splits = this.valid_division(str_input);
                sign = this.getSign("÷");
            }else if (!this.valid_multiplication(str_input).isEmpty()){
                splits = this.valid_multiplication(str_input);
                sign = this.getSign("x");
            }else if (!this.valid_sqrt(str_input).isEmpty()){
                splits = this.valid_sqrt(str_input);
                sign = this.getSign("√");
            }else if (!this.valid_pow(str_input).isEmpty()){
                splits = this.valid_pow(str_input);
                sign = this.getSign("^");
            }

            //Remove empty values
            while (splits.remove("")) {}


            Struct by_par =  new Struct(null, null, null, null, null);
            if (splits.size() == 1) {

                double number2 = 0.0;
                if (sign.equals("product") || sign.equals("quotient") || sign.equals("pow")){
                    number2 = 1.0;
                }

                by_par.setSign(sign);
                try {
                    by_par.setNumber1(Double.parseDouble(splits.get(0)));
                } catch (NumberFormatException nfe){
                    by_par.setExp1(this.processParentheses(splits.get(0)));
                }
                by_par.setNumber2(number2);
                result = by_par;

            }else if (splits.size() == 2){

                by_par.setSign(sign);
                try {
                    by_par.setNumber1(Double.parseDouble(splits.get(0)));
                } catch (NumberFormatException nfe){
                    by_par.setExp1(this.processParentheses(splits.get(0)));
                }

                try {
                    by_par.setNumber2(Double.parseDouble(splits.get(1)));
                } catch (NumberFormatException nfe){
                    by_par.setExp2(this.processParentheses(splits.get(1)));
                }
                result = by_par;

            }else{

                for (int i=0; i<splits.size(); i++){
                    if (i==0){
                        by_par.setSign(sign);

                        //verify if this position is digit o expression
                        try {
                            Double number1 = Double.parseDouble(splits.get(i));
                            by_par.setNumber1(number1);
                        } catch (NumberFormatException nfe){
                            by_par.setExp1(this.processParentheses(splits.get(i)));
                        }

                        //verify if this position is digit o expression
                        try {
                            Double number2 = Double.parseDouble(splits.get(i+1));
                            by_par.setNumber2(number2);
                        } catch (NumberFormatException nfe){
                            by_par.setExp2(this.processParentheses(splits.get(i+1)));
                        }
                        i++;
                    }else{
                        result.setSign(sign);
                        result.setExp1(by_par);

                        //verify if this position is digit o expression
                        try {
                            Double number2 = Double.parseDouble(splits.get(i));
                            result.setNumber2(number2);
                        } catch (NumberFormatException nfe){
                            result.setExp2(this.processParentheses(splits.get(i)));
                        }

                        //restart by_par for set all content of result
                        by_par = new Struct(null, null, null, null, null);
                        by_par.setSign(sign);
                        by_par.setExp1(result.getExp1());
                        by_par.setNumber1(result.getNumber1());
                        by_par.setExp2(result.getExp2());
                        by_par.setNumber2(result.getNumber2());

                        //if has other iteration, cleaning result struct
                        if (i+1<splits.size()) {
                            result = new Struct(null, null, null, null, null);
                        }
                    }
                }

            }

        }else{
            //it has not parenthesis
            return this.processString(str_input);
        }

        return result;
    }

    private List<String> valid_plus(String str_input){
        Integer count_open = 0;
        Integer count_close = 0;

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        List<Integer> valid_plus_aux = new ArrayList<>();
        boolean plus_valid = false;
        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("(")) {
                count_open++;
            } else if (this_char.equals(")")) {
                count_close++;
            }

            if (count_open == 0) {
                plus_valid = true;
                //find a closed parentheses
            }else if(count_open > count_close){
                plus_valid = false;
            }else if (count_open > 0 && count_open.equals(count_close)) {
                plus_valid = true;
            }

            if (this_char.equals("+") && plus_valid){
                valid_plus_aux.add(i);
                count_open = 0;
                count_close = 0;
                plus_valid = false;
            }
        }

        List<String> splits = new ArrayList<>();
        int end_split = 0;
        for (int j=0; j<valid_plus_aux.size(); j++){
            //Log.i("List", valid_plus_aux.get(j)+"");
            splits.add(str_input.substring(end_split,valid_plus_aux.get(j)));
            end_split = valid_plus_aux.get(j)+1;

            if (j == valid_plus_aux.size()-1 ){
                splits.add(str_input.substring(end_split));
            }
        }

        return splits;
    }

    private List<String> valid_minus(String str_input){
        Integer count_open = 0;
        Integer count_close = 0;

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        List<Integer> valid_minus = new ArrayList<>();
        boolean minus_valid = false;
        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("(")) {
                count_open++;
            } else if (this_char.equals(")")) {
                count_close++;
            }

            if (count_open == 0) {
                minus_valid = true;
                //find a closed parentheses
            }else if(count_open > count_close){
                minus_valid = false;
            }else if (count_open > 0 && count_open.equals(count_close)) {
                minus_valid = true;
            }

            if (this_char.equals("-") && minus_valid){
                valid_minus.add(i);
                count_open = 0;
                count_close = 0;
                minus_valid = false;
            }
        }

        List<String> splits = new ArrayList<>();
        int end_split = 0;
        for (int j=0; j<valid_minus.size(); j++){
            //Log.i("List", valid_minus.get(j)+"");
            splits.add(str_input.substring(end_split,valid_minus.get(j)));
            end_split = valid_minus.get(j)+1;

            if (j == valid_minus.size()-1 ){
                splits.add(str_input.substring(end_split));
            }
        }

        return splits;
    }

    private List<String> valid_division(String str_input){
        Integer count_open = 0;
        Integer count_close = 0;

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        List<Integer> valid_division_aux = new ArrayList<>();
        boolean division_valid = false;
        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("(")) {
                count_open++;
            } else if (this_char.equals(")")) {
                count_close++;
            }

            if (count_open == 0) {
                division_valid = true;
                //find a closed parentheses
            }else if(count_open > count_close){
                division_valid = false;
            }else if (count_open > 0 && count_open.equals(count_close)) {
                division_valid = true;
            }

            if (this_char.equals("÷") && division_valid){
                valid_division_aux.add(i);
                count_open = 0;
                count_close = 0;
                division_valid = false;
            }
        }

        List<String> splits = new ArrayList<>();
        int end_split = 0;
        for (int j=0; j<valid_division_aux.size(); j++){
            //Log.i("List", valid_division_aux.get(j)+"");
            splits.add(str_input.substring(end_split,valid_division_aux.get(j)));
            end_split = valid_division_aux.get(j)+1;

            if (j == valid_division_aux.size()-1 ){
                splits.add(str_input.substring(end_split));
            }
        }

        return splits;
    }

    private List<String> valid_multiplication(String str_input){
        Integer count_open = 0;
        Integer count_close = 0;
        Integer start_open = 0;
        List<String> splits = new ArrayList<>();

        //If this str_input -> √(
        if (str_input.length()>=2){
            if (Character.toString(str_input.charAt(0)).equals("√") && Character.toString(str_input.charAt(1)).equals("(")){
                return splits;
            }
        }

        if (str_input.length()>=3){
            if (Character.toString(str_input.charAt(0)).equals("(") && Character.toString(str_input.charAt(1)).equals("√") && Character.toString(str_input.charAt(2)).equals("(")){
                return splits;
            }
        }

        //add parentheses to sqrt exp
        str_input = this.addParenthesesToSqrt(str_input);

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        List<Integer> valid_multiplication_aux = new ArrayList<>();

        boolean multiplication_valid = false;
        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("(")) {
                if (count_open.equals(0)){
                    start_open = i;
                }
                count_open++;
            } else if (this_char.equals(")")) {
                count_close++;
            }

            if (count_open == 0) {
                multiplication_valid = true;
                //find a closed parentheses
            }else if(count_open > count_close){
                multiplication_valid = false;
            }else if (count_open > 0 && count_open.equals(count_close)) {
                valid_multiplication_aux.add(start_open);
                valid_multiplication_aux.add(i);
                count_open = 0;
                count_close = 0;
                start_open = 0;
                multiplication_valid = true;
            }

            if (this_char.equals("x") && multiplication_valid){
                valid_multiplication_aux.add(i);
                multiplication_valid = false;
            }
        }


        int end_split = 0;
        for (int j=0; j<valid_multiplication_aux.size(); j++){

            splits.add(str_input.substring(end_split,valid_multiplication_aux.get(j)));
            end_split = valid_multiplication_aux.get(j)+1;

            if (j == valid_multiplication_aux.size()-1 ){
                splits.add(str_input.substring(end_split));
            }
        }

        return splits;
    }

    private List<String> valid_pow(String str_input){
        Integer count_open = 0;
        Integer count_close = 0;

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        List<Integer> valid_pow_aux = new ArrayList<>();
        boolean pow_valid = false;
        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("(")) {
                count_open++;
            } else if (this_char.equals(")")) {
                count_close++;
            }

            if (count_open == 0) {
                pow_valid = true;
                //find a closed parentheses
            }else if(count_open > count_close){
                pow_valid = false;
            }else if (count_open > 0 && count_open.equals(count_close)) {
                pow_valid = true;
            }

            if (this_char.equals("^") && pow_valid){
                valid_pow_aux.add(i);
                count_open = 0;
                count_close = 0;
                pow_valid = false;
            }
        }

        List<String> splits = new ArrayList<>();
        int end_split = 0;
        for (int j=0; j<valid_pow_aux.size(); j++){
            //Log.i("List", valid_pow_aux.get(j)+"");
            splits.add(str_input.substring(end_split,valid_pow_aux.get(j)));
            end_split = valid_pow_aux.get(j)+1;

            if (j == valid_pow_aux.size()-1 ){
                splits.add(str_input.substring(end_split));
            }
        }

        return splits;
    }

    private List<String> valid_sqrt(String str_input){

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        List<Integer> valid_sqrt_aux = new ArrayList<>();
        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("√")){
                valid_sqrt_aux.add(i);
            }
        }

        List<String> splits = new ArrayList<>();
        int end_split = 0;
        for (int j=0; j<valid_sqrt_aux.size(); j++){
            splits.add(str_input.substring(end_split,valid_sqrt_aux.get(j)));
            end_split = valid_sqrt_aux.get(j)+1;

            if (j == valid_sqrt_aux.size()-1 ){
                splits.add(str_input.substring(end_split));
            }
        }

        return splits;
    }

    private String addParenthesesToSqrt(String str_input){
        Integer count_open = 0;
        Integer count_close = 0;
        Integer start_open = 0;

        //split string by character
        char[] array_str_input = str_input.toCharArray();

        String result = str_input;

        for (int i=0; i<array_str_input.length; i++) {
            String this_char = Character.toString(array_str_input[i]);
            if (this_char.equals("(")) {
                if (count_open.equals(0)){
                    start_open = i;
                }
                count_open++;
            } else if (this_char.equals(")")) {
                count_close++;
            }

            if (count_open > 0 && count_open.equals(count_close)) {

                if (start_open-1 >=0){
                    if (Character.toString(array_str_input[start_open-1]).equals("√")){
                        String aux = str_input.substring(0,start_open-1)+"("+str_input.substring(start_open-1,i+1)+")";
                        if (i < array_str_input.length){
                            result = aux + this.addParenthesesToSqrt(str_input.substring(i+1));
                        }else{
                            result = aux;
                        }
                        break;
                    }
                }
                count_open = 0;
                count_close = 0;
                start_open = 0;

            }
        }
        return result;
    }

    private String getSign(String sign){
        String result;

        switch (sign){
            case "+":
                result = "add";
                break;
            case "-":
                result = "diff";
                break;
            case "÷":
                result = "quotient";
                break;
            default:
                result = "product";
                break;
            case "√":
                result = "sqrt";
                break;
            case "^":
                result = "pow";
                break;
        }

        return result;

    }

    private Struct processString(String all_string){
        //string process in sign order
        String sign;
        String[] array_all_string = null;
        Struct result = new Struct(null, null, null, null, null);

        if (all_string.contains("+")){
            sign = "add";
            array_all_string = all_string.split("\\+");
        }else if(all_string.contains("-")){
            sign = "diff";
            array_all_string = all_string.split("-");
        }else if(all_string.contains("x")){
            sign = "product";
            array_all_string = all_string.split("x");
        }else if (all_string.contains("÷")){
            sign = "quotient";
            array_all_string = all_string.split("÷");
        }else if(all_string.contains("√")){
            sign = "sqrt";
            array_all_string = all_string.split("√");
            if (array_all_string.length == 0){
                array_all_string[0] = "0";
            }else{

            }
        }else if(all_string.contains("^")){
            sign = "pow";
            array_all_string = all_string.split("\\^");
        }else{
            sign = "add";
            array_all_string = new String[1];
            array_all_string[0] = all_string;
        }

        //length of array_all_string
        Integer length = array_all_string.length;

        //Build struct if the array split has two or more elements
        if (length > 2) {//1+2+3 or 2x3 + 3/2 + 4
            Struct struct_aux = new Struct(null, null, null, null, null);
            for (Integer i=0; i<length; i++) {
                if (i==0){

                    struct_aux.setSign(sign);

                    //verify if this position is digit o expression
                    try {
                        Double number1 = Double.parseDouble(array_all_string[i]);
                        struct_aux.setNumber1(number1);
                    } catch (NumberFormatException nfe){
                        struct_aux.setExp1(this.processString(array_all_string[i]));
                    }

                    //verify if this position is digit o expression
                    try {
                        Double number2 = Double.parseDouble(array_all_string[i+1]);
                        struct_aux.setNumber2(number2);
                    } catch (NumberFormatException nfe){
                        struct_aux.setExp2(this.processString(array_all_string[i+1]));
                    }
                    i++;
                }else{
                    result.setSign(sign);
                    result.setExp1(struct_aux);

                    //verify if this position is digit o expression
                    try {
                        Double number2 = Double.parseDouble(array_all_string[i]);
                        result.setNumber2(number2);
                    } catch (NumberFormatException nfe){
                        result.setExp2(this.processString(array_all_string[i]));
                    }

                    //restart struct_aux for set all content of result
                    struct_aux = new Struct(null, null, null, null, null);
                    struct_aux.setSign(sign);
                    struct_aux.setExp1(result.getExp1());
                    struct_aux.setNumber1(result.getNumber1());
                    struct_aux.setExp2(result.getExp2());
                    struct_aux.setNumber2(result.getNumber2());

                    //if has other iteration, cleaning result struct
                    if (i+1<length) {
                        result = new Struct(null, null, null, null, null);
                    }
                }

            }
        }else if(length == 2){
            if (sign.equals("sqrt")){

                Struct sqrt = new Struct(null, null, null, null, null);
                sqrt.setSign(sign);
                sqrt.setNumber1(2.0);
                try {
                    Double number2 = Double.parseDouble(array_all_string[1]);
                    sqrt.setNumber2(number2);
                } catch (NumberFormatException nfe){
                    sqrt.setExp2(this.processString(array_all_string[1]));
                }

                result.setSign("product");
                try {
                    Double number1 = Double.parseDouble(array_all_string[0]);
                    result.setNumber1(number1);
                } catch (NumberFormatException nfe){
                    result.setExp1(this.processString(array_all_string[0]));
                }
                result.setExp2(sqrt);


            }else{
                result.setSign(sign);
                try {
                    Double number1 = Double.parseDouble(array_all_string[0]);
                    result.setNumber1(number1);
                } catch (NumberFormatException nfe){
                    result.setExp1(this.processString(array_all_string[0]));
                }

                try {
                    Double number2 = Double.parseDouble(array_all_string[1]);
                    result.setNumber2(number2);
                } catch (NumberFormatException nfe){
                    result.setExp2(this.processString(array_all_string[1]));
                }
            }
        }else if(length == 1) {//process only numeric value, eg: 7 or 43 or 10000
            //if is digit
            if (!signs.contains(array_all_string[0])){
                result.setSign(sign);

                try {
                    Double number1 = Double.parseDouble(array_all_string[0]);
                    result.setNumber1(number1);
                } catch (NumberFormatException nfe){
                    result.setExp1(this.processString(array_all_string[0]));
                }

                if (sign.equals("add") || sign.equals("diff")){
                    result.setNumber2(0.0);
                }else if (sign.equals("product") || sign.equals("quotient") || sign.equals("pow")){
                    result.setNumber2(1.0);
                }
            }
        }else{
            result.setSign(sign);

            try {
                Double number1 = Double.parseDouble(array_all_string[0]);
                result.setNumber1(number1);
            } catch (NumberFormatException nfe){
                result.setExp1(this.processString(array_all_string[0]));
            }

            if (sign.equals("add") || sign.equals("diff")){
                result.setNumber2(0.0);
            }else if (sign.equals("product") || sign.equals("quotient") || sign.equals("pow")){
                result.setNumber2(1.0);
            }
        }
        return result;
    }

}
