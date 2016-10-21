package edu.feicui.news.news.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Created by Administrator on 2016/9/22.
 */
public class NetAsync extends AsyncTask<Request,Object,Response>{

    ProgressDialog mDialog;
    HttpURLConnection httpConnection=null;
    OnResultFinishListener mListener;
    public NetAsync(Context context,OnResultFinishListener mListener){
        mDialog=ProgressDialog.show(context,"","加载中...");//在FindPasswordFrgment里面没有初始化activity 结果这里报错了，是因为这里用到了context
        this.mListener=mListener;

    }
    @Override
    protected Response doInBackground(Request... params) {
        Response response=new Response();
        Request request=params[0];
        try {
            URL url=new URL(request.url);//实例化URL
            Log.e("aaa", "doInBackground: request.url:"+request.url);
            httpConnection= (HttpURLConnection) url.openConnection();//打开连接
            httpConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);//设置超时的
            httpConnection.setReadTimeout(Constant.READ_TIMEOUT);//设置读取超时的事件
            if (request.type==Constant.GET){//get判断是何种方式的请求
                httpConnection.setRequestMethod("GET");
            }else{
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoOutput(true);
                OutputStream out=httpConnection.getOutputStream();
                out.write(Utils.getUrl(request.params,Constant.POST).getBytes());
            }
            int code=httpConnection.getResponseCode();
            response.code=code;
            if (code==HttpURLConnection.HTTP_OK){
                InputStream in=httpConnection.getInputStream();
                byte[] bytes=new byte[1024];
                int len;
                StringBuffer buffer=new StringBuffer();
                while ((len=in.read(bytes))!=-1){
                    buffer.append(new String(bytes,0,len));
                }
               //拿到了结果
                response.result=buffer.toString();
                Log.e("aaa", "doInBackground: response.result:"+response.result);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpConnection!=null){
                httpConnection.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response o) {
        super.onPostExecute(o);
        //拿到结果
        mDialog.dismiss();
        Response response=o;
        if (o.code!=HttpURLConnection.HTTP_OK){//失败
            mListener.failed(response);
        }else{//成功
            mListener.success(response);
        }
    }
}
//code   Object