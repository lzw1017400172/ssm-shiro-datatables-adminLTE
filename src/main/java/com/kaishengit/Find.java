package com.kaishengit;

import java.io.File;

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

    public void deleteFile(File file) {
        if(file.isFile()){
            file.delete();
        }else{
            File[] files = file.listFiles();
            if(files.length==0){//空文件夹
                file.delete();
            } else {
                for(int i = 0;i<files.length;i++){
                    deleteFile(files[i]);//递归所有的文件都做一次这个判断
                }
                //直到这个循环结束，当前文件夹内东西会被删玩。剩下空文件夹也要删除。然后又一级一级网上都是空的，都删
                file.delete();
            }

        }

    }

    //确定是文件夹
    public void delete(File file){

        File[] files = file.listFiles();
        if(files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                } else {
                    delete(files[i]);
                }
            }
            file.delete();
        }else{
            file.delete();
        }

    }



    //二分法,使用递归。原数组不改变，通过改变首位索引来求中间值
    public Integer find2(int low,int high,int b ,int[] a){
        int c = (low+high)/2;
        if(a[c] > b){//b在左，再进行二分。改变尾的索引
            return find2(low,c,b,a);    //如果这里面找到了，就return出去
        } else if(a[c] == b){
            return c;
        } else {//b在右,在进行二分，改变头的索引
           return find2(c,high,b,a);
        }



    }

}
