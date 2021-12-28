package agh.ics.oop.proj1;

import javafx.scene.Parent;

import java.util.Arrays;
import java.util.Random;

public class Animal{


    private int orientation;
    private int energy;
    public final int[] genotype;
    private Vector2d position;
    private int rotation;
    private final Map map;
    private int children;
    private int descendants;
    public boolean isDead;
    public Animal parent1, parent2;
    public int deathDay;

    Animal(Vector2d position, int energy, Map map, int[] genotype) {
        this.genotype = new int[32];
        this.position = position;
        this.map = map;
        this.rotation = (new Random()).nextInt(8);
        this.energy = energy;
        this.isDead = false;
        this.parent1 = null;
        this.parent2 = null;

        if(genotype==null){
            this.generateRandomGenotype();
        }else if(genotype.length != 32){
            throw new IllegalArgumentException();
        }else{
            for (int i = 0; i < 32; i++) {
                this.genotype[i] = genotype[i];
            }
        }

    }

    public int getEnergy(){
        return energy;
    }

    private void generateRandomGenotype(){
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
    public void decreaseEnergy(int energyLoss){
        energy -= energyLoss;
        if(energy <= 0){
            map.animalDied(this);
            isDead = true;
        }
    }
    public void increaseEnergy(int energyGain){
        energy += energyGain;
    }

    public Animal breed(Animal other){


        if(this == other){
            throw new IllegalStateException();
        }

        Animal stronger, weaker;
        if(this.energy > other.getEnergy()){
            stronger = this;
            weaker = other;
        }else {
            stronger = other;
            weaker = this;
        }
        boolean strongerRightSide = (new Random()).nextBoolean();
        int[] newGenotype = new int[32];
        int totalEnergy = stronger.getEnergy() + weaker.getEnergy();
        int div;
        if(strongerRightSide){
            div = weaker.getEnergy()/totalEnergy;
            // geny słabszego rodzica
            for (int i = 0; i < div; i++) {
                newGenotype[i] = weaker.genotype[i];
            }
            for (int i = div; i < 32; i++) {
                newGenotype[i] = stronger.genotype[i];
            }
        }else{
            div = stronger.getEnergy()/totalEnergy;
            for (int i = 0; i < div; i++) {
                newGenotype[i] = stronger.genotype[i];
            }
            for (int i = div; i < 32; i++) {
                newGenotype[i] = weaker.genotype[i];
            }
        }

        int childEnergy = stronger.getEnergy()/4 + weaker.getEnergy()/4;
        stronger.decreaseEnergy(stronger.getEnergy()/4);
        weaker.decreaseEnergy(weaker.getEnergy()/4);

        Animal child = new Animal(stronger.getPosition(), childEnergy, stronger.map, newGenotype);

        child.setParents(stronger, weaker);
        stronger.children += 1;
        weaker.children += 1;
        stronger.descendants += 1;
        weaker.descendants += 1;
        stronger.updateParent1descendants();
        if(stronger.parent1 != weaker.parent1){
            weaker.updateParent1descendants();
        }
        stronger.updateParent2descendants();
        if(stronger.parent2 != weaker.parent2){
            weaker.updateParent2descendants();
        }


        return child;
    }

    public int getChildren(){
        return children;
    }

    public int getDescendants(){
        return descendants;
    }

    public void setParents(Animal parent1, Animal parent2){
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public void updateParent1descendants(){
        if(parent1 != null){
            parent1.descendants += 1;
            parent1.updateParent1descendants();
            parent1.updateParent2descendants();
        }
    }
    public void updateParent2descendants(){
        if(parent2 != null){
            parent2.descendants += 1;
            parent1.updateParent1descendants();
            parent1.updateParent2descendants();

        }
    }

    public void setWatched(){
        this.children = 0;
        this.descendants = 0;
    }


}
