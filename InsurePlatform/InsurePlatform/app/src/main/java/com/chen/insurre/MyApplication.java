package com.chen.insurre;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.chen.insurre.adapter.ItemAdapter;
import com.chen.insurre.adapter.ProvinceAdapter;
import com.chen.insurre.bean.ItemInfo;
import com.chen.insurre.bean.ParamInfo;
import com.chen.insurre.bean.ProvinceInfo;
import com.chen.insurre.bean.ResultInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by hm-soft on 2015/8/11.
 */
public class MyApplication extends Application{
    private static MyApplication mInstance = null;
    private List<ItemInfo> caijiList;
    private List<ItemInfo> reasonList;
    private List<ProvinceInfo> provsList;
    private List<ItemInfo> stateList;
    private List<ItemInfo> canbaoList;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        readJsonDate();
    }

    private void readJsonDate() {
        if(!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){//SD卡不存在则不操作
            return;//返回到程序的被调用处
        }
        File fileDir=new File(mInstance.getExternalFilesDir(null)+"/json");
        File file=new File(fileDir,"json.txt");
        if(file.exists()){

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String readline = "";
                StringBuffer sb = new StringBuffer();
                while ((readline = br.readLine()) != null) {
                    sb.append(readline);
                }
                br.close();
                ResultInfo resultInfo = new Gson().fromJson(sb.toString(), new TypeToken<ResultInfo<ParamInfo>>() {
                }.getType());
                if (resultInfo != null && resultInfo.getResult() != null
                        && resultInfo.getResult().equals("0")) {
                    ParamInfo paramInfo = (ParamInfo) resultInfo.getBean();
                    caijiList = paramInfo.getCaiji();
                    reasonList = paramInfo.getReason();
                    provsList = paramInfo.getProvs();
                    stateList = paramInfo.getState();
                    canbaoList = paramInfo.getCanbao();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       

    }


    public void saveJsonDate(String result){
        if(!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){//SD卡不存在则不操作
            return;//返回到程序的被调用处
        }
        File fileDir=new File(mInstance.getExternalFilesDir(null)+"/json");
        if(!fileDir.exists()){
            fileDir.mkdir();
        }

        File file=new File(fileDir,"json.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            out.print(result);//将数据变为字符串后保存
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(out!=null){
                out.close();//关闭输出
            }
        }

    }


    public String getAppDir() {
        if(!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){//SD卡不存在则不操作
            return "";//返回到程序的被调用处
        }
        File fileDir=new File(mInstance.getExternalFilesDir(null)+"/json");
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        return fileDir.getAbsolutePath();
    }

    //采集
    public void setCaijiList(List<ItemInfo> caijiList){
        this.caijiList=caijiList;
    }

    public List<ItemInfo> getCaijiList(){
       return caijiList;
    }

    //原因
    public void setReasonList(List<ItemInfo> reasonList){
        this.reasonList=reasonList;
    }

    public List<ItemInfo> getReasonList(){
        return reasonList;
    }

    //状态
    public void setStateList(List<ItemInfo> stateList){
        this.stateList=stateList;
    }

    public List<ItemInfo> getStateList(){
        return stateList;
    }

    //参保
    public void setCanbaoList(List<ItemInfo> canbaoList){
        this.canbaoList=canbaoList;
    }

    public List<ItemInfo> getCanbaoList(){
        return canbaoList;
    }

    //省市
    public void setProvsList(List<ProvinceInfo> provsList){
        this.provsList=provsList;
    }

    public List<ProvinceInfo> getProvsList(){
        return provsList;
    }


}
