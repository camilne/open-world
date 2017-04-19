package com.camilne.noise;

import java.util.Random;

public class SimplexNoise {
    
    private SimplexNoise_octave[] octaves;
    private double[] frequencies;
    private double[] amplitudes;
    private double largestFeature;
    
    public SimplexNoise(final double largestFeature, final double persistence) {
	this(largestFeature, persistence, new Random().nextInt());
    }
    
    public SimplexNoise(final double largestFeature, final double persistence, final int seed) {
	this.largestFeature = largestFeature;
	
	final int numberOfOctaves = 2 * (int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));
	
	octaves = new SimplexNoise_octave[numberOfOctaves];
	frequencies = new double[numberOfOctaves];
	amplitudes = new double[numberOfOctaves];
	
	Random rand = new Random(seed);
	
	for(int i = 0; i < numberOfOctaves; i++) {
	    octaves[i] = new SimplexNoise_octave(rand.nextInt());
	    frequencies[i] = Math.pow(2, i);
	    amplitudes[i] = Math.pow(persistence, numberOfOctaves - 1);
	}
    }
    
    public double getNoise(final double x, final double y) {
	double noise = 0;
	
	for(int i = 0; i < octaves.length; i++) {
	    noise += octaves[i].noise(x / frequencies[i], y / frequencies[i]) * amplitudes[i];
	}
	
	return noise;
    }
    
    public double getScaledNoise(final double x, final double y) {
	return Math.log(Math.pow(10, largestFeature)) * getNoise(x, y);
    }

}
