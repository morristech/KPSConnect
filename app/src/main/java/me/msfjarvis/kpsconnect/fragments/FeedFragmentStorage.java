package me.msfjarvis.kpsconnect.fragments;

import java.util.ArrayList;

class FeedFragmentStorage {

    private static ArrayList<String>[] feedLists;

    static class FeedType {
        static final int
                TITLES      = 0,
                CATEGORIES  = 1,
                LINKS       = 2,
                IMAGES      = 3,
                CONTENT     = 4;
    }

    @SafeVarargs
    static void setFeeds(ArrayList<String>... feeds) {
        feedLists = feeds;
    }

    static String[] getFeeds(int feedType) {
        return feedLists[feedType].toArray(new String[feedLists[feedType].toArray().length]);
    }

}
