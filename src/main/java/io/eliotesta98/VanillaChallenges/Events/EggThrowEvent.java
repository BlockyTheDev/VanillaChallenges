package io.eliotesta98.VanillaChallenges.Events;

import io.eliotesta98.VanillaChallenges.Core.Main;
import io.eliotesta98.VanillaChallenges.Utils.DebugUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EggThrowEvent implements Listener {

    private DebugUtils debugUtils = new DebugUtils();
    private boolean debugActive = Main.instance.getConfigGestion().getDebug().get("EggThrowEvent");
    private String mob = Main.dailyChallenge.getMob();
    private int point = Main.dailyChallenge.getPoint();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.player.PlayerEggThrowEvent e) {
        long tempo = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (mob.equalsIgnoreCase("CHICKEN")) {
                    if (e.isHatching()) {
                        byte number = e.getNumHatches();
                        Main.dailyChallenge.increment(e.getPlayer().getName(), (long) point * number);
                    } else {
                        Main.dailyChallenge.increment(e.getPlayer().getName(), (long) point);
                    }
                } else {
                    Main.dailyChallenge.increment(e.getPlayer().getName(), point);
                }
            }
        });
        //Main.instance.getDailyChallenge().stampaNumero(e.getPlayer().getName());
        if (debugActive) {
            debugUtils.addLine("EggThrowEvent execution time= " + (System.currentTimeMillis() - tempo));
            debugUtils.debug("EggThrowEvent");
        }
        return;
    }
}
