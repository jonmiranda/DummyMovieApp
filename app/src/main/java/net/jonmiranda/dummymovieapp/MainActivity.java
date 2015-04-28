package net.jonmiranda.dummymovieapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        // build list of images
        final Drawable[] movieImages = new Drawable[MOVIE_IMAGE_IDS.length];
        for (int i = 0; i < MOVIE_IMAGE_IDS.length; ++i) {
            movieImages[i] = getResources().getDrawable(MOVIE_IMAGE_IDS[i]);
        }

        // set up adapter
        RecyclerView.Adapter<MovieItemViewHolder> adapter = new MovieListAdapter(MOVIE_NAMES, movieImages);
        mRecyclerView.setAdapter(adapter);
    }

    static class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

        private final String[] titles;
        private final Drawable[] images;

        public MovieListAdapter(String[] titles, Drawable[] images) {
            this.titles = titles;
            this.images = images;
        }

        @Override
        public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item_layout, parent, false);
            return new MovieItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieItemViewHolder holder, int position) {
            holder.movieTitle.setText(titles[position]);
            holder.movieImage.setImageDrawable(images[position]);
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

    static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView movieTitle;
        protected ImageView movieImage;

        public MovieItemViewHolder(View view) {
            super(view);
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
