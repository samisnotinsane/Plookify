//package common;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import playlist.*;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
///**
// * Created by sameenislam on 09/02/2016.
// */
//public class Launcher extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//
//    }
//
//    // Entry point
//    public static void main(String[] args) {
//
//        // ----------Initialisation-------------
//        System.out.println("Debug Mode - Sandbox - Playlist\nWelcome to Plookify!");
//        Scanner input = new Scanner(System.in);
//        Playlist playlist;
//        Track track;
//        Artist artist;
//        System.out.print("System initialised. ");
//
//        // ----------- AUTHENTICATION -----------
//        System.out.println("Please authenticate");
//        System.out.println("email:");
//        String usrname = input.nextLine();
//        System.out.println("password:");
//        String pwd = input.nextLine();
//        if(User.authenticate(usrname,pwd)) {
//            System.out.println("Authentication successful!");
//        } else {
//            System.out.println("Authentication failed. Please run again with valid credentials.");
//            System.exit(0);
//        }
//
//        System.out.println();
//        System.out.println("Welcome to Plookify.\nWARNING! This is a pre-alpha build.");
//        System.out.println();
//
//        // --------------Artist----------------
////        System.out.println("Add new artist(y/n)?");
////        if(input.nextLine().equals("y"))
////        {
////            System.out.println("Please enter new artist name: ");
////            String aName = input.nextLine();
////            artist = new Artist();
////            artist.create(aName);
////        }
//
//        System.out.println("Search for artists(y/n)?");
//        if(input.nextLine().equals("y"))
//        {
//            System.out.println("Enter artist name: ");
//            String query = input.nextLine();
//
////            System.out.println("Attempting to display available tracks...");
////            track = new Track();
////            track.showAll();
//            System.out.println("Searching, please wait...");
//              artist = new Artist();
//              artist.search(query);
//        }
//
//
//
//        System.out.println("Show all artists(y/n)?");
//        if(input.nextLine().equals("y")) {
//            artist = new Artist();
//            artist.showAll();
//        }
//
//        // ----------------Playlist--------------
//        PlaylistManager plManager;
//        System.out.println("Create new playlist(y/n)?");
//        if(input.nextLine().equals("y"))
//        {
//            plManager = new PlaylistManager();
//            System.out.println("Please enter new playlist name: ");
//            String plName = input.nextLine();
//            System.out.println("Would you like to set privacy(y/n)?");
//            String priv=null;
//            if(input.nextLine().equals("y")) {
//                System.out.println("Private or public(pv/pb)? ");
//                String plType = input.nextLine(); // set privacy by hand
//                if(plType.equalsIgnoreCase("pv")) {
//                    priv = "private";
//                } else if(plType.equalsIgnoreCase("pb")) {
//                    priv = "friend";
//                }
//                plManager.create(plName,priv); // invoke O/L method
//
//            } else {
//                plManager.create(plName);
//            }
//
//
//        }
//        System.out.println("Display stored playlists(y/n)?");
//        if(input.nextLine().equals("y")) {
//            ArrayList<String> names = PlaylistManager.showStored();
//            for(int i=0; i<names.size(); i++) {
//                System.out.println(i+1 + ". " + names.get(i));
//            }
//        }
//
//        System.out.println("Add tracks to playlist(y/n)?");
//        if(input.nextLine().equals("y")) {
//            System.out.println("Available playlists:");
//            ArrayList<String> playlistNames = PlaylistManager.showStored();
//            for(int i=0; i<playlistNames.size(); i++) {
//                System.out.println(i+1 + ". " + playlistNames.get(i));
//            }
//            System.out.println("Please enter corresponding number:");
//            String plNo = input.nextLine();
//            System.out.println("You selected playlist no" + plNo); // implement adding a track to a playlist
//            playlist = null; // dereference object
//            playlist = PlaylistFactory.load(Integer.parseInt(plNo)); // load chosen playlist as object
//            System.out.println("Available tracks:");
//            track = new Track();
//            track.showAll(); // fetch track list
//            // get track id
//            System.out.println("Enter the id of your chosen track:");
//            int trkId = Integer.parseInt(input.nextLine());
//            System.out.println("You chose track no "+trkId);
//            // add the chosen track into playlist
//
//
//        }
//
//
//        // ------------------Track------------------
//        System.out.println("Add new track(y/n)?"); // not possible due to FK constraint issues in database.
//        if(input.nextLine().equals("y"))
//        {
//            System.out.println("Name: ");
//            String name = input.nextLine();
//            System.out.println("Artist ID: ");
//            String artistId = input.nextLine();
//            System.out.println("Duration: ");
//            String duration = input.nextLine();
//            System.out.println("Genre ID: ");
//            String genreId = input.nextLine();
//            System.out.println("Setting path to: /home/userOne");
//            String path = "/home/userOne";
//            track = new Track();
//            track.create(name, artistId, duration, genreId, path);
//        }
//
//
//
//        System.out.println();
//        System.out.println("Exiting...");
//
//    }
//
//}
