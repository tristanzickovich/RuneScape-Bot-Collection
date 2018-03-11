package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

import java.util.concurrent.Callable;

public class dismissDialogue extends Task<ClientContext>{

    public dismissDialogue(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.chat.chatting();
    }

    @Override
    public void execute() {
        while (ctx.chat.chatting()){
            ctx.chat.clickContinue(true);
        }
        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return !ctx.chat.chatting();
            }
        }, 200, 20);
    }
}
