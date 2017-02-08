package com.kaishengit;

/**
 * Created by 刘忠伟 on 2017/1/22.
 */
public class Find {


    //使用递归法，写二分算法
    //仍然需要要两个参数，首位索引low，末位索引high
    //初始条件low=0，high=a.length-1;
    public static int find(int[] a,int b,int low,int high){

        int middle = (low+high)/2;
        //low增，high减，当两个值相当还没找到，就不再递归，返回-1
        if(low<=high){
            if(a[middle] == b){
                //找到了就返回。不需要递归
                return middle;
            } else if(a[middle] > b){
                //说明在前半部分，改变high
                return find(a,b,low,middle-1);
            } else {
                //说明在后半部分，改变low
                return find(a,b,middle+1,high);
            }

        }else{
            return  -1;
        }

    }

}
