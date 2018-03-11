package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

public class Fish extends Task<ClientContext> {
    private int[] fishSpotIds = {323,324};
    static int stillCounter = 0;

    public Fish(ClientContext ctx) {
        super(ctx);
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
                && !ctx.npcs.select().id(fishSpotIds).isEmpty()
                && stillCounter >= 5;
    }

    @Override
    public void execute() {
        Npc fishSpot = ctx.npcs.select().id(fishSpotIds).nearest().poll();
        if(fishSpot.inViewport()){
            fishSpot.click();
            //fishSpot.interact("Fish");
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
