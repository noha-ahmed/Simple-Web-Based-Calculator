<template>
  <div class="calculator" id="app">
    <div class='calculator-screen'>
      <div id='history' class='scroll'>{{ history || 0 }}</div>
      <div id='output'>{{ output }}</div>
    </div>

    <div class='calculator-keys' >
      <button class='operator' @click="percentage" :disabled='error' id='%'>%</button>
      <button class='operator' @click="clear" id='CE'>CE</button>
      <button class='operator' @click="clear" id='C'>C</button>
      <button class='operator' :disabled='error' @click="del" id='del'>del</button>
    </div>
    <div class='calculator-keys' >
      <button class='operator' :disabled='error' @click="inverse" id='1/x'>1/x</button>
      <button class='operator' :disabled='error' @click="square" id='power-2'>x&sup2;</button>
      <button class='operator' :disabled='error' @click="squareRoot" id='root-2'>&sup2;&#8730; x</button>
      <button class='operator' :disabled='error' @click="appendOperator('&divide;')" id='/'>&divide;</button>
    </div>
    <div class='calculator-keys' >
      <button class='number' @click="append('7')" id='7'>7</button>
      <button class='number' @click="append('8')"  id='8'>8</button>
      <button class='number' @click="append('9')"  id='9'>9</button>
      <button class='operator' :disabled='error' @click="appendOperator('&times;')" id='x'>&times;</button>
    </div>
    <div class='calculator-keys' >
      <button class='number' @click="append('4')" id='4'>4</button>
      <button class='number' @click="append('5')" id='5'>5</button>
      <button class='number' @click="append('6')"  id='6'>6</button>
      <button class='operator' :disabled='error' @click="appendOperator('-')" id='-'>&minus;</button>
    </div>
    <div class='calculator-keys' >
      <button class='number' @click="append('1')" id='1'>1</button>
      <button class='number' @click="append('2')" id='2'>2</button>
      <button class='number' @click="append('3')" id='3'>3</button>
      <button class='operator' :disabled='error' @click="appendOperator('&plus;')" id='+'>&plus;</button>
    </div>
    <div class='calculator-keys' >
      <button class='number' @click='negate' :disabled='error' id='+/-'>+/-</button>
      <button class='number' @click="append('0')" id='0'>0</button>
      <button class='number' :disabled='error' @click="appendDot" id='.'>.</button>
      <button class='equal-sign' :disabled='error || isExpressionEvaluated' @click="equal" id='='>=</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return{
      history: '',
      output: '0',
      currentOperand: '0',
      firstOperand:'0',
      secondOperand:'0',
      operator:'',
      decimal: false,
      operatorExistlast: false,
      error:false,
      isExpressionEvaluated:false,
      unaryOperand:"",
      unaryOperator:""
    }
  },
  methods: {
    append(num){
      if( this.error || (this.isExpressionEvaluated && this.unaryOperand === "" )){
        console.log("dd");

        this.clear();
      }
      if( this.unaryOperand !== ""){
        this.deleteUnaryTerm();
        this.currentOperand = '0';
        this.unaryOperand = "";
      }
      if ( this.currentOperand !== '0'  ){
        this.currentOperand += num;
      }
      else{
        this.currentOperand = num;
      }
      this.output = this.currentOperand;
      this.operatorExistlast = false;

    },
    appendDot(){
      if(this.isExpressionEvaluated){
        this.clear();
      }
      if( this.unaryOperand !== ""){
        this.deleteUnaryTerm();
        this.currentOperand = '0';
        this.unaryOperand = "";
      }
      if(!this.decimal){
        this.currentOperand+= '.';
        this.decimal = true;
        this.output = this.currentOperand;
        this.operatorExistlast = false;
      }
    },

    appendOperator(op){

      if( this.isExpressionEvaluated && this.unaryOperand === ""){
        this.history = this.firstOperand + " ";
        this.isExpressionEvaluated = false;
        this.currentOperand ='0';
        this.decimal=false;
      }
      else if( this.operatorExistlast  ){
        this.history = this.history.slice(0,-1);
      }
      else if( this.operator === '' ){

        this.firstOperand = this.currentOperand;
        if(this.unaryOperand === ""){
          this.history+= " " + this.currentOperand + " ";
          if( this.currentOperand.charAt(this.currentOperand.length-1) ==='.' ){
            this.currentOperand = this.currentOperand.slice(0,-1);
          }
        }
        else{
          this.unaryOperand = "";
          this.history+=" ";
        }
        this.currentOperand ='0';
        this.decimal=false;
      }
      else{
        this.secondOperand = this.currentOperand;
        if(this.unaryOperand === ""){
          this.history+= " " + this.currentOperand + " ";
          if( this.currentOperand.charAt(this.currentOperand.length-1)==='.' ){
            this.currentOperand = this.currentOperand.slice(0,-1);
          }
        }
        else{
          this.unaryOperand = "";
          this.history+=" ";
        }
        this.evaluateBiOperation();
        this.currentOperand ='0';
        this.decimal=false;
      }
      this.operator = op;
      this.history += op;
      this.operatorExistlast = true;
      this.isExpressionEvaluated = false;
      this.unaryOperand ="";
    },

    clear(){
      this.output = '0';
      this.history = '';
      this.currentOperand = '0';
      this.decimal = false;
      this.operatorExistlast= false;
      this.operator = '';
      this.error = false;
      this.isExpressionEvaluated = false;
      this.unaryOperand = "";
    },
    del(){
      if(this.isExpressionEvaluated){
        this.history = '';
        return;
      }
      if( this.operatorExistlast ){
        return ;
      }
      if( this.currentOperand.length === 1 ){
        this.currentOperand = '0';
      }
      else{
        if(this.currentOperand.charAt(this.currentOperand.length - 1) === '.'){
          this.decimal = false;
        }
        this.currentOperand = this.currentOperand.slice(0,-1);
      }
      this.output = this.currentOperand;
    },
    percentage(){
      this.unaryOperator = 'p';
      this.evaluateUniOperation();
    },
    inverse(){
      this.operatorExistlast = false;
      this.currentOperand = this.output;
      if( this.unaryOperand === "" ){
        this.unaryOperand = "1/( "+ this.currentOperand + " )";
      }
      else{
        this.deleteUnaryTerm();
        this.unaryOperand = "1/( "+ this.unaryOperand + " )";
      }
      this.history += " " + this.unaryOperand;
      this.unaryOperator = 'i';
      this.evaluateUniOperation();
    },
    square(){
      this.operatorExistlast = false;
      this.currentOperand = this.output;
      if( this.unaryOperand === "" ){
        this.unaryOperand = "sqr( "+ this.currentOperand + " )";
      }
      else{
        this.deleteUnaryTerm();
        this.unaryOperand = "sqr( "+ this.unaryOperand + " )";
      }
      this.history += " " + this.unaryOperand;
      this.unaryOperator = 's';
      this.evaluateUniOperation();
    },
    squareRoot(){
      this.operatorExistlast = false;
      this.currentOperand = this.output;
      if( this.unaryOperand === "" ){
        this.unaryOperand = '√( '+ this.currentOperand + " )";
      }
      else{
        this.deleteUnaryTerm();
        this.unaryOperand = '√( '+ this.unaryOperand + " )";
      }
      this.history += " " + this.unaryOperand;
      this.unaryOperator = 'r';
      this.evaluateUniOperation();
    },
    negate(){
      this.operatorExistlast = false;
      if( this.unaryOperand !== "" ){
        this.deleteUnaryTerm();
        if(this.unaryOperand.charAt(0)==='-'){
          this.unaryOperand = this.unaryOperand.slice(1);
        }
        else{
          this.unaryOperand = "-" + this.unaryOperand;
        }
        this.history += " " +this.unaryOperand;
      }
      this.currentOperand = this.output;
      let temp;
      temp = parseFloat(this.currentOperand)*-1;
      this.currentOperand = temp.toString();
      this.output = this.currentOperand;



    },
    equal(){

      if( this.operator === '' ){
        this.operatorExistlast = true;
        if( this.currentOperand !== this.firstOperand ){
            this.firstOperand = this.currentOperand;
            this.history = this.currentOperand + " =";
            this.currentOperand = "0";
        }
      }
      else{
        if(this.unaryOperand !== ""){
          this.secondOperand = this.currentOperand;
          this.history += " =";
          this.unaryOperand = "";
        }
        else{
          if( this.operatorExistlast ){
            this.secondOperand = this.firstOperand;
          }
          else{
            this.secondOperand = this.currentOperand;
          }
          this.history+= " " + this.secondOperand + " =";
        }
        this.evaluateBiOperation();
      }
      this.isExpressionEvaluated = true;
    },
    evaluateBiOperation(){
      axios.get('http://localhost:8085/calculateBinOp',{params: {
          firstOperand: this.firstOperand,
          operator:this.operator,
          secondOperand: this.secondOperand,
        }})
          .then(function (response) {
            if(response.data.toString() === "ERROR"){
              this.error = true;
              this.output = 'ERROR';
            }
            else{
              this.firstOperand = response.data.toString();
              this.output = this.firstOperand;
            }
          }.bind(this))
    },
    evaluateUniOperation(){
      console.log(this.currentOperand);
      console.log(this.unaryOperator);
      axios.get('http://localhost:8085/calculateUniOp',{params: {
          operand: this.currentOperand,
          operator:this.unaryOperator,
        }})
          .then(function (response) {
            if(response.data.toString() === "ERROR"){
              this.error = true;
              this.output='ERROR';
            }
            else{
              this.currentOperand = response.data.toString();
              this.output = this.currentOperand;

            }
          }.bind(this))
    },
    deleteUnaryTerm(){
      this.history = this.history.slice(0,-1*this.unaryOperand.length);
    }



  }
}
</script>

<style>
.calculator{
  text-align: right;
  width: 285px;
  background-color: #fffafab6;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid rgb(204, 204, 204);
  padding: 10px;
  align-items: center;
}
.calculator-screen{
  width: 100%;
  height: 120px;
  text-align: right;
  font-size: 36px;
  border: none;
  overflow: auto;
  box-sizing: border-box;
}
div.scroll{

}
#history{
  margin-top:10px;
  color: rgb(138, 138, 138);
  font-size: 20px;
}
#output{
  margin-top:20px;
}
.calculator-keys{
  width: 100%;
}



button{
  margin:0.3px;
  height: 55px;
  background-color: rgb(255, 250, 250);
  border: 1px solid rgb(204, 204, 204);
  font-size: 20px;
  color: rgb(0, 0, 0);
  width: 70px;
  outline: none;

}



.operator{
  background-color: rgba(228, 228, 228, 0.486);
}
button:hover{
  background-color: darkgray;
}
button:active{
  background-color: rgb(214, 208, 208);
}
.equal-sign{
  background-color: rgb(165, 206, 223);

}
.equal-sign:hover {

}
button:disabled,
button[disabled]{
  border: 1px solid #d4d4d4;
  background-color: #cccccc;
  color: #c1bebe;

}
</style>
