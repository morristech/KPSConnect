package me.msfjarvis.kpsconnect.fragments;

import java.util.ArrayList;

public class FeedFragmentStorage {

    private static ArrayList<String>[] feedLists;

    public static class FeedType {
        public static final int
                    TITLES      = 0,
                    CATEGORIES  = 1,
                    LINKS       = 2,
                    IMAGES      = 3,
                    CONTENT     = 4
                            ;
    }

    @SafeVarargs
    public static void setFeeds(ArrayList<String>... feeds) {
        feedLists = feeds;
    }

    public static String[] getFeeds(int feedType) {
        return feedLists[feedType].toArray(new String[feedLists[feedType].toArray().length]);
    }

}
