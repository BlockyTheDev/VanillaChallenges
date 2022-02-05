package io.eliotesta98.VanillaChallenges.Utils;

import io.eliotesta98.VanillaChallenges.Core.Main;
import io.eliotesta98.VanillaChallenges.Database.Challenger;
import io.eliotesta98.VanillaChallenges.Database.H2Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Challenge {

    private HashMap<String, Integer> players = new HashMap<String, Integer>();
    private String block = "ALL";
    private String blockOnPlace = "ALL";
    private String typeChallenge = "nessuna";
    private String reward = "nessuno";
    private String title = "nessuno";
    private String subTitle = "nessuno";
    private String item = "ALL";
    private String itemInHand = "ALL";
    private String mob = "ALL";
    // timer del salvataggio punti
    int number = 20*60*10;
    private BukkitTask task;

    public Challenge() {

    }

    public Challenge(String block, String blockOnPlace,String typeChallenge, String reward, String title, String subTitle, String item, String itemInHand, String mob) {
        this.block = block;
        this.blockOnPlace = blockOnPlace;
        this.typeChallenge = typeChallenge;
        this.reward = reward;
        this.title = title;
        this.subTitle = subTitle;
        this.item = item;
        this.itemInHand = itemInHand;
        this.mob = mob;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getBlockOnPlace() {
        return blockOnPlace;
    }

    public void setBlockOnPlace(String blockOnPlace) {
        this.blockOnPlace = blockOnPlace;
    }

    public HashMap<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Challenger> challengers) {
        while(!challengers.isEmpty()) {
            players.put(challengers.get(0).getNomePlayer(),challengers.get(0).getPoints());
            challengers.remove(0);
        }
    }

    public String getTypeChallenge() {
        return typeChallenge;
    }

    public void setTypeChallenge(String typeChallenge) {
        this.typeChallenge = typeChallenge;
    }

    public void increment(String playerName) {
        if (!players.containsKey(playerName)) {
            players.put(playerName, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    H2Database.instance.insertChallenger(playerName, 1);
                }
            });
        } else {
            players.replace(playerName, players.get(playerName) + 1);
        }
    }

    public void increment(String playerName, int amount) {
        if (!players.containsKey(playerName)) {
            players.put(playerName, amount);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    H2Database.instance.insertChallenger(playerName, 1);
                }
            });
        } else {
            players.replace(playerName, players.get(playerName) + amount);
        }
    }

    public void clearPlayers() {
        stopTask();
        players.clear();
    }

    public void stampaNumero(String playerName) {
        int blocchiPiazzati = players.get(playerName);
        Bukkit.getPlayer(playerName).sendMessage(ChatColor.RED + typeChallenge + ": " + blocchiPiazzati);
    }

    public void savePoints() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.instance, new Runnable() {
            @Override
            public void run() {
                //Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Vanilla Challenges] Start Backup player points");
                for (Map.Entry<String, Integer> player : players.entrySet()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                        @Override
                        public void run() {
                            H2Database.instance.updateChallenger(player.getKey(), player.getValue());
                        }
                    });
                }
                //Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Vanilla Challenges] End Backup player points");
            }
        }, 0, number);
    }

    public void stopTask() {
        task.cancel();
    }

    public int getPointFromPLayerName(String playerName) {
        if(players.get(playerName) == null) {
            return 0;
        } else {
            return players.get(playerName);
        }
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public ArrayList<Challenger> getTopPlayers() {
        ArrayList<Challenger> top = new ArrayList<>();
        for (Map.Entry<String, Integer> player : players.entrySet()) {
            System.out.println("TOP: " + player.getKey());
            Challenger current = new Challenger(player.getKey(),player.getValue());
            if(top.isEmpty()) {
                top.add(current);
            } else {
                for(int x = 0; x < top.size(); x++) {
                    if(current.getPoints() > top.get(x).getPoints()) {
                        top.set(x,current);
                    } else {
                        top.add(current);
                    }
                    if(x == 1) {
                        break;
                    }
                }
            }
        }
        return top;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(String itemInHand) {
        this.itemInHand = itemInHand;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "players=" + players +
                ", block='" + block + '\'' +
                ", blockOnPlace='" + blockOnPlace + '\'' +
                ", typeChallenge='" + typeChallenge + '\'' +
                '}';
    }
}
