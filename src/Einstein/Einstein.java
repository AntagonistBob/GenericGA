/**
 * Created by Paul Cloete on 3/21/2016.
 */

package Einstein;

import Generic.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

public class Einstein extends Solution {
    private static int ID = 0;
    private static String absolutePathToDirectory = "C:\\Users\\Antag\\Documents\\GenericGA\\GenericGAImageMagick\\";
    private int myId = 0;
    private static int imageWidth;
    private static int imageHeight;
    private static int numberOfTriangles = 50;
    private static double ageMultiplier = 0.85;
    private static int mutationPercentage = 15;
    private static int numberOfChildrenFromBreeding = 3;
    private static Random randomNumberGenerator = null;
    private ArrayList<Triangle> triangles = new ArrayList<>(numberOfTriangles);
    private double fitness = 0;

    public Einstein(Random random, int width, int height){
        ID++;
        this.myId = ID;
        if(randomNumberGenerator == null){
            randomNumberGenerator = random;
        }

        imageWidth = width;
        imageHeight = height;
        for(int i = 0; i < numberOfTriangles; i++) {
            this.triangles.add(new Triangle(randomNumberGenerator, width, height));
        }

        determineFitness();
    }

    public Einstein(ArrayList<Triangle> triangles){
        ID++;
        this.myId = ID;
        this.triangles = triangles;
        determineFitness();
    }

    @Override
    public Vector<? extends Solution> breed(Solution mate, Random random) {
        if(randomNumberGenerator == null){
           randomNumberGenerator = random;
        }

        Einstein einsteinMate = (Einstein) mate;
        Vector<Einstein> vector = new Vector<>(numberOfChildrenFromBreeding);
        for(int j = 0; j < numberOfChildrenFromBreeding; j++) {
            ArrayList<Triangle> childRepresentation = new ArrayList<>(numberOfTriangles);
            for (int i = 0; i < numberOfTriangles; i++) {
                if (randomNumberGenerator.nextInt(100) < mutationPercentage) {
                    childRepresentation.add(new Triangle(randomNumberGenerator, imageWidth, imageHeight));
                } else if (randomNumberGenerator.nextBoolean()) {
                    childRepresentation.add(this.triangles.get(i));
                } else {
                    childRepresentation.add(einsteinMate.triangles.get(i));
                }
            }

            vector.add(new Einstein(childRepresentation));
        }

        return vector;
    }

    @Override
    public Comparator<Solution> fitnessComparator() {
        return (solution1, solution2) -> {
            Einstein einsteinOne = (Einstein) solution1;
            Einstein einsteinTwo = (Einstein) solution2;

            if(einsteinOne.fitness == einsteinTwo.fitness){
                return 0;
            } else if(einsteinOne.fitness > einsteinTwo.fitness) {
                return -1;
            } else {
                return 1;
            }
        };
    }

    @Override
    public void age() {
        this.fitness *= ageMultiplier;
    }

    @Override
    public void mutate() {
        for(int i = 0; i < numberOfTriangles; i++) {
            this.triangles.set(i, new Triangle(randomNumberGenerator, imageWidth, imageHeight));
        }

        determineFitness();
    }

    @Override
    public void print() {
        super.print();
        System.out.println("\tFitness: " + this.fitness);
        String [] commands = {"cmd",
        };

        //create image from svg
        String svgFileName = "svgImage" + this.myId + ".svg";
        String imageFileName = "image" + this.myId + ".png";
        try{
            Process process = Runtime.getRuntime().exec(commands);
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            stdin.println("cd " + absolutePathToDirectory);
            stdin.println("convert.exe svg\\" + svgFileName + " elite\\" + imageFileName);
            stdin.flush();
            stdin.close();
            int errCode = process.waitFor();

        }catch(IOException ex){
            System.out.println("Error creating image: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("Error creating image due to interrupt exception: " + ex.getMessage());
        }
    }

    private void determineFitness() {
        //create svg
        String stringToWrite = "<svg height=\"" + imageHeight + "\" width=\" " + imageWidth + "\">" + "\r\n";
        for(Triangle triangle : this.triangles) {
            stringToWrite += triangle.toString() + "\r\n";
        }
        stringToWrite += "</svg>";

        String svgFileName = "svgImage" + this.myId + ".svg";
        String imageFileName = "image" + this.myId + ".png";
        try {

            PrintWriter writer = new PrintWriter("GenericGAImageMagick\\svg\\" + svgFileName, "UTF-8");
            writer.write(stringToWrite);
            writer.flush();
            writer.close();

        } catch(IOException ex){
            System.out.println("Could not create svg image: " + ex.getMessage());
        }

        String [] commands = {"cmd",
        };

        //create image from svg
        try{
            Process process = Runtime.getRuntime().exec(commands);
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            stdin.println("cd " + absolutePathToDirectory);
            stdin.println("convert.exe svg\\" + svgFileName + " results\\" + imageFileName);
            stdin.flush();
            stdin.close();
            int errCode = process.waitFor();

        }catch(IOException ex){
            System.out.println("Error creating image: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("Error creating image due to interrupt exception: " + ex.getMessage());
        }

        //Compare image to goal image
        try{
            Process process = Runtime.getRuntime().exec(commands);
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            stdin.println("cd " + absolutePathToDirectory);
            stdin.println("compare.exe -metric PSNR goal\\einstein.jpg results\\" + imageFileName + " null: 2>&1");
            stdin.flush();
            stdin.close();
            int errCode = process.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String output = "";
            String input = null;
            while((input = stdInput.readLine()) != null){
                output += input;
            }
            String str = "null: 2>&1";
            int indexOfStartOfFitness = output.indexOf(str) + str.length();
            int indexOfEndOfFitness = output.lastIndexOf("C:\\");
            output = output.substring(indexOfStartOfFitness, indexOfEndOfFitness);

//            System.out.println("Output for " + this.myId + ": " + output);

            this.fitness = Double.parseDouble(output);

        }catch(IOException ex){
            System.out.println("Error comparing image: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("Error comparing image due to interrupt exception: " + ex.getMessage());
        }

        //Cleanup
        try{
            Process process = Runtime.getRuntime().exec(commands);
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            stdin.println("cd " + absolutePathToDirectory);
            stdin.println("DEL results\\" + imageFileName);
            stdin.flush();
            stdin.close();

        }catch(IOException ex){
            System.out.println("Error creating image: " + ex.getMessage());
        }

    }

}
