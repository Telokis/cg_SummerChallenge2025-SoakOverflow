# SummerChallenge2025-SoakOverflow

CLI referee compatible with [cg-selfarena](https://github.com/Telokis/cg-selfarena) and others.

## How to build?

If you have Docker, you can build the jar using the `build.sh` script provided at the root of the repository.  
Use the file `target/summer-challenge-2025-soak-overflow-1.0-SNAPSHOT.jar`.

If you don't have Docker, directly run `mvn clean package`. (You will need Maven)

## Changing max turn duration

Based on [this forum post](https://www.codingame.com/forum/t/avoid-local-referee-timeout/202871).

You can change the max turn duration if needed.  
Simply edit the following file:
[`src/main/java/com/codingame/game/Referee.java` line 26](https://github.com/Telokis/cg_SummerChallenge2025-SoakOverflow/blob/main/src/main/java/com/codingame/game/Referee.java#L26)
