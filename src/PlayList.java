import java.util.*;
import java.io.*;

public class PlayList {
  public static void main(String[] args) throws Exception{ 
    String[] files = new String[]{"regional-global-weekly-2020-01-31--2020-02-07.csv", "regional-global-weekly-2020-02-07--2020-02-14.csv",
                                  "regional-global-weekly-2020-02-14--2020-02-21.csv", "regional-global-weekly-2020-02-21--2020-02-28.csv"};
    //there are 200 songs in each file
    String[] ReadSongs = new String[200];
    ArrayList<String> Songs = new ArrayList<>();
    
   //reads in songs name from each file into an array. 
   //Then the elements in the array are put into the ArrayList minus the duplicate songs.
    for(int f = 0; f <files.length; f++) {
      read(files[f], ReadSongs);
      for(int i = 0; i <ReadSongs.length; i++) {
        if(Songs.contains(ReadSongs[i]) == false)
          Songs.add(ReadSongs[i]);
      } 
    }
    
    //sorts the the arrayList alphabetically using Java collections framework
    Collections.sort(Songs, new Comparator<String>(){
      //overrides the sort so that it ignores lower/uppercases
      @Override
      public int compare(String s1, String s2){
        return s1.compareToIgnoreCase(s2);
      }
    });
    
    //creates a queue. 
    //The elements in the arrayList are put into one queue and written into a file.
    Queue list = new Queue();
    PrintWriter pw = new PrintWriter("QueueOutput.txt");
    for(int i = 0; i < Songs.size(); i++) {
      list.insertLast(Songs.get(i));
      pw.println(Songs.get(i));
    }
    pw.close();
    
    //creates a stack. 
    //The elements removed fromthe queue are put into the stack and written into a file.
    HistoryList history = new HistoryList();
    PrintWriter writer = new PrintWriter("StackHistoryOutput.txt");
    for(int i = 0; i <Songs.size(); i++) {
      history.addSong(list.removeFirst());
      writer.println(history.LastListened());
    } 
    writer.close();
  }
  
  public static void read(String file, String[]arr) throws Exception{
    BufferedReader br = new BufferedReader(new FileReader(file));
    String note = br.readLine();
    String titles = br.readLine();   
    String data = br.readLine();
  
    //splits around commas. 
    // ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" is a short-hand for ignoring commas within quotations.
    String[] line = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    arr[0] = line[1].trim();
    int count = 1;
    for(int i = 1; i < arr.length; i++){
      data = br.readLine();
      line = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
      arr[count] = line[1].trim();
      count++;
    }
    br.close();
  }
}

//each Track represents a node
class Track{
  private String name;
  public Track next;
  
  //first constructer takes in a string parameter
  public Track(String name) {
    this.name = name;
    next = null;
  }
  
  //second constructer takes no parameter
  public Track(){
    next = null;
  }
  
  public void setName(String s) {
    name = s;
  }
  
  public String getName() {
    return name;
  }
}

//Queue is a collection of track nodes
//First-In-First-Out structure
class Queue { 
  public Track head;
  public Track rear;
  public int size;
  
  public Queue() {
    head = null;
    rear = null;
    size = 0;
  }
  
  public boolean isEmpty() {
    return (size == 0);
  }
  
  public void insertLast(String name) {
    Track song = new Track(name);
    if(rear == null) {
      head = song;
      rear = song;
    }
    rear.next = song;
    rear = song;
    size++;
  }
  
  public String removeFirst() {
    if(isEmpty()) {
      String error = "The queue is empty.";
      rear = null;
      return error;
    } else {
      Track song = head;
      head = head.next;
      size--;
      return song.getName();
    }
  }
  
  public String peekFront() {
    if(isEmpty()){
      return "The queue is empty.";
    } else {
      return head.getName();
    }
  }
  
  public String peekRear() {
    if(isEmpty()) {
      return "The queue is empty.";
    } else {
      return rear.getName();
    }
  }
}

//HistoryList is a stack structure.
//First-In-Last-Out
class HistoryList {
  private Track first;
  public int size;
  
  public HistoryList() {
    first = null;
    size = 0;
  }
  
  public boolean isEmpty(){
    return (size == 0);
  }
  
  //push function: adds an element to the top of the stack.
  public void addSong(String song) {
    Track s = new Track();
    s.setName(song);
    s.next = first;
    first = s;
    size++;
  }
  
  //peek function: looks at the top of the stack to see which song was last listened to.
  public String LastListened() { 
    if(size == 0) { 
      return "The list is empty.";
    } else {
      return first.getName();
    } 
  }
}
