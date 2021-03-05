package com.gdztyy.incaapi;

public class test {
    public static void main(String[] args) {
       /* int a =1;
        int b=2;
        swap(a,b);
        System.out.print("a="+a +"b="+b);*/

        int x = 5;
        int y = 10;
       /* swap(x,y);
        System.out.println(x);
        System.out.println(y);

        Value v = new Value(5,10);
        swap(v);
        System.out.println(v.x);
        System.out.println(v.y);*/

        Value v1 = new Value(5,10);
        swap(v1);
        System.out.println("v1交换之后的结果为：");
        System.out.println(v1.x);
        System.out.println(v1.y);
    }
    public static void swap(Value v) {
        v.x = v.x + v.y;//15
        v.y = v.x - v.y;//5
        v.x = v.x - v.y;//10
    }
   /* // 无效的交换：形参的改变无法反作用于实参
    public static void swap(int x,int y) {
        int temp = x;
        x = y;
        y = temp;
    }

    // 有效的交换：通过引用（变量指向一个对象）来修改成员变量
    public static void swap(Value value) {
        int temp = value.x;
        value.x = value.y;
        value.y = temp;
    }*/
    /*private static void swap(int a, int b) {
        int temp= a;
        a=b;
        b=temp;
    }*/


 

        // 无效的交换：形参的改变无法反作用于实参
       
    }

    class Value{
        int x;
        int y;

        public Value(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

