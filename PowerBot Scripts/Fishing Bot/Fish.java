package scripts;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class Fish extends Task<ClientContext> {
    private int[] fishSpotIds = {323,324};
    private int[] fishIds = {377, 371, 317, 321, 359};

    public Fish(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.backpack.select().count() < 28
                && !ctx.objects.select().id(fishSpotIds).isEmpty()
                && ctx.players.local().animation() == -1;
    }

    @Override
    public void execute() {
        GameObject fishSpot = ctx.objects.select().id(fishSpotIds).nearest().poll();
        if(fishSpot.inViewport()){
            fishSpot.click();
            //fishSpot.interact("Fish");
        }
        else{
            ctx.movement.step(fishSpot);
            ctx.camera.turnTo(fishSpot);
        }
    }
}
