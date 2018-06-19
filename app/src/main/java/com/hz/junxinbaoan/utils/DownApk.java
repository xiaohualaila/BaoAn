package com.hz.junxinbaoan.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DownApk {
    String vcode;
    DownloadManager downloadManager;
    private ProgressState progressState;
    private String url;
    public void setProgressState(ProgressState progressState){
        this.progressState = progressState;
    }
    private Context context;

    public DownApk(String vcode, Context context, String url) {
        this.vcode = vcode;
        this.context = context;
        this.url = url;
    }

    public void downApk() {
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse( url ) );
        //指定下载路径和下载文件名

        request.setDestinationInExternalPublicDir( "/download/", "baoan" + vcode + ".apk" );
        //获取下载管理器
        downloadManager = (DownloadManager) context.getSystemService( Context.DOWNLOAD_SERVICE );
        //将下载任务加入下载队列，否则不会进行下载
        updateViews( downloadManager.enqueue( request ) );
    }


    private void updateViews(final Long downlaodId) {
        final Timer myTimer = new Timer();
        myTimer.schedule( new TimerTask() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query().setFilterById( downlaodId );
                Cursor cursor = ((DownloadManager) context.getSystemService( Context.DOWNLOAD_SERVICE )).query( query );
                cursor.moveToFirst();
                float bytes_downloaded = cursor.getInt( cursor.getColumnIndex( DownloadManager
                        .COLUMN_BYTES_DOWNLOADED_SO_FAR ) );
                float bytes_total = cursor.getInt( cursor.getColumnIndex( DownloadManager.COLUMN_TOTAL_SIZE_BYTES ) );
                cursor.close();
                final int dl_progress = (int) (bytes_downloaded * 100 / bytes_total);
                Log.i( "czx", "progress:" + dl_progress );
                progressState.setSeek(dl_progress);
                if (dl_progress >= 100) {
                    myTimer.cancel();
                    String path = "/storage/emulated/0/download/" + "baoan" + vcode + ".apk";
                    setPermission(path);
                    openFile(path);


                }
            }
        }, 0, 800 );
    }

    /**
     * 提升读写权限
     * @return
     * @throws IOException
     */
    private void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(String path) {

        File file = new File( path );
        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_VIEW );
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        //判读版本是否在7.0以上
        Uri apkUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            apkUri = FileProvider.getUriForFile( context, "com.hz.junxinbaoan.fileProvider", file );
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(file);

        }
        intent.setDataAndType( apkUri, "application/vnd.android.package-archive" );
        context.startActivity( intent );
    }

    public interface ProgressState{
        void setSeek(int dl_progress);
    }
}
