// class that presents one step in loading process
public class OneLoadStep {
    public void load(String from, String to) {
        DBLoader loader = new DBLoader(from);
        loader.loadFileToDB();
        FileLoader fileLoader = new FileLoader(to);
        fileLoader.writeStatistics();
        System.out.println("The load complete");
    }
}