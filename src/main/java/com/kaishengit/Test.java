package com.kaishengit;

import java.io.*;

/**
 * Created by 刘忠伟 on 2017/1/22.
 */
public class Test {


    public static void main(String[] args) {



        //文件拷贝。需要缓存区，所以适应bufferFileInputStream

        //后缀stream的全是字节流。以字节为单位
        //后缀是write,read的全是字符流，以字符为单位读取

        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File("D:/x.jpg")));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File("D:/亚淡淡.jpg")));

            byte[] b = new byte[100];
            int len = -1;
            while ((len = inputStream.read(b)) != -1){
                outputStream.write(b,0,len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }











       /* //对于升序数组int a[],编写折半算法找到int b 在数组a中的下标

        //折半算法要b于中间元素比较，所以需要求中间元素。需要变量 int low,int high

        int[] a = {1,2,3,4,5,6,7,8,9,10};
        int b = 8;

        int low = 0;//首位索引默认为0
        int high = a.length -1;

        Find find = new Find();
        System.out.println(find.find2(low,high,b,a));*/






        /*//使用递归实现文件夹的删除
        Find find = new Find();
        File file = new File("F:/d");
        find.deleteFile(file);*/







       /* try {
            //一般使用buffered线程安全的待缓存的复制
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("D:/鸭蛋.png"));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("E:/2.png"));
            *//*FileInputStream inputStream = new FileInputStream("D:/鸭蛋.png");
            FileOutputStream outputStream = new FileOutputStream("E:/1.png");*//*
            byte[] b = new byte[100];
            int len = -1;
            while((len = inputStream.read(b))!= -1){
                outputStream.write(b,0,len);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/



       /*String str = "adclcjhjfcxl";
        //先把字符串转成一个char数组，在进行冒泡排序
        char[] ch = str.toCharArray();
        for(int i= 0;i<ch.length-1;i++){
            for(int j= 0;j<ch.length-1-i;j++){
                if(ch[j]>ch[j+1]){
                    //换位置
                    char a = ch[j];
                    ch[j] = ch[j+1];
                    ch[j+1] = a;
                }
            }
        }
        //变成字符串，StringBUffer连加
        StringBuffer stringBuffer = new StringBuffer("");
        for(int i = 0;i<ch.length;i++){
            stringBuffer.append(ch[i]);
        }
        System.out.println(stringBuffer);


*/

  /*      //实现排序，首先按照字幕排序，在按照每个字母的大写在前，小写在后
        //大写字母ASCII码 65-90，小写 97-122，相差32
        char[] f ={'a','A','h','f','H','F','b','D','E','e','z'};//字符使用单引号，字符串使用双引号
        //按照冒泡排序法，每次排出最后一位的。所以最后一次不需要排，排f.length-1次

        for(int i = 0 ;i<f.length-1;i++){
            //每轮排出来最后一位
            for(int j = 0;j<f.length-1-i;j++){

                //排序规则。
                if(65<=f[j] && f[j]<=90){//左边大写
                    if(97<=f[j+1] && f[j+1]<=122){//右边小写
                        if(f[j]>f[j+1]-32){//换位置
                            char a = f[j];
                            f[j] = f[j+1];
                            f[j+1] = a;
                        } else if(f[j]==f[j+1]-32){//=时，大写在前，也不用换位置

                        }//小于不用换就该在前面
                    } else if (65<=f[j+1] && f[j+1]<=90){//右边大写
                        if(f[j]>f[j+1]){//换位置
                            char a = f[j];
                            f[j] = f[j+1];
                            f[j+1] = a;
                        }//相等和小于都不用换位置了
                    }
                } else if(97<=f[j] && f[j]<=122){//左边小写
                    if(97<=f[j+1] && f[j+1]<=122){//右边小写
                        if(f[j]>f[j+1]){//换位置
                            char a = f[j];
                            f[j] = f[j+1];
                            f[j+1] = a;
                        }//=和小于不用换位置
                    } else if (65<=f[j+1] && f[j+1]<=90){//右边大写
                        if(f[j]-32>f[j+1]){//换位置
                            char a = f[j];
                            f[j] = f[j+1];
                            f[j+1] = a;
                        } else if(f[j]-32==f[j+1]){//=时，大写在前
                            char a = f[j];
                            f[j] = f[j+1];
                            f[j+1] = a;
                        }//小于不用换就该在前面
                    }
                }
            }
        }


        for(int i = 0;i<f.length;i++){
            System.out.println(f[i]);
        }

*/


   /*     //输出乘法表
        for(int i = 1;i<=9;i++){
            for(int j = 1;j<=9;j++){
                if(j<i){
                    System.out.print(j+"*"+i+"="+j*i+",");
                } else if(j==i){
                    System.out.print(j+"*"+i+"="+j*i);
                }
            }
            System.out.println();//作用是换行
        }
*/








/*        //折半查找算法（二分法）。需要参数low首位索引，末尾索引high。可求出中间索引

        //方法一使用循环，比递归的效率高
        int[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        int b = 16;
        Find.find(a,b,0,a.length-1);*/




/*        //定义变量首位索引，末尾索引
        int low = 0;
        int high = a.length - 1;

        //中间位置变量。根据二分之后比较在前半部分或者后半部分。而改变low和high的值，从而改变中间值。
        //并且low一直增大，high一直减小。知道相等时，没找到就说明不存在，返回-1

        while(low <= high) {
            int middle = (low + high) / 2;
            if(a[middle] == b){
                //找到了
                System.out.print(middle);
                break;
            } else if(a[middle]>b){
                //说明在前半部分，改变high变小
                high = middle -1;
            } else{
                low = middle +1;
            }
        }*/














    }

}
