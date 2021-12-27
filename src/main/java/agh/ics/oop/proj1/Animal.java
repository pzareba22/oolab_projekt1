package agh.ics.oop.proj1;

import java.util.Random;

public class Animal{


    /*
        Pytania:
        - Jakie geny mają mieć pierwsze zwierzęta? (losowe?)
        - Czy zwierzęta mogą stać na tym samym polu? - tak
     */


    private int orientation;
    private int energy;
    private final int[] genotype;
    private Vector2d position;

    Animal(Vector2d position) {
        this.genotype = new int[32];
        this.position = position;
    }

    public int getEnergy(){
        final int res = energy;
        return res;
    }

    public void generateRandomGenotype(){
        Random random = new Random();
        int sum = 0;

        double[] numbers = new double[8];

        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(1000);
            sum += x;
            numbers[i] = x;
        }

        int sum2 = 0;

        for (int i = 0; i < 8; i++) {
            numbers[i] /= sum;
            numbers[i] *= 32;
            numbers[i] = Math.floor(numbers[i]);
            sum2 += (int) numbers[i];
        }

        int diff = 32 - sum2;
        for (int i = 0; i < diff; i++) {
            numbers[random.nextInt(8)] += 1;
        }

        int ind = 0;

        for (int gene = 0; gene < 8; gene++) {
            for (int i = 0; i < numbers[gene]; i++) {
                genotype[ind] = gene;
                ind++;
            }
        }

        System.out.print("Animal generated a random genotype: ");
        for (int i = 0; i < 32; i++) {
            System.out.print(genotype[i]);
        }
        System.out.println();

    }

    public void move(){
        Random random = new Random();
        int index = random.nextInt(32);
        int move = genotype[index];
    }
}
