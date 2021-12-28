package agh.ics.oop.proj1;

import java.util.Random;

public class Animal{


    private int orientation;
    private int energy;
    private final int[] genotype;
    private Vector2d position;
    private int rotation;
    private final Map map;

    Animal(Vector2d position, int energy, Map map) {
        this.genotype = new int[32];
        this.position = position;
        this.map = map;
        this.rotation = (new Random()).nextInt(8);
        this.energy = energy;
    }

    public int getEnergy(){
        return energy;
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
    }

    public void move(){
        Random random = new Random();
        int index = random.nextInt(32);
        int move = genotype[index];
        if(move == 0 || move == 4){
            Vector2d oldPosition = new Vector2d(position.x, position.y);
            Vector2d positionDiff = switch (rotation){
                case 0 -> new Vector2d(0, -1);
                case 1 -> new Vector2d(1, -1);
                case 2 -> new Vector2d(1, 0);
                case 3 -> new Vector2d(1, 1);
                case 4 -> new Vector2d(0, 1);
                case 5 -> new Vector2d(-1, 1);
                case 6 -> new Vector2d(-1, 0);
                case 7 -> new Vector2d(-1, -1);
                default -> throw new IllegalStateException("Unexpected value: " + rotation);
            };
            if(move == 4){
                 positionDiff =  positionDiff.opposite();
            }
            Vector2d newPosition = position.add(positionDiff);
            if(map.canMoveTo(newPosition)){
                position = newPosition;
                map.animalPositionChanged(this, oldPosition, position);
            }
        } else{
            rotation = (rotation+move)%8;
        }
    }

    public Vector2d getPosition(){
        return this.position;
    }
    public void setPosition(Vector2d position){
        this.position = position;
    }
    public void decrementEnergy(){
        energy -= 1;
        if(energy <= 0){
            map.animalDied(this);
            energy = 0;
        }
    }
    public void increaseEnergy(int energyGain){
        energy += energyGain;
    }
}
