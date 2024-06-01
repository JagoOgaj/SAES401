package com.example.saes401.utilities;

import android.content.Context;

import com.example.saes401.entities.Enemie;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;

import java.util.Random;

public class GameFight {
    private Random random;
    private Player player;
    private Enemie enemie;
    private Context context;

    public GameFight(Player player, Enemie enemie, Context context) {
        this.enemie = enemie;
        this.player = player;
        this.random = new Random();
        this.context = context;
    }

    public int[] getDicePlayer() {
        int[] dices = new int[random.nextInt(3) + 1];
        for (int i = 0; i < dices.length; i++) {
            dices[i] = random.nextInt(6) + 1;
        }
        return dices;
    }

    private int interpreter(String s) throws Exception {
        int result = 0;
        for (int i = 0; i < s.length(); i += 3) {
            result += getResult(spliterFunction(s));
        }
        return result;
    }

    public int getDiceEnemie() throws Exception {
        return interpreter(
                JsonReader.getEnemieDamageStringFormat(
                        context,
                        String.format(GameConstant.FORMAT_LEVEL, enemie.getCurrentLevelFile()),
                        enemie.getIndex()
                )
        );
    }

    public int getResultPlayer(int result) throws Exception {
        int resultPlayer = result;
        String bonus = player.getItem().getDamage();
        Object[] split = splitOperationObjectDamage(bonus);
        if (split[0].equals("+")) {
            resultPlayer += (int) split[1];
        }
        else if (split[0].equals("*")) {
            resultPlayer *= (int) split[1];
        }
        return resultPlayer;
    }

    public int getResultEnemie(int result) throws Exception {
        int resultEnemie = result;
        Object[] split = splitOperationObjectDamage(enemie.getItem().getDamage());
        if (split[0].equals("+")) {
            resultEnemie += (int) split[1];
        }
        else if (split[0].equals("x")) {
            resultEnemie *= (int) split[1];
        }
        return resultEnemie;
    }

    private int getResult(int[] numbers) {
        int result = 0;
        for (int i = 0; i < numbers[0]; i++)
            result += (random.nextInt(numbers[1]) + 1);
        return result;
    }

    private int[] spliterFunction(String s) {
        String[] sSplit = s.split(GameConstant.REGEX_SPLITER);
        if (sSplit.length < 2) {
            throw new IllegalArgumentException("La chaîne ne peut pas être divisée en deux parties.");
        }
        return new int[]{Integer.parseInt(sSplit[0]), Integer.parseInt(sSplit[1])};
    }

    private static Object[] splitOperationObjectDamage(String operation) throws Exception {
        if (operation == null || operation.length() < 2) {
            throw new Exception("Invalid operation string");
        }

        char operator = operation.charAt(0);
        String numberPart = operation.substring(1);

        if (!Character.isDigit(numberPart.charAt(0))) {
            throw new Exception("Invalid number format");
        }

        int number = Integer.parseInt(numberPart);

        return new Object[]{String.valueOf(operator), number};
    }
}
