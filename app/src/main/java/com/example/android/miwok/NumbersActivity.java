package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {
    //Handles the playback of all sound files
       MediaPlayer mMediaPlayer ;
       //Handles Audio focus when playing Audio file
    private AudioManager mAudioManager ;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
        @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||focusChange ==  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback because your Audio Focus was
                        // temporarily stolen, but will be back soon.
                        // i.e. for a phone call

                        mMediaPlayer.pause();
                        //WHEN THE AUDIO FOCUS BACK, it'll be BETTER FOR THE USER TO HEAR
                        // THE WORD FROM THE BEGINNING OF THE AUDIO FILE
                        //INSTEAD OF JUST HEARING THE REMAINING OF THE WORD
                        mMediaPlayer.seekTo(0);
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //AUDIOFOCUS_GAIN >>> It means we have regained Audio Focus and we can resume the playback

                        mMediaPlayer.start();
                    }



                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // AUDIOFOCUS_LOSS>>> It means we've lost the Audio Focus and
                        // STOP & cleanup resources
                        releaseMediaPlayer();

                    }
                }
            };




       private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mediaPlayer) {
               releaseMediaPlayer();
           }
       };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //create and setup AudioManager to request audio focus
        mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("one", "lutti",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two", "otiiko",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three", "tolookosu",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four", "oyyisa",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five", "massokka",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six", "temmokka",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven", "kenekaku",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight", "kawinta",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine", "wo’e",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("ten", "na’aacha",R.drawable.number_ten,R.raw.number_ten));

        // Create an {@link WordAdapter}, whose data sourcrtem in the list.
        WordAdapter adapter = new WordAdapter(this, words,R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);
                //request Audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //USE THE MUSIC STREAM
                        AudioManager.STREAM_MUSIC,
                        //REQUEST TEMPORARY AUDIO FOCUS
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                //release the media player if it currently exists because we are about
                //to play a different sound file
                releaseMediaPlayer();

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //WE HAVE AUDIO FOCUS NOW
                    // create and setup {@link MediaPlayer} for the audio resource id associated with the current word
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getmAudioResourceId());
                    // start the audio file
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }
    @Override
    protected void onStop() {

        super.onStop();
        //when the activity is stopped,release the mediaplayer resources because we won't
        //be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            // Register whether or not we were granted Audio focus ,abandon it
            // and this is also unregistered OnAudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);


        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}