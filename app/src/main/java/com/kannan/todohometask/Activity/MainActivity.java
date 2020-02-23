package com.kannan.todohometask.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kannan.todohometask.Model.DataModel;
import com.kannan.todohometask.Utils.DatabaseHandler;
import com.kannan.todohometask.Utils.Global;
import com.kannan.todohometask.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RelativeLayout notask;
    MyGridAdapter myGridAdapter;
    GridView gridView;
    boolean doubleBackToExitPressedOnce = false;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        notask = findViewById(R.id.notask);
        gridView = findViewById(R.id.gridview1);
        FloatingActionButton addnew = findViewById(R.id.fab);
        final Target viewTarget = new ViewTarget(R.id.rel, this);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(getApplicationContext());

        if (db.getcount() > 0) {
            notask.setVisibility(View.GONE);

            DisplayTodo(db.getAll());
        }


        final TextPaint paint = new TextPaint(Paint.DEV_KERN_TEXT_FLAG);
        paint.setTextSize(getResources().getDimension(R.dimen.abc_text_size_headline_material));
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf"));

        final ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                .setTarget(viewTarget)
                .setContentText("Click on the 'Add new' to Add new Todo !!!")
                .setContentTextPaint(paint)
                .setShowcaseDrawer(new CustomShowcaseView(getResources()))
                .blockAllTouches()
                .singleShot(42)
                .hideOnTouchOutside()
                .build();
        showcaseView.setShowcase(viewTarget,true);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.setMargins(30,30,30,30);
        showcaseView.setButtonPosition(lps);
        showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_CENTER);
        showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_CENTER);

        showcaseView.overrideButtonClick(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(showcaseView.isShowing()) {
                    showcaseView.hide();

                }
            }
        });
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.Flag="N";
                Intent newtask = new Intent(MainActivity.this, AddTask.class);
                startActivity(newtask);
            }
        });
    }

    private void DisplayTodo(String array)
    {
        JSONArray Jarray = null;
        try {
            Jarray = new JSONArray(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("jarray", Jarray.toString());

        ArrayList<DataModel> imageObjects = new ArrayList<>();
        for (int count = 0; count < Jarray.length(); count++) {
            JSONObject obj = null;
            try {
                obj = Jarray.getJSONObject(count);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            imageObjects.add(new DataModel(obj));

        }
        myGridAdapter = new MyGridAdapter(MainActivity.this, imageObjects);
        gridView.setAdapter(myGridAdapter);
    }


        public class MyGridAdapter extends BaseAdapter
        {
            private Context context;
            private ArrayList<DataModel> imageObjects;
            String yourCuttedString;
            String[] a;
            String b;
            private LayoutInflater mLayoutInflate;


            public MyGridAdapter(Context context, ArrayList<DataModel> imageObjects) {
                this.context = context;
                this.imageObjects = imageObjects;
                this.mLayoutInflate = LayoutInflater.from(context);
            }

            public int getCount() {
                if (imageObjects != null) return imageObjects.size();
                return 0;
            }

            @Override
            public Object getItem(int position) {
                if (imageObjects != null && imageObjects.size() > position)
                    return imageObjects.get(position);
                return null;
            }

            @Override
            public long getItemId(int position)
            {
                return 0;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = mLayoutInflate.inflate(R.layout.data_adapter, parent,
                            false);

                    viewHolder.id = (TextView) convertView.findViewById(R.id.ID);
                    viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                    viewHolder.data = (TextView) convertView.findViewById(R.id.data);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                    viewHolder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                final DataModel imageObject = (DataModel) getItem(position);

                viewHolder.date.setText(imageObject.getdate());
                viewHolder.time.setText(imageObject.gettime());
                viewHolder.data.setText(imageObject.getdata());
                viewHolder.id.setText(imageObject.getid());

                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Global.Flag="O";
                        Intent edittask = new Intent(MainActivity.this, AddTask.class);
                        edittask.putExtra("Data",imageObject.getdata());
                        edittask.putExtra("Date",imageObject.getdate());
                        edittask.putExtra("ID",imageObject.getid());
                        edittask.putExtra("Time",imageObject.gettime());
                        startActivity(edittask);
                    }
                });
                return convertView;
            }

            private class ViewHolder
            {
                public TextView id;
                public TextView date;
                public TextView time;
                public TextView data;
                public RelativeLayout rel;
            }
        }
    private static class CustomShowcaseView implements ShowcaseDrawer
    {

        private final float width;
        private final float height;
        private final Paint eraserPaint;
        private final Paint basicPaint;
        private final int eraseColour;
        private final RectF renderRect;
        private float mRadius;
        public CustomShowcaseView(Resources resources) {
            width = resources.getDimension(R.dimen.custom_showcase_width);
            height = resources.getDimension(R.dimen.custom_showcase_height);
            PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
            eraserPaint = new Paint();
            eraserPaint.setColor(0xFFFFFF);
            eraserPaint.setAlpha(0);
            eraserPaint.setXfermode(xfermode);
            eraserPaint.setAntiAlias(true);
            eraseColour = resources.getColor(R.color.custom_showcase_bg);
            basicPaint = new Paint();
            renderRect = new RectF();
        }

        @Override
        public void setShowcaseColour(int color) {

        }

        @Override
        public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier) {
            Canvas bufferCanvas = new Canvas(buffer);
            renderRect.left = x - width / 2f;
            renderRect.right = x + width / 2f;
            renderRect.top = y - height / 2f;
            renderRect.bottom = y + height / 2f;
            bufferCanvas.drawRect(renderRect, eraserPaint);

        }

        @Override
        public int getShowcaseWidth() {
            return (int) width;
        }

        @Override
        public int getShowcaseHeight() {
            return (int) height;
        }

        @Override
        public float getBlockedRadius() {
            return width;
        }

        @Override
        public void setBackgroundColour(int backgroundColor) {
            // No-op, remove this from the API?
        }

        @Override
        public void erase(Bitmap bitmapBuffer) {
            bitmapBuffer.eraseColor(eraseColour);
        }

        @Override
        public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
            canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
        }

    }
   @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    }
