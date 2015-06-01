package net.jonmiranda.dummymovieapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;


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


        List<Movie> movies;  // this will hold live references to the objects in the database

        Realm realm = Realm.getInstance(this);
        if (realm.where(Movie.class).count() == 0) {
            // database is empty, so let's fill it up with data!
            final Movie[] movieArray = new Movie[MOVIE_NAMES.length];
            for (int i = 0; i < movieArray.length; ++i) {
                Movie movie = new Movie();
                movie.setTitle(MOVIE_NAMES[i]);
                movie.setImgId(MOVIE_IMAGE_IDS[i]);
                movie.setColor(Palette.generate(BitmapFactory.decodeResource(getResources(),
                        MOVIE_IMAGE_IDS[i])).getVibrantColor(Color.WHITE));
                movieArray[i] = movie;
            }

            realm.beginTransaction();
            movies = realm.copyToRealm(Arrays.asList(movieArray));
            realm.commitTransaction();
        } else {
            movies = realm.where(Movie.class).findAll();
        }

        // set up adapter
        RecyclerView.Adapter<MovieItemViewHolder> adapter = new MovieListAdapter(this, movies);
        mRecyclerView.setAdapter(adapter);
    }

    static class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

        private final MainActivity activity;
        private final List<Movie> movies;

        public MovieListAdapter(MainActivity activity, List<Movie> movies) {
            this.activity = activity;
            this.movies = movies;
        }

        @Override
        public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item_layout, parent, false);
            return new MovieItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MovieItemViewHolder holder, final int position) {
            final Movie movie = movies.get(position);
            holder.cardView.setBackgroundColor(movie.getColor());
            holder.movieTitle.setText(movie.getTitle());
            holder.movieImage.setImageDrawable(activity.getResources().getDrawable(movie.getImgId()));

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // WARNING: Below only works on API 21+

                    // create the transition animation - the views in the layouts
                    // of both activities are defined with android:transitionName="movie_title"
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(activity, holder.movieTitle, "movie_title");

                    Intent intent = new Intent(activity, MovieActivity.class);
                    intent.putExtra(MovieActivity.MOVIE_TITLE_KEY, movie.getTitle());

                    // start the new activity
                    activity.startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return movies.size();
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
