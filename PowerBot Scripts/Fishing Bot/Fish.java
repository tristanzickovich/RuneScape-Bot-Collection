package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class Fish extends Task<ClientContext> {
    private List<Integer> fishSpotIds = new ArrayList<Integer>();
    private List<String> fishMethods = new ArrayList<String>();
    static int stillCounter = 0;

    public Fish(ClientContext ctx) {
        super(ctx);
        addFishMethod("Cage", 324);
        addFishMethod("Harpoon", 324);
        //addFishMethod("Net", 323);
    }

    @Override
    public boolean activate() {
        if(ctx.players.local().animation() == -1){
           stillCounter++;
        }
        else{
            stillCounter = 0;
        }
        return ctx.backpack.select().count() < 28
                && !ctx.npcs.select().id(fishSpotIdsArray()).isEmpty()
                && stillCounter >= 5;
    }

    public int[] fishSpotIdsArray(){
        int[] ids = new int[fishSpotIds.size()];
        for(int i = 0; i < ids.length; ++i){
            ids[i] = fishSpotIds.get(i).intValue();
        }
        return ids;
    }

    public void addFishMethod(String method, int id){
        if(!fishMethods.contains(method)) {
            fishMethods.add(method);
        }
        if(!fishSpotIds.contains(id)) {
            fishSpotIds.add(id);
        }
    }

    @Override
    public void execute() {
        stillCounter = 0;
        Npc fishSpot = ctx.npcs.select().id(fishSpotIdsArray()).nearest().poll();
        if(fishSpot.inViewport()){
            String[] actions = fishSpot.actions().clone();
            Collections.shuffle(Arrays.asList(actions));
            for(int i = 0; i < actions.length; ++i) {
                if(fishMethods.contains(actions[i])) {
                    fishSpot.interact(actions[i]);
                    break;
                }
            }
            //wait until player is fishing to return
            Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() != -1;
                }
            }, 200, 20);
        }
        else{
            ctx.movement.step(fishSpot);
            ctx.camera.turnTo(fishSpot);
        }
    }
}
