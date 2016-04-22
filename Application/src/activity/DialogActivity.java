package activity;

import com.google.gson.Gson;
import game.Game;
import tpa.application.Context;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by germangb on 20/04/16.
 */
public class DialogActivity extends Activity {

    private class QuestionJson {
        String text;
        int answer;
    }

    private class AnswerJson {
        String text;
        Object data;
        int jump;
    }

    private class NodeJson {
        QuestionJson[] questions;
        AnswerJson[] answers;
    }

    private class DialogJson {
        NodeJson[] dialog;
    }

    /** Dialog file */
    private String file;

    /** Dialog */
    private DialogJson dialog;

    /** Current dialog node */
    private NodeJson node;

    public DialogActivity (String file) {
        this.file = file;
    }

    @Override
    public void onInit(Context context) {
        // load file
        Gson gson = new Gson();
        try {
            dialog = gson.fromJson(new FileReader(file), DialogJson.class);
            node = dialog.dialog[0];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            dialog = null;
        }
    }

    private void display () {
        for (int i = 1; i <= node.questions.length; ++i) {
            System.out.println("["+i+"] "+node.questions[i-1].text);
        }
    }

    @Override
    public void onBegin(Context context) {
        System.out.println("---------- BEGIN ----------");
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_ESCAPE) {
                    System.out.println("----------  END  ----------");
                    Game.getInstance().popActivity();
                } else {
                    key = key - KeyboardInput.KEY_1;
                    if (key >= 0 && key < node.questions.length) {
                        int ansId = node.questions[key].answer;
                        AnswerJson ans = node.answers[ansId];
                        if (ans.data != null) report(ans.data);
                        System.out.println("\n[ANSWER] "+ans.text+"\n");
                        node = dialog.dialog[ans.jump];
                        display();
                    }
                }
            }
        });

        display();
    }

    @Override
    public void onUpdate(Context context) {
        context.renderer.setClearColor(0, 0, 0, 1);
        context.renderer.clearBuffers();
    }

    @Override
    public void onEnd(Context context) {

    }
}
