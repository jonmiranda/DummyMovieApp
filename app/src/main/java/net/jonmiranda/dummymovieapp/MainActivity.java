package net.jonmiranda.dummymovieapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;


public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    String API_ENDPOINT = "http://jonmiranda.net/static/dummymovieapp";

    public interface MovieProvider {
        @GET("/movies")
        void getMovies(Callback<Movie[]> callback);
    }

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

        // Let's start networking!
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .build();

        // Create a MovieProvider base off of the RestAdapter we created
        MovieProvider movieProvider = restAdapter.create(MovieProvider.class);

        // We're going to use a Callback so Retrofit will make the network request
        // off of the UI thread.
        movieProvider.getMovies(new Callback<Movie[]>() {
            @Override
            public void success(Movie[] movies, Response response) {
                showMovies(movies);
            }

            @Override
            public void failure(RetrofitError error) {
                String errorMessage = "RetrofitError: " + error.getMessage();
                Log.e("MainActivity", errorMessage);
                Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMovies(Movie[] movies) {
        // set up adapter
        RecyclerView.Adapter<MovieItemViewHolder> adapter = new MovieListAdapter(this, movies);
        mRecyclerView.setAdapter(adapter);
    }

    static class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

        private final MainActivity activity;
        private final Movie[] movies;

        public MovieListAdapter(MainActivity activity, Movie[] movies) {
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
            holder.cardView.setBackgroundColor(movies[position].color);
            holder.movieTitle.setText(movies[position].title);

            // Picasso will handle making the network request, retrieving the image data, and
            // putting the image into the View
            Picasso.with(activity).load(movies[position].imgLink).into(holder.movieImage);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // WARNING: Below only works on API 21+

                    // create the transition animation - the views in the layouts
                    // of both activities are defined with android:transitionName="movie_title"
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(activity, holder.movieTitle, "movie_title");

                    Intent intent = new Intent(activity, MovieActivity.class);
                    intent.putExtra(MovieActivity.MOVIE_TITLE_KEY, movies[position].title);

                    // start the new activity
                    activity.startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return movies.length;
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
