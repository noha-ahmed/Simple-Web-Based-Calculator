package com.calculator.backend;

public class CalculatorOperations {
    public static String add( String firstOperand , String secondOperand ){
        double firstOp = Double.parseDouble(firstOperand);
        double secondOp = Double.parseDouble(secondOperand);
        return String.valueOf(firstOp + secondOp);
    }

    public static String subtract( String firstOperand , String secondOperand ){
        double firstOp = Double.parseDouble(firstOperand);
        double secondOp = Double.parseDouble(secondOperand);
        return String.valueOf(firstOp - secondOp);
    }

    public static String multiply( String firstOperand , String secondOperand ){
        double firstOp = Double.parseDouble(firstOperand);
        double secondOp = Double.parseDouble(secondOperand);
        return String.valueOf(firstOp*secondOp);
    }

    public static String divide( String firstOperand , String secondOperand ){
        double firstOp = Double.parseDouble(firstOperand);
        double secondOp = Double.parseDouble(secondOperand);

        if( secondOp == 0 )
            return "ERROR";

        return String.valueOf(firstOp/secondOp);
    }

    public static String squareRoot( String operand ){
        double op = Double.parseDouble(operand);
        if(op<0)
            return "ERROR";

        return String.valueOf(Math.sqrt(op));
    }
}
