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
    private String sneaking = Main.dailyChallenge.getSneaking();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(org.bukkit.event.player.PlayerEggThrowEvent e) {
        long tempo = System.currentTimeMillis();
        final String playerName = e.getPlayer().getName();
        final boolean hatching = e.isHatching();
        final byte numberHatches = e.getNumHatches();
        final boolean sneakingPlayer = e.getPlayer().isSneaking();
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (debugActive) {
                    debugUtils.addLine("EggThrowEvent PlayerThrowing= " + playerName);
                    debugUtils.addLine("EggThrowEvent MobConfig= " + mob);
                    debugUtils.addLine("EggThrowEvent PlayerSneaking= " + sneakingPlayer);
                    debugUtils.addLine("EggThrowEvent ConfigSneaking= " + sneaking);
                }
                if (sneaking.equalsIgnoreCase("NOBODY")) {
                    if (mob.equalsIgnoreCase("CHICKEN")) {
                        if (debugActive) {
                            debugUtils.addLine("EggThrowEvent Hatching= " + hatching);
                        }
                        if (hatching) {
                            Main.dailyChallenge.increment(playerName, (long) point * numberHatches);
                        } else {
                            Main.dailyChallenge.increment(playerName, (long) point);
                        }
                        if (debugActive) {
                            debugUtils.addLine("EggThrowEvent Conditions= 1");
                        }
                    } else {
                        if (debugActive) {
                            debugUtils.addLine("EggThrowEvent Conditions= 0");
                        }
                        Main.dailyChallenge.increment(playerName, point);
                    }
                } else {
                    if (Boolean.parseBoolean(sneaking) == sneakingPlayer) {
                        if (mob.equalsIgnoreCase("CHICKEN")) {
                            if (debugActive) {
                                debugUtils.addLine("EggThrowEvent Hatching= " + hatching);
                            }
                            if (hatching) {
                                Main.dailyChallenge.increment(playerName, (long) point * numberHatches);
                            } else {
                                Main.dailyChallenge.increment(playerName, (long) point);
                            }
                            if (debugActive) {
                                debugUtils.addLine("EggThrowEvent Conditions= 1");
                            }
                        } else {
                            if (debugActive) {
                                debugUtils.addLine("EggThrowEvent Conditions= 0");
                            }
                            Main.dailyChallenge.increment(playerName, point);
                        }
                    }
                }
                if (debugActive) {
                    debugUtils.addLine("EggThrowEvent execution time= " + (System.currentTimeMillis() - tempo));
                    debugUtils.debug("EggThrowEvent");
                }
                return;
            }
        });
        //Main.instance.getDailyChallenge().stampaNumero(e.getPlayer().getName());
    }
}
