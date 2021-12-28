//polymorphism: allows subclasses of a class to define their own unique behaviours 
// and yet share some of the same functionality of the super class.

//overriding: the method inherited from the super class is overridden by the sub class.

//hiding: static method overriding, this means that both methods can be used 
// depending on the calling environment of the method, not erased or deleted.


package edu.handong.csee.java.inheritance;

public class Cat extends Animal {
	
    public static void testClassMethod() {
        System.out.println("The static method in Cat");
    }
    public void testInstanceMethod() {
        System.out.println("The instance method in Cat");
    }

    public static void main(String[] args) {
    	
    	Animal.testClassMethod(); //result:The static method in Animal
    	
        Cat myCat = new Cat(); 
        
        myCat.testClassMethod(); //result: The static method in Cat
        myCat.testInstanceMethod(); //result: The instance method in Cat
        
        Animal myAnimal = myCat;   //polymorphism
        
        myAnimal.testClassMethod(); //hiding  result: The static method in Animal
        myAnimal.testInstanceMethod(); //overriding  result: The instance method in Cat
        
        Dog myDog = new Dog();
        
        myAnimal = myDog;   //polymorphism  
        
        myAnimal.testClassMethod(); //hiding  result: The static method in Animal
        myAnimal.testInstanceMethod(); //overriding  result: The instance method in Dog
   
        Monkey myMonkey = new Monkey(); 
        
        myAnimal = myMonkey;   //polymorphism
       
        myAnimal.testClassMethod();//hiding  result: The static method in Animal
        myAnimal.testInstanceMethod(); //overriding  result: The instance method in Monkey

    }
}
