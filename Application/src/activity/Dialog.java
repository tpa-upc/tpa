package activity;

/**
 * Created by germangb on 25/04/2016.
 */
public class Dialog {

    /** dialog answers */
    public class Answer {
        public String text;
        public Object data;
        public int jump;
    }

    /** dialog questions */
    public class Question {
        public String preview;
        public String text;
        public Object data;
        public int answer;
    }

    /** Dialog nodes */
    public class Node {
        public Question[] questions;
        public Answer[] answers;
    }

    public Node[] dialog;
}
