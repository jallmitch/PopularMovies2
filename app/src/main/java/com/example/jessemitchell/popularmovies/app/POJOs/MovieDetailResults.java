package com.example.jessemitchell.popularmovies.app.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesse.mitchell on 1/16/2017.
 *
 * Resources for Parcelable
 * https://developer.android.com/reference/android/os/Parcelable.html
 * http://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
 * http://stackoverflow.com/questions/7042272/how-to-properly-implement-parcelable-with-an-arraylistparcelable
 */

public class MovieDetailResults
{
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<MovieDetail> results = null;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MovieDetail> getResults()
    {
        return results;
    }

    public void setResults(List<MovieDetail> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public static class MovieDetail implements Parcelable {

        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("adult")
        @Expose
        private Boolean adult;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;
        @SerializedName("genre_ids")
        @Expose
        private List<Integer> genreIds = null;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("original_title")
        @Expose
        private String originalTitle;
        @SerializedName("original_language")
        @Expose
        private String originalLanguage;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("popularity")
        @Expose
        private Double popularity;
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        @SerializedName("video")
        @Expose
        private Boolean video;
        @SerializedName("vote_average")
        @Expose
        private Double voteAverage;

        public MovieDetail(){}

        public MovieDetail(Parcel in)
        {
            this.posterPath = in.readString();
            this.adult = in.readByte() != 0;
            this.overview = in.readString();
            this.releaseDate = in.readString();
            this.genreIds = (ArrayList<Integer>)in.readSerializable();
            this.id = in.readInt();
            this.originalTitle = in.readString();
            this.originalLanguage = in.readString();
            this.title = in.readString();
            this.backdropPath = in.readString();
            this.popularity = in.readDouble();
            this.voteCount = in.readInt();
            this.video = in.readByte() != 0;
            this.voteAverage = in.readDouble();
        }

        public String getPosterPath()
        {
            StringBuilder sb = new StringBuilder("http://image.tmdb.org/t/p/")
                    .append("w185")
                    .append(this.posterPath);

            return sb.toString();
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate()
        {
            return  "Release Date: " + this.releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage/2;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            ArrayList<Integer> genIds = new ArrayList<>(this.genreIds);
            dest.writeString(posterPath);
            dest.writeByte((byte) (adult ? 1 : 0));
            dest.writeString(overview);
            dest.writeString(releaseDate);
            dest.writeSerializable(genIds);
            dest.writeInt(id);
            dest.writeString(originalTitle);
            dest.writeString(originalLanguage);
            dest.writeString(title);
            dest.writeString(backdropPath);
            dest.writeDouble(popularity);
            dest.writeInt(voteCount);
            dest.writeByte((byte)(video ? 1 : 0));
            dest.writeDouble(voteAverage);
        }



        public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
            @Override
            public MovieDetail createFromParcel(Parcel parcel) {
                return new MovieDetail(parcel);
            }

            @Override
            public MovieDetail[] newArray(int i) {
                return new MovieDetail[i];
            }

        };
    }
}
