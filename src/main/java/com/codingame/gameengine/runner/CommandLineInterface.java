package com.codingame.gameengine.runner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.codingame.gameengine.runner.dto.GameResultDto;
import com.google.common.io.Files;

public class CommandLineInterface {

    public static void main(String[] args) {
        try {
            Options options = new Options();

            options.addOption("h", false, "Print the help")
                    .addOption("p1", true, "Required. Player 1 command line.")
                    .addOption("p2", true, "Required. Player 2 command line.")
                    .addOption("s", false, "Server mode")
                    .addOption("league", true, "League level")
                    .addOption("seed", true, "Seed")
                    .addOption("l", true, "File output for logs")
                    .addOption("d", false, "Referee initial data")
                    .addOption("timeout", true, "Minimum timeout in milliseconds for both turn and first turn");

            CommandLine cmd = new DefaultParser().parse(options, args);

            if (cmd.hasOption("h") || !cmd.hasOption("p1") || !cmd.hasOption("p2")) {
                new HelpFormatter().printHelp(
                        "-p1 <player1 command line> -p2 <player2 command line> [-s -l <File output for logs>]",
                        options);
                System.exit(0);
            }

            MultiplayerGameRunner runner = new MultiplayerGameRunner();
            runner.setLeagueLevel(5);

            if (cmd.hasOption("seed")) {
                runner.setSeed(Long.valueOf(cmd.getOptionValue("seed")));
            }

            if (cmd.hasOption("league")) {
                runner.setLeagueLevel(Integer.valueOf(cmd.getOptionValue("league")));
            } else {
                runner.setLeagueLevel(5);
            }

            if (cmd.hasOption("timeout")) {
                System.setProperty("game.min.timeout", cmd.getOptionValue("timeout"));
            }

            Field getGameResult = GameRunner.class.getDeclaredField("gameResult");
            getGameResult.setAccessible(true);
            GameResultDto result = (GameResultDto) getGameResult.get(runner);

            int playerCount = 0;

            for (int i = 1; i <= 4; ++i) {
                if (cmd.hasOption("p" + i)) {
                    runner.addAgent(cmd.getOptionValue("p" + i));
                    playerCount += 1;
                }
            }

            if (cmd.hasOption("s")) {
                runner.start();
            } else {
                Method run = GameRunner.class.getDeclaredMethod("runGame");
                run.setAccessible(true);
                run.invoke(runner);

                for (int i = 0; i < playerCount; ++i) {
                    System.out.println(result.scores.get(i));
                }

                for (String line : result.uinput) {
                    System.out.println(line);
                }
            }

            if (cmd.hasOption("l")) {
                Method getJSONResult = GameRunner.class.getDeclaredMethod("getJSONResult");
                getJSONResult.setAccessible(true);

                Files.asCharSink(Paths.get(cmd.getOptionValue("l")).toFile(), Charset.defaultCharset())
                        .write((String) getJSONResult.invoke(runner));
            }

            Field getPlayers = GameRunner.class.getDeclaredField("players");
            getPlayers.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Agent> players = (List<Agent>) getPlayers.get(runner);

            if (players != null) {
                for (Agent player : players) {
                    Field getProcess = CommandLinePlayerAgent.class.getDeclaredField("process");
                    getProcess.setAccessible(true);
                    Process process = (Process) getProcess.get(player);

                    process.destroy();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
