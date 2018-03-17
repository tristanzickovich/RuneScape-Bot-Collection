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
    public UI ui;
    @Override
    public void start(){
        taskList.addAll(Arrays.asList(new dismissDialogue(ctx), new Fish(ctx), new Drop(ctx)));
        ui = new UI();
        ui.setVisible(true);
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
        if(ui.isRunning) {
            for (Task task : taskList) {
                if (task.activate()) {
                    task.execute();
                }
            }
        }
        delay(300);
    }
}
