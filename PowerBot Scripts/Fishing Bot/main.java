package scripts;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="fish and drop", description="fishes and drops", properties="author=Me; topic=999; client=6")
public class main extends PollingScript<ClientContext>{
    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start(){
        taskList.addAll(Arrays.asList(new Fish(ctx), new Drop(ctx)));

    }

    @Override
    public void stop(){
        System.out.println("stopped");
    }

    @Override
    public void poll() {
        //main loop
        for(Task task : taskList){
            if(task.activate()){
                task.execute();
            }
        }
    }
}
