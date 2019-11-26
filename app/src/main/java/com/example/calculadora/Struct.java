package com.example.calculadora;

public class Struct {
    private String sign;
    private Struct exp1;
    private Struct exp2;
    private Double number1;
    private Double number2;

    public Struct(String sign, Struct exp1, Double number1, Struct exp2, Double number2){
        this.sign = sign;
        this.exp1 = exp1;
        this.number1 = number1;
        this.exp2 = exp2;
        this.number2 = number2;
    }

    //Gets
    public String getSign(){
        return sign;
    }

    public Struct getExp1(){
        return exp1;
    }

    public Double getNumber1(){
        return number1;
    }

    public Struct getExp2(){return exp2;};

    public Double getNumber2(){return number2;};

    //Sets
    public void setSign(String sign){
        this.sign = sign;
    }

    public void setExp1(Struct exp1){
        this.exp1 = exp1;
    }

    public void setNumber1(Double number1){this.number1 = number1;};

    public void setExp2(Struct exp2){
        this.exp2 = exp2;
    }

    public void setNumber2(Double number2){this.number2 = number2;}

    public String structToString(Struct struct){
        String str_struct = "";
        str_struct += "[";
        str_struct += struct.getSign()+", ";

        //exp1 or number1
        if (struct.getExp1() != null){
            str_struct += this.structToString(struct.getExp1())+", ";
        }else if (struct.getNumber1() != null){
            str_struct += struct.getNumber1()+", ";
        }

        //exp2 or number2
        if (struct.getExp2() != null){
            str_struct += this.structToString(struct.getExp2());
        }else if (struct.getNumber2() != null){
            str_struct += Double.toString(struct.getNumber2());
        }

        str_struct += "]";

        return str_struct;
    }

    public Double calculate(Struct struct){
        Double result = 0.0;

        String sign = struct.getSign();
        Struct exp1 = struct.getExp1();
        Double number1 = struct.getNumber1();
        Struct exp2 = struct.getExp2();
        Double number2 = struct.getNumber2();


        if (sign.equals("add")){
            if (exp1 != null){
                if (exp2 != null){
                    result = this.calculate(exp1)+this.calculate(exp2);
                }else if(number2 != null){
                    result = this.calculate(exp1)+number2;
                }
            }else if (number1 != null){
                if (exp2 != null){
                    result = number1+this.calculate(exp2);
                }else if(number2 != null){
                    result = number1+number2;
                }
            }
        }else if(sign.equals("diff")){
            if (exp1 != null){
                if (exp2 != null){
                    result = this.calculate(exp1)-this.calculate(exp2);
                }else if(number2 != null){
                    result = this.calculate(exp1)-number2;
                }
            }else if (number1 != null){
                if (exp2 != null){
                    result = number1-this.calculate(exp2);
                }else if(number2 != null){
                    result = number1-number2;
                }
            }
        }else if(sign.equals("product")){
            if (exp1 != null){
                if (exp2 != null){
                    result = this.calculate(exp1)*this.calculate(exp2);
                }else if(number2 != null){
                    result = this.calculate(exp1)*number2;
                }
            }else if (number1 != null){
                if (exp2 != null){
                    result = number1*this.calculate(exp2);
                }else if(number2 != null){
                    result = number1*number2;
                }
            }
        }else if(sign.equals("quotient")){
            if (exp1 != null){
                if (exp2 != null){
                    result = this.calculate(exp1)/this.calculate(exp2);
                }else if(number2 != null){
                    result = this.calculate(exp1)/number2;
                }
            }else if (number1 != null){
                if (exp2 != null){
                    result = number1/this.calculate(exp2);
                }else if(number2 != null){
                    result = number1/number2;
                }
            }
        }else if(sign.equals("pow")){
            if (exp1 != null){
                if (exp2 != null){

                    result = Math.pow(this.calculate(exp1), this.calculate(exp2)) ;
                }else if(number2 != null){
                    result = Math.pow(this.calculate(exp1),number2);
                }
            }else if (number1 != null){
                if (exp2 != null){
                    result = Math.pow(number1,this.calculate(exp2));
                }else if(number2 != null){
                    result = Math.pow(number1,number2);
                }
            }
        }else if(sign.equals("sqrt")){
            if (exp2 != null){
                result = Math.sqrt(this.calculate(exp2)) ;
            }else if(number2 != null){
                result = Math.sqrt(number2);
            }
        }
        return result;
    }
}
