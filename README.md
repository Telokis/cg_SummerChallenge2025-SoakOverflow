# SummerChallenge2025-SoakOverflow

CLI referee compatible with [cg-selfarena](https://github.com/Telokis/cg-selfarena) and others.

## How to build?

If you have Docker, you can build the jar using the `build.sh` script provided at the root of the repository.  
Use the file `target/summer-challenge-2025-soak-overflow-1.0-SNAPSHOT.jar`.

If you don't have Docker, directly run `mvn clean package`. (You will need Maven)

## Changing turn duration

> Based on [this forum post](https://www.codingame.com/forum/t/avoid-local-referee-timeout/202871).

You can now change the turn timeout using the `-timeout` CLI option.  
The timeout option sets a minimum value - it will use the maximum of your specified timeout and the current defaults (50ms for regular turns, 1000ms for first turn).

## Referee Usage

The referee can be run from the command line using the built JAR file:

```bash
java -jar target/summer-challenge-2025-soak-overflow-1.0-SNAPSHOT.jar -p1 "<player1 command>" -p2 "<player2 command>" [options]
```

### Required Arguments
- `-p1 <command>` - Player 1 command line
- `-p2 <command>` - Player 2 command line

### Optional Arguments
- `-h` - Print help message
- `-s` - Run in server mode
- `-league <level>` - Set league level (default: 5)
- `-seed <value>` - Set random seed
- `-timeout <milliseconds>` - Set minimum timeout for both turn and first turn (uses max of specified value and current defaults)
- `-l <file>` - Output logs to specified file
- `-d` - Include referee initial data

### Examples

Basic match between two players:
```bash
java -jar target/summer-challenge-2025-soak-overflow-1.0-SNAPSHOT.jar -p1 "python player1.py" -p2 "binary.exe"
```

Match with custom seed and log output:
```bash
java -jar target/summer-challenge-2025-soak-overflow-1.0-SNAPSHOT.jar -p1 "python player1.py" -p2 "python player2.py" -seed 12345 -l game.json
```

Match with custom timeout (ensures both players get at least 500ms per turn):
```bash
java -jar target/summer-challenge-2025-soak-overflow-1.0-SNAPSHOT.jar -p1 "python player1.py" -p2 "python player2.py" -timeout 500
```
