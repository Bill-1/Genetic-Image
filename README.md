# Genetic Image Evolver 🖼️🔬

This is a Java program that uses a **genetic algorithm** to evolve an image from a white canvas, making it resemble a target image over time. It follows **Object-Oriented Programming (OOP)** principles.

## Stars are beautiful tonight
![alt text](https://github.com/Bill-1/Genetic-Image-Evolver/blob/main/src/main/resources/images/nightOutput.png?raw=true)
## 🌟 Features
- **Evolutionary Process**: Uses genetic algorithms to progressively generate an image using only triangles.
- **OOP Structure**: Encapsulates pixels, canvas, and triangle mutations.
- **Performance Optimization**: Efficient triangle selection and mutation strategy.

## 📷 How It Works
1. Reads the target image.
2. Initializes a white canvas.
3. Iteratively paints triangles to minimize the **Mean Squared Error (MSE)**.
4. Saves the final evolved image.

## 🛠️ Usage
### **Prerequisites**
- Change the name in Main file to name of your to be generated image in resources/images folder
- Tweak the variables frameLimit, areaLimit, and numberOfIterations in Main.java to your liking. Framelimit is the smallest square length that triangle can be generated in, and the arealimit is the smallest area of a triangle that can be generated. Final resulting image is heavily dependent on these values, and ensure that these values don't contradict each other. 
- Note that images' paths are relative to the directory!

### **Run the Program**
```sh
javac Main.java && java Main
