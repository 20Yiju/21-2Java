package edu.handong.csee.java.hw2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import edu.handong.csee.java.hw2.converters.*; // You will learn the import statement in L11.
/**
 * This is main class
 */
public class IntegratedConverter {

    /**
     * This is feild used in every class. This is targetMeasure
     */
    public String targetMeasure;
    /**
     * This is feild used in every class This is our input measure
     */
    public String originalMeasure;
    /**
     * This is feild used in every class. This is input Value
     */
    private double fromValue;

  
     

    /**
     * This is Km to m converter class
     */
    public class KMToMConverter implements Convertible {
        /**
         * This is input value
         */
        public double Value;
        /**
         * This is setvalue method
         */ 
        public void setFromValue(double fromValue){
            Value = fromValue;
            
        }
        /**
        * This is getConverteredValue method
        */
        public void convert(){
            Value = Value * 1000;
        }
        /**
         * This is getConverteredValue method
         */
        public double getConvertedValue(){
            return Value;
    
        }
        
        
    }
    /**
     * This is Km to mile converter class
     */  
    public class KMToMILEConverter implements Convertible {
        /**
         * This is input value
         */
        public double Value;
        /**
         * This is setvalue method
         */
        public void setFromValue(double fromValue){
            Value = fromValue;
        }
        /**
         * This is convering method
         */
        public void convert(){
            Value = Value / 1.6;
        }
        /**
        * This is getConverteredValue method
        */
        public double getConvertedValue(){
            return Value;
    
        }
        
        
    }
    /**
    * This is mile to km converter class
    */
    public class MILEToKMConverter implements Convertible {
        /**
         * This is input value
         */
        public double Value;
        /**
        * This is setvalue method
        */
        public void setFromValue(double fromValue){
            Value = fromValue;
        }
        /**
         * This is convering method
         */
        public void convert(){
            Value = Value * 1.6;
        }
        /**
        * This is getConverteredValue method
        */
        public double getConvertedValue(){
            return Value;
    
        }
        
        
    }
    /**
    * This is ton to g converter class
    */   
    public class TONToGConverter implements Convertible {

        /**
         * This is input value
         */
        public double Value;
        /**
        * This is setvalue method
        */
        public void setFromValue(double fromValue){
            Value = fromValue;
        }
        /**
         * This is convering method
         */
        public void convert(){
            Value = Value * 1000000;
        }
        /**
        * This is getConverteredValue method
        */
        public double getConvertedValue(){
            return Value;
    
        }
        
    }
    
    /**
    * This is ton to kg converter class
    */ 
    public class TONToKGConverter implements Convertible {
        /**
         * This is input value
         */
        public double Value;
        /**
        * This is setvalue method
        */
        public void setFromValue(double fromValue){
            Value = fromValue;
        }
        /**
         * This is convering method
         */
        public void convert(){
            Value = Value * 1000;
        }
        /**
        * This is getConverteredValue method
        */
        public double getConvertedValue(){
            return Value;

        }
        
        
    }

    /**
     * This is main method of IntergratedConverter
     * @param args we can get Values and Converter type by this.
     */
    public static void main(String[] args) {

        IntegratedConverter myConverter = new IntegratedConverter();

        myConverter.run(args);

    }
    /**
     * This is main run method of IntergratedConverter
     * @param args we can get Values and Converter type by this.
     */

