package com.example.jessemitchell.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessemitchell.popularmovies.app.POJOs.MovieDetailResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG_ACT = DisplayMovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.activity_display_movie_details, new MovieDetailFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.title_activity_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MovieDetailFragment extends Fragment
    {
        private final String LOG_TAG_FRAG = MovieDetailFragment.class.getSimpleName();
        private List<String> groupHeaders;
        private HashMap<String, List<String>> listChildren;


        private MovieDetailResults.MovieDetail movie;
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();
            String movieDetailData = getString(R.string.movie_details_data);
            View rootView = inflater.inflate(R.layout.movie_detail_main,container,false);


            if(intent != null && intent.hasExtra(movieDetailData))
            {
                movie = intent.getParcelableExtra(movieDetailData);

                // Set Title
                ((TextView)rootView.findViewById(R.id.movie_text_view)).setText(movie.getTitle());

                // Set Voter Average
                ((RatingBar)rootView.findViewById(R.id.vote_average_bar)).setNumStars(5);
                ((RatingBar)rootView.findViewById(R.id.vote_average_bar)).setRating(movie.getVoteAverage().floatValue());

                // Change Image Size and display
                ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_image_view);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(10, 10, 10, 10);
                Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);

                // Set Release Date
                ((TextView)rootView.findViewById(R.id.date_text_view)).setText(movie.getReleaseDate());

                // Set Overview text.
                // setMovementMethod http://stackoverflow.com/questions/1748977/making-textview-scrollable-in-android
                ((TextView)rootView.findViewById(R.id.overview_text_view)).setText(movie.getOverview());
                ((TextView)rootView.findViewById(R.id.overview_text_view))
                        .setMovementMethod(new ScrollingMovementMethod());

                loadData();

                ExpandableListView exListView = (ExpandableListView) rootView.findViewById(R.id.detail_expand_view);
                ExpandableListAdapter exListAdapter = new ExpandableListAdapter(getContext(), groupHeaders, listChildren);

                exListView.setAdapter(exListAdapter);

                exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                    {
                        Toast.makeText(getContext(), "Yes you have selected to expand the group", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {

                        Toast.makeText(getContext(), "Group Should Expand", Toast.LENGTH_SHORT).show();
                    }
                });

                exListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {

                        Toast.makeText(getContext(), "Group should collapse", Toast.LENGTH_SHORT).show();
                    }
                });

                exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                        Toast.makeText(getContext(), "Yes you have selected to expand the child", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });
            }
            else
                Log.e(LOG_TAG_FRAG, "Intent was null or data was not sent.");
            return rootView;
        }

        private void loadData()
        {
            groupHeaders =new ArrayList<>();
            listChildren = new HashMap<>();

            groupHeaders.add(0, "Videos");
            groupHeaders.add(1, "Reviews");

            List<String> videos = new ArrayList<>();
            videos.add(0, "Official Trailer #1");
            videos.add(1, "Team Suicide Squad");
            videos.add(2, "Harley Quinn Therapy");
            videos.add(3, "comic con remix trailer");
            videos.add(4, "Blitz Trailer");

            List<String> reviews = new ArrayList<>();
            reviews.add(0, "Summertime 2016 has not been very kind to DC Comics-based personalities looking to shine consistently like their big screen Marvel Comics counterparts. Following the super-sized dud that was _Batman v. Superman: Dawn of Justice_ released a few months ago must really put some major pressure on Warner Bros. to gamble on ensuring that the presence of **Suicide Squad** does not meet the same kind of indifferent reception. Well, it turns out that although the anticipation was high for writer-director David Ayer's supervillain saga involving high-powered imprisoned rogues recruited as U.S. governmental operatives out to stop other skillful baddies (as it was for Zack Ryder's aforementioned \\\"Dawn of Justice\\\") the concoction of **Suicide Squad** feels like a colorful mishmash of collective misfits laboriously taking up space in a disjointed eye candy-coated spectacle that never manages to match its intended sizzle.\\r\\n\\r\\nOne would think that the premise for **Suicide Squad** would tap into the intriguing naughtiness with more robust gumption given the collection of super-powered oddballs asked to be immediate anti-heroes in this toothless jamboree of renegade rejects. Strangely, the grim and brooding presentation of **Suicide Squad** is more of an erratic downer than a hyperactive high-wire act as intended at the creative hands of Ayer. There is no reason why this lively group of adventurous agitators should appear so flat and inconsequential in a boisterous blockbuster that sporadically limps.\\r\\n\\r\\nGiven the twisted members that comprise this elite team of terrorizing tools it is very disappointing to see how **Suicide Squad** struggles with its so-called subversive themes. Sadly, this splattered mess never firmly grasps its bid for distinctive irreverence or off-balance exploitation. Instead, **Squad** feels strained in its execution and we are never really invested in entirely watching these treasured troublemakers find redemption because the story is soggy and uninspired. Furthermore, not all of the **Squad** participants are fleshed out satisfyingly for us to get behind with thirsty cynicism. The headlining leads in Will Smith's Floyd Lawton/Deadshot, Oscar-winner Jared Leto's green-haired Joker and Australian beauty Margot Robbie's Harleen Quinzel/Harley Quinn get the meaty standout parts while the lesser known supporting cast get stuck with chewing on the thankless remaining bone while seemingly acting as background furniture to the bigger names.\\r\\n\\r\\nNaturally, desperation has set in for the U.S. government as they need to safeguard national security against advanced sinister forces that threaten the fiber of American self-interests everywhere. What better way to hire gifted protection than to consider employing the world's most incarcerated corruptible, cutthroat cretins to perform the dirty work in unforgivable mission ops that require death-defying determination. Enter U.S. Intelligence agent Amanda Waller (Oscar nominee Viola Davis). Waller's duties are to assemble the ragtag team known as the Suicide Squad--ominous (yet talented) jailbirds tapped to step in and assume superhero status (especially when the real superheroes are tied up in other crime-stopping affairs) while helping out for the greater good of our vulnerable society. In exchange for the Suicide Squad's sacrifice in turning from hell-bent heels to reluctant heralded heroes they are promised commuted prison sentences should they effectively defend and destroy the deadly foes out to promote heavy-handed havoc across the board.\\r\\n\\r\\nConveniently, bureaucratic bigwig Waller (through voiceover) introduces the Suicide Squad and describes what beneficial assets they bring to the turbulent table. Among the naughty notables include the well-known ace sniper Floyd Lawton/Deadshot as well as legendary lethal joy-boy Joker and his better (or perhaps worst half) in girlfriend Harley Quinn. The other toxic tag-a-longs along for the thrill ride of becoming rebellious rescuers include George Harkness/Boomerang (Jai Courtney), Chato Santana/El Diablo (Jay Hernandez), Waylon Jones/Killer Croc (Adewale Akinnuoye-Agbaje),  Tatsu Yamashiro/Katana, Enchantress (Cara Delevingne) and Rick Flag (Joel Kinnaman).\\r\\n\\r\\nOverall, **Suicide Squad** is surprisingly depressing and goes through the proverbial motions without so much as taking advantage of its surrealistic makeup. The movie never realizes its excitable potential and drifts into yet another superhero yarn that is more patchy than pronounced. Smith's Deadshot is out in the forefront but for the most part feels restrained and not as spry and savvy as one would imagine. Leto's Joker obviously pales in comparison to the brilliant and mesmerizing psychotic take on the role that earned the late Heath Ledger his posthumous Oscar statuette. In all fairness, nobody could inhabit the Clown Prince of Crime as Ledger uncannily did with committed concentration. Still, Leto's Joker--although viciously off-balance--felt recycled and furiously empty at times. Robbie's turn as Joker's misguided main squeeze merely comes off as a bratty Barbie Doll with synthetic edginess. The other **Squad** participants settle for the back burner more or less which is a crying shame because they should have been more engaged than the tepid material allowed them to be initially.\\r\\n\\r\\nWoefully sketchy and missing the fueled opulence that one would expect emerging from this cockeyed costume caper **Suicide Squad** is a detonating dud for the missing explosive DC Comics movie brand that needs to step up the pace if they expect to make a consistent and challenging impression on the devoted fanboys at the box office looking to move beyond the sardonic fantasy-based realm of another redundant serving of a _Batman/Superman_ entry.\\r\\n\\r\\n**Suicide Squad** (2016)\\r\\n\\r\\nWarner Bros.\\r\\n\\r\\n2 hrs. 3 mins.\\r\\n\\r\\nStarring: Will Smith, Jared Leto,  Margo Robbie, Viola Davis, Joel Kinnaman, Jay Hernandez, Jai Courtney, Scott Eastwood, Adewale Akinnuoye-Agbaje, Ike Barinholtz, Common, Cara Delevinge, Karen Fukuhara, Adam Beach\\r\\n\\r\\nDirected and Written by: David Ayer\\r\\n\\r\\nMPPA Rating: PG-13\\r\\n\\r\\nGenre: Superheroes Saga/Action & Adventure/Comic Book Fantasy\\r\\n\\r\\nCritic's rating: ** stars (out of 4 stars)\\r\\n\\r\\n(c) **Frank Ochieng** (2016)");
            reviews.add(1, "Suicide Squad is the third and latest entry into the DCEU, and is about a bunch of bad guys that are rounded up to fight for someone else. And just like this year's BvS, this movie received overwhelmingly negative reviews by the critics and was divided among the fans. I was super curious to watch it because unlike many, I actually enjoyed the DCEU till this point. Enjoyed both Man of Steel and BvS. But unfortunately, this one's a mess.\\r\\n\\r\\nThe majority of the movie just feels choppy, editing was all over the place. Like they had a final product but because of disagreements, they took out a lot of scenes, shorten the runtime, and added others, making a giant choppy mess in the end. Scenes don't properly flow, including the flashbacks. Some scenes feel like they were added later (Probably the re-shoots) and they definitely didn't fit, particularly the elevator scene with Harley. The songs were all over the place as well. Some worked with their respective scenes, but most of them didn't, and again it felt like something added later, to give the movie a more jolly feel. And difference between development given to each member of the squad is astounding. Some were completely left in the dust, while some got a bit of line here and there, while some got a lot more development. Basically, to me it felt like that the movie reeked of studio involvement.\\r\\n\\r\\nAlso, the focus was just off. Movie is called Suicide Squad yet there is a whole lot of other stuff that gets way too much screentime. The whole end of the world plot was totally generic, uninteresting and unnecessary. The villain wasn't good, and the movements were weird, and not in a good way. Joker-Harley romance was also something padded on, and could have been removed in exchange for more screentime with the squad. \\r\\n\\r\\nSpeaking of the Joker, he and his whole weird mafia/gangster lord type vibe didn't work for me. Jared Leto felt like he was trying too hard at times. There were moments where I saw the Joker I wanted in him, but those moments were swiftly followed by over the top feel that he gave most of the time. And that laugh....Yeah NOPE!!  \\r\\n\\r\\nEven the action was mostly OK, apart from a couple of good scenes. There was no proper thrill, no proper buildup. Too many cuts. Say what you want about Snyder, but you have to admit that the dude can atleast direct amazing action sequences. \\r\\n\\r\\nAnd all of that sucks because there is stuff in the movie that works, like the main squad. Will Smith as Deadshot was great. He played his usual cool self and it worked. Margot Robbie as Harley Quinn was sexy and mostly good, because there were few instances where her dialogues gave me cringe. These two had a good chemistry together too. Also liked other members like El Diablo, Killer Croc, and captain Boomerang, all of whom were likable, had some fun moments etc. \\r\\n\\r\\nPlus, among all the mediocrity, there were glimpses of what the movie could have been like. Fun moments between the squad, some touchy moments, rare cool action sequences, full group scenes or rather a scene, the bar scene and such. \\r\\n\\r\\nUltimately, The movie is like a mediocre cake covered with a thin layer of good frosting. The overall taste isn't bad, but it isn't good either. You enjoy the good frosting for a short while, and then have to deal with a whole lot of mediocre tasting body of the cake. I was disappointed. I really REALLY wanted to like this film. Pushed back all the negative or positive criticism and went in with an open mind. I'm not too hopeful what the extended cut will improve as 13 mins of footage isn't much, and I'm guessing it is Joker footage mostly.\\r\\n\\r\\nIt's funny that after watching this, I respect MCU more now considering what they were able to do with the more risky project: Guardians of the Galaxy. \\r\\n\\r\\n6/10\\r\\n\\r\\nBlog Post Link: http://reviewsreactor.blogspot.com/2016/10/suicide-squad-2016-movie-review.html");
            reviews.add(2, "Some semi-interesting visuals and a few characters I'd like to get to know, but an absolute mess of a movie. The thing feels like a trailer, or a clipshow, or a music video or some other sort of two-hour long promotional material for the actual _Suicide Squad_ that comes out later.\\r\\n\\r\\n_Final rating:★★ - Had some things that appeal to me, but a poor finished product._\"");
            reviews.add(3, "**They are not superheroes, they are supervillains.**\\r\\n\\r\\nIt's nothing against DC, but overall I'm starting to think the todays cinema is getting crowded with the lots of superheroes. Just like any pollution or the over population on the earth's surface. It needs stability, but nobody cares about it other than money making agenda. I also think it's going to last for only a few more years, when this trend going to end like that happened in the 70s, 80s and the 90s. And the space travel era to begin which is already kick- started. So DC or Marvel and others, they should be careful, for far they could take their products.\\r\\n\\r\\nLike the title say, it's not just about the film characters, the film itself a suicidal. I'm not saying the film was unnecessary, but the plot was dragged too much. There are too many pauses, or you can call time wasting moments. I could not take another blowing up city concept. And that swirling thing in the sky, I don't know how long they are going to use it in the superhero films. I did not like the supernatural concept which is supposed to be a pure science fiction action adventure. At least Thor was from another planet, more like he's an alien, but the witch in this film, ruined my appetite.\\r\\n\\r\\nThe actors were not bad and so the graphics, including the stunt sequences. The story was very familiar. It was more or less, same as the animated flick 'Monsters vs Aliens'. It can be watched for entertainment purpose, the majority won't say it's their favourite or one of the best of the year. But surely there are people who would love it. It was a massive box office hit and I don't see any hurdle for its sequel, but all I hope is it to get better in the follow-up. So finally, it's not a bad film or boring, but it just did not have the midas touch that all the superhero films had. That means a watchable film, only for once.\\r\\n\\r\\n_5/10_");

            listChildren.put(groupHeaders.get(0), videos);
            listChildren.put(groupHeaders.get(1), reviews);
        }

    }
}
