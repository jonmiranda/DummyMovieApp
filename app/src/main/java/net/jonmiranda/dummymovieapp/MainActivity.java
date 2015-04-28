package net.jonmiranda.dummymovieapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    public static final String[] MOVIE_NAMES = {
            "Snow White and the Seven Dwarfs",
            "Lady and the Tramp",
            "The Jungle Book",
            "Cinderella",
            "Fantasia",
            "Bambi",
            "Sleeping Beauty",
            "Dumbo"
    };

    public static final int[] MOVIE_IMAGE_IDS = {
            R.drawable.snow_white_and_the_seven_dwarfs,
            R.drawable.lady_and_the_tramp,
            R.drawable.the_jungle_book,
            R.drawable.cinderella,
            R.drawable.fantasia,
            R.drawable.bambi,
            R.drawable.sleeping_beauty,
            R.drawable.dumbo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // grab reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // set up the layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        // build list of images and palettes
        final Drawable[] movieImages = new Drawable[MOVIE_IMAGE_IDS.length];
        final Palette[] palettes = new Palette[MOVIE_IMAGE_IDS.length];

        for (int i = 0; i < MOVIE_IMAGE_IDS.length; ++i) {
            movieImages[i] = getResources().getDrawable(MOVIE_IMAGE_IDS[i]);
            palettes[i] = Palette.generate(BitmapFactory.decodeResource(getResources(), MOVIE_IMAGE_IDS[i]));
        }

        // set up adapter
        RecyclerView.Adapter<MovieItemViewHolder> adapter =
                new MovieListAdapter(this, MOVIE_NAMES, movieImages, palettes);
        mRecyclerView.setAdapter(adapter);
    }

    static class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

        private final MainActivity activity;
        private final String[] titles;
        private final Drawable[] images;
        private final Palette[] palettes;

        public MovieListAdapter(MainActivity activity, String[] titles, Drawable[] images, Palette[] palettes) {
            this.activity = activity;
            this.titles = titles;
            this.images = images;
            this.palettes = palettes;
        }

        @Override
        public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item_layout, parent, false);
            return new MovieItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MovieItemViewHolder holder, final int position) {
            holder.cardView.setBackgroundColor(palettes[position].getVibrantColor(Color.WHITE));
            holder.movieTitle.setText(titles[position]);
            holder.movieImage.setImageDrawable(images[position]);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // WARNING: Below only works on API 21+

                    // create the transition animation - the views in the layouts
                    // of both activities are defined with android:transitionName="movie_title"
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(activity, holder.movieTitle, "movie_title");

                    Intent intent = new Intent(activity, MovieActivity.class);
                    intent.putExtra(MovieActivity.MOVIE_TITLE_KEY, titles[position]);

                    // start the new activity
                    activity.startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

    static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        protected View cardView;
        protected TextView movieTitle;
        protected ImageView movieImage;

        public MovieItemViewHolder(View view) {
            super(view);
            cardView = view;
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
            movieImage = (ImageView) view.findViewById(R.id.movie_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