    private void run(String[] args) {
   
        fromValue = Double.parseDouble(args[0]);
        String originalMeasure = Util.getUppercaseString(args[1]);
        String targetMeasure = Util.getUppercaseString(args[2]);

        /**
         * This is interface. we can use other class inside of this method.
         */
    
        if(!targetMeasure.equals("ALL")) {
            String converterName = "edu.handong.csee.java.hw2.converters." + originalMeasure + "To" + targetMeasure + "Converter";

            if(originalMeasure.equals("TON")) {
                if (targetMeasure.equals("KG")){
                    converterName = "edu.handong.csee.java.hw2.IntegratedConverter$TONToKGConverter";
                }
                else if (targetMeasure.equals("G")) {
                    converterName = "edu.handong.csee.java.hw2.IntegratedConverter$TONToGConverter";
                }
            }
            else if(originalMeasure.equals("KM")) {
                if (targetMeasure.equals("MILE")){
                    converterName = "edu.handong.csee.java.hw2.IntegratedConverter$KMToMILEConverter";
                
                }
                else if (targetMeasure.equals("M")){
                    converterName = "edu.handong.csee.java.hw2.IntegratedConverter$KMToMConverter";
                    
                }
            }
            else if(originalMeasure.equals("MILE")) {
                if (targetMeasure.equals("KM")){
                    converterName = "edu.handong.csee.java.hw2.IntegratedConverter$MILEToKMConverter";
                    
                }
            }

            // You will learn about the try-catch block and Exception in L19
            try {
                Class<?> TOPClass = (Class<?>) Class.forName("edu.handong.csee.java.hw2.IntegratedConverter");
                Constructor<?> Constructor1 = (Constructor<?>) TOPClass.getConstructor();
                Object TOPClassInstance = (Object) Constructor1.newInstance();
                // Advanced Java feature: Reflection (it lets us use classes with their String class names.)
                // (JC does not teach this topic as it is advanced one but you can study by yourself!!)
                // https://www.oracle.com/technical-resources/articles/java/javareflection.html
            
                Class<?> converterClass = (Class<?>) Class.forName(converterName);
                Constructor<?> constructor = (Constructor<?>) converterClass.getConstructor(TOPClass);
                Convertible myConverter = (Convertible) constructor.newInstance(TOPClassInstance);

                myConverter.setFromValue(fromValue);
                myConverter.convert();
                myConverter.getConvertedValue();
                System.out.println(fromValue +" " + originalMeasure + " is " + myConverter.getConvertedValue() + " " + targetMeasure + "!");

            } catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.out.println("The converter (" + converterName + ") for " + originalMeasure + " to " + targetMeasure + " does not exist!!");
            }

        } else {

            AllConverter myAllConverter = new AllConverter();

            // When your method returns `this' in methods of AllConverter, you can call them in this way (method chaining).
            // Think/understand how and why this work. Study and search for Method chaining
            // https://stackoverflow.com/questions/21180269/how-to-achieve-method-chaining-in-java
            // https://www.geeksforgeeks.org/method-chaining-in-java-with-examples/#:~:text=Method%20Chaining%20is%20the%20practice,with%20a%20(dot.).
            myAllConverter.setFromValue(fromValue).setOriginalMeasure(originalMeasure).convertAndPrintOut();
        }
    }

    /**
     * This is allconverter slass
     */
    static class AllConverter extends IntegratedConverter {
        public double Value;

        /**
         * This is setfromvalue method
         * @param fromValue get input vlalue
         * @return set my input value
         */
        public AllConverter setFromValue(double fromValue) {
            this.Value = fromValue;
            return this;
        }
        
        /**
         * This is setoriginmeasure method we can set out input measure.
         */
        public AllConverter setOriginalMeasure (String Measure) {
            this.originalMeasure = Measure;
            return this;
    
        }
        /**
         * This is convert and print method. we convert our value and print it out.
         */
    
        public void convertAndPrintOut() {
     
            if (originalMeasure.equals("TON")) {
                IntegratedConverter TTK = new IntegratedConverter();
                TONToKGConverter inalltontTOkg = TTK.new TONToKGConverter();
                inalltontTOkg.setFromValue(Value);
                inalltontTOkg.convert();
                System.out.println(Value +" " + originalMeasure + " to " + inalltontTOkg.getConvertedValue() + " " + "KG");
                IntegratedConverter TTK1 = new IntegratedConverter();
                TONToGConverter inalltontTOg = TTK1.new TONToGConverter();
                inalltontTOg.setFromValue(Value);
                inalltontTOg.convert();
                System.out.println(Value +" " + originalMeasure + " to " + inalltontTOg.getConvertedValue()  + " " + "G");
                
            }
            else if (originalMeasure.equals("KM")) {
                IntegratedConverter KMT = new IntegratedConverter();
                IntegratedConverter.KMToMConverter inallkmTOm = KMT.new KMToMConverter();
                inallkmTOm.setFromValue(Value);
                inallkmTOm.convert();
                System.out.println(Value +" " + originalMeasure + " to " + inallkmTOm.getConvertedValue()  + " " + "M");
                IntegratedConverter KMT2 = new IntegratedConverter();
                IntegratedConverter.KMToMILEConverter inallkmTOmile = KMT2.new KMToMILEConverter();
                inallkmTOmile.setFromValue(Value);
                inallkmTOmile.convert();
                System.out.println(Value +" " + originalMeasure + " to " + inallkmTOmile.getConvertedValue()  + " " + "MILE");
       
            }
            else{
                System.out.println("AllConverter cannot support the measure!");
            }
        }
    
    }
    
}




