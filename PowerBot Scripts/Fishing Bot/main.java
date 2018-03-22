package scripts;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Script.Manifest(name="fish and drop", description="fishes and drops", properties="author=Me; topic=999; client=6")
public class main extends PollingScript<ClientContext>{
    private List<Task> taskList = new ArrayList<Task>();
    private ArrayList<String> chosenMethods = new ArrayList<String>();
    public UI ui;
    @Override
    public void start(){
        ui = new UI();
        initializeTaskList();
        ui.setVisible(true);
    }
    private void initializeTaskList(){
        chosenMethods.clear();
        chosenMethods = (ArrayList<String>) ui.chosenMethods.clone();
        taskList.clear();
        taskList.addAll(Arrays.asList(new dismissDialogue(ctx), new Drop(ctx), new Fish(ctx, chosenMethods)));
    }
    @Override
    public void stop(){
        System.out.println("stopped");
    }

    public void delay(int ms){
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void poll() {
        //main loop
        if(!Arrays.equals(chosenMethods.toArray(),ui.chosenMethods.toArray())){
            initializeTaskList();
        }
        else {
            for (Task task : taskList) {
                if (task.activate()) {
                    task.execute();
                }
            }
        }
        delay(300);
    }
}
