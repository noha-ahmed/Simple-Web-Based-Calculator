package com.calculator.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RequestMapping
@CrossOrigin
@RestController
public class CalculatorController {

    public static void main(String[] args) {
        SpringApplication.run(CalculatorController.class, args);
    }

    @GetMapping("/calculateBinOp")
    public String calculate(@RequestParam(name="firstOperand") String firstOperand ,@RequestParam(name="operator") char operator ,@RequestParam( name="secondOperand") String secondOperand ){
        String result="";
        switch(operator){
            case '+':
                result = CalculatorOperations.add(firstOperand,secondOperand);
                break;
            case '-':
                result = CalculatorOperations.subtract(firstOperand,secondOperand);
                break;
            case '×':
                result = CalculatorOperations.multiply(firstOperand,secondOperand);
                break;
            case '÷':
                result = CalculatorOperations.divide(firstOperand,secondOperand);
                break;
        }
        return result;
    }

    @GetMapping("/calculateUniOp")
    public String calculate(@RequestParam(name="operand") String operand ,@RequestParam(name="operator") char operator ){
        String result="";
        switch(operator){
            case 'i': // inverse operation
                result = CalculatorOperations.divide("1",operand);
                break;
            case 's': //square operation
                result = CalculatorOperations.multiply(operand,operand);
                break;
            case 'r': //root operation
                result = CalculatorOperations.squareRoot(operand);
                break;
            case 'p'://percent operation
                result = CalculatorOperations.divide(operand,"100");
                break;
        }
        return result;
    }

}
