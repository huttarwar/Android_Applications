package com.heramb.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.text.method.ScrollingMovementMethod;


public class ArticleInfo extends Fragment {
    private static final String TAG = "ArticleInfo";

    TextView headline;
    TextView date;
    TextView author;
    TextView content;
    ImageView photo;
    TextView count;
    Article article;
    int displayCount;
    View v;

    public static final String ARTICLE = "ARTICLE";
    public static final String INDEX = "INDEX";
    public static final String TOTAL = "TOTAL";

    public static final ArticleInfo newInstance(Article article, int index, int total)
    {
        ArticleInfo f = new ArticleInfo();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(ARTICLE, article);
        bundle.putInt(INDEX, index);
        bundle.putInt(TOTAL, total);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       article = (Article) getArguments().getSerializable(ARTICLE);
       displayCount = getArguments().getInt(INDEX)+1;
       int total = getArguments().getInt(TOTAL);
       String displayLine = displayCount +" of "+ total;


         v = inflater.inflate(R.layout.info_article, container, false);
        headline = (TextView)v.findViewById(R.id.headline);
        date = (TextView) v.findViewById(R.id.date);
        author = (TextView) v.findViewById(R.id.author);
        content = (TextView) v.findViewById(R.id.content);
        count = (TextView) v.findViewById(R.id.index);
        photo = (ImageView) v.findViewById(R.id.photo);

        count.setText(displayLine);
        if(article.getAtclTitle() != null){ headline.setText(article.getAtclTitle());
            }
        else{headline.setText("");}

        if(article.getAtclPublishedAt() !=null && !article.getAtclPublishedAt().isEmpty()) {

            String publishedDate = article.getAtclPublishedAt();

            Date date1 = null;
            String publisheDate1 = "";
            try {
                if(publishedDate != null){
                    date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(publishedDate);}
                String pattern = "MMM dd, yyyy HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                publisheDate1 = simpleDateFormat.format(date1);
                this.date.setText(publisheDate1);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }
        if(article.getAtclDescription()!=null){
            author.setText(article.getAtclDescription());
        }
        else{
            author.setText("");
        }

        if(article.getAtclAuthor() != null){
            content.setText(article.getAtclAuthor());
        }
        else{
            content.setText("");
        }

        author.setMovementMethod(new ScrollingMovementMethod());

        if(article.getAtclUrlToImage()!=null){loadRemoteImage(article.getAtclUrlToImage());}

        headline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(article.getAtclUrl()));
                startActivity(intent);
            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(article.getAtclUrl()));
                startActivity(intent);
            }
        });

        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(article.getAtclUrl()));
                startActivity(intent);
            }
        });


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(article.getAtclUrl()));
                startActivity(intent);
            }
        });

        return v;
    }


    private  void loadRemoteImage(final String imageURL){

        if (imageURL != null) {
            Picasso picasso = new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = imageURL.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .fit()
                            .centerCrop()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photo);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        } else {
            Picasso.with(getActivity()).load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photo);
        }
    }
}
