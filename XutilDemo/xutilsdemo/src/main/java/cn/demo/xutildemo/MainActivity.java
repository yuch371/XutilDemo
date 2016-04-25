package cn.demo.xutildemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.Date;
import java.util.List;

import cn.demo.xutildemo.db.Parent;

public class MainActivity extends AppCompatActivity {
    DbManager.DaoConfig mDaoConfig;
    TextView mTextView;
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFind();
        initXUTIL();
        initDBData();
    }

    private void initXUTIL() {
        //初始化，建议放到Application
        org.xutils.x.Ext.init(getApplication());
        org.xutils.x.Ext.setDebug(true); // 是否输出debug日志
    }

    private void initDBData() {
        //初始化数据库
        DbManager.DaoConfig daoConfig=new DbManager.DaoConfig();
        daoConfig.setDbName("demodb").setDbVersion(1).setDbUpgradeListener(
                new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //db.addColumn();
                        //db.dropDb();
                    }
                });
        //初始化
    }

    private void initFind() {
        mTextView= (TextView) findViewById(R.id.textView2);
        mImageView= (ImageView) findViewById(R.id.imageView1);
    }

    public void button1XClick(View v){
        int id =v.getId();
        switch (id){
            case R.id.button11:
                doDBAdd();
                break;
            case R.id.button12:
                doDBDelete();
                break;
            case R.id.button13:
                doDBUpdate();
                break;
            case R.id.button14:
                doDBSelect();
                break;
        }
    }
    public void button2XClick(View v){
        int id =v.getId();
        switch (id){
            case R.id.button21:
                doNetText();
                break;
            case R.id.button22:
                doNetJson();
                break;
            case R.id.button23:
                doNetPhotoDown();
                break;
            case R.id.button24:
                doNetNhotoBind();
                break;
        }
    }
    //-DB部分--------------------------------------------------
    private void doDBUpdate() {
        //先查询出当前的记录集，然后修改第一个和最后一个
        DbManager db = org.xutils.x.getDb(mDaoConfig);
        Parent parent = null;
        String text="";
        try {
            //查询部分符合条件的记录=》的第一行
            //parent = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).findFirst();
            //if (parent != null) {
            //    text = parent.toString();
            //}
            //查找所有记录数据
            List<Parent> parentList = db.selector(Parent.class).findAll();
            text += "parentList size:" + parentList.size() + "\n";
            if (parentList.size() > 0) {
                text += "first row:id=" + parentList.get(0).getId() + ",name="+parentList.get(0).name+"\n";
                text += "last row:id=" + parentList.get(parentList.size()-1).getId() + ",name="+parentList.get(parentList.size()-1).name+"\n";
                //找出第一个,修改用户名
                parent= parentList.get(0);
                parent.name = "修改" + System.currentTimeMillis();
                db.update(parent);
                //找出最后一个,修改用户名
                parent=parentList.get(parentList.size()-1);
                parent.name = "修改" + System.currentTimeMillis();
                db.update(parent);
            }
            //重新查询并显示
            parentList = db.selector(Parent.class).findAll();
            text += "parentList size:" + parentList.size() + "\n";
            if (parentList.size() > 0) {
                text += "first row:id=" + parentList.get(0).getId() + ",name="+parentList.get(0).name+"\n";
                text += "last row:id=" + parentList.get(parentList.size()-1).getId() + ",name="+parentList.get(parentList.size()-1).name+"\n";
            }

        } catch (DbException e) {
            text=e.getMessage();
            e.printStackTrace();
        }
        //Parent test = db.selector(Parent.class).where("id", "between", new String[]{"1", "5"}).findFirst();
        mTextView.setText(text);
        Toast.makeText(MainActivity.this, "selector：" + text, Toast.LENGTH_SHORT).show();

    }

    private void doDBDelete() {
        //先查询出第一个，然后删除第一个
        DbManager db = org.xutils.x.getDb(mDaoConfig);
        Parent parent = null;
        String text="";
        try {
            //查询部分符合条件的记录=》的第一行
            //parent = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).findFirst();
            //if (parent != null) {
            //    text = parent.toString();
            //}
            //查找所有记录数据
            List<Parent> parentList = db.selector(Parent.class).findAll();
            text += "parentList size:" + parentList.size() + "\n";
            if (parentList.size() > 0) {
                text += "first row:id=" + parentList.get(0).getId() + ",name="+parentList.get(0).name+"\n";
                text += "last row:id=" + parentList.get(parentList.size()-1).getId() + ",name="+parentList.get(parentList.size()-1).name+"\n";
                //找出第一个
                parent= parentList.get(0);
                //删除第一个
                db.delete(parent);
            }
            //重新查询并显示
            parentList = db.selector(Parent.class).findAll();
            text += "parentList size:" + parentList.size() + "\n";
            if (parentList.size() > 0) {
                text += "first row:id=" + parentList.get(0).getId() + ",name="+parentList.get(0).name+"\n";
                text += "last row:id=" + parentList.get(parentList.size()-1).getId() + ",name="+parentList.get(parentList.size()-1).name+"\n";
            }

        } catch (DbException e) {
            text=e.getMessage();
            e.printStackTrace();
        }
        //Parent test = db.selector(Parent.class).where("id", "between", new String[]{"1", "5"}).findFirst();
        mTextView.setText(text);
        Toast.makeText(MainActivity.this, "selector："+text, Toast.LENGTH_SHORT).show();
    }

    private void doDBAdd() {
        DbManager db = org.xutils.x.getDb(mDaoConfig);
        Parent parent = new Parent();
        parent.name = "添加" + System.currentTimeMillis();
        parent.setAdmin(true);
        parent.setEmail("wyouflf@qq.com");
        parent.setTime(new Date());
        parent.setDate(new java.sql.Date(new Date().getTime()));
        String text="";
        try {
            //记录中的普通数据保存
            //db.save(parent);
            //记录中的普通数据保存，并把保存后数据库中记录的ID同步到类实例中
            db.saveBindingId(parent);
            text="OK :"+parent.toString();
        } catch (DbException e) {
            text="Error :"+e.getMessage();
            e.printStackTrace();
        }
        //db.saveBindingId(child);//保存对象关联数据库生成的id
        mTextView.setText(text);
        Toast.makeText(MainActivity.this,"Save "+ text, Toast.LENGTH_SHORT).show();
    }

    private void doDBSelect() {
        DbManager db = org.xutils.x.getDb(mDaoConfig);
        Parent parent = null;
        String text="";
        try {
            //查询部分符合条件的记录=》的第一行
            //parent = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).findFirst();
            //if (parent != null) {
            //    text = parent.toString();
            //}
            //查找所有记录数据
            List<Parent> parentList = db.selector(Parent.class).findAll();
            text += "parentList size:" + parentList.size() + "\n";
            if (parentList.size() > 0) {
                text += "first row:id=" + parentList.get(0).getId() + ",name="+parentList.get(0).name+"\n";
                text += "last row:id=" + parentList.get(parentList.size()-1).getId() + ",name="+parentList.get(parentList.size()-1).name+"\n";
            }
        } catch (DbException e) {
            text=e.getMessage();
            e.printStackTrace();
        }
        //Parent test = db.selector(Parent.class).where("id", "between", new String[]{"1", "5"}).findFirst();
        mTextView.setText(text);
        Toast.makeText(MainActivity.this, "selector："+text, Toast.LENGTH_SHORT).show();

    }
    //--Net部分---------------------------------------------
    private void doNetNhotoBind() {
        mImageView.setImageResource(R.mipmap.ic_launcher);
        String url="http://p2.so.qhimg.com/t013b296393c3eef447.jpg";
        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                .setImageScaleType(ImageView.ScaleType.MATRIX).build();

        x.image().bind(mImageView,url, imageOptions);
        // assets file
        //x.image().bind(iv_big_img, "assets://test.gif", imageOptions);

        // local file
        //x.image().bind(iv_big_img, new File("/sdcard/test.gif").toURI().toString(), imageOptions);
        //x.image().bind(iv_big_img, "/sdcard/test.gif", imageOptions);
        //x.image().bind(iv_big_img, "file:///sdcard/test.gif", imageOptions);
        //x.image().bind(iv_big_img, "file:/sdcard/test.gif", imageOptions);
    }


    private void doNetPhotoDown() {
        mImageView.setImageResource(R.mipmap.ic_launcher);
        String url="http://p0.so.qhimg.com/t01f2fd6562028caae0.jpg";

        RequestParams params=new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<byte[]>() {

            @Override
            public void onSuccess(byte[] result) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "onError：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void doNetJson() {
    }

    private void doNetText() {
        RequestParams requestParams=new RequestParams("http://www.baidu.com");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                // 得到缓存数据
                //
                // * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
                //   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
                //   逻辑, 那么xUtils将请求新数据, 来覆盖它.
                //
                // * 如果信任该缓存返回 true, 将不再请求网络;
                //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
                //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
                //
                //this.result = result;
                return false; // true: 信任缓存数据; false不信任缓存数据
            }

            @Override
            public void onSuccess(String result) {
                Log.d("doNetText", "onSuccess=>result=" + result);
                mTextView.setText(result);
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    // ...
                } else { // 其他错误
                    // ...
                }
                Log.d("doNetText","onError=>"+ ex.getMessage());
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), cex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
        /*
        RequestParams params = new RequestParams("https://www.baidu.com/s");
        params.setSslSocketFactory(...); // 设置ssl
        params.addQueryStringParameter("wd", "xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
       */
    }

    private void doBitmapUtil(){
        /**
         * @param context 上下文
          * @param diskCachePath  磁盘高速缓存路径
          * @param memoryCacheSize 内存缓存大小
          * @param diskCacheSize 磁盘缓存空间大小
         */
        //x.task();
        //BitmapUtils bitmapUtils=new    ;
        //BitmapUtils(Context  context, String  diskCachePath,
        //int  memoryCacheSize, int  diskCacheSize)
    }
}

