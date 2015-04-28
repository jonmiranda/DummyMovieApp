package net.jonmiranda.dummymovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

        // set up adapter
        RecyclerView.Adapter<MovieItemViewHolder> adapter = new MovieListAdapter(MOVIE_NAMES);
        mRecyclerView.setAdapter(adapter);
    }

    static class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

        private final String[] movies;

        public MovieListAdapter(String[] movies) {
            this.movies = movies;
        }

        @Override
        public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item_layout, parent, false);
            return new MovieItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieItemViewHolder holder, int position) {
            holder.movieTitle.setText(movies[position]);
        }

        @Override
        public int getItemCount() {
            return movies.length;
        }
    }

    static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView movieTitle;

        public MovieItemViewHolder(View view) {
            super(view);
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
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
